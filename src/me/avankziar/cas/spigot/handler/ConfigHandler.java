package me.avankziar.cas.spigot.handler;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import me.avankziar.cas.spigot.gui.objects.SettingsLevel;

public class ConfigHandler
{
	public enum CountType
	{
		HIGHEST, ADDUP;
	}
	
	public CountType getCountPermType()
	{
		String s = config.getString("Mechanic.CountPerm", "HIGHEST");
		CountType ct;
		try
		{
			ct = CountType.valueOf(s);
		} catch (Exception e)
		{
			ct = CountType.HIGHEST;
		}
		return ct;
	}
	
	public static YamlConfiguration config = null;
	
	public static boolean isMechanicModifierEnabled()
	{
		return config.getBoolean("EnableMechanic.Modifier", false);
	}
	
	public static boolean isMechanicValueEntryEnabled()
	{
		return config.getBoolean("EnableMechanic.ValueEntry", false);
	}
	
	public static boolean isForceSettingsLevel()
	{
		return config.getBoolean("SignStorage.Gui.ForceSettingsLevel", false);
	}
	
	public static long getGuiClickCooldown()
	{
		return config.getLong("SignStorage.Gui.ClickCooldown", 500);
	}
	
	public static SettingsLevel getForcedSettingsLevel()
	{
		try
		{
			return SettingsLevel.valueOf(config.getString("SignStorage.Gui.ToBeForcedSettingsLevel"));
		} catch(Exception e)
		{
			return SettingsLevel.BASE;
		}
	}
	
	public static boolean fillNotDefineGuiSlots()
	{
		return config.getBoolean("SignStorage.Gui.FillNotDefineGuiSlots", true);
	}
	
	public static Material fillerMaterial()
	{
		try
		{
			return Material.valueOf(config.getString("SignStorage.Gui.FillerItemMaterial"));
		} catch(Exception e)
		{
			return Material.LIGHT_GRAY_STAINED_GLASS_PANE;
		}
	}
}