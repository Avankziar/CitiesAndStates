package me.avankziar.cas.spigot.modifiervalueentry;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import me.avankziar.cas.general.cmdtree.BaseConstructor;
import me.avankziar.cas.spigot.CAS;
import me.avankziar.cas.spigot.handler.ConfigHandler;
import me.avankziar.cas.spigot.handler.ConfigHandler.CountType;

public class ModifierValueEntry
{
	public static boolean hasPermission(Player player, BaseConstructor bc)
	{
		if(CAS.getPlugin().getValueEntry() != null)
		{
			Boolean ss = CAS.getPlugin().getValueEntry().getBooleanValueEntry(
					player.getUniqueId(),
					bc.getValueEntryPath(CAS.getPlugin().pluginname),
					CAS.getPlugin().getServername(),
					player.getWorld().getName());
			if(ss == null)
			{
				if(CAS.getPlugin().getYamlHandler().getConfig().getBoolean("ValueEntry.OverrulePermission", false))
				{
					return false;
				} else
				{
					return player.hasPermission(bc.getPermission());
				}
			}
			if(CAS.getPlugin().getYamlHandler().getConfig().getBoolean("ValueEntry.OverrulePermission", false))
			{
				return ss;
			} else
			{
				if(ss || player.hasPermission(bc.getPermission()))
				{
					return true;
				}
			}
			return false;
		}
		return player.hasPermission(bc.getPermission());
	}
	
	public static boolean hasPermission(Player player, Bypass.Permission bypassPermission)
	{
		if(CAS.getPlugin().getValueEntry() != null)
		{
			Boolean ss = CAS.getPlugin().getValueEntry().getBooleanValueEntry(
					player.getUniqueId(),
					bypassPermission.getValueLable(),
					CAS.getPlugin().getServername(),
					player.getWorld().getName());
			if(ss == null)
			{
				if(CAS.getPlugin().getYamlHandler().getConfig().getBoolean("ValueEntry.OverrulePermission", false))
				{
					return false;
				} else
				{
					return player.hasPermission(Bypass.get(bypassPermission));
				}
			}
			if(CAS.getPlugin().getYamlHandler().getConfig().getBoolean("ValueEntry.OverrulePermission", false))
			{
				return ss;
			} else
			{
				if(ss || player.hasPermission(Bypass.get(bypassPermission)))
				{
					return true;
				}
			}
			return false;
		}
		return player.hasPermission(Bypass.get(bypassPermission));
	}
	
	public static boolean hasPermission(Player player, Bypass.Permission bypassPermission, String addition)
	{
		if(CAS.getPlugin().getValueEntry() != null)
		{
			Boolean ss = CAS.getPlugin().getValueEntry().getBooleanValueEntry(
					player.getUniqueId(),
					bypassPermission.getValueLable(),
					CAS.getPlugin().getServername(),
					player.getWorld().getName());
			if(ss == null)
			{
				if(CAS.getPlugin().getYamlHandler().getConfig().getBoolean("ValueEntry.OverrulePermission", false))
				{
					return false;
				} else
				{
					return player.hasPermission(Bypass.get(bypassPermission)+addition);
				}
			}
			if(CAS.getPlugin().getYamlHandler().getConfig().getBoolean("Condition.ConditionOverrulePermission", false))
			{
				return ss;
			} else
			{
				if(ss || player.hasPermission(Bypass.get(bypassPermission)+addition))
				{
					return true;
				}
			}
			return false;
		}
		return player.hasPermission(Bypass.get(bypassPermission));
	}
	
	public static int getResult(@NonNull Player player, Bypass.Counter countPermission)
	{
		return getResult(player, 0.0, countPermission);
	}
	
	public static int getResult(@NonNull Player player, double value, Bypass.Counter countPermission)
	{
		if(player.hasPermission(Bypass.get(countPermission)+"*"))
		{
			return Integer.MAX_VALUE;
		}
		int possibleAmount = 0;
		CountType ct = new ConfigHandler().getCountPermType();
		switch(ct)
		{
		case ADDUP:
			for(int i = 1000; i >= 0; i--)
			{
				if(player.hasPermission(Bypass.get(countPermission)+i))
				{
					possibleAmount += i;
				}
			}
			break;
		case HIGHEST:
			for(int i = 1000; i >= 0; i--)
			{
				if(player.hasPermission(Bypass.get(countPermission)+i))
				{
					possibleAmount = i;
					break;
				}
			}
			break;
		}
		possibleAmount += (int) value;
		if(CAS.getPlugin().getModifier() != null)
		{
			return (int) CAS.getPlugin().getModifier().getResult(
					player.getUniqueId(),
					possibleAmount,
					countPermission.getModification(),
					CAS.getPlugin().getServername(),
					player.getWorld().getName());
		}
		return possibleAmount;
	}
	
	public static double getResult(UUID uuid, double value, Bypass.Counter countPermission)
	{
		double possibleAmount = value;
		if(CAS.getPlugin().getModifier() != null)
		{
			return CAS.getPlugin().getModifier().getResult(
					uuid,
					possibleAmount,
					countPermission.getModification());
		}
		return possibleAmount;
	}
}