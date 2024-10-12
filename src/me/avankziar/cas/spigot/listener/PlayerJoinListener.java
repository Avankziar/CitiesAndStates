package me.avankziar.cas.spigot.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.cas.general.database.MysqlType;
import me.avankziar.cas.general.objects.PlayerData;
import me.avankziar.cas.spigot.CAS;
import me.avankziar.cas.spigot.gui.objects.SettingsLevel;

public class PlayerJoinListener implements Listener
{
	private CAS plugin;
	
	public PlayerJoinListener(CAS plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				PlayerData pd = (PlayerData) plugin.getMysqlHandler().getData(MysqlType.PLAYERDATA, "`player_uuid` = ?", player.getUniqueId().toString());
				if(pd == null)
				{
					pd = new PlayerData(0, player.getUniqueId(), player.getName(), SettingsLevel.BASE, System.currentTimeMillis());
					plugin.getMysqlHandler().create(MysqlType.PLAYERDATA, pd);
				} else
				{
					pd.setName(player.getName());
					pd.setLastLogin(System.currentTimeMillis());
					plugin.getMysqlHandler().updateData(MysqlType.PLAYERDATA, pd, "`player_uuid` = ?", player.getUniqueId().toString());
				}
			}
		}.runTaskAsynchronously(plugin);
	}
}