package me.avankziar.cas.spigot.handler.city;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import main.java.me.avankziar.tt.spigot.handler.CatTechHandler;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.MainCategory;
import main.java.me.avankziar.tt.spigot.objects.ram.misc.SubCategory;
import me.avankziar.cas.general.ChatApi;
import me.avankziar.cas.general.objects.Region3D;
import me.avankziar.cas.general.objects.city.City;
import me.avankziar.cas.spigot.CAS;
import me.avankziar.cas.spigot.assistance.MatchApi;
import me.avankziar.cas.spigot.handler.DirectionHandler;
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
	
	public static void expandBorderInfo(Player player, int blocklenght)
	{
		LinkedHashMap<Long, HashMap<String, String>> moneyCost = getExpandMoneyCostHashMap();
		LinkedHashMap<Long, String> expCost = getExpandExpCostHashMap();
		LinkedHashMap<Long, HashMap<Material, String>> matCost = getExpandMaterialCostHashMap();
		long c = AreaHandler.isInCity(player.getLocation());
		Region3D cityregion = MemoryHandler.getCity(c);
		if(cityregion == null)
		{
			player.sendMessage(ChatApi.tl(getPath("City.NotInCity")));
			return;
		}
		Region3D region = getNewBorderPoints(cityregion, player, blocklenght);
		if(region == null)
		{
			return;
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
			return;
		}
		final long area = region.getArea();
		Optional<Long> mightArea = moneyCost.keySet().stream()
				.filter(x -> x < area).findFirst();
		if(mightArea.isEmpty())
		{
			player.sendMessage(ChatApi.tl(getPath("")));
			return;
		}
		long underArea = mightArea.get();
		City city = CityHandler.getCityFromSQL(c);
		HashMap<String, Double> formulaVariables = CityFormulaHandler.getFormulaVariables(city);
		if(CAS.getPlugin().getIFHEco() != null)
		{
			LinkedHashMap<Account, Double> accToPay = new LinkedHashMap<>();
			for(Entry<String, String> e : moneyCost.get(underArea).entrySet().stream()
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
					player.sendMessage(ChatApi.tl(getPath("")));
					return;
				}
				Account acc = CAS.getPlugin().getIFHEco().getAccount(city.getCityIFHAccount());
				if(acc == null)
				{
					player.sendMessage(ChatApi.tl(getPath("")));
					return;
				}
				if(!acc.getCurrency().getUniqueName().equals(currency))
				{
					player.sendMessage(ChatApi.tl(getPath("")));
					return;
				}
				if(acc.getBalance() < costs)
				{
					player.sendMessage(ChatApi.tl(getPath("NotEnought")));
					return;
				}
				accToPay.put(acc, costs);
			}
			String category = getPath("Economy.Category");
			String comment = getPath("Economy.Comment")
					.replace("%blocklenght%", String.valueOf(blocklenght))
					.replace("%direction%", 
							getPath("City.Expand.Direction."+getNewBorderDirectionExpand(player).toString()));
			for(Entry<Account, Double> e : accToPay.entrySet())
			{
				Account v = CAS.getPlugin().getIFHEco().getDefaultAccount(player.getUniqueId(), AccountCategory.VOID,
							CAS.getPlugin().getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL));
				
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
			}
		} else
		{
			for(Entry<String, String> e : moneyCost.get(underArea).entrySet().stream()
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
				CAS.getPlugin().getVaultEco().withdrawPlayer(player, costs);
				break;
			}
		}
	}
	
	public static void expandBorder(Player player, int blocklenght)
	{
		long c = AreaHandler.isInCity(player.getLocation());
		Region3D cityregion = MemoryHandler.getCity(c);
		if(cityregion == null)
		{
			player.sendMessage(ChatApi.tl(getPath("City.NotInCity")));
			return;
		}
		Region3D region = getNewBorderPoints(cityregion, player, blocklenght);
		if(region == null)
		{
			return;
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
			return;
		}
		City city = CityHandler.getCityFromSQL(c);
		if(CAS.getPlugin().getIFHEco() != null)
		{
			for(Entry<String, String> e : moneycosts.entrySet().stream()
				.filter(x -> !x.getKey().equals("vault")).toList())
			{
				String currency = e.getKey();
				String formula = e.getValue();
				HashMap<String, Double> formulaVariables = new HashMap<>();
				double costPerBlockLenght = new MathFormulaParser().parse(formula, formulaVariables);
				if(CAS.getPlugin().getIFHEco().getCurrency(currency) == null)
				{
					player.sendMessage(ChatApi.tl(CAS.getPlugin().getYamlHandler().getLang().getString("")));
					return;
				}
				Account acc = CAS.getPlugin().getIFHEco().getAccount(city.getCityIFHAccount());
				if(acc == null)
				{
					player.sendMessage(ChatApi.tl(CAS.getPlugin().getYamlHandler().getLang().getString("")));
					return;
				}
				if(!acc.getCurrency().getUniqueName().equals(currency))
				{
					player.sendMessage(ChatApi.tl(CAS.getPlugin().getYamlHandler().getLang().getString("")));
					return;
				}
			}
		} else
		{
			for(Entry<String, Double> e : moneycosts.entrySet().stream()
					.filter(x -> x.getKey().equals("vault")).toList())
			{
				String currency = e.getKey();
				double costPerBlockLenght = e.getValue();
			}
		}
	}
}