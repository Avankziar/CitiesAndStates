package me.avankziar.cas.general.objects;

import java.util.LinkedHashMap;
import java.util.logging.Level;

import me.avankziar.cas.spigot.CAS;

public enum MonumentType
{
	NONE(0, true),
	SMALLEST_HOUSE_OF_PRAYER(1, false),
	SMALL_HOUSE_OF_PRAYER(2, false),
	NORMAL_HOUSE_OF_PRAYER(3, false),
	LARGE_HOUSE_OF_PRAYER(4, false),
	LARGEST_HOUSE_OF_PRAYER(5, true),
	//ADDME Expand here,
	;
	
	private static final LinkedHashMap<Integer, MonumentType> values = new LinkedHashMap<>();
	
	static
	{
		try
		{
			for(MonumentType mt : MonumentType.values())
			{
				values.put(mt.getOrdinal(), mt);
			}
		} catch(Exception e)
		{
			CAS.log.log(Level.SEVERE, "MonumentType not initialized!");
		}
	}
	
	public static MonumentType get(int ordinal)
	{
		return values.get(ordinal);
	}
	
	private int ordinal;
	private boolean isHighest;
	
	MonumentType(int ordinal, boolean isHighest)
	{
		this.ordinal = ordinal;
		this.isHighest = isHighest;
	}
	
	public int getOrdinal()
	{
		return this.ordinal;
	}
	
	public boolean isHighest()
	{
		return this.isHighest;
	}
	
	public MonumentType getNextLevelType()
	{
		if(!isHighest())
		{
			return values.get(getOrdinal()+1);
		} else
		{
			return this;
		}
	}
}
