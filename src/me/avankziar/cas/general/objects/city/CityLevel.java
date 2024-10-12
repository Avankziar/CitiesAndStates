package me.avankziar.cas.general.objects.city;

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