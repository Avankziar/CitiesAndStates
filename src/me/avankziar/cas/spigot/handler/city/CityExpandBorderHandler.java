package me.avankziar.cas.spigot.handler.city;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.avankziar.cas.general.ChatApi;
import me.avankziar.cas.general.objects.Region3D;
import me.avankziar.cas.general.objects.city.City;
import me.avankziar.cas.spigot.CAS;
import me.avankziar.cas.spigot.assistance.MatchApi;
import me.avankziar.cas.spigot.handler.DirectionHandler;
import me.avankziar.cas.spigot.handler.InventarHandler;
import me.avankziar.cas.spigot.handler.region.AreaHandler;
import me.avankziar.cas.spigot.handler.region.MemoryHandler;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.general.economy.currency.CurrencyType;
import me.avankziar.ifh.general.math.MathFormulaParser;
import me.avankziar.ifh.spigot.economy.account.Account;

public class CityExpandBorderHandler
{
	private static String getPath(String s)
	{
		return CAS.getPlugin().getYamlHandler().getLang().getString(s);
	}
	
	private static LinkedHashMap<Long, HashMap<String, String>> getExpandMoneyCostHashMap()
	{
		LinkedHashMap<Long, HashMap<String, String>> moneyCost = new LinkedHashMap<>();
		for(String split : CityConfigHandler.getExpandCityMoneyCost())
		{
			String[] s = split.split(";");
			if(s.length != 3 || !MatchApi.isLong(s[0]) || !MatchApi.isDouble(s[2]))
			{
				continue;
			}
			long barrier = Long.valueOf(s[0]);
			HashMap<String, String> moneyCostSub = new HashMap<>();
			if(moneyCost.containsKey(barrier))
			{
				moneyCostSub = moneyCost.get(barrier);
			}
			moneyCostSub.put(s[1], s[2]);
			moneyCost.put(barrier, moneyCostSub);
		}
		return moneyCost;
	}
	
	private static LinkedHashMap<Long, String> getExpandExpCostHashMap()
	{
		LinkedHashMap<Long, String> expCost = new LinkedHashMap<>();
		for(String split : CityConfigHandler.getExpandCityExpCost())
		{
			String[] s = split.split(";");
			if(s.length != 2 || !MatchApi.isLong(s[0]) || !MatchApi.isLong(s[1]))
			{
				continue;
			}
			expCost.put(Long.valueOf(s[0]), s[1]);
		}
		return expCost;
	}
	
	private static LinkedHashMap<Long, HashMap<Material, String>> getExpandMaterialCostHashMap()
	{
		LinkedHashMap<Long, HashMap<Material, String>> materialCost = new LinkedHashMap<>();
		for(String split : CityConfigHandler.getExpandCityMoneyCost())
		{
			String[] s = split.split(";");
			if(s.length != 3 || !MatchApi.isLong(s[0]) || !MatchApi.isInteger(s[2]))
			{
				continue;
			}
			Material mat = null;
			try
			{
				mat = Material.valueOf(s[1]);
			} catch(Exception e)
			{
				continue;
			}
			long barrier = Long.valueOf(s[0]);
			HashMap<Material, String> materialCostSub = new HashMap<>();
			if(materialCost.containsKey(barrier))
			{
				materialCostSub = materialCost.get(barrier);
			}
			materialCostSub.put(mat, s[2]);
			materialCost.put(barrier, materialCostSub);
		}
		return materialCost;
	}
	
	private static BlockFace getNewBorderDirectionExpand(Player player)
	{
		BlockFace bf = DirectionHandler.getDirection(player.getLocation().getDirection());
		switch(bf)
		{
		default:
			player.sendMessage(ChatApi.tl(getPath("City.CannotDetermineDirection")));
			return null;
		//To -z
		case NORTH_NORTH_WEST:
		case NORTH:
		case NORTH_NORTH_EAST:
			return BlockFace.NORTH;
		//to +x
		case EAST_NORTH_EAST:
		case EAST:
		case EAST_SOUTH_EAST:
			return BlockFace.EAST;	
		//to +z
		case SOUTH_SOUTH_EAST:
		case SOUTH:
		case SOUTH_SOUTH_WEST:
			return BlockFace.SOUTH;
		//to -x
		case WEST_SOUTH_WEST:
		case WEST:
		case WEST_NORTH_WEST:
			return BlockFace.WEST;
		}
	}
	
	private static Region3D getNewBorderPoints(Region3D city, Player player, int blocklenght)
	{
		Vector min = city.getMinimumPoint();
		Vector max = city.getMaximumPoint();
		BlockFace bf = DirectionHandler.getDirection(player.getLocation().getDirection());
		switch(bf)
		{
		default:
			player.sendMessage(ChatApi.tl(getPath("City.CannotDetermineDirection")));
			return null;
		//To -z
		case NORTH_NORTH_WEST:
		case NORTH:
		case NORTH_NORTH_EAST:
			if(Math.min(min.getZ(), max.getZ()) == min.getZ())
			{
				//Min Point to -z
				min.subtract(new Vector(0, 0, blocklenght));			
			} else
			{
				max.subtract(new Vector(0, 0, blocklenght));
			}
			break;
		//to +x
		case EAST_NORTH_EAST:
		case EAST:
		case EAST_SOUTH_EAST:
			if(Math.max(min.getX(), max.getX()) == min.getX())
			{
				//Min Point to -z
				min.add(new Vector(blocklenght, 0, 0));			
			} else
			{
				max.add(new Vector(blocklenght, 0, 0));
			}
			break;		
		//to +z
		case SOUTH_SOUTH_EAST:
		case SOUTH:
		case SOUTH_SOUTH_WEST:
			if(Math.max(min.getX(), max.getX()) == min.getX())
			{
				//Min Point to -z
				min.add(new Vector(0, 0, blocklenght));			
			} else
			{
				max.add(new Vector(0, 0, blocklenght));
			}
			break;
		//to -x
		case WEST_SOUTH_WEST:
		case WEST:
		case WEST_NORTH_WEST:
			if(Math.min(min.getZ(), max.getZ()) == min.getZ())
			{
				//Min Point to -z
				min.subtract(new Vector(blocklenght, 0, 0));			
			} else
			{
				max.subtract(new Vector(blocklenght, 0, 0));
			}
			break;
		}
		return new Region3D(city.getServername(), city.getWorldname(), min, max);
	}
	
	private static Region3D intersect(long c, Player player, int blocklenght)
	{
		Region3D cityregion = MemoryHandler.getCity(c);
		if(cityregion == null)
		{
			player.sendMessage(ChatApi.tl(getPath("City.NotInCity")));
			return null;
		}
		Region3D region = getNewBorderPoints(cityregion, player, blocklenght);
		if(region == null)
		{
			return null;
		}
		Vector min = region.getMinimumPoint();
		Vector max = region.getMaximumPoint();
		long intersectCity = AreaHandler.intersectCity(min, max);
		if(c != intersectCity)
		{
			int possiblelenght = 0;
			for(int i = blocklenght; i > 0; i--)
			{
				Region3D r = getNewBorderPoints(cityregion, player, i);
				if(r == null)
				{
					continue;
				}
				long intersect = AreaHandler.intersectCity(r.getMinimumPoint(), r.getMaximumPoint());
				if(c == intersect)
				{
					possiblelenght = i;
					break;
				}
			}
			player.sendMessage(ChatApi.tl(getPath("City.Expand.IntersectCity")
					.replace("%blocklenght%", String.valueOf(blocklenght))
					.replace("%possibleblocklenght%", String.valueOf(possiblelenght))
					));
			return null;
		}
		return region;
	}
	
	public static void expandBorderInfo(Player player, int blocklenght)
	{
		LinkedHashMap<Long, HashMap<String, String>> moneyCost = getExpandMoneyCostHashMap();
		LinkedHashMap<Long, String> expCost = getExpandExpCostHashMap();
		LinkedHashMap<Long, HashMap<Material, String>> matCost = getExpandMaterialCostHashMap();
		long c = AreaHandler.isInCity(player.getLocation());
		Region3D region = intersect(c, player, blocklenght);
		final long area = region.getArea();
		Optional<Long> mightAreaMoney = moneyCost.keySet().stream()
				.filter(x -> x < area).findFirst();
		long underAreaMoney = 0L;
		if(mightAreaMoney.isEmpty())
		{
			List<Long> list = moneyCost.keySet().stream().toList();
			underAreaMoney = list.getLast();
		} else
		{
			underAreaMoney = mightAreaMoney.get();
		}
		City city = CityHandler.getCityFromSQL(c);
		HashMap<String, Double> formulaVariables = CityFormulaHandler.getFormulaVariables(city);
		ArrayList<String> description = new ArrayList<>();
		description.add(getPath("City.Expand.CostHeadline")
				.replace("%direction%", getPath("City.Expand.Direction."+getNewBorderDirectionExpand(player).toString()))
				.replace("%blocklenght%", String.valueOf(blocklenght)));
		if(CAS.getPlugin().getIFHEco() != null)
		{
			for(Entry<String, String> e : moneyCost.get(underAreaMoney).entrySet().stream()
				.filter(x -> !x.getKey().equals("vault")).toList())
			{
				String currency = e.getKey();
				String formula = e.getValue();
				double costs = 0.0;
				try
				{
					costs = new MathFormulaParser().parse(formula, formulaVariables);
				} catch(Exception ex)
				{
					player.sendMessage(ChatApi.tl(getPath("City.Expand.FormulaFailure")));
					return;
				}
				if(CAS.getPlugin().getIFHEco().getCurrency(currency) == null)
				{
					player.sendMessage(ChatApi.tl(getPath("CurrencyDontExist")));
					return;
				}
				Account acc = CAS.getPlugin().getIFHEco().getAccount(city.getCityIFHAccount());
				if(acc == null)
				{
					player.sendMessage(ChatApi.tl(getPath("AccountNotExist")
							.replace("%value%", String.valueOf(city.getCityIFHAccount()))));
					return;
				}
				if(!acc.getCurrency().getUniqueName().equals(currency))
				{
					player.sendMessage(ChatApi.tl(getPath("CurrencyDontMatch")));
					return;
				}
				if(costs > 0)
				{
					description.add(getPath("City.Expand.Moneyline")
							.replace("%money%", CAS.getPlugin().getIFHEco().format(costs, acc.getCurrency())));
				}				
			}
		} else
		{
			for(Entry<String, String> e : moneyCost.get(underAreaMoney).entrySet().stream()
					.filter(x -> x.getKey().equals("vault")).toList())
			{
				String formula = e.getValue();
				double costs = 0.0;
				try
				{
					costs = new MathFormulaParser().parse(formula, formulaVariables);
				} catch(Exception ex)
				{
					player.sendMessage(ChatApi.tl(getPath("City.Expand.FormulaFailure")));
					return;
				}
				if(costs > 0)
				{
					description.add(getPath("City.Expand.Moneyline")
							.replace("%money%", costs+CAS.getPlugin().getVaultEco().currencyNamePlural()));
				}
				break;
			}
		}
		Optional<Long> mightAreaExp = expCost.keySet().stream()
				.filter(x -> x < area).findFirst();
		long underAreaExp = 0L;
		if(mightAreaExp.isEmpty())
		{
			List<Long> list = expCost.keySet().stream().toList();
			underAreaExp = list.getLast();
		} else
		{
			underAreaExp = mightAreaExp.get();
		}
		String expFormula = expCost.get(underAreaExp);
		double expCosts = 0.0;
		try
		{
			expCosts = new MathFormulaParser().parse(expFormula, formulaVariables);
		} catch(Exception ex)
		{
			player.sendMessage(ChatApi.tl(getPath("City.Expand.FormulaFailure")));
			return;
		}
		if(expCosts > 0)
		{
			description.add(getPath("City.Expand.Expline")
					.replace("%exp%", String.valueOf(expCosts)));
		}
		Optional<Long> mightAreaMat = matCost.keySet().stream()
				.filter(x -> x < area).findFirst();
		long underAreaMat = 0L;
		if(mightAreaMat.isEmpty())
		{
			List<Long> list = matCost.keySet().stream().toList();
			underAreaMat = list.getLast();
		} else
		{
			underAreaMat = mightAreaMat.get();
		}
		for(Entry<Material, String> e : matCost.get(underAreaMat).entrySet())
		{
			String formula = e.getValue();
			int costs = 0;
			try
			{
				costs = (int) new MathFormulaParser().parse(formula, formulaVariables);
			} catch(Exception ex)
			{
				player.sendMessage(ChatApi.tl(getPath("City.Expand.FormulaFailure")));
				return;
			}
			if(costs > 0)
			{
				description.add(getPath("City.Expand.Materialline")
						.replace("%amount%", String.valueOf(costs))
						.replace("%material%", e.getKey().toString()));
			}	
		}
		description.stream().forEach(x -> player.sendMessage(ChatApi.tl(x)));
	}
	
	public static void expandBorder(Player player, int blocklenght)
	{
		LinkedHashMap<Long, HashMap<String, String>> moneyCost = getExpandMoneyCostHashMap();
		LinkedHashMap<Long, String> expCost = getExpandExpCostHashMap();
		LinkedHashMap<Long, HashMap<Material, String>> matCost = getExpandMaterialCostHashMap();
		long c = AreaHandler.isInCity(player.getLocation());
		Region3D region = intersect(c, player, blocklenght);
		final long area = region.getArea();
		Optional<Long> mightAreaMoney = moneyCost.keySet().stream()
				.filter(x -> x < area).findFirst();
		long underAreaMoney = 0L;
		if(mightAreaMoney.isEmpty())
		{
			List<Long> list = moneyCost.keySet().stream().toList();
			underAreaMoney = list.getLast();
		} else
		{
			underAreaMoney = mightAreaMoney.get();
		}
		City city = CityHandler.getCityFromSQL(c);
		HashMap<String, Double> formulaVariables = CityFormulaHandler.getFormulaVariables(city);
		boolean money = false;
		LinkedHashMap<Account, Double> accToPay = new LinkedHashMap<>();
		if(CAS.getPlugin().getIFHEco() != null)
		{
			for(Entry<String, String> e : moneyCost.get(underAreaMoney).entrySet().stream()
				.filter(x -> !x.getKey().equals("vault")).toList())
			{
				String currency = e.getKey();
				String formula = e.getValue();
				double costs = 0.0;
				try
				{
					costs = new MathFormulaParser().parse(formula, formulaVariables);
				} catch(Exception ex)
				{
					player.sendMessage(ChatApi.tl(getPath("City.Expand.FormulaFailure")));
					return;
				}
				if(costs < 0)
				{
					continue;
				}
				if(CAS.getPlugin().getIFHEco().getCurrency(currency) == null)
				{
					player.sendMessage(ChatApi.tl(getPath("CurrencyDontExist")));
					return;
				}
				Account acc = CAS.getPlugin().getIFHEco().getAccount(city.getCityIFHAccount());
				if(acc == null)
				{
					player.sendMessage(ChatApi.tl(getPath("AccountNotExist")
							.replace("%value%", String.valueOf(city.getCityIFHAccount()))));
					return;
				}
				if(!acc.getCurrency().getUniqueName().equals(currency))
				{
					player.sendMessage(ChatApi.tl(getPath("CurrencyDontMatch")));
					return;
				}
				if(acc.getBalance() >= costs)
				{
					money = true;
				}
				accToPay.put(acc, costs);
			}
		} else
		{
			for(Entry<String, String> e : moneyCost.get(underAreaMoney).entrySet().stream()
					.filter(x -> x.getKey().equals("vault")).toList())
			{
				String formula = e.getValue();
				double costs = 0.0;
				try
				{
					costs = new MathFormulaParser().parse(formula, formulaVariables);
				} catch(Exception ex)
				{
					player.sendMessage(ChatApi.tl(getPath("City.Expand.FormulaFailure")));
					return;
				}
				if(CAS.getPlugin().getVaultEco().has(player, costs))
				{
					money = true;
				}
				break;
			}
		}
		boolean exp = false;
		Optional<Long> mightAreaExp = expCost.keySet().stream()
				.filter(x -> x < area).findFirst();
		long underAreaExp = 0L;
		if(mightAreaExp.isEmpty())
		{
			List<Long> list = expCost.keySet().stream().toList();
			underAreaExp = list.getLast();
		} else
		{
			underAreaExp = mightAreaExp.get();
		}
		String expFormula = expCost.get(underAreaExp);
		double expCosts = 0.0;
		try
		{
			expCosts = new MathFormulaParser().parse(expFormula, formulaVariables);
		} catch(Exception ex)
		{
			player.sendMessage(ChatApi.tl(getPath("City.Expand.FormulaFailure")));
			return;
		}
		if(city.getCityStoredExperience() >= (int) expCosts)
		{
			exp = true;
		}
		boolean material = false;
		Optional<Long> mightAreaMat = matCost.keySet().stream()
				.filter(x -> x < area).findFirst();
		long underAreaMat = 0L;
		if(mightAreaMat.isEmpty())
		{
			List<Long> list = matCost.keySet().stream().toList();
			underAreaMat = list.getLast();
		} else
		{
			underAreaMat = mightAreaMat.get();
		}
		for(Entry<Material, String> e : matCost.get(underAreaMat).entrySet())
		{
			String formula = e.getValue();
			int costs = 0;
			try
			{
				costs = (int) new MathFormulaParser().parse(formula, formulaVariables);
			} catch(Exception ex)
			{
				player.sendMessage(ChatApi.tl(getPath("City.Expand.FormulaFailure")));
				return;
			}
			if(InventarHandler.hasItem(player, e.getKey(), costs))
			{
				material = true;
			}
		}
		if(!money || !exp || !material)
		{
			if(!money)
			{
				player.sendMessage(ChatApi.tl(getPath("NotEnought")));
				return;
			} else if(!exp)
			{
				player.sendMessage(ChatApi.tl(getPath("NotEnoughtExp")));
				return;
			} else if(!material)
			{
				player.sendMessage(ChatApi.tl(getPath("NotEnoughtMaterial")));
				return;
			}
		}
		ArrayList<String> description = new ArrayList<>();
		description.add(getPath("City.Expand.CostHeadline")
				.replace("%direction%", getPath("City.Expand.Direction."+getNewBorderDirectionExpand(player).toString()))
				.replace("%blocklenght%", String.valueOf(blocklenght)));
		if(CAS.getPlugin().getIFHEco() != null)
		{			
			String category = getPath("Economy.Category");
			String comment = getPath("Economy.Comment")
					.replace("%blocklenght%", String.valueOf(blocklenght))
					.replace("%direction%", 
							getPath("City.Expand.Direction."+getNewBorderDirectionExpand(player).toString()));
			for(Entry<Account, Double> e : accToPay.entrySet())
			{
				Account v = CAS.getPlugin().getIFHEco().getDefaultAccount(player.getUniqueId(), AccountCategory.VOID,
							CAS.getPlugin().getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL));
				if(e.getValue() > 0)
				{
					if(v != null)
					{
						CAS.getPlugin().getIFHEco().transaction(
								e.getKey(), v, e.getValue(), OrdererType.PLAYER, player.getUniqueId().toString(),
								category,
								comment);
					} else
					{
						CAS.getPlugin().getIFHEco().withdraw(
								e.getKey(), e.getValue(), OrdererType.PLAYER, player.getUniqueId().toString(),
								category,
								comment);
					}
					description.add(getPath("City.Expand.Moneyline")
							.replace("%money%", CAS.getPlugin().getIFHEco().format(e.getValue(), e.getKey().getCurrency())));
				}
			}
		} else
		{
			for(Entry<String, String> e : moneyCost.get(underAreaMoney).entrySet().stream()
					.filter(x -> x.getKey().equals("vault")).toList())
			{
				String formula = e.getValue();
				double costs = 0.0;
				try
				{
					costs = new MathFormulaParser().parse(formula, formulaVariables);
				} catch(Exception ex)
				{
					player.sendMessage(ChatApi.tl(getPath("City.Expand.FormulaFailure")));
					return;
				}
				if(!CAS.getPlugin().getVaultEco().has(player, costs))
				{
					player.sendMessage(ChatApi.tl(getPath("NotEnought")));
					return;
				}
				if(costs > 0)
				{
					CAS.getPlugin().getVaultEco().withdrawPlayer(player, costs);
					description.add(getPath("City.Expand.Moneyline")
							.replace("%money%", costs+CAS.getPlugin().getVaultEco().currencyNamePlural()));
				}
				break;
			}
		}
		if(expCosts > 0)
		{
			city.setCityStoredExperience(city.getCityStoredExperience()-(int)expCosts);
			description.add(getPath("City.Expand.Expline")
					.replace("%exp%", String.valueOf(expCosts)));
		}
		for(Entry<Material, String> e : matCost.get(underAreaMat).entrySet())
		{
			String formula = e.getValue();
			int costs = 0;
			try
			{
				costs = (int) new MathFormulaParser().parse(formula, formulaVariables);
			} catch(Exception ex)
			{
				player.sendMessage(ChatApi.tl(getPath("City.Expand.FormulaFailure")));
				return;
			}
			if(costs > 0)
			{
				InventarHandler.removeItem(player, e.getKey(), costs);
				description.add(getPath("City.Expand.Materialline")
						.replace("%amount%", String.valueOf(costs))
						.replace("%material%", e.getKey().toString()));
			}
		}
		city.setMaximumPoint(region.getMaximumPoint());
		city.setMinimumPoint(region.getMinimumPoint());
		city.saveRAM();
		city.saveMysql();
	}
}