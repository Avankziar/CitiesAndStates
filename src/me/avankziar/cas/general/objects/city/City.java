package me.avankziar.cas.general.objects.city;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.util.Vector;

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
	private int additionalRedstoneBlocksAmount;

	private int actualLivestockAmount;
	private int additionalLivestockAmount;
	
	public City(){}
	
	public City(long id, String cityName, Material guiMaterial, ArrayList<String> cityDescription,
			long creationTime, UUID creator, Vector creationLocation, UUID governor,
			CityLevel cityLevel, CitySecurityLevel citySecurityLevel,
			String servername, String worldname, Vector minimumPoint, Vector maximumPoint,
			int cityIFHAccount, int cityStoredExperience, 
			boolean wouldLikeToLevelUp, boolean canLevelUp, String whatNeedsToBeDoneMessage, String lastLevelApprover,
			double defaultCityManagerExpenseAllowance, double defaultCityMemberExpenseAllowance,
			int additionalPurchasedDistricts, int additionalPurchasedProperties,
			int actualRedstoneBlocksAmount, int additionalRedstoneBlocksAmount,
			int actualLivestockAmount, int additionalLivestockAmount)
	{
		super(servername, worldname, minimumPoint, maximumPoint);
		setId(id);
		setCityName(cityName);
		setGuiMaterial(guiMaterial);
		setCityDescription(cityDescription);
		setCreationTime(creationTime);
		setCreator(creator);
		setCreationLocation(creationLocation);
		setGovernor(governor);
		setCityLevel(cityLevel);
		setCitySecurityLevel(citySecurityLevel);
		setCityIFHAccount(cityIFHAccount);
		setCityStoredExperience(cityStoredExperience);
		setWouldLikeToLevelUp(wouldLikeToLevelUp);
		setCanLevelUp(canLevelUp);
		setWhatNeedsToBeDoneMessage(whatNeedsToBeDoneMessage);
		setLastLevelApprover(lastLevelApprover);
		setDefaultCityManagerExpenseAllowance(defaultCityManagerExpenseAllowance);
		setDefaultCityMemberExpenseAllowance(defaultCityMemberExpenseAllowance);
		setAdditionalPurchasedDistricts(additionalPurchasedDistricts);
		setAdditionalPurchasedProperties(additionalPurchasedProperties);
		setActualRedstoneBlocksAmount(actualRedstoneBlocksAmount);
		setAdditionalRedstoneBlocksAmount(additionalRedstoneBlocksAmount);
		setActualLivestockAmount(actualLivestockAmount);
		setAdditionalLivestockAmount(additionalLivestockAmount);
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

	public Material getGuiMaterial()
	{
		return guiMaterial;
	}

	public void setGuiMaterial(Material guiMaterial)
	{
		this.guiMaterial = guiMaterial;
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

	public ArrayList<String> getCityDescription()
	{
		return cityDescription;
	}

	public void setCityDescription(ArrayList<String> cityDescription)
	{
		this.cityDescription = cityDescription;
	}

	public UUID getCreator()
	{
		return creator;
	}

	public void setCreator(UUID creator)
	{
		this.creator = creator;
	}

	public Vector getCreationLocation()
	{
		return creationLocation;
	}

	public void setCreationLocation(Vector creationLocation)
	{
		this.creationLocation = creationLocation;
	}

	public UUID getGovernor()
	{
		return governor;
	}

	public void setGovernor(UUID governor)
	{
		this.governor = governor;
	}

	public CityLevel getCityLevel()
	{
		return cityLevel;
	}

	public void setCityLevel(CityLevel cityLevel)
	{
		this.cityLevel = cityLevel;
	}

	public CitySecurityLevel getCitySecurityLevel()
	{
		return citySecurityLevel;
	}

	public void setCitySecurityLevel(CitySecurityLevel citySecurityLevel)
	{
		this.citySecurityLevel = citySecurityLevel;
	}

	public int getCityIFHAccount()
	{
		return cityIFHAccount;
	}

	public void setCityIFHAccount(int cityIFHAccount)
	{
		this.cityIFHAccount = cityIFHAccount;
	}

	public int getCityStoredExperience()
	{
		return cityStoredExperience;
	}

	public void setCityStoredExperience(int cityStoredExperience)
	{
		this.cityStoredExperience = cityStoredExperience;
	}

	public boolean isWouldLikeToLevelUp()
	{
		return wouldLikeToLevelUp;
	}

	public void setWouldLikeToLevelUp(boolean wouldLikeToLevelUp)
	{
		this.wouldLikeToLevelUp = wouldLikeToLevelUp;
	}

	public boolean isCanLevelUp()
	{
		return canLevelUp;
	}

	public void setCanLevelUp(boolean canLevelUp)
	{
		this.canLevelUp = canLevelUp;
	}

	public String getWhatNeedsToBeDoneMessage()
	{
		return whatNeedsToBeDoneMessage;
	}

	public void setWhatNeedsToBeDoneMessage(String whatNeedsToBeDoneMessage)
	{
		this.whatNeedsToBeDoneMessage = whatNeedsToBeDoneMessage;
	}

	public String getLastLevelApprover()
	{
		return lastLevelApprover;
	}

	public void setLastLevelApprover(String lastLevelApprover)
	{
		this.lastLevelApprover = lastLevelApprover;
	}

	public int getAdditionalPurchasedDistricts()
	{
		return additionalPurchasedDistricts;
	}

	public void setAdditionalPurchasedDistricts(int additionalPurchasedDistricts)
	{
		this.additionalPurchasedDistricts = additionalPurchasedDistricts;
	}

	public int getAdditionalPurchasedProperties()
	{
		return additionalPurchasedProperties;
	}

	public void setAdditionalPurchasedProperties(int additionalPurchasedProperties)
	{
		this.additionalPurchasedProperties = additionalPurchasedProperties;
	}

	public int getActualRedstoneBlocksAmount()
	{
		return actualRedstoneBlocksAmount;
	}

	public void setActualRedstoneBlocksAmount(int actualRedstoneBlocksAmount)
	{
		this.actualRedstoneBlocksAmount = actualRedstoneBlocksAmount;
	}

	public int getAdditionalRedstoneBlocksAmount()
	{
		return additionalRedstoneBlocksAmount;
	}

	public void setAdditionalRedstoneBlocksAmount(int additionalRedstoneBlocksAmount)
	{
		this.additionalRedstoneBlocksAmount = additionalRedstoneBlocksAmount;
	}

	public int getActualLivestockAmount()
	{
		return actualLivestockAmount;
	}

	public void setActualLivestockAmount(int actualLivestockAmount)
	{
		this.actualLivestockAmount = actualLivestockAmount;
	}

	public int getAdditionalLivestockAmount()
	{
		return additionalLivestockAmount;
	}

	public void setAdditionalLivestockAmount(int additionalLivestockAmount)
	{
		this.additionalLivestockAmount = additionalLivestockAmount;
	}

	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`city_name`, `gui_material`, `city_description`,"
					+ " `creation_time`, `creator_uuid`, `creation_x`, `creation_y`, `creation_z`,"
					+ " `governor_uuid`, `city_level`, `city_security_level`,"
					+ " `server_name`, `world_name`, `minimum_x`, `minimum_y`, `minimum_z`, `maximum_x`, `maximum_y`, `maximum_z`,"
					+ " `city_ifh_account`, `city_stored_experience`,"
					+ " `would_like_to_level_up`, `can_level_up`, `what_needs_to_be_done_message`, `last_level_approver`,"
					+ " `default_city_manager_expense_allowance`, `default_city_member_expense_allowance`,"
					+ " `additional_purchased_districts`, `additional_purchased_properties`,"
					+ " `actual_redstone_blocks_amount`, `additional_redstone_blocks_amount`,"
					+ " `actual_livestock_amount`, `additional_livestock_amount`) " 
					+ "VALUES("
					+ "?, ?, ?,"
					+ "?, ?, ?, ?, ?,"
					+ "?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?,"
					+ "?, ?, ?, ?,"
					+ "?, ?,"
					+ "?, ?,"
					+ "?, ?,"
					+ "?, ?"
					+ ")";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getCityName());
			ps.setString(2, getGuiMaterial().toString());
			ps.setString(3, String.join("!::!", getCityDescription()));
			ps.setLong(4, getCreationTime());
			ps.setString(5, getCreator().toString());
			ps.setInt(6, getCreationLocation().getBlockX());
			ps.setInt(7, getCreationLocation().getBlockY());
			ps.setInt(8, getCreationLocation().getBlockZ());
			ps.setString(9, getGovernor().toString());
			ps.setInt(10, getCityLevel().getOrdinal());
			ps.setInt(11, getCitySecurityLevel().getOrdinal());
			ps.setString(12, getServername());
			ps.setString(13, getWorldname());
			ps.setInt(14, getMinimumPoint().getBlockX());
			ps.setInt(15, getMinimumPoint().getBlockY());
			ps.setInt(16, getMinimumPoint().getBlockZ());
			ps.setInt(17, getMaximumPoint().getBlockX());
			ps.setInt(18, getMaximumPoint().getBlockY());
			ps.setInt(19, getMaximumPoint().getBlockZ());
			ps.setInt(20, getCityIFHAccount());
			ps.setInt(21, getCityStoredExperience());
			ps.setBoolean(22, isWouldLikeToLevelUp());
			ps.setBoolean(23, isCanLevelUp());
			ps.setString(24, getWhatNeedsToBeDoneMessage());
			ps.setString(25, getLastLevelApprover());
			ps.setDouble(26, getDefaultCityManagerExpenseAllowance());
			ps.setDouble(27, getDefaultCityMemberExpenseAllowance());
			ps.setInt(28, getAdditionalPurchasedDistricts());
			ps.setInt(29, getAdditionalPurchasedProperties());
			ps.setInt(30, getActualRedstoneBlocksAmount());
			ps.setInt(31, getAdditionalRedstoneBlocksAmount());
			ps.setInt(32, getActualLivestockAmount());
			ps.setInt(33, getAdditionalLivestockAmount());
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
				+ "` SET `city_name` = ?, `gui_material` = ?, `city_description` = ?,"
				+ " `creation_time` = ?, `creator_uuid` = ?, `creation_x` = ?, `creation_y` = ?, `creation_z` = ?,"
				+ " `governor_uuid` = ?, `city_level` = ?, `city_security_level` = ?,"
				+ " `server_name` = ?, `world_name` = ?, `minimum_x` = ?, `minimum_y` = ?, `minimum_z` = ?,"
				+ " `maximum_x` = ?, `maximum_y` = ?, `maximum_z` = ?,"
				+ " `city_ifh_account` = ?, `city_stored_experience` = ?,"
				+ " `would_like_to_level_up` = ?, `can_level_up` = ?, `what_needs_to_be_done_message` = ?, `last_level_approver` = ?,"
				+ " `default_city_manager_expense_allowance` = ?, `default_city_member_expense_allowance` = ?,"
				+ " `additional_purchased_districts` = ?, `additional_purchased_properties` = ?,"
				+ " `actual_redstone_blocks_amount` = ?, `additional_redstone_blocks_amount` = ?,"
				+ " `actual_livestock_amount` = ?, `additional_livestock_amount` = ?" 
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getCityName());
			ps.setString(2, getGuiMaterial().toString());
			ps.setString(3, String.join("!::!", getCityDescription()));
			ps.setLong(4, getCreationTime());
			ps.setString(5, getCreator().toString());
			ps.setInt(6, getCreationLocation().getBlockX());
			ps.setInt(7, getCreationLocation().getBlockY());
			ps.setInt(8, getCreationLocation().getBlockZ());
			ps.setString(9, getGovernor().toString());
			ps.setInt(10, getCityLevel().getOrdinal());
			ps.setInt(11, getCitySecurityLevel().getOrdinal());
			ps.setString(12, getServername());
			ps.setString(13, getWorldname());
			ps.setInt(14, getMinimumPoint().getBlockX());
			ps.setInt(15, getMinimumPoint().getBlockY());
			ps.setInt(16, getMinimumPoint().getBlockZ());
			ps.setInt(17, getMaximumPoint().getBlockX());
			ps.setInt(18, getMaximumPoint().getBlockY());
			ps.setInt(19, getMaximumPoint().getBlockZ());
			ps.setInt(20, getCityIFHAccount());
			ps.setInt(21, getCityStoredExperience());
			ps.setBoolean(22, isWouldLikeToLevelUp());
			ps.setBoolean(23, isCanLevelUp());
			ps.setString(24, getWhatNeedsToBeDoneMessage());
			ps.setString(25, getLastLevelApprover());
			ps.setDouble(26, getDefaultCityManagerExpenseAllowance());
			ps.setDouble(27, getDefaultCityMemberExpenseAllowance());
			ps.setInt(28, getAdditionalPurchasedDistricts());
			ps.setInt(29, getAdditionalPurchasedProperties());
			ps.setInt(30, getActualRedstoneBlocksAmount());
			ps.setInt(31, getAdditionalRedstoneBlocksAmount());
			ps.setInt(32, getActualLivestockAmount());
			ps.setInt(33, getAdditionalLivestockAmount());
			int i = 34;
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
						Material.valueOf(rs.getString("gui_material")),
						(ArrayList<String>) Arrays.asList(rs.getString("city_description").split("!::!")),
						rs.getLong("creation_time"),
						UUID.fromString(rs.getString("creator_uuid")),
						new Vector(rs.getInt("creation_x"), rs.getInt("creation_y"), rs.getInt("creation_z")),
						UUID.fromString(rs.getString("governor_uuid")),
						CityLevel.get(rs.getInt("city_level")),
						CitySecurityLevel.get(rs.getInt("city_security_level")),
						rs.getString("server_name"),
						rs.getString("world_name"),
						new Vector(rs.getInt("minimum_x"), rs.getInt("minimum_y"), rs.getInt("minimum_z")),
						new Vector(rs.getInt("maximum_x"), rs.getInt("maximum_y"), rs.getInt("maximum_z")),
						rs.getInt("city_ifh_account"),
						rs.getInt("city_stored_experience"),
						rs.getBoolean("would_like_to_level_up"),
						rs.getBoolean("can_level_up"),
						rs.getString("what_needs_to_be_done_message"),
						rs.getString("last_level_approver"),
						rs.getDouble("default_city_manager_expense_allowance"),
						rs.getDouble("default_city_member_expense_allowance"),
						rs.getInt("additional_purchased_districts"),
						rs.getInt("additional_purchased_properties"),
						rs.getInt("actual_redstone_blocks_amount"),
						rs.getInt("additional_redstone_blocks_amount"),
						rs.getInt("actual_livestock_amount"),
						rs.getInt("additional_livestock_amount")
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
						Material.valueOf(rs.getString("gui_material")),
						(ArrayList<String>) Arrays.asList(rs.getString("city_description").split("!::!")),
						rs.getLong("creation_time"),
						UUID.fromString(rs.getString("creator_uuid")),
						new Vector(rs.getInt("creation_x"), rs.getInt("creation_y"), rs.getInt("creation_z")),
						UUID.fromString(rs.getString("governor_uuid")),
						CityLevel.get(rs.getInt("city_level")),
						CitySecurityLevel.get(rs.getInt("city_security_level")),
						rs.getString("server_name"),
						rs.getString("world_name"),
						new Vector(rs.getInt("minimum_x"), rs.getInt("minimum_y"), rs.getInt("minimum_z")),
						new Vector(rs.getInt("maximum_x"), rs.getInt("maximum_y"), rs.getInt("maximum_z")),
						rs.getInt("city_ifh_account"),
						rs.getInt("city_stored_experience"),
						rs.getBoolean("would_like_to_level_up"),
						rs.getBoolean("can_level_up"),
						rs.getString("what_needs_to_be_done_message"),
						rs.getString("last_level_approver"),
						rs.getDouble("default_city_manager_expense_allowance"),
						rs.getDouble("default_city_member_expense_allowance"),
						rs.getInt("additional_purchased_districts"),
						rs.getInt("additional_purchased_properties"),
						rs.getInt("actual_redstone_blocks_amount"),
						rs.getInt("additional_redstone_blocks_amount"),
						rs.getInt("actual_livestock_amount"),
						rs.getInt("additional_livestock_amount")
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
		if(!this.getServername().equals(CAS.getPlugin().getServername()))
		{
			return;
		}
		MemoryHandler.addCity(getId(), this);
	}
	
	public void saveMysql()
	{
		CAS.getPlugin().getMysqlHandler().updateData(MysqlType.CITY, this, "`id` = ?", this.id);
	}
}