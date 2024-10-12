package me.avankziar.cas.spigot.cmd.storage;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.cas.general.cmdtree.ArgumentConstructor;
import me.avankziar.cas.spigot.CAS;
import me.avankziar.cas.spigot.cmdtree.ArgumentModule;

public class _ARG_Toggle extends ArgumentModule
{
	private CAS plugin;
	
	public _ARG_Toggle(CAS plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		/*if(SignQuantityHandler.bypassToggle.contains(player.getUniqueId().toString()))
		{
			SignQuantityHandler.bypassToggle.remove(player.getUniqueId().toString());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Toggle.Deactive")));
		} else
		{
			SignQuantityHandler.bypassToggle.add(player.getUniqueId().toString());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Toggle.Active")));
		}*/
	}
}