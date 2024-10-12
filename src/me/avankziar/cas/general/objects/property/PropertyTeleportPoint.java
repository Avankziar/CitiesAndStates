package me.avankziar.cas.general.objects.property;

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

public class PropertyTeleportPoint extends TeleportPoint implements MysqlHandable
{
	private long propertyID;
	private ListType listType;
	
	public PropertyTeleportPoint(){}
	
	public PropertyTeleportPoint(long id, long propertyID, String teleportname, ListType listType, 
			String servername, String worldname, Vector point, float yaw, float pitch)
	{
		super(id, teleportname, servername, worldname, point.getX(), point.getY(), point.getZ(), yaw, pitch);
		setPropertyID(propertyID);
		setListType(listType);
	}

	public long getPropertyID()
	{
		return propertyID;
	}

	public void setPropertyID(long propertyID)
	{
		this.propertyID = propertyID;
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
					+ "`(`property_id`, `teleport_name`, `list_type`,"
					+ " `server_name`, `world_name`, `x`, `y`, `z`, `yaw`, `pitch`) " 
					+ "VALUES("
					+ "?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?"
					+ ")";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, getPropertyID());
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
				+ "` SET `property_id` = ?, `teleport_name` = ?, `list_type` = ?,"
				+ " `server_name` = ?, `world_name` = ?, `x` = ?, `y` = ?, `z` = ?,"
				+ " `yaw` = ?, `pitch` = ?" 
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, getPropertyID());
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
				al.add(new PropertyTeleportPoint(
						rs.getLong("id"),
						rs.getLong("property_id"),
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
				al.add(new PropertyTeleportPoint(
						rs.getLong("id"),
						rs.getLong("property_id"),
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
	
	public static ArrayList<PropertyTeleportPoint> convert(ArrayList<Object> arrayList)
	{
		ArrayList<PropertyTeleportPoint> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof PropertyTeleportPoint)
			{
				l.add((PropertyTeleportPoint) o);
			}
		}
		return l;
	}
}