package me.avankziar.cas.spigot.handler.city;

import java.util.List;

import me.avankziar.cas.spigot.CAS;

public class CityConfigHandler 
{
	public static List<String> getExpandCityMoneyCost()
	{
		return CAS.getPlugin().getYamlHandler().getConfig_City().getStringList("ExpandCity.MoneyCost");
	}
	
	public static List<String> getExpandCityExpCost()
	{
		return CAS.getPlugin().getYamlHandler().getConfig_City().getStringList("ExpandCity.ExpCost");
	}
	
	public static List<String> getExpandCityMaterialCost()
	{
		return CAS.getPlugin().getYamlHandler().getConfig_City().getStringList("ExpandCity.MaterialCost");
	}
	
	public static int getMinimumDistanceBetweenCities()
	{
		return CAS.getPlugin().getYamlHandler().getConfig_City().getInt("MinimumDistanceBetweenCities", 100);
	}
}