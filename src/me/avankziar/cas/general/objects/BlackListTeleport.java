package me.avankziar.cas.general.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import me.avankziar.cas.general.database.MysqlHandable;
import me.avankziar.cas.general.database.QueryType;
import me.avankziar.cas.spigot.database.MysqlHandler;

public class BlackListTeleport implements MysqlHandable
{
	public enum Type
	{
		CITY, PROPERTY;
	}
	
	private long id;
	private long valueID; //City or type id
	private Type type;
	private UUID uuid;
	
	public BlackListTeleport(){}
	
	public BlackListTeleport(long id, long valueID, BlackListTeleport.Type type, UUID uuid)
	{
		setId(id);
		setValueID(valueID);
		setType(type);
		setUUID(uuid);
	}
	
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getValueID()
	{
		return valueID;
	}

	public void setValueID(long valueID)
	{
		this.valueID = valueID;
	}

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
	{
		this.type = type;
	}

	public UUID getUUID()
	{
		return uuid;
	}

	public void setUUID(UUID uuid)
	{
		this.uuid = uuid;
	}

	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`value_id`, `teleport_type`, `player_uuid`) " 
					+ "VALUES("
					+ "?, ?, ?"
					+ ")";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, getValueID());
			ps.setString(2, getType().toString());
			ps.setString(3, getUUID().toString());
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
				+ "` SET `value_id` = ?, `teleport_type` = ?, `player_uuid` = ?" 
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, getValueID());
			ps.setString(2, getType().toString());
			ps.setString(3, getUUID().toString());
			int i = 4;
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
				al.add(new BlackListTeleport(
						rs.getLong("id"),
						rs.getLong("value_id"),
						BlackListTeleport.Type.valueOf(rs.getString("teleport_type")),
						UUID.fromString(rs.getString("player_uuid"))
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
				al.add(new BlackListTeleport(
						rs.getLong("id"),
						rs.getLong("value_id"),
						BlackListTeleport.Type.valueOf(rs.getString("teleport_type")),
						UUID.fromString(rs.getString("player_uuid"))
						));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<BlackListTeleport> convert(ArrayList<Object> arrayList)
	{
		ArrayList<BlackListTeleport> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof BlackListTeleport)
			{
				l.add((BlackListTeleport) o);
			}
		}
		return l;
	}
}