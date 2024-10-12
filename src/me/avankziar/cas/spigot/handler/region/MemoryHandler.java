package me.avankziar.cas.spigot.handler.region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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

/**
 * Useage by Event which have a demanding respond time, like blockbreak etc.
 * ONLY Object which happend to be on the server!
 */
public class MemoryHandler
{
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
	
	public static void setPropertyRoommate(PropertyRoommate propertyRoommate)
	{
		if(propertyRoommate == null || propertyRoommate.getId() < 1)
		{
			return;
		}
		property_roommate.put(propertyRoommate.getId(), propertyRoommate);
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
	
	public static void setPropertyOwner(PropertyOwner propertyOwner)
	{
		if(propertyOwner == null || propertyOwner.getId() < 1)
		{
			return;
		}
		property_owner.put(propertyOwner.getId(), propertyOwner);
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
	
	public static void setProperty(Property property)
	{
		if(property == null || property.getId() < 1)
		{
			return;
		}
		MemoryHandler.property.put(property.getId(), property);
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
	
	public static void setDistrictMember(DistrictMember districtMember)
	{
		if(districtMember == null || districtMember.getId() < 1)
		{
			return;
		}
		MemoryHandler.district_member.put(districtMember.getId(), districtMember);
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
	
	public static void setDistrictManager(DistrictManager districtManager)
	{
		if(districtManager == null || districtManager.getId() < 1)
		{
			return;
		}
		MemoryHandler.district_manager.put(districtManager.getId(), districtManager);
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
	
	public static void setDistrict(District district)
	{
		if(district == null || district.getId() < 1)
		{
			return;
		}
		MemoryHandler.district.put(district.getId(), district);
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
	
	public static void setCityMember(CityMember cityMember)
	{
		if(cityMember == null || cityMember.getId() < 1)
		{
			return;
		}
		MemoryHandler.city_member.put(cityMember.getId(), cityMember);
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
	
	public static void setCityManager(CityManager cityManager)
	{
		if(cityManager == null || cityManager.getId() < 1)
		{
			return;
		}
		MemoryHandler.city_manager.put(cityManager.getId(), cityManager);
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
	
	public static void setCity(City city)
	{
		if(city == null || city.getId() < 1)
		{
			return;
		}
		MemoryHandler.city.put(city.getId(), city.getRegion());
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
		city_manager.remove(cityManagerID);
	}
	
	public static ArrayList<CityFlag> getCityAttributes(long id)
	{
		return city_flags.get(id);
	}
}