package me.avankziar.cas.spigot.cmd.vss;

import java.io.IOException;

import org.bukkit.command.CommandSender;

import me.avankziar.cas.general.ChatApi;
import me.avankziar.cas.general.cmdtree.ArgumentConstructor;
import me.avankziar.cas.spigot.CAS;
import me.avankziar.cas.spigot.cmdtree.ArgumentModule;

public class _ARGStorage extends ArgumentModule
{
	private CAS plugin;
	
	public _ARGStorage(CAS plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.OtherCmd")));
	}
}