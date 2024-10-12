package me.avankziar.cas.spigot.handler.region;

import me.avankziar.cas.general.database.MysqlType;
import me.avankziar.cas.general.objects.city.City;
import me.avankziar.cas.spigot.CAS;

public class CityHandler
{
	public static City getCity(long id)
	{
		return (City) CAS.getPlugin().getMysqlHandler().getData(MysqlType.CITY, "`id` = ?", id);
	}
}