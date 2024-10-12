package me.avankziar.cas.spigot.assistance;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.cas.general.database.MysqlType;
import me.avankziar.cas.general.objects.PlayerData;
import me.avankziar.cas.spigot.CAS;

public class BackgroundTask
{
	private static CAS plugin;
	
	public BackgroundTask(CAS plugin)
	{
		BackgroundTask.plugin = plugin;
		initBackgroundTask();
	}
	
	public boolean initBackgroundTask()
	{
		cleanUpPlayerData(plugin.getYamlHandler().getConfig().getBoolean("CleanUpTask.Player.Active", false));
		return true;
	}
	
	public void cleanUpPlayerData(boolean active)
	{
		if(!active)
		{
			return;
		}
		final long offlineSinceAtLeast = System.currentTimeMillis()
				-1000L*60*60*24*plugin.getYamlHandler().getConfig().getInt("CleanUpTask.Player.DeleteAfterXDaysOffline");
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				int playerCount = plugin.getMysqlHandler().getCount(MysqlType.PLAYERDATA, "`last_login` < ?", offlineSinceAtLeast);
				if(playerCount <= 0)
				{
					return;
				}
				int signStorageCount = 0;
				long itemLost = 0;
				ArrayList<UUID> uuidlist = new ArrayList<>();
				ArrayList<Integer> ssIdList = new ArrayList<>();
				for(PlayerData pd : PlayerData.convert(plugin.getMysqlHandler().getFullList(MysqlType.PLAYERDATA,
						"`id` ASC",	"`last_login` < ?", offlineSinceAtLeast)))
				{
					if(pd == null)
					{
						continue;
					}
					UUID owner = pd.getUUID();
					uuidlist.add(owner);
				}
				plugin.getMysqlHandler().deleteData(MysqlType.PLAYERDATA,
						"`last_login` < ?", offlineSinceAtLeast);
				if(playerCount <= 0)
				{
					return;
				}
				plugin.getLogger().info("==========VSS Database DeleteTask==========");
				plugin.getLogger().info("Deleted PlayerData: "+playerCount);
				plugin.getLogger().info("Deleted SignStorage: "+signStorageCount);
				plugin.getLogger().info("Lost ItemAmount: "+itemLost);
				plugin.getLogger().info("===========================================");
			}
		}.runTaskLaterAsynchronously(plugin, 20L*5);
	}
}