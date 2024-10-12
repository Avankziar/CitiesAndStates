package me.avankziar.cas.spigot.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.cas.general.ChatApi;
import me.avankziar.cas.general.cmdtree.ArgumentConstructor;
import me.avankziar.cas.general.cmdtree.BaseConstructor;
import me.avankziar.cas.general.cmdtree.CommandConstructor;
import me.avankziar.cas.general.cmdtree.CommandSuggest;
import me.avankziar.cas.spigot.CAS;
import me.avankziar.cas.spigot.assistance.MatchApi;
import me.avankziar.cas.spigot.cmdtree.ArgumentModule;
import me.avankziar.cas.spigot.modifiervalueentry.ModifierValueEntry;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CASCommandExecutor implements CommandExecutor
{
	private CAS plugin;
	private static CommandConstructor cc;
	
	public CASCommandExecutor(CAS plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		CASCommandExecutor.cc = cc;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if(cc == null)
		{
			return false;
		}
		if (args.length == 1) 
		{
			if (!(sender instanceof Player)) 
			{
				plugin.getLogger().info("Cmd is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(MatchApi.isInteger(args[0]))
			{
				if(!ModifierValueEntry.hasPermission(player, cc))
				{
					player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
					return false;
				}
				baseCommands(player, Integer.parseInt(args[0]));
				return true;
			}
		} else if(args.length == 0)
		{
			if (!(sender instanceof Player)) 
			{
				plugin.getLogger().info("Cmd is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!ModifierValueEntry.hasPermission(player, cc))
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			baseCommands(player, 0); //Base and Info Command
			return true;
		}
		int length = args.length-1;
		ArrayList<ArgumentConstructor> aclist = cc.subcommands;
		for(int i = 0; i <= length; i++)
		{
			for(ArgumentConstructor ac : aclist)
			{
				if(args[i].equalsIgnoreCase(ac.getName()))
				{
					if(length >= ac.minArgsConstructor && length <= ac.maxArgsConstructor)
					{
						if (sender instanceof Player)
						{
							Player player = (Player) sender;
							if(ModifierValueEntry.hasPermission(player, ac))
							{
								ArgumentModule am = plugin.getArgumentMap().get(ac.getPath());
								if(am != null)
								{
									try
									{
										am.run(sender, args);
									} catch (IOException e)
									{
										e.printStackTrace();
									}
								} else
								{
									plugin.getLogger().info("ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
											.replace("%ac%", ac.getName()));
									player.spigot().sendMessage(ChatApi.tctl(
											"ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
											.replace("%ac%", ac.getName())));
									return false;
								}
								return false;
							} else
							{
								player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
								return false;
							}
						} else
						{
							ArgumentModule am = plugin.getArgumentMap().get(ac.getPath());
							if(am != null)
							{
								try
								{
									am.run(sender, args);
								} catch (IOException e)
								{
									e.printStackTrace();
								}
							} else
							{
								plugin.getLogger().info("ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
										.replace("%ac%", ac.getName()));
								sender.spigot().sendMessage(ChatApi.tctl(
										"ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
										.replace("%ac%", ac.getName())));
								return false;
							}
							return false;
						}
					} else
					{
						aclist = ac.subargument;
						break;
					}
				}
			}
		}
		sender.spigot().sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getLang().getString("InputIsWrong"),
				ClickEvent.Action.RUN_COMMAND, CAS.infoCommand));
		return false;
	}
	
	public void baseCommands(final Player player, int page)
	{
		int count = 0;
		int start = page*10;
		int end = page*10+9;
		int last = 0;
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Headline")));
		for(BaseConstructor bc : plugin.getCommandHelpList())
		{
			if(count >= start && count <= end)
			{
				if(ModifierValueEntry.hasPermission(player, bc))
				{
					sendInfo(player, bc);
				}
			}
			count++;
			last++;
		}
		boolean lastpage = false;
		if(end >= last)
		{
			lastpage = true;
		}
		pastNextPage(player, page, lastpage, CAS.infoCommand);
	}
	
	private void sendInfo(Player player, BaseConstructor bc)
	{
		player.spigot().sendMessage(ChatApi.apiChat(
				bc.getHelpInfo(),
				ClickEvent.Action.SUGGEST_COMMAND, bc.getSuggestion(),
				HoverEvent.Action.SHOW_TEXT,plugin.getYamlHandler().getLang().getString("GeneralHover")));
	}
	
	public void pastNextPage(Player player,
			int page, boolean lastpage, String cmdstring, String...objects)
	{
		if(page==0 && lastpage)
		{
			return;
		}
		int i = page+1;
		int j = page-1;
		TextComponent MSG = ChatApi.tctl("");
		List<BaseComponent> pages = new ArrayList<BaseComponent>();
		if(page!=0)
		{
			TextComponent msg2 = ChatApi.tctl(
					plugin.getYamlHandler().getLang().getString("Past"));
			String cmd = cmdstring+" "+String.valueOf(j);
			for(String o : objects)
			{
				cmd += " "+o;
			}
			msg2.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			pages.add(msg2);
		}
		if(!lastpage)
		{
			TextComponent msg1 = ChatApi.tctl(
					plugin.getYamlHandler().getLang().getString("Next"));
			String cmd = cmdstring+" "+String.valueOf(i);
			for(String o : objects)
			{
				cmd += " "+o;
			}
			msg1.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			if(pages.size()==1)
			{
				pages.add(ChatApi.tc(" | "));
			}
			pages.add(msg1);
		}
		MSG.setExtra(pages);	
		player.spigot().sendMessage(MSG);
	}
	
	public static void pastNextPage(Player player,
			int page, CommandSuggest.Type cet, String...objects)
	{
		String cmdstring = CommandSuggest.get(cet).getCommandString();
		int i = page+1;
		int j = page-1;
		TextComponent MSG = ChatApi.tctl("");
		List<BaseComponent> pages = new ArrayList<BaseComponent>();
		if(page!=0)
		{
			TextComponent msg2 = ChatApi.tctl(
					CAS.getPlugin().getYamlHandler().getLang().getString("Past"));
			String cmd = cmdstring+" "+String.valueOf(j);
			for(String o : objects)
			{
				cmd += " "+o;
			}
			msg2.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			pages.add(msg2);
		}
		TextComponent msg1 = ChatApi.tctl(
				CAS.getPlugin().getYamlHandler().getLang().getString("Next"));
		String cmd = cmdstring+" "+String.valueOf(i);
		for(String o : objects)
		{
			cmd += " "+o;
		}
		msg1.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
		if(pages.size()==1)
		{
			pages.add(ChatApi.tc(" | "));
		}
		pages.add(msg1);
		MSG.setExtra(pages);	
		player.spigot().sendMessage(MSG);
	}
}