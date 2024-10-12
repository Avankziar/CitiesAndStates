package me.avankziar.cas.spigot.handler.region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import me.avankziar.cas.general.database.MysqlType;
import me.avankziar.cas.general.objects.Region3D;
import me.avankziar.cas.general.objects.city.City;
import me.avankziar.cas.general.objects.city.CityFlag;
import me.avankziar.cas.general.objects.city.CityManager;
import me.avankziar.cas.general.objects.city.CityMember;
import me.avankziar.cas.general.objects.district.District;
import me.avankziar.cas.general.objects.district.DistrictManager;
import me.avankziar.cas.general.objects.district.DistrictMember;
import me.avankziar.cas.general.objects.property.Property;
import me.avankziar.cas.general.objects.property.PropertyOwner;
import me.avankziar.cas.general.objects.property.PropertyRoommate;
import me.avankziar.cas.spigot.CAS;
import me.avankziar.cas.spigot.database.MysqlHandler;

/**
 * Useage by Event which have a demanding respond time, like blockbreak etc.
 * ONLY Object which happend to be on the server!
 */
public class MemoryHandler
{
	private static void log(boolean log, String msg)
	{
		if(log)
		{
			CAS.log.info(msg);
		}
	}
	public static void initialize(boolean log)
	{
		MysqlHandler sql = CAS.getPlugin().getMysqlHandler();
		String server = CAS.getPlugin().getServername();
		ArrayList<City> city = City.convert(sql.getFullList(MysqlType.CITY, "`id` ASC",
				"`server_name` = ?", server));
		log(log, "MemoryHandler loading for this server %x%...".replace("%x%", server));
		log(log, ">> Load %x% Cities".replace("%x%", String.valueOf(city.size())));
		long cityflag = 0;
		long citymanager = 0;
		long citymember = 0;
		for(City c : city)
		{
			addCity(c.getId(), c.getRegion());
			ArrayList<CityFlag> cf = CityFlag.convert(sql.getFullList(MysqlType.CITY_FLAGS, "`id` ASC",
					"`city_id` = ?", c.getId()));
			cityflag += cf.size();
			cf.stream().forEach(x -> addCityFlag(x));
			ArrayList<CityManager> cma = CityManager.convert(sql.getFullList(MysqlType.CITY_MANAGER, "`id` ASC",
					"`city_id` = ?", c.getId()));
			citymanager += cma.size();
			cma.stream().forEach(x -> addCityManager(x.getId(), x));
			ArrayList<CityMember> cme = CityMember.convert(sql.getFullList(MysqlType.CITY_MEMBER, "`id` ASC",
					"`city_id` = ?", c.getId()));
			citymember += cme.size();
			cme.stream().forEach(x -> addCityMember(x.getId(), x));
		}
		log(log, ">> Load %x% CityFlags".replace("%x%", String.valueOf(cityflag)));
		log(log, ">> Load %x% CityManagers".replace("%x%", String.valueOf(citymanager)));
		log(log, ">> Load %x% CityMembers".replace("%x%", String.valueOf(citymember)));
		
		ArrayList<District> district = District.convert(sql.getFullList(MysqlType.DISTRICT, "`id` ASC",
				"`server_name` = ?", server));
		log(log, ">> Load %x% Districts".replace("%x%", String.valueOf(district.size())));
		long districtmanager = 0;
		long districtmember = 0;
		for(District d : district)
		{
			addDistrict(d.getId(), d.getRegion());
			ArrayList<DistrictManager> dma = DistrictManager.convert(sql.getFullList(MysqlType.DISTRICT_MANAGER, "`id` ASC",
					"`district_id` = ?", d.getId()));
			districtmanager += dma.size();
			dma.stream().forEach(x -> addDistrictManager(x.getId(), x));
			ArrayList<DistrictMember> dme = DistrictMember.convert(sql.getFullList(MysqlType.DISTRICT_MEMBER, "`id` ASC",
					"`district_id` = ?", d.getId()));
			districtmember += dme.size();
			dme.stream().forEach(x -> addDistrictMember(x.getId(), x));
		}
		log(log, ">> Load %x% DistrictManagers".replace("%x%", String.valueOf(districtmanager)));
		log(log, ">> Load %x% DistrictMembers".replace("%x%", String.valueOf(districtmember)));
		
		ArrayList<Property> property = Property.convert(sql.getFullList(MysqlType.PROPERTY, "`id` ASC",
				"`server_name` = ?", server));
		log(log, ">> Load %x% Properties".replace("%x%", String.valueOf(property.size())));
		long propertyowner = 0;
		long propertyroommate = 0;
		for(Property p : property)
		{
			addProperty(p.getId(), p);
			ArrayList<PropertyOwner> pow = PropertyOwner.convert(sql.getFullList(MysqlType.PROPERTY_OWNER, "`id` ASC",
					"`property_id`", p.getId()));
			pow.stream().forEach(x -> addPropertyOwner(x.getId(), x));
			propertyowner += pow.size();
			ArrayList<PropertyRoommate> pro = PropertyRoommate.convert(sql.getFullList(MysqlType.PROPERTY_ROOMMATE, "`id` ASC",
					"`property_id`", p.getId()));
			propertyroommate += pro.size();
			pro.stream().forEach(x -> addPropertyRoommate(x.getId(), x));
		}
		log(log, ">> Load %x% PropertyOwners".replace("%x%", String.valueOf(propertyowner)));
		log(log, ">> Load %x% PropertyRoommates".replace("%x%", String.valueOf(propertyroommate)));
	}
	
	private static ConcurrentHashMap<Long, PropertyRoommate> property_roommate = new ConcurrentHashMap<>();
	
	public static void addPropertyRoommate(long propertyRoommateID, PropertyRoommate propertyRoommate)
	{
		property_roommate.put(propertyRoommateID, propertyRoommate);
	}
	
	public static void removePropertyRoommate(long propertyRoommateID)
	{
		property_roommate.remove(propertyRoommateID);
	}
	
	public static PropertyRoommate getPropertyRoommate(long property_RoommateID)
	{
		return property_roommate.get(property_RoommateID);
	}
	
	public static Collection<PropertyRoommate> getPropertyRoommates()
	{
		return property_roommate.values();
	}
	
	//--------------
	
	private static ConcurrentHashMap<Long, PropertyOwner> property_owner = new ConcurrentHashMap<>();
	
	public static void addPropertyOwner(long propertyOwnerID, PropertyOwner propertyOwner)
	{
		property_owner.put(propertyOwnerID, propertyOwner);
	}
	
	public static void removePropertyOwner(long propertyOwnerID)
	{
		property_owner.remove(propertyOwnerID);
	}
	
	public static PropertyOwner getPropertyOwner(long property_OwnerID)
	{
		return property_owner.get(property_OwnerID);
	}
	
	public static Collection<PropertyOwner> getPropertyOwner()
	{
		return property_owner.values();
	}
	
	//--------------
	
	private static ConcurrentHashMap<Long, Property> property = new ConcurrentHashMap<>();
	
	public static void addProperty(long propertyID, Property property)
	{
		MemoryHandler.property.put(propertyID, property);
	}
	
	public static void removeProperty(long propertyID)
	{
		property.remove(propertyID);
	}
	
	public static Property getProperty(long propertyid)
	{
		return property.get(propertyid);
	}
	
	public static Set<Entry<Long, Property>> getProperties()
	{
		return property.entrySet();
	}
	
	//--------------
	
	private static ConcurrentHashMap<Long, DistrictMember> district_member = new ConcurrentHashMap<>();
	
	public static void addDistrictMember(long districtMemberID, DistrictMember districtMember)
	{
		district_member.put(districtMemberID, districtMember);
	}
	
	public static void removeDistrictMember(long districtMemberID)
	{
		district_member.remove(districtMemberID);
	}
	
	public static DistrictMember getDistrictMember(long id)
	{
		return district_member.get(id);
	}
	
	public static Collection<DistrictMember> getDistrictMember()
	{
		return district_member.values();
	}
	
	//--------------
	
	private static ConcurrentHashMap<Long, DistrictManager> district_manager = new ConcurrentHashMap<>();
	
	public static void addDistrictManager(long districtManagerID, DistrictManager districtManager)
	{
		district_manager.put(districtManagerID, districtManager);
	}
	
	public static void removeDistrictManager(long districtManagerID)
	{
		district_manager.remove(districtManagerID);
	}
	
	public static DistrictManager getDistrictManager(long id)
	{
		return district_manager.get(id);
	}
	
	public static Collection<DistrictManager> getDistrictManager()
	{
		return district_manager.values();
	}
	
	//--------------
	
	private static ConcurrentHashMap<Long, Region3D> district = new ConcurrentHashMap<>();
	
	public static void addDistrict(long districtID, Region3D district)
	{
		MemoryHandler.district.put(districtID, district);
	}
	
	public static void removeDistrict(long districtID)
	{
		district.remove(districtID);
	}
	
	public static Region3D getDistrict(long id)
	{
		return district.get(id);
	}
	
	public static Set<Entry<Long, Region3D>> getDistrict()
	{
		return district.entrySet();
	}
	
	//--------------
	
	private static ConcurrentHashMap<Long, CityMember> city_member = new ConcurrentHashMap<>();
	
	public static void addCityMember(long cityMemberID, CityMember cityMember)
	{
		city_member.put(cityMemberID, cityMember);
	}
	
	public static void removeCityMember(long cityMemberID)
	{
		city_member.remove(cityMemberID);
	}
	
	public static CityMember getCityMember(long id)
	{
		return city_member.get(id);
	}
	
	public static Collection<CityMember> getCityMember()
	{
		return city_member.values();
	}
	
	//--------------
	
	private static ConcurrentHashMap<Long, CityManager> city_manager = new ConcurrentHashMap<>();
	
	public static void addCityManager(long cityManagerID, CityManager cityManager)
	{
		city_manager.put(cityManagerID, cityManager);
	}
	
	public static void removeCityManager(long cityManagerID)
	{
		city_manager.remove(cityManagerID);
	}
	
	public static CityManager getCityManager(long id)
	{
		return city_manager.get(id);
	}
	
	public static Collection<CityManager> getCityManager()
	{
		return city_manager.values();
	}
	
	//--------------
	
	private static ConcurrentHashMap<Long, Region3D> city = new ConcurrentHashMap<>();
	
	
	public static void addCity(long cityID, Region3D city)
	{
		MemoryHandler.city.put(cityID, city);
	}
	
	public static void removeCity(long cityID)
	{
		city.remove(cityID);
	}
	
	public static Region3D getCity(long id)
	{
		return city.get(id);
	}
	
	public static Set<Entry<Long, Region3D>> getCity()
	{
		return city.entrySet();
	}
	
	//--------------
	
	private static ConcurrentHashMap<Long, ArrayList<CityFlag>> city_flags = new ConcurrentHashMap<>();
	
	public static void addCityFlag(CityFlag cityFlag)
	{
		ArrayList<CityFlag> cf = new ArrayList<>();
		if(city_flags.contains(cityFlag.getCityID()))
		{
			cf = city_flags.get(cityFlag.getCityID());
		}
		int index = -1;
		for(CityFlag cfs : cf)
		{
			if(index < 0)
			{
				index = 0;
			}
			if(cfs.getId() == cityFlag.getId())
			{
				break;
			}
			index++;
		}
		if(index >= 0)
		{
			cf.set(index, cityFlag);
		} else
		{
			cf.add(cityFlag);
		}
	}
	
	public static void removeCityFlag(long cityID, long cityFlagID)
	{
		ArrayList<CityFlag> cf = new ArrayList<>();
		if(city_flags.contains(cityID))
		{
			cf = city_flags.get(cityID);
		}
		int index = -1;
		for(CityFlag cfs : cf)
		{
			if(index < 0)
			{
				index = 0;
			}
			if(cfs.getId() == cityFlagID)
			{
				break;
			}
			index++;
		}
		if(index >= 0)
		{
			cf.remove(index);
		}
	}
	
	public static ArrayList<CityFlag> getCityAttributes(long id)
	{
		return city_flags.get(id);
	}
}