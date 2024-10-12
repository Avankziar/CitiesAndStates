package me.avankziar.cas.spigot.handler.region;

import me.avankziar.cas.general.database.MysqlType;
import me.avankziar.cas.general.objects.district.District;
import me.avankziar.cas.spigot.CAS;

public class DistrictHandler
{
	public static District getDistrict(long id)
	{
		return (District) CAS.getPlugin().getMysqlHandler().getData(MysqlType.DISTRICT, "`id` = ?", id);
	}
}