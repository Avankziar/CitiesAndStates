package me.avankziar.cas.spigot.handler.city;

import java.util.HashMap;

import me.avankziar.cas.general.database.MysqlType;
import me.avankziar.cas.general.objects.Region3D;
import me.avankziar.cas.general.objects.city.City;
import me.avankziar.cas.spigot.CAS;
import me.avankziar.cas.spigot.database.MysqlHandler;
import me.avankziar.cas.spigot.handler.region.MemoryHandler;

public class CityFormulaHandler 
{
	public static String 
			X_DISTANCE = "",
			Y_DISTANCE = "",
			Z_DISTANCE = "",
			DISTRICT_AMOUNT = "",
			PROPERTY_AMOUNT = ""
			;
	public static HashMap<String, Double> getFormulaVariables(City c)
	{
		HashMap<String, Double> map = new HashMap<>();
		Region3D r = MemoryHandler.getCity(c.getId());
		map.put(X_DISTANCE, r.getXAbsolutLenght());
		map.put(Y_DISTANCE, r.getYAbsolutLenght());
		map.put(Z_DISTANCE, r.getZAbsolutLenght());
		MysqlHandler sql = CAS.getPlugin().getMysqlHandler();
		map.put(DISTRICT_AMOUNT, (double) sql.getCount(MysqlType.DISTRICT, "`city_id` = ?", c.getId()));
		map.put(PROPERTY_AMOUNT, (double) sql.getCount(MysqlType.PROPERTY, "`city_id` = ?", c.getId()));
		//ADDME Erweiterung Disrictmember etc.
		return map;
	}
}