package me.avankziar.cas.general.database;

import me.avankziar.cas.general.objects.BlackListTeleport;
import me.avankziar.cas.general.objects.PlayerData;
import me.avankziar.cas.general.objects.city.City;
import me.avankziar.cas.general.objects.city.CityFlag;
import me.avankziar.cas.general.objects.city.CityManager;
import me.avankziar.cas.general.objects.city.CityMember;
import me.avankziar.cas.general.objects.city.CityTeleportPoint;
import me.avankziar.cas.general.objects.district.District;
import me.avankziar.cas.general.objects.district.DistrictManager;
import me.avankziar.cas.general.objects.district.DistrictMember;
import me.avankziar.cas.general.objects.property.Property;
import me.avankziar.cas.general.objects.property.PropertyOwner;
import me.avankziar.cas.general.objects.property.PropertyRoommate;
import me.avankziar.cas.general.objects.property.PropertyTeleportPoint;

public enum MysqlType
{
	PLAYERDATA("casPlayerData", new PlayerData(), ServerType.ALL,
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
			+ " player_uuid char(36) NOT NULL UNIQUE,"
			+ " player_name text,"
			+ " last_setting_level text,"
			+ " last_login BIGINT);"),
	PROPERTY_ROOMMATE("casProperty_Roommate", new PropertyRoommate(), ServerType.SPIGOT,
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id bigint AUTO_INCREMENT PRIMARY KEY,"
			+ " property_id bigint,"
			+ " player_uuid char(36) NOT NULL);"),
	PROPERTY_OWNER("casProperty_Owner", new PropertyOwner(), ServerType.SPIGOT,
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id bigint AUTO_INCREMENT PRIMARY KEY,"
			+ " property_id bigint,"
			+ " player_uuid char(36) NOT NULL);"),
	PROPERTY("casProperty", new Property(), ServerType.SPIGOT,
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id bigint AUTO_INCREMENT PRIMARY KEY,"
			+ " district_id bigint,"
			+ " city_id bigint,"
			+ " property_name text,"
			+ " creation_time bigint,"
			+ " server_name text,"
			+ " world_name text,"
			+ " minimum_x int,"
			+ " minimum_y int,"
			+ " minimum_z int,"
			+ " maximum_x int,"
			+ " maximum_y int,"
			+ " maximum_z int,"
			+ " actual_property_status text,"
			+ " future_property_status text,"
			+ " time_until_property_status_change bigint,"
			+ " actual_monument_type text,"
			+ " future_monument_type text,"
			+ " actual_buy_price double,"
			+ " future_buy_price double,"
			+ " actual_rent_price double,"
			+ " future_rent_price double,"
			+ " rent_arrears double,"
			+ " restoration_active boolean,"
			+ " actual_redstoneblock_amount int,"
			+ " maximum_redstoneblock_amount int,"
			+ " actual_livestock_amount int,"
			+ " maximum_livestock_amount int);"),
	PROPERTY_TELEPORTPOINT("casProperty_Teleport_Point", new PropertyTeleportPoint(), ServerType.SPIGOT,
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id bigint AUTO_INCREMENT PRIMARY KEY,"
			+ " property_id bigint,"
			+ " teleport_name text,"
			+ " list_type text,"
			+ " server_name text,"
			+ " world_name text,"
			+ " x double,"
			+ " y double,"
			+ " z double,"
			+ " yaw float,"
			+ " pitch float);"),
	DISTRICT_MANAGER("casDistrict_Manager", new DistrictManager(), ServerType.SPIGOT,
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id bigint AUTO_INCREMENT PRIMARY KEY,"
			+ " district_id bigint,"
			+ " player_uuid char(36) NOT NULL,"
			+ " expense_allowance double);"),
	DISTRICT_MEMBER("casDistrict_Member", new DistrictMember(), ServerType.SPIGOT,
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id bigint AUTO_INCREMENT PRIMARY KEY,"
			+ " district_id bigint,"
			+ " player_uuid char(36) NOT NULL,"
			+ " expense_allowance double);"),
	DISTRICT("casDistrict", new District(), ServerType.SPIGOT,
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id bigint AUTO_INCREMENT PRIMARY KEY,"
			+ " city_id bigint,"
			+ " district_name text,"
			+ " creation_time bigint,"
			+ " server_name text,"
			+ " world_name text,"
			+ " minimum_x int,"
			+ " minimum_y int,"
			+ " minimum_z int,"
			+ " maximum_x int,"
			+ " maximum_y int,"
			+ " maximum_z int,"
			+ " default_district_manager_expense_allowance double,"
			+ " default_district_member_expense_allowance double);"),
	CITY_MANAGER("casCity_Manager", new CityManager(), ServerType.SPIGOT,
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id bigint AUTO_INCREMENT PRIMARY KEY,"
			+ " city_id bigint,"
			+ " player_uuid char(36) NOT NULL,"
			+ " expense_allowance double);"),
	CITY_MEMBER("casCity_Member", new CityMember(), ServerType.SPIGOT,
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id bigint AUTO_INCREMENT PRIMARY KEY,"
			+ " city_id bigint,"
			+ " player_uuid char(36) NOT NULL,"
			+ " expense_allowance double);"),
	CITY("casCity", new City(), ServerType.SPIGOT,
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id bigint AUTO_INCREMENT PRIMARY KEY,"
			+ " city_name text,"
			+ " creation_time bigint,"
			+ " server_name text,"
			+ " world_name text,"
			+ " minimum_x int,"
			+ " minimum_y int,"
			+ " minimum_z int,"
			+ " maximum_x int,"
			+ " maximum_y int,"
			+ " maximum_z int,"
			+ " default_city_manager_expense_allowance double,"
			+ " default_city_member_expense_allowance double);"),
	CITY_TELEPORTPOINT("casCity_Teleport_Point", new CityTeleportPoint(), ServerType.SPIGOT,
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id bigint AUTO_INCREMENT PRIMARY KEY,"
			+ " city_id bigint,"
			+ " teleport_name text,"
			+ " list_type text,"
			+ " server_name text,"
			+ " world_name text,"
			+ " x double,"
			+ " y double,"
			+ " z double,"
			+ " yaw float,"
			+ " pitch float);"),
	CITY_FLAGS("casCity_Flag", new CityFlag(), ServerType.SPIGOT,
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id bigint AUTO_INCREMENT PRIMARY KEY,"
			+ " city_id bigint,"
			+ " unique_name text,"
			+ " result_name text,"
			+ " isactive boolean,"
			+ " actual_cooldown_to_change_active_status bigint);"),
	BLACKLIST_TELEPORT("casBlackList_Teleport", new BlackListTeleport(), ServerType.SPIGOT,
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id bigint AUTO_INCREMENT PRIMARY KEY,"
			+ " value_id bigint,"
			+ " teleport_type text,"
			+ " player_uuid char(36) NOT NULL);"),
	;
	
	private MysqlType(String tableName, Object object, ServerType usedOnServer, String setupQuery)
	{
		this.tableName = tableName;
		this.object = object;
		this.usedOnServer = usedOnServer;
		this.setupQuery = setupQuery.replace("%%tablename%%", tableName);
	}
	
	private final String tableName;
	private final Object object;
	private final ServerType usedOnServer;
	private final String setupQuery;

	public String getValue()
	{
		return tableName;
	}
	
	public Object getObject()
	{
		return object;
	}
	
	public ServerType getUsedOnServer()
	{
		return usedOnServer;
	}
	
	public String getSetupQuery()
	{
		return setupQuery;
	}
}