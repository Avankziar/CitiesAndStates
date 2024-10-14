package me.avankziar.cas.spigot.handler.region;

import java.util.Map.Entry;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import me.avankziar.cas.general.objects.Region3D;
import me.avankziar.cas.general.objects.property.Property;

public class AreaHandler
{
	public static long isInCity(Location point)
	{
		return isInCity(point.toVector());
	}
	
	public static long isInCity(Vector point)
	{
		Optional<Entry<Long, Region3D>> o = MemoryHandler.getCities().stream()
				.filter(x -> point.isInAABB(x.getValue().getMinimumPoint(), x.getValue().getMaximumPoint()))
				.findAny();
		return o.isPresent() ? o.get().getKey() : -1;
	}
	
	public static boolean intersectCity(Vector min, Vector max)
	{
		Optional<Entry<Long, Region3D>> o = MemoryHandler.getCities().stream()
				.filter(x -> min.getBlockX() <= x.getValue().getMaximumPoint().getBlockX())
				.filter(x -> max.getBlockX() >= x.getValue().getMinimumPoint().getBlockX())
				.filter(x -> min.getBlockY() <= x.getValue().getMaximumPoint().getBlockY())
				.filter(x -> max.getBlockY() >= x.getValue().getMinimumPoint().getBlockY())
				.filter(x -> min.getBlockZ() <= x.getValue().getMaximumPoint().getBlockZ())
				.filter(x -> max.getBlockZ() >= x.getValue().getMinimumPoint().getBlockZ())
				.findAny();
		return o.isPresent() ? true : false;
	}
	
	public static long isInDistrict(Location point)
	{
		return isInDistrict(point.toVector());
	}
	
	public static long isInDistrict(Vector point)
	{
		Optional<Entry<Long, Region3D>> o = MemoryHandler.getDistrict().stream()
		.filter(x -> point.isInAABB(x.getValue().getMinimumPoint(), x.getValue().getMaximumPoint()))
		.findAny();
		return o.isPresent() ? o.get().getKey() : -1;
	}
	
	public static long isInProperty(Location point)
	{
		return isInProperty(point.toVector());
	}
	
	public static long isInProperty(Vector point)
	{
		Optional<Entry<Long, Property>> o = MemoryHandler.getProperties().stream()
		.filter(x -> point.isInAABB(x.getValue().getRegion().getMinimumPoint(), x.getValue().getRegion().getMaximumPoint()))
		.findAny();
		return o.isPresent() ? o.get().getKey() : -1;
	}
	
	public static boolean intersectProperty(Vector min, Vector max)
	{
		Optional<Entry<Long, Property>> o = MemoryHandler.getProperties().stream()
				.filter(x -> min.getBlockX() <= x.getValue().getMaximumPoint().getBlockX())
				.filter(x -> max.getBlockX() >= x.getValue().getMinimumPoint().getBlockX())
				.filter(x -> min.getBlockY() <= x.getValue().getMaximumPoint().getBlockY())
				.filter(x -> max.getBlockY() >= x.getValue().getMinimumPoint().getBlockY())
				.filter(x -> min.getBlockZ() <= x.getValue().getMaximumPoint().getBlockZ())
				.filter(x -> max.getBlockZ() >= x.getValue().getMinimumPoint().getBlockZ())
				.findAny();
		return o.isPresent() ? true : false;
	}
}