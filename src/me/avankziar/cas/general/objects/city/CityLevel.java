package me.avankziar.cas.general.objects.city;

import java.util.LinkedHashMap;
import java.util.logging.Level;

import me.avankziar.cas.spigot.CAS;

public enum CityLevel
{
	CAMP(0), //Lager
	OUTPOST(1), //Außenposten
	SETTLEMENT(2), //Siedlung
	VILLAGE(3), //Dort
	MUNICIPALITY(4), //Kommune
	SMALL_CITY(5), //Kleinstadt
	MIDDLE_CITY(6), //Mittlestadt oder Stadt
	LARGE_CITY(7), //Großstadt
	METROPOLIS(8), //Metropole
	CITY_STATE(9) //Stadtstaat
	;
	
	private static final LinkedHashMap<Integer, CityLevel> values = new LinkedHashMap<>();
	
	static
	{
		try
		{
			for(CityLevel mt : CityLevel.values())
			{
				values.put(mt.getOrdinal(), mt);
			}
		} catch(Exception e)
		{
			CAS.log.log(Level.SEVERE, "SecurityLevel not initialized!");
		}
	}
	
	public static CityLevel get(int ordinal)
	{
		return values.get(ordinal);
	}
	
	private int ordinal;
	
	CityLevel(int ordinal)
	{
		this.ordinal = ordinal;
	}
	
	public int getOrdinal()
	{
		return this.ordinal;
	}
}