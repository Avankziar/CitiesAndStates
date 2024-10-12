package me.avankziar.cas.spigot.hook;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.avankziar.cas.spigot.modifiervalueentry.Bypass;
import me.avankziar.cas.spigot.modifiervalueentry.ModifierValueEntry;

public class WorldGuardHook
{
	public static StateFlag CITY_CREATE;
	public static StateFlag STATE_CREATE;
	
	public static boolean init()
	{
		FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
		try 
		{
			StateFlag cc = new StateFlag("cas-city-create", true);
	        registry.register(cc);
	        CITY_CREATE = cc;
	        StateFlag sc = new StateFlag("cas-state-create", true);
	        registry.register(sc);
	       	STATE_CREATE = sc;
	    } catch (FlagConflictException e) 
		{
	        return false;
	    }
		return true;
	}
	
	public static boolean canCreateCity(Player player, Location minimumPoint)
	{
		RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        com.sk89q.worldedit.util.Location loc1 = BukkitAdapter.adapt(minimumPoint);
        return ModifierValueEntry.hasPermission(player, Bypass.Permission.CITY_CREATION_WORLDGUARD)
        		? true : query.testState(loc1, WorldGuardPlugin.inst().wrapPlayer(player), CITY_CREATE);
	}
	
	public static boolean canCreateState(Player player, Location minimumPoint)
	{
		RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        com.sk89q.worldedit.util.Location loc1 = BukkitAdapter.adapt(minimumPoint);
        return ModifierValueEntry.hasPermission(player, Bypass.Permission.STATE_CREATION_WORLDGUARD)
        		? true : query.testState(loc1, WorldGuardPlugin.inst().wrapPlayer(player), STATE_CREATE);
	}
}