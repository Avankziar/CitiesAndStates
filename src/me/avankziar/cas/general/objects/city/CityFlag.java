package me.avankziar.cas.general.objects.city;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.collect.ImmutableList;

import me.avankziar.cas.general.database.MysqlHandable;
import me.avankziar.cas.general.database.QueryType;
import me.avankziar.cas.general.database.YamlHandler;
import me.avankziar.cas.general.objects.Result;
import me.avankziar.cas.spigot.assistance.TimeHandler;
import me.avankziar.cas.spigot.database.MysqlHandler;

public class CityFlag implements MysqlHandable
{
	private static ArrayList<String> attributeUniques = new ArrayList<>();
	public static ImmutableList<String> getRegisteredFlagUniqueNames()
	{
		return ImmutableList.copyOf(attributeUniques);
	}
	private static ArrayList<CityFlag> attributes = new ArrayList<>();
	public static ImmutableList<CityFlag> getRegisteredFlags()
	{
		return ImmutableList.copyOf(attributes);
	}
	
	public static void init(YamlHandler y)
	{
		YamlConfiguration yca = y.getConfig_CityFlags();
		for(String un : y.getConfig_CityFlags().getKeys(false))
		{
			CityFlag ca = new CityFlag(un, yca.getString(un+".Displayname"),
					Result.valueOf(yca.getString(un+".DefaultResultIfNotSpecified")),
							TimeHandler.getRepeatingTime(yca.getString(un+".CooldownToChangeActiveStatus")),
							yca.getDouble(un+".BuyCosts"), yca.getInt(un+".BuyableAtCityLevel"),
							yca.getDouble(un+".ActivationCosts"),
							yca.getDouble(un+".AdministrativeExpensedIfActive"),
							yca.getDouble(un+".AdministrativeExpensedIfDeactive"));
			add(ca);
		}
	}
	
	public static void add(CityFlag attribute)
	{
		if(attributes.stream().filter(x -> x.getUniqueName().equals(attribute.getUniqueName())).findAny().isEmpty())
		{
			CityFlag.attributes.add(attribute);
			CityFlag.attributeUniques.add(attribute.getUniqueName());
		}
	}
	
	private String uniqueName;
	private String displayname;
	private Result defaultResultIfNotSpecified;
	private double buyCosts;
	private int buyableAtCityLevel;
	private long cooldownToChangeActiveStatus;
	private double activationCosts;
	private double administrativeExpensesIfActive;
	private double administrativeExpensesIfDeactive;
	
	private long id = 0;
	private long cityID = 0;
	private Result result = null;
	private boolean active = false;
	private long actualCooldownToChangeActiveStatus = 0L;
	
	//For Static Variables
	public CityFlag(String uniqueName, String displayname, 
			Result defaultResultIfNotSpecified,	long cooldownToChangeActiveStatus,
			double buyCosts, int buyableAtCityLevel,
			double activationCosts, double administrativeExpensesIfActive, double administrativeExpensesIfDeactive)
	{
		setUniqueName(uniqueName);
		setDisplayname(displayname);
		setDefaultResultIfNotSpecified(defaultResultIfNotSpecified);
	}
	
	//For INTERNAL Use!
	public CityFlag(){}
	
	//For Dynamic and Mysql Variables
	public CityFlag(long id, long cityID, String uniqueName, Result result, boolean active, long actualCooldownToChangeActiveStatus)
	{
		Optional<String> o = getRegisteredFlagUniqueNames().stream().filter(x -> uniqueName.equals(x)).findAny();
		if(o.isEmpty())
		{
			return;
		}
		CityFlag staticAttribute = getRegisteredFlags().stream().filter(x -> uniqueName.equals(x.getUniqueName())).findAny().get();
		setDisplayname(staticAttribute.getDisplayname());
		setDefaultResultIfNotSpecified(staticAttribute.getDefaultResultIfNotSpecified());
		setCooldownToChangeActiveStatus(staticAttribute.getCooldownToChangeActiveStatus());
		setBuyCosts(staticAttribute.getBuyCosts());
		setBuyableAtCityLevel(staticAttribute.getBuyableAtCityLevel());
		setActivationCosts(staticAttribute.getActivationCosts());
		setAdministrativeExpensesIfActive(staticAttribute.getAdministrativeExpensesIfActive());
		setAdministrativeExpensesIfDeactive(staticAttribute.getAdministrativeExpensesIfDeactive());
		
		setUniqueName(uniqueName);
		setId(id);
		setCityID(cityID);
		setResult(result);
		setActive(active);
		setActualCooldownToChangeActiveStatus(actualCooldownToChangeActiveStatus);
	}

	public String getUniqueName()
	{
		return uniqueName;
	}

	public void setUniqueName(String uniqueName)
	{
		this.uniqueName = uniqueName;
	}

	public String getDisplayname()
	{
		return displayname;
	}

	public void setDisplayname(String displayname)
	{
		this.displayname = displayname;
	}

	public Result getDefaultResultIfNotSpecified()
	{
		return defaultResultIfNotSpecified;
	}

	public void setDefaultResultIfNotSpecified(Result defaultResultIfNotSpecified)
	{
		this.defaultResultIfNotSpecified = defaultResultIfNotSpecified;
	}

	public Result getResult()
	{
		return result;
	}

	public void setResult(Result result)
	{
		this.result = result;
	}

	public double getBuyCosts()
	{
		return buyCosts;
	}

	public void setBuyCosts(double buyCosts)
	{
		this.buyCosts = buyCosts;
	}

	public int getBuyableAtCityLevel()
	{
		return buyableAtCityLevel;
	}

	public void setBuyableAtCityLevel(int buyableAtCityLevel)
	{
		this.buyableAtCityLevel = buyableAtCityLevel;
	}

	public long getCooldownToChangeActiveStatus()
	{
		return cooldownToChangeActiveStatus;
	}

	public void setCooldownToChangeActiveStatus(long cooldownToChangeActiveStatus)
	{
		this.cooldownToChangeActiveStatus = cooldownToChangeActiveStatus;
	}

	public double getActivationCosts()
	{
		return activationCosts;
	}

	public void setActivationCosts(double activationCosts)
	{
		this.activationCosts = activationCosts;
	}

	public double getAdministrativeExpensesIfActive()
	{
		return administrativeExpensesIfActive;
	}

	public void setAdministrativeExpensesIfActive(double administrativeExpensesIfActive)
	{
		this.administrativeExpensesIfActive = administrativeExpensesIfActive;
	}

	public double getAdministrativeExpensesIfDeactive()
	{
		return administrativeExpensesIfDeactive;
	}

	public void setAdministrativeExpensesIfDeactive(double administrativeExpensesIfDeactive)
	{
		this.administrativeExpensesIfDeactive = administrativeExpensesIfDeactive;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getCityID()
	{
		return cityID;
	}

	public void setCityID(long cityID)
	{
		this.cityID = cityID;
	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public long getActualCooldownToChangeActiveStatus()
	{
		return actualCooldownToChangeActiveStatus;
	}

	public void setActualCooldownToChangeActiveStatus(long actualCooldownToChangeActiveStatus)
	{
		this.actualCooldownToChangeActiveStatus = actualCooldownToChangeActiveStatus;
	}
	
	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`city_id`, `unique_name`,"
					+ " `result_name`, `isactive`, `actual_cooldown_to_change_active_status`) " 
					+ "VALUES("
					+ "?, ?,"
					+ "?, ?, ?"
					+ ")";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, getCityID());
			ps.setString(2, getUniqueName());
			ps.setString(3, getResult().toString());
			ps.setBoolean(4, isActive());
			ps.setLong(5, getActualCooldownToChangeActiveStatus());
	        int i = ps.executeUpdate();
	        MysqlHandler.addRows(QueryType.INSERT, i);
	        return true;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not create a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return false;
	}

	@Override
	public boolean update(Connection conn, String tablename, String whereColumn, Object... whereObject)
	{
		try
		{
			String sql = "UPDATE `" + tablename
				+ "` SET `city_id` = ?, `unique_name` = ?,"
				+ " `result_name` = ?, `isactive` = ?, `actual_cooldown_to_change_active_status` = ?" 
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, getCityID());
			ps.setString(2, getUniqueName());
			ps.setString(3, getResult().toString());
			ps.setBoolean(4, isActive());
			ps.setLong(5, getActualCooldownToChangeActiveStatus());
			int i = 6;
			for(Object o : whereObject)
			{
				ps.setObject(i, o);
				i++;
			}			
			int u = ps.executeUpdate();
			MysqlHandler.addRows(QueryType.UPDATE, u);
			return true;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not update a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return false;
	}

	@Override
	public ArrayList<Object> get(Connection conn, String tablename, String orderby, String limit, String whereColumn, Object... whereObject)
	{
		try
		{
			String sql = "SELECT * FROM `" + tablename
				+ "` WHERE "+whereColumn+" ORDER BY "+orderby+limit;
			PreparedStatement ps = conn.prepareStatement(sql);
			int i = 1;
			for(Object o : whereObject)
			{
				ps.setObject(i, o);
				i++;
			}
			
			ResultSet rs = ps.executeQuery();
			MysqlHandler.addRows(QueryType.READ, rs.getMetaData().getColumnCount());
			ArrayList<Object> al = new ArrayList<>();
			while (rs.next()) 
			{
				al.add(new CityFlag(
						rs.getLong("id"),
						rs.getLong("city_id"),
						rs.getString("unique_name"),
						Result.valueOf(rs.getString("result_name")),
						rs.getBoolean("isactive"),
						rs.getLong("actual_cooldown_to_change_active_status")
						));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	@Override
	public ArrayList<Object> get(Connection conn, String tablename, String sql, Object... whereObject)
	{
		try
		{
			PreparedStatement ps = conn.prepareStatement(sql);
			int i = 1;
			for(Object o : whereObject)
			{
				ps.setObject(i, o);
				i++;
			}
			
			ResultSet rs = ps.executeQuery();
			MysqlHandler.addRows(QueryType.READ, rs.getMetaData().getColumnCount());
			ArrayList<Object> al = new ArrayList<>();
			while (rs.next()) 
			{
				al.add(new CityFlag(
						rs.getLong("id"),
						rs.getLong("city_id"),
						rs.getString("unique_name"),
						Result.valueOf(rs.getString("result_name")),
						rs.getBoolean("isactive"),
						rs.getLong("actual_cooldown_to_change_active_status")
						));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<CityFlag> convert(ArrayList<Object> arrayList)
	{
		ArrayList<CityFlag> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof CityFlag)
			{
				l.add((CityFlag) o);
			}
		}
		return l;
	}
}