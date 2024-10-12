package me.avankziar.cas.general.objects.city;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import javax.management.MBeanServerNotification;

import org.bukkit.Material;
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.min;

import me.avankziar.cas.general.database.MemoryHandable;
import me.avankziar.cas.general.database.MysqlHandable;
import me.avankziar.cas.general.database.MysqlType;
import me.avankziar.cas.general.database.QueryType;
import me.avankziar.cas.general.objects.Region3D;
import me.avankziar.cas.spigot.CAS;
import me.avankziar.cas.spigot.database.MysqlHandler;
import me.avankziar.cas.spigot.handler.region.MemoryHandler;

public class City extends Region3D implements MysqlHandable, MemoryHandable
{
	private long id;
	private String cityName;
	private Material guiMaterial;
	private ArrayList<String> cityDescription;
	private long creationTime;
	private UUID creator;
	private Vector creationLocation;
	private UUID governor; //City owner
	private CityLevel cityLevel;
	private CitySecurityLevel citySecurityLevel;
	
	private int cityIFHAccount;
	private int cityStoredExperience;
	
	private boolean wouldLikeToLevelUp; //true, if a citymanager would like to level up, but the config dont let him directly
	private boolean canLevelUp; //admin boolean if there are manual things, what must be done to level up.
	private String whatNeedsToBeDoneMessage; //Message, what must be done to level up. Message from admin.
	private String lastLevelApprover; //Playername
	
	private double defaultCityManagerExpenseAllowance;
	private double defaultCityMemberExpenseAllowance;
	
	private int additionalPurchasedDistricts;
	private int additionalPurchasedProperties;
	
	private int actualRedstoneBlocksAmount;
	private int maximumRedstoneBlocksAmount;

	private int actualLivestockAmount;
	private int maximumLivestockAmount;
	
	public City(){}
	
	public City(long id, String cityName, long creationTime,
			String servername, String worldname, Vector minimumPoint, Vector maximumPoint,
			double defaultCityManagerExpenseAllowance, double defaultCityMemberExpenseAllowance)
	{
		super(servername, worldname, minimumPoint, maximumPoint);
		setId(id);
		setCityName(cityName);
		setCreationTime(creationTime);
		setDefaultCityManagerExpenseAllowance(defaultCityManagerExpenseAllowance);
		setDefaultCityMemberExpenseAllowance(defaultCityMemberExpenseAllowance);
	}
	
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getCityName()
	{
		return cityName;
	}

	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	public long getCreationTime()
	{
		return creationTime;
	}

	public void setCreationTime(long creationTime)
	{
		this.creationTime = creationTime;
	}

	public double getDefaultCityManagerExpenseAllowance()
	{
		return defaultCityManagerExpenseAllowance;
	}

	public void setDefaultCityManagerExpenseAllowance(double defaultCityManagerExpenseAllowance)
	{
		this.defaultCityManagerExpenseAllowance = defaultCityManagerExpenseAllowance;
	}

	public double getDefaultCityMemberExpenseAllowance()
	{
		return defaultCityMemberExpenseAllowance;
	}

	public void setDefaultCityMemberExpenseAllowance(double defaultCityMemberExpenseAllowance)
	{
		this.defaultCityMemberExpenseAllowance = defaultCityMemberExpenseAllowance;
	}

	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`city_name`, `creation_time`,"
					+ " `server_name`, `world_name`, `minimum_x`, `minimum_y`, `minimum_z`, `maximum_x`, `maximum_y`, `maximum_z`, "
					+ " `default_city_manager_expense_allowance`, `default_city_member_expense_allowance`) " 
					+ "VALUES("
					+ "?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?"
					+ ")";
			PreparedStatement ps = conn.prepareStatement(sql);
			//ps.setLong(1, getPropertyID());
			//ADDME
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
				+ "` SET `city_name` = ?, `creation_time` = ?,"
				+ " `server_name` = ?, `world_name` = ?, `minimum_x` = ?, `minimum_y` = ?, `minimum_z` = ?,"
				+ " `maximum_x` = ?, `maximum_y` = ?, `maximum_z` = ?,"
				+ " `default_city_manager_expense_allowance` = ?, `default_city_member_expense_allowance` = ?" 
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			//ps.setLong(1, getID());
			//ADDME
			int i = 0;
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
				al.add(new City(
						rs.getLong("id"),
						rs.getString("city_name"),
						rs.getLong("creation_time"),
						rs.getString("server_name"),
						rs.getString("world_name"),
						new Vector(rs.getInt("minimum_x"), rs.getInt("minimum_y"), rs.getInt("minimum_z")),
						new Vector(rs.getInt("maximum_x"), rs.getInt("maximum_y"), rs.getInt("maximum_z")),
						rs.getDouble("default_city_manager_expense_allowance"),
						rs.getDouble("default_city_member_expense_allowance")
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
				al.add(new City(
						rs.getLong("id"),
						rs.getString("city_name"),
						rs.getLong("creation_time"),
						rs.getString("server_name"),
						rs.getString("world_name"),
						new Vector(rs.getInt("minimum_x"), rs.getInt("minimum_y"), rs.getInt("minimum_z")),
						new Vector(rs.getInt("maximum_x"), rs.getInt("maximum_y"), rs.getInt("maximum_z")),
						rs.getDouble("default_city_manager_expense_allowance"),
						rs.getDouble("default_city_member_expense_allowance")
						));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<City> convert(ArrayList<Object> arrayList)
	{
		ArrayList<City> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof City)
			{
				l.add((City) o);
			}
		}
		return l;
	}
	
	public void create()
	{
		final long now = this.creationTime;
		CAS.getPlugin().getMysqlHandler().create(MysqlType.CITY, this);
		if(this.getServername().equals(CAS.getPlugin().getServername()))
		{
			City p = (City) CAS.getPlugin().getMysqlHandler().getData(MysqlType.CITY, "`creation_time` = ?", now);
			MemoryHandler.addCity(p.getId(), p);
		}
	}
	
	public void saveRAM()
	{
		MemoryHandler.addCity(getId(), this);
	}
	
	public void saveMysql()
	{
		CAS.getPlugin().getMysqlHandler().updateData(MysqlType.CITY, this, "`id` = ?", this.id);
	}
}