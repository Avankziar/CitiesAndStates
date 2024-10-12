package me.avankziar.cas.spigot.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.avankziar.cas.spigot.CAS;
import me.avankziar.cas.spigot.handler.region.ConstructionHandler;

public class ConstructionListener implements Listener
{
	private CAS plugin;
	
	public ConstructionListener(CAS plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event)
	{
		if(!ConstructionHandler.canBuild(event.getPlayer().getUniqueId(), event.getBlock().getLocation().toVector()))
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if(!ConstructionHandler.canBuild(event.getPlayer().getUniqueId(), event.getBlock().getLocation().toVector()))
		{
			event.setCancelled(true);
		}
	}
}