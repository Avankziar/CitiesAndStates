package me.avankziar.cas.general.objects.district;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.util.Vector;

import me.avankziar.cas.general.database.MemoryHandable;
import me.avankziar.cas.general.database.MysqlHandable;
import me.avankziar.cas.general.database.MysqlType;
import me.avankziar.cas.general.database.QueryType;
import me.avankziar.cas.general.objects.Region3D;
import me.avankziar.cas.spigot.CAS;
import me.avankziar.cas.spigot.database.MysqlHandler;
import me.avankziar.cas.spigot.handler.region.MemoryHandler;

public class District extends Region3D implements MysqlHandable, MemoryHandable
{
	private long id;
	private long cityID;
	private String districtName;
	private long creationTime;
	
	private double defaultDistrictManagerExpenseAllowance;
	private double defaultDistrictMemberExpenseAllowance;
	
	public District(){}
	
	public District(long id, long cityID, String districtName, long creationTime,
			String servername, String worldname, Vector minimumPoint, Vector maximumPoint,
			double defaultDistrictManagerExpenseAllowance, double defaultDistrictMemberExpenseAllowance)
	{
		super(servername, worldname, minimumPoint, maximumPoint);
		setId(id);
		setCityID(cityID);
		setDistrictName(districtName);
		setDefaultDistrictManagerExpenseAllowance(defaultDistrictManagerExpenseAllowance);
		setDefaultDistrictMemberExpenseAllowance(defaultDistrictMemberExpenseAllowance);
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

	public String getDistrictName()
	{
		return districtName;
	}

	public void setDistrictName(String districtName)
	{
		this.districtName = districtName;
	}

	public long getCreationTime()
	{
		return creationTime;
	}

	public void setCreationTime(long creationTime)
	{
		this.creationTime = creationTime;
	}

	public double getDefaultDistrictManagerExpenseAllowance()
	{
		return defaultDistrictManagerExpenseAllowance;
	}

	public void setDefaultDistrictManagerExpenseAllowance(double defaultDistrictManagerExpenseAllowance)
	{
		this.defaultDistrictManagerExpenseAllowance = defaultDistrictManagerExpenseAllowance;
	}

	public double getDefaultDistrictMemberExpenseAllowance()
	{
		return defaultDistrictMemberExpenseAllowance;
	}

	public void setDefaultDistrictMemberExpenseAllowance(double defaultDistrictMemberExpenseAllowance)
	{
		this.defaultDistrictMemberExpenseAllowance = defaultDistrictMemberExpenseAllowance;
	}

	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`city_id`, `district_name`, `creation_time`,"
					+ " `server_name`, `world_name`, `minimum_x`, `minimum_y`, `minimum_z`, `maximum_x`, `maximum_y`, `maximum_z`, "
					+ " `default_district_manager_expense_allowance`, `default_district_member_expense_allowance`) " 
					+ "VALUES("
					+ "?, ?, ?,"
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
				+ "` SET `id` = ?, `city_id` = ?, `district_name` = ?, `creation_time` = ?,"
				+ " `server_name` = ?, `world_name` = ?, `minimum_x` = ?, `minimum_y` = ?, `minimum_z` = ?,"
				+ " `maximum_x` = ?, `maximum_y` = ?, `maximum_z` = ?,"
				+ " `default_district_manager_expense_allowance` = ?, `default_district_member_expense_allowance` = ?" 
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
				al.add(new District(
						rs.getLong("id"),
						rs.getLong("city_id"),
						rs.getString("district_name"),
						rs.getLong("creation_time"),
						rs.getString("server_name"),
						rs.getString("world_name"),
						new Vector(rs.getInt("minimum_x"), rs.getInt("minimum_y"), rs.getInt("minimum_z")),
						new Vector(rs.getInt("maximum_x"), rs.getInt("maximum_y"), rs.getInt("maximum_z")),
						rs.getDouble("default_district_manager_expense_allowance"),
						rs.getDouble("default_district_member_expense_allowance")
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
				al.add(new District(
						rs.getLong("id"),
						rs.getLong("city_id"),
						rs.getString("district_name"),
						rs.getLong("creation_time"),
						rs.getString("server_name"),
						rs.getString("world_name"),
						new Vector(rs.getInt("minimum_x"), rs.getInt("minimum_y"), rs.getInt("minimum_z")),
						new Vector(rs.getInt("maximum_x"), rs.getInt("maximum_y"), rs.getInt("maximum_z")),
						rs.getDouble("default_district_manager_expense_allowance"),
						rs.getDouble("default_district_member_expense_allowance")
						));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<District> convert(ArrayList<Object> arrayList)
	{
		ArrayList<District> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof District)
			{
				l.add((District) o);
			}
		}
		return l;
	}
	
	public void create()
	{
		CAS.getPlugin().getMysqlHandler().create(MysqlType.DISTRICT, this);
		if(this.getServername().equals(CAS.getPlugin().getServername()))
		{
			saveRAM();
		}
	}
	
	public void saveRAM()
	{
		if(!this.getServername().equals(CAS.getPlugin().getServername()))
		{
			return;
		}
		MemoryHandler.addDistrict(getId(), this);
	}
	
	public void saveMysql()
	{
		CAS.getPlugin().getMysqlHandler().updateData(MysqlType.DISTRICT, this, "`id` = ?", this.id);
	}
}