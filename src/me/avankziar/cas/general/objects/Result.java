package me.avankziar.cas.general.objects;

public enum Result
{
	ALLOW(1, true),
	DENY(-1, false);
	
	private int ordinal;
	private Boolean value;
	
	Result(int ordinal, boolean value)
	{
		this.ordinal = ordinal;
		this.value = value;
	}
	
	public int getOrdinal()
	{
		return this.ordinal;
	}
	
	public Boolean getValue()
	{
		return this.value;
	}
}
