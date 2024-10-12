package me.avankziar.cas.spigot.modifiervalueentry;

import java.util.LinkedHashMap;

import me.avankziar.cas.spigot.CAS;

public class Bypass
{
	public enum Permission
	{
		STORAGE_CREATION,
		CITY_CREATION_WORLDGUARD,
		STORAGE_GUI_BYPASS,
		STATE_CREATION_WORLDGUARD;
		
		public String getValueLable()
		{
			return CAS.getPlugin().pluginname.toLowerCase()+"-"+this.toString().toLowerCase();
		}
	}
	private static LinkedHashMap<Bypass.Permission, String> mapPerm = new LinkedHashMap<>();
	
	public static void set(Bypass.Permission bypass, String perm)
	{
		mapPerm.put(bypass, perm);
	}
	
	public static String get(Bypass.Permission bypass)
	{
		return mapPerm.get(bypass);
	}
	
	public enum Counter
	{
		STORAGE_CREATION_AMOUNT_,
		COST_ADDING_STORAGE(false);
		
		private boolean forPermission;
		
		Counter()
		{
			this.forPermission = true;
		}
		
		Counter(boolean forPermission)
		{
			this.forPermission = forPermission;
		}
	
		public boolean forPermission()
		{
			return this.forPermission;
		}
		
		public String getModification()
		{
			return CAS.getPlugin().pluginname.toLowerCase()+"-"+this.toString().toLowerCase();
		}
	}
	private static LinkedHashMap<Bypass.Counter, String> mapCount = new LinkedHashMap<>();
	
	public static void set(Bypass.Counter bypass, String perm)
	{
		mapCount.put(bypass, perm);
	}
	
	public static String get(Bypass.Counter bypass)
	{
		return mapCount.get(bypass);
	}
}