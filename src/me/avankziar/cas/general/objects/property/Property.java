package me.avankziar.cas.general.objects.property;

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
import me.avankziar.cas.general.objects.MonumentType;
import me.avankziar.cas.general.objects.Region3D;
import me.avankziar.cas.spigot.CAS;
import me.avankziar.cas.spigot.database.MysqlHandler;
import me.avankziar.cas.spigot.handler.region.MemoryHandler;

public class Property extends Region3D implements MysqlHandable, MemoryHandable
{
	public enum PropertyStatus
	{
		FREE, MONUMENT, BUYABLE, RENTABLE, BOUGHT, RENTED;
	}
	
	private long id;
	private long districtID;
	private long cityID;
	private String propertyName;
	private long creationTime;
	
	private PropertyStatus actualPropertyStatus;
	private PropertyStatus futurePropertyStatus;
	private long timeUntilPropertyStatusChange;
	
	private MonumentType actualMonumentType;
	private MonumentType futureMonumentType;
	
	private double actualBuyPrice;
	private double futureBuyPrice;
	
	private double actualRentPrice;
	private double futureRentPrice;
	private double rentArrears; //Mietrückstand
	
	private boolean restorationActive;
	
	private int actualRedstoneBlocksAmount;
	private int maximumRedstoneBlocksAmount;

	private int actualLivestockAmount;
	private int maximumLivestockAmount;
	
	public Property(){}
	
	public Property(long id, long districtID, long cityID, String propertyName, long creationTime,
			String servername, String worldname, Vector minimumPoint, Vector maximumPoint,
			PropertyStatus actualPropertyStatus, PropertyStatus futurePropertyStatus, long timeUntilPropertyStatusChange,
			MonumentType actualMonumentType, MonumentType futureMonumentType,
			double actualBuyPrice, double futureBuyPrice,
			double actualRentPrice, double futureRentPrice, double rentArrears,
			boolean restorationActive,
			int actualRedstoneBlocksAmount, int maximumRedstoneBlocksAmount,
			int actualLivestockAmount, int maximumLivestockAmount)
	{
		super(servername, worldname, minimumPoint, maximumPoint);
		setID(id);
		setDistrictID(districtID);
		setCityID(cityID);
		setPropertyName(propertyName);
		setCreationTime(creationTime);
		
		setActualPropertyStatus(actualPropertyStatus);
		setFuturePropertyStatus(futurePropertyStatus);
		setTimeUntilPropertyStatusChange(timeUntilPropertyStatusChange);
		
		setActualMonumentType(actualMonumentType);
		setFutureMonumentType(futureMonumentType);
		
		setActualBuyPrice(actualBuyPrice);
		setFutureBuyPrice(futureBuyPrice);
		
		setActualRentPrice(actualRentPrice);
		setFutureRentPrice(futureRentPrice);
		setRentArrears(rentArrears);
		
		setRestorationActive(restorationActive);
		
		setActualRedstoneBlocksAmount(actualRedstoneBlocksAmount);
		setMaximumRedstoneBlocksAmount(maximumRedstoneBlocksAmount);
		
		setActualLivestockAmount(actualLivestockAmount);
		setMaximumLivestockAmount(maximumLivestockAmount);
		
	}

	public long getID()
	{
		return id;
	}
	
	public void setID(long id)
	{
		this.id = id;
	}
	
	public long getId()
	{
		return id;
	}

	public void setId(long id)
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

	public long getCityID()
	{
		return cityID;
	}

	public void setCityID(long cityID)
	{
		this.cityID = cityID;
	}

	public String getPropertyName()
	{
		return propertyName;
	}

	public void setPropertyName(String propertyName)
	{
		this.propertyName = propertyName;
	}

	public long getCreationTime()
	{
		return creationTime;
	}

	public void setCreationTime(long creationTime)
	{
		this.creationTime = creationTime;
	}

	public PropertyStatus getActualPropertyStatus()
	{
		return actualPropertyStatus;
	}

	public void setActualPropertyStatus(PropertyStatus actualPropertyStatus)
	{
		this.actualPropertyStatus = actualPropertyStatus;
	}

	public PropertyStatus getFuturePropertyStatus()
	{
		return futurePropertyStatus;
	}

	public void setFuturePropertyStatus(PropertyStatus futurePropertyStatus)
	{
		this.futurePropertyStatus = futurePropertyStatus;
	}

	public long getTimeUntilPropertyStatusChange()
	{
		return timeUntilPropertyStatusChange;
	}

	public void setTimeUntilPropertyStatusChange(long timeUntilPropertyStatusChange)
	{
		this.timeUntilPropertyStatusChange = timeUntilPropertyStatusChange;
	}

	public MonumentType getActualMonumentType()
	{
		return actualMonumentType;
	}

	public void setActualMonumentType(MonumentType actualMonumentType)
	{
		this.actualMonumentType = actualMonumentType;
	}

	public MonumentType getFutureMonumentType()
	{
		return futureMonumentType;
	}

	public void setFutureMonumentType(MonumentType futureMonumentType)
	{
		this.futureMonumentType = futureMonumentType;
	}

	public double getActualBuyPrice()
	{
		return actualBuyPrice;
	}

	public void setActualBuyPrice(double actualBuyPrice)
	{
		this.actualBuyPrice = actualBuyPrice;
	}

	public double getFutureBuyPrice()
	{
		return futureBuyPrice;
	}

	public void setFutureBuyPrice(double futureBuyPrice)
	{
		this.futureBuyPrice = futureBuyPrice;
	}

	public double getActualRentPrice()
	{
		return actualRentPrice;
	}

	public void setActualRentPrice(double actualRentPrice)
	{
		this.actualRentPrice = actualRentPrice;
	}

	public double getFutureRentPrice()
	{
		return futureRentPrice;
	}

	public void setFutureRentPrice(double futureRentPrice)
	{
		this.futureRentPrice = futureRentPrice;
	}

	public double getRentArrears()
	{
		return rentArrears;
	}

	public void setRentArrears(double rentArrears)
	{
		this.rentArrears = rentArrears;
	}

	public boolean isRestorationActive()
	{
		return restorationActive;
	}

	public void setRestorationActive(boolean restorationActive)
	{
		this.restorationActive = restorationActive;
	}

	public int getActualRedstoneBlocksAmount()
	{
		return actualRedstoneBlocksAmount;
	}

	public void setActualRedstoneBlocksAmount(int actualRedstoneBlocks)
	{
		this.actualRedstoneBlocksAmount = actualRedstoneBlocks;
	}

	public int getMaximumRedstoneBlocksAmount()
	{
		return maximumRedstoneBlocksAmount;
	}

	public void setMaximumRedstoneBlocksAmount(int maximumRedstoneBlocks)
	{
		this.maximumRedstoneBlocksAmount = maximumRedstoneBlocks;
	}

	public int getActualLivestockAmount()
	{
		return actualLivestockAmount;
	}

	public void setActualLivestockAmount(int actualLivestockAmount)
	{
		this.actualLivestockAmount = actualLivestockAmount;
	}

	public int getMaximumLivestockAmount()
	{
		return maximumLivestockAmount;
	}

	public void setMaximumLivestockAmount(int maximumLivestockAmount)
	{
		this.maximumLivestockAmount = maximumLivestockAmount;
	}

	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`district_id`, `city_id`, `property_name`, `creation_time`,"
					+ " `server_name`, `world_name`, `minimum_x`, `minimum_y`, `minimum_z`, `maximum_x`, `maximum_y`, `maximum_z`, "
					+ " `actual_property_status`, `future_property_status`, `time_until_property_status_change`,"
					+ " `actual_monument_type`, `future_monument_type`,"
					+ " `actual_buy_price`, `future_buy_price`,"
					+ " `actual_rent_price`, `future_rent_price`, `rent_arrears`,"
					+ " `restoration_active`,"
					+ " `actual_redstoneblock_amount`, `maximum_redstoneblock_amount`,"
					+ " `actual_livestock_amount`, `maximum_livestock_amount`) " 
					+ "VALUES("
					+ "?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?,"
					+ "?, ?,"
					+ "?, ?,"
					+ "?, ?, ?,"
					+ "?,"
					+ "?, ?,"
					+ "?, ?"
					+ ")";
			PreparedStatement ps = conn.prepareStatement(sql);
			//ps.setLong(1, getPropertyID());
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
				+ "` SET `id` = ?, `district_id` = ?, `city_id` = ?, `property_name` = ?, `creation_time` = ?,"
				+ " `server_name` = ?, `world_name` = ?, `minimum_x` = ?, `minimum_y` = ?, `minimum_z` = ?,"
				+ " `maximum_x` = ?, `maximum_y` = ?, `maximum_z` = ?,"
				+ " `actual_property_status` = ?, `future_property_status` = ?, `time_until_property_status_change` = ?,"
				+ " `actual_monument_type` = ?, `future_monument_type` = ?,"
				+ " `actual_buy_price` = ?, `future_buy_price` = ?,"
				+ " `actual_rent_price` = ?, `future_rent_price` = ?, `rent_arrears` = ?,"
				+ " `restoration_active` = ?,"
				+ " `actual_redstoneblock_amount` = ?, `maximum_redstoneblock_amount` = ?,"
				+ " `actual_livestock_amount` = ?, `maximum_livestock_amount` = ?" 
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, getID());
			
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
				al.add(new Property(
						rs.getLong("id"),
						rs.getLong("district_id"),
						rs.getLong("city_id"),
						rs.getString("property_name"),
						rs.getLong("creation_time"),
						rs.getString("server_name"),
						rs.getString("world_name"),
						new Vector(rs.getInt("minimum_x"), rs.getInt("minimum_y"), rs.getInt("minimum_z")),
						new Vector(rs.getInt("maximum_x"), rs.getInt("maximum_y"), rs.getInt("maximum_z")),
						PropertyStatus.valueOf(rs.getString("actual_property_status")),
						PropertyStatus.valueOf(rs.getString("future_property_status")),
						rs.getLong("time_until_property_status_change"),
						MonumentType.valueOf(rs.getString("actual_monument_type")),
						MonumentType.valueOf(rs.getString("future_monument_type")),
						rs.getDouble("actual_buy_price"),
						rs.getDouble("future_buy_price"),
						rs.getDouble("actual_rent_price"),
						rs.getDouble("future_rent_price"),
						rs.getDouble("rent_arrears"),
						rs.getBoolean("restoration_active"),
						rs.getInt("actual_redstoneblock_amount"),
						rs.getInt("maximum_redstoneblock_amount"),
						rs.getInt("actual_livestock_amount"),
						rs.getInt("maximum_livestock_amount")
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
				al.add(new Property(
						rs.getLong("id"),
						rs.getLong("district_id"),
						rs.getLong("city_id"),
						rs.getString("property_name"),
						rs.getLong("creation_time"),
						rs.getString("server_name"),
						rs.getString("world_name"),
						new Vector(rs.getInt("minimum_x"), rs.getInt("minimum_y"), rs.getInt("minimum_z")),
						new Vector(rs.getInt("maximum_x"), rs.getInt("maximum_y"), rs.getInt("maximum_z")),
						PropertyStatus.valueOf(rs.getString("actual_property_status")),
						PropertyStatus.valueOf(rs.getString("future_property_status")),
						rs.getLong("time_until_property_status_change"),
						MonumentType.valueOf(rs.getString("actual_monument_type")),
						MonumentType.valueOf(rs.getString("future_monument_type")),
						rs.getDouble("actual_buy_price"),
						rs.getDouble("future_buy_price"),
						rs.getDouble("actual_rent_price"),
						rs.getDouble("future_rent_price"),
						rs.getDouble("rent_arrears"),
						rs.getBoolean("restoration_active"),
						rs.getInt("actual_redstoneblock_amount"),
						rs.getInt("maximum_redstoneblock_amount"),
						rs.getInt("actual_livestock_amount"),
						rs.getInt("maximum_livestock_amount")
						));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<Property> convert(ArrayList<Object> arrayList)
	{
		ArrayList<Property> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof Property)
			{
				l.add((Property) o);
			}
		}
		return l;
	}
	
	public void create()
	{
		CAS.getPlugin().getMysqlHandler().create(MysqlType.PROPERTY, this);
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
		MemoryHandler.addProperty(getId(), this);
	}
	
	public void saveMysql()
	{
		CAS.getPlugin().getMysqlHandler().updateData(MysqlType.PROPERTY, this, "`id` = ?", this.id);
	}
}