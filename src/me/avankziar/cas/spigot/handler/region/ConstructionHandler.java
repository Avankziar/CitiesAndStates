package me.avankziar.cas.spigot.handler.region;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.util.Vector;

import me.avankziar.cas.general.objects.Region3D;
import me.avankziar.cas.general.objects.property.Property;
import me.avankziar.cas.general.objects.property.Property.PropertyStatus;

public class ConstructionHandler
{
	private static CopyOnWriteArrayList<UUID> bypassConstruction = new CopyOnWriteArrayList<>(); //Player which bypass
	
	public static void addBypassConstruction(UUID uuid)
	{
		bypassConstruction.add(uuid);
	}
	
	public static void removeBypassConstruction(UUID uuid)
	{
		bypassConstruction.remove(uuid);
	}
	
	public static boolean onBypassConstruction(UUID uuid)
	{
		return bypassConstruction.stream().filter(x -> x.equals(uuid)).findAny().isPresent();
	}
	
	private static CopyOnWriteArrayList<Region3D> forbiddenRegionToBuild = new CopyOnWriteArrayList<>();
	
	public static void addForbiddenWorldToBuildOutsideCitys(Region3D region)
	{
		forbiddenRegionToBuild.add(region);
	}
	
	public static void removeForbiddenRegionToBuildOutsideCitys(Region3D region)
	{
		for(int i = 0; i < forbiddenRegionToBuild.size(); i++)
		{
			if(forbiddenRegionToBuild.get(i).equal(region))
			{
				forbiddenRegionToBuild.remove(i);
				break;
			}
		}
	}
	
	public static boolean isInForbiddenRegionToBuildOutsideCitys(Vector vector)
	{
		return forbiddenRegionToBuild.stream().filter(x -> vector.isInAABB(x.getMinimumPoint(), x.getMaximumPoint())).findAny().isPresent();
	}
	
	public static boolean canBuild(UUID uuid, Vector vector)
	{
		long cityID = AreaHandler.isInCity(vector);
		if(cityID < 0)
		{
			//No City
			return isInForbiddenRegionToBuildOutsideCitys(vector) ? onBypassConstruction(uuid) : false;
		}
		long propertyID = AreaHandler.isInProperty(vector);
		if(propertyID < 0)
		{
			//No Property maybe a district
			long districtID = AreaHandler.isInDistrict(vector);
			if(districtID < 0)
			{
				//No district, but City
				return MemoryHandler.getCityManager().stream()
						.filter(x -> x.getCityID() == cityID)
						.filter(x -> x.getUUID().equals(uuid))
						.findAny().isPresent()
						||
						MemoryHandler.getCityMember().stream()
						.filter(x -> x.getCityID() == cityID)
						.filter(x -> x.getUUID().equals(uuid))
						.findAny().isPresent();
			} else
			{
				//District and City
				return MemoryHandler.getCityManager().stream()
						.filter(x -> x.getCityID() == cityID)
						.filter(x -> x.getUUID().equals(uuid))
						.findAny().isPresent()
						||
						MemoryHandler.getDistrictManager().stream()
						.filter(x -> x.getDistrictID() == districtID)
						.filter(x -> x.getUUID().equals(uuid))
						.findAny().isPresent()
						||
						MemoryHandler.getDistrictMember().stream()
						.filter(x -> x.getDistrictID() == districtID)
						.filter(x -> x.getUUID().equals(uuid))
						.findAny().isPresent();
			}
		} else
		{
			//Property
			Property p = MemoryHandler.getProperty(propertyID);
			if(p.getActualPropertyStatus() == PropertyStatus.BOUGHT || p.getActualPropertyStatus() == PropertyStatus.RENTED)
			{
				return MemoryHandler.getPropertyOwner().stream()
						.filter(x -> x.getPropertyID() == propertyID)
						.filter(x -> x.getUUID().equals(uuid))
						.findAny().isPresent()
						||
						MemoryHandler.getPropertyRoommates().stream()
						.filter(x -> x.getPropertyID() == propertyID)
						.filter(x -> x.getUUID().equals(uuid))
						.findAny().isPresent();
			} else
			{
				long districtID = AreaHandler.isInDistrict(vector);
				if(districtID < 0)
				{
					//Only Property
					return MemoryHandler.getCityManager().stream()
							.filter(x -> x.getCityID() == cityID)
							.filter(x -> x.getUUID().equals(uuid))
							.findAny().isPresent()
							||
							MemoryHandler.getPropertyRoommates().stream()
							.filter(x -> x.getPropertyID() == propertyID)
							.filter(x -> x.getUUID().equals(uuid))
							.findAny().isPresent();
				} else
				{
					//Property and District
					return MemoryHandler.getCityManager().stream()
							.filter(x -> x.getCityID() == cityID)
							.filter(x -> x.getUUID().equals(uuid))
							.findAny().isPresent()
							||
							MemoryHandler.getDistrictManager().stream()
							.filter(x -> x.getDistrictID() == districtID)
							.filter(x -> x.getUUID().equals(uuid))
							.findAny().isPresent();
				}
			}
		}
	}
}