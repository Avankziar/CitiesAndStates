package me.avankziar.cas.spigot.handler.region;

import me.avankziar.cas.general.database.MysqlType;
import me.avankziar.cas.general.objects.property.Property;
import me.avankziar.cas.spigot.CAS;

public class PropertyHandler
{
	public static Property getProperty(long id)
	{
		return (Property) CAS.getPlugin().getMysqlHandler().getData(MysqlType.PROPERTY, "`id` = ?", id);
	}
}