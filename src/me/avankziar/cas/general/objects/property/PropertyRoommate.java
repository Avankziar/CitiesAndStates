package me.avankziar.cas.general.objects.property;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import me.avankziar.cas.general.database.MemoryHandable;
import me.avankziar.cas.general.database.MysqlHandable;
import me.avankziar.cas.general.database.MysqlType;
import me.avankziar.cas.general.database.QueryType;
import me.avankziar.cas.spigot.CAS;
import me.avankziar.cas.spigot.database.MysqlHandler;
import me.avankziar.cas.spigot.handler.region.MemoryHandler;
import me.avankziar.cas.spigot.handler.region.PropertyHandler;

//Lives in a property, but pays no rent and has no other rights.
public class PropertyRoommate implements MysqlHandable, MemoryHandable
{
	private long id;
	private long propertyID;
	private UUID uuid;
	
	public PropertyRoommate(){}
	
	public PropertyRoommate(long id, long propertyID, UUID uuid)
	{
		setID(id);
		setPropertyID(propertyID);
		setUUID(uuid);
	}

	public long getId()
	{
		return id;
	}

	public void setID(long id)
	{
		this.id = id;
	}

	public long getPropertyID()
	{
		return propertyID;
	}

	public void setPropertyID(long propertyID)
	{
		this.propertyID = propertyID;
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
					+ "`(`property_id`, `player_uuid`) " 
					+ "VALUES(?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, getPropertyID());
	        ps.setString(2, getUUID().toString());
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
				+ "` SET `property_id` = ?, `player_uuid` = ?" 
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, getPropertyID());
	        ps.setString(2, getUUID().toString());
			int i = 3;
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
				al.add(new PropertyRoommate(rs.getLong("id"),
						rs.getLong("property_id"),
						UUID.fromString(rs.getString("player_uuid"))));
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
				al.add(new PropertyRoommate(rs.getLong("id"),
						rs.getLong("property_id"),
						UUID.fromString(rs.getString("player_uuid"))));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<PropertyRoommate> convert(ArrayList<Object> arrayList)
	{
		ArrayList<PropertyRoommate> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof PropertyRoommate)
			{
				l.add((PropertyRoommate) o);
			}
		}
		return l;
	}
	
	public void create()
	{
		final long pid = this.propertyID;
		final UUID uuid = this.uuid;
		Property pro = PropertyHandler.getProperty(pid);
		CAS.getPlugin().getMysqlHandler().create(MysqlType.PROPERTY_ROOMMATE, this);
		if(pro != null)
		{
			PropertyRoommate po = (PropertyRoommate) CAS.getPlugin().getMysqlHandler().getData(MysqlType.PROPERTY_ROOMMATE,
					"`property_id` = ? AND `player_uuid` = ?", pid, uuid.toString());
			MemoryHandler.addPropertyRoommate(po.getId(), po);
		}
	}
	
	public void saveRAM()
	{
		MemoryHandler.addPropertyRoommate(getId(), this);
	}
	
	public void saveMysql()
	{
		CAS.getPlugin().getMysqlHandler().updateData(MysqlType.PROPERTY_ROOMMATE, this, "`id` = ?", this.id);
	}
}