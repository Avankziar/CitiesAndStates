package me.avankziar.cas.general.objects.district;

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

public class DistrictManager implements MysqlHandable, MemoryHandable
{
	private long id;
	private long districtID;
	private UUID uuid;
	private double expenseAllowance; //Aufwandsentschädigung
	
	public DistrictManager(){}
	
	public DistrictManager(long id, long districtID, UUID uuid, double expenseAllowance)
	{
		setID(id);
		setDistrictID(districtID);
		setUUID(uuid);
		setExpenseAllowance(expenseAllowance);
	}

	public long getId()
	{
		return id;
	}

	public void setID(long id)
	{
		this.id = id;
	}

	public long getDistrictID()
	{
		return districtID;
	}

	public void setDistrictID(long districtID)
	{
		this.districtID = districtID;
	}

	public UUID getUUID()
	{
		return uuid;
	}

	public void setUUID(UUID uuid)
	{
		this.uuid = uuid;
	}
	
	public double getExpenseAllowance()
	{
		return expenseAllowance;
	}

	public void setExpenseAllowance(double expenseAllowance)
	{
		this.expenseAllowance = expenseAllowance;
	}

	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`district_id`, `player_uuid`, `expense_allowance`) " 
					+ "VALUES(?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, getDistrictID());
	        ps.setString(2, getUUID().toString());
	        ps.setDouble(3, getExpenseAllowance());
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
				+ "` SET `district_id` = ?, `player_uuid` = ?, `expense_allowance` = ?" 
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, getDistrictID());
	        ps.setString(2, getUUID().toString());
	        ps.setDouble(3, getExpenseAllowance());
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
				al.add(new DistrictManager(rs.getLong("id"),
						rs.getLong("district_id"),
						UUID.fromString(rs.getString("player_uuid")),
						rs.getDouble("expense_allowance")));
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
				al.add(new DistrictManager(rs.getLong("id"),
						rs.getLong("district_id"),
						UUID.fromString(rs.getString("player_uuid")),
						rs.getDouble("expense_allowance")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<DistrictManager> convert(ArrayList<Object> arrayList)
	{
		ArrayList<DistrictManager> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof DistrictManager)
			{
				l.add((DistrictManager) o);
			}
		}
		return l;
	}
	
	public void create()
	{
		final long pid = this.districtID;
		CAS.getPlugin().getMysqlHandler().create(MysqlType.DISTRICT_MANAGER, this);
		if(MemoryHandler.getDistrict(pid) != null)
		{
			saveRAM();
		}
	}
	
	public void saveRAM()
	{
		MemoryHandler.addDistrictManager(getId(), this);
	}
	
	public void saveMysql()
	{
		CAS.getPlugin().getMysqlHandler().updateData(MysqlType.DISTRICT_MANAGER, this, "`id` = ?", this.id);
	}
}