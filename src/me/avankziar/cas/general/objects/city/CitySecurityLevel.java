package me.avankziar.cas.general.objects.city;

import java.util.LinkedHashMap;
import java.util.logging.Level;

import me.avankziar.cas.spigot.CAS;

public enum CitySecurityLevel
{
	LOWEST(1), 
	/*
	 * Spieler die nicht zugehörig zum City sind,
	 * können auf alle Knöpfe, Druckplatten. Sowie alles andere auch(Ambosse, etc)
	 */
	LOW(2),
	/*
	 * Spieler die nicht zugehörig zum City sind,
	 * können alle Knöpfe, Druckplatten.
	 */
	MEDIUM(3),
	/*
	 * Spieler die nicht zugehörig zum City sind,
	 * können nur Holz Knopfe und Druckplatten.
	 */
	HIGH(4),
	/*
	 * Spieler die nicht zugehörig zum City sind,
	 * können nur Holz Knopfe und Druckplatten.
	 */
	VERYHIGH(5);
	/*
	 * Spieler die nicht zugehörig zum District sind,
	 * können nicht im District benutzen. ??
	 */
	
	private static final LinkedHashMap<Integer, CitySecurityLevel> values = new LinkedHashMap<>();
	
	static
	{
		try
		{
			for(CitySecurityLevel mt : CitySecurityLevel.values())
			{
				values.put(mt.getOrdinal(), mt);
			}
		} catch(Exception e)
		{
			CAS.log.log(Level.SEVERE, "SecurityLevel not initialized!");
		}
	}
	
	public static CitySecurityLevel get(int ordinal)
	{
		return values.get(ordinal);
	}
	
	private int ordinal;
	
	CitySecurityLevel(int ordinal)
	{
		this.ordinal = ordinal;
	}
	
	public int getOrdinal()
	{
		return this.ordinal;
	}
}
