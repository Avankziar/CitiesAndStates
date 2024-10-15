package me.avankziar.cas.spigot.handler.city;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import me.avankziar.cas.general.ChatApi;
import me.avankziar.cas.general.database.MysqlType;
import me.avankziar.cas.general.objects.Region3D;
import me.avankziar.cas.general.objects.city.City;
import me.avankziar.cas.spigot.CAS;
import me.avankziar.cas.spigot.assistance.MatchApi;
import me.avankziar.cas.spigot.handler.DirectionHandler;
import me.avankziar.cas.spigot.handler.EntityHandler;
import me.avankziar.cas.spigot.handler.region.AreaHandler;
import me.avankziar.cas.spigot.handler.region.MemoryHandler;
import me.avankziar.ifh.general.math.MathFormulaParser;
import me.avankziar.ifh.spigot.economy.account.Account;

public class CityHandler
{
	
	public static City getCityFromSQL(long id)
	{
		return (City) CAS.getPlugin().getMysqlHandler().getData(MysqlType.CITY, "`id` = ?", id);
	}
	
	public static int getEntityWithinTheCity(long id)
	{
		City c = getCityFromSQL(id);
		if(c == null)
		{
			return 0;
		}
		Vector min = c.getRegion().getMinimumPoint();
		Vector max = c.getRegion().getMaximumPoint();
		int count = 0;
		for(Entity e : Bukkit.getWorld(c.getWorldname()).getNearbyEntities(
				new BoundingBox(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ())))
		{
			if(EntityHandler.isLivestock(e.getType()))
			{
				count++;
			}
		}
		return count;
	}
	
	
}