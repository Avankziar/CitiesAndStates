package me.avankziar.cas.spigot.handler.region;

import org.bukkit.Bukkit;
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
import me.avankziar.cas.spigot.handler.DirectionHandler;
import me.avankziar.cas.spigot.handler.EntityHandler;

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
	
	public static int getMinimumDistanceBetweenCities()
	{
		return CAS.getPlugin().getYamlHandler().getConfig_City().getInt("MinimumDistanceBetweenCities", 100);
	}
	
	public static boolean expandBorder(Player player, int blocklenght)
	{
		long c = AreaHandler.isInCity(player.getLocation());
		Region3D city = MemoryHandler.getCity(c);
		if(city == null)
		{
			player.sendMessage(ChatApi.tl(CAS.getPlugin().getYamlHandler().getLang().getString("")));
			return false;
		}
		Vector maxmax = city.getMaximumPoint();
		BlockFace bf = DirectionHandler.getDirection(player.getLocation().getDirection());
		switch(bf)
		{
		default:
			player.sendMessage(ChatApi.tl(CAS.getPlugin().getYamlHandler().getLang().getString("")));
			return false;
		case NORTH_NORTH_WEST:
		case NORTH:
		case NORTH_NORTH_EAST:
			
		case EAST_NORTH_EAST:
		case EAST:
		case EAST_SOUTH_EAST:
			
		case SOUTH_SOUTH_EAST:
		case SOUTH:
		case SOUTH_SOUTH_WEST:
			
		case WEST_SOUTH_WEST:
		case WEST:
		case WEST_NORTH_WEST:
		}
	}
}