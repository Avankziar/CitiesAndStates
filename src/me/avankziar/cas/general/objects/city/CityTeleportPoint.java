package me.avankziar.cas.general.objects.city;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.util.Vector;

import me.avankziar.cas.general.database.MysqlHandable;
import me.avankziar.cas.general.database.QueryType;
import me.avankziar.cas.general.objects.ListType;
import me.avankziar.cas.general.objects.TeleportPoint;
import me.avankziar.cas.spigot.database.MysqlHandler;

public class CityTeleportPoint extends TeleportPoint implements MysqlHandable
{
	private long cityID;
	private ListType listType;
	
	public CityTeleportPoint(){}
	
	public CityTeleportPoint(long id, long cityID, String teleportname, ListType listType, 
			String servername, String worldname, Vector point, float yaw, float pitch)
	{
		super(id, teleportname, servername, worldname, point.getX(), point.getY(), point.getZ(), yaw, pitch);
		setCityID(cityID);
		setListType(listType);
	}

	public long getCityID()
	{
		return cityID;
	}

	public void setCityID(long cityID)
	{
		this.cityID = cityID;
	}

	public ListType getListType()
	{
		return listType;
	}

	public void setListType(ListType listType)
	{
		this.listType = listType;
	}
	
	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`city_id`, `teleport_name`, `list_type`,"
					+ " `server_name`, `world_name`, `x`, `y`, `z`, `yaw`, `pitch`) " 
					+ "VALUES("
					+ "?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?"
					+ ")";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, getCityID());
			ps.setString(2, getTeleportname());
			ps.setString(3, getListType().toString());
			ps.setString(4, getServername());
			ps.setString(5, getWorldname());
			ps.setDouble(6, getX());
			ps.setDouble(7, getY());
			ps.setDouble(8, getZ());
			ps.setFloat(9, getYaw());
			ps.setFloat(10, getPitch());
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
				+ "` SET `city_id` = ?, `teleport_name` = ?, `list_type` = ?,"
				+ " `server_name` = ?, `world_name` = ?, `x` = ?, `y` = ?, `z` = ?,"
				+ " `yaw` = ?, `pitch` = ?" 
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, getCityID());
			ps.setString(2, getTeleportname());
			ps.setString(3, getListType().toString());
			ps.setString(4, getServername());
			ps.setString(5, getWorldname());
			ps.setDouble(6, getX());
			ps.setDouble(7, getY());
			ps.setDouble(8, getZ());
			ps.setFloat(9, getYaw());
			ps.setFloat(10, getPitch());
			int i = 11;
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
				al.add(new CityTeleportPoint(
						rs.getLong("id"),
						rs.getLong("city_id"),
						rs.getString("teleport_name"),
						ListType.valueOf(rs.getString("list_type")),
						rs.getString("server_name"),
						rs.getString("world_name"),
						new Vector(rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z")),
						rs.getFloat("yaw"), rs.getFloat("pitch")
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
				al.add(new CityTeleportPoint(
						rs.getLong("id"),
						rs.getLong("city_id"),
						rs.getString("teleport_name"),
						ListType.valueOf(rs.getString("list_type")),
						rs.getString("server_name"),
						rs.getString("world_name"),
						new Vector(rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z")),
						rs.getFloat("yaw"), rs.getFloat("pitch")
						));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<CityTeleportPoint> convert(ArrayList<Object> arrayList)
	{
		ArrayList<CityTeleportPoint> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof CityTeleportPoint)
			{
				l.add((CityTeleportPoint) o);
			}
		}
		return l;
	}
}