package me.avankziar.cas.spigot.handler;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;

import me.avankziar.cas.general.ChatApi;
import me.avankziar.cas.spigot.CAS;

public class MessageHandler
{
	private CAS plugin;
	
	public MessageHandler()
	{
		this.plugin = CAS.getPlugin();
	}
	
	public void sendMessage(UUID uuid, String msg)
	{
		if(Bukkit.getPlayer(uuid) != null)
		{
			Bukkit.getPlayer(uuid).sendMessage(ChatApi.tl(msg));
		} else
		{
			if(plugin.getMtV() != null)
			{
				plugin.getMtV().sendMessage(uuid, msg);
			}
		}
	}
	
	public void sendMessage(UUID uuid, ArrayList<String> msg)
	{
		if(Bukkit.getPlayer(uuid) != null)
		{
			msg.stream().forEach(x -> Bukkit.getPlayer(uuid).sendMessage(ChatApi.tl(x)));
		} else
		{
			if(plugin.getMtV() != null)
			{
				plugin.getMtV().sendMessage(uuid, msg.toArray(new String[msg.size()]));
			}
		}
	}
}