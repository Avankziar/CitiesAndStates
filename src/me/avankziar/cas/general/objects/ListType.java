package me.avankziar.cas.general.objects;

public enum ListType
{
	OTHER(0),
	PROPERTY_MEMBER(1),
	PROPERTY_OWNER(2),
	DISTRICT_MEMBER(3),
	DISTRICT_MANAGER(4),
	CITY_MEMBER(5),
	CITY_MANAGER(6),
	;
	
	private int ordinal;
	
	ListType(int ordinal)
	{
		this.ordinal = ordinal;
	}
	
	public int getOrdinal()
	{
		return this.ordinal;
	}
}