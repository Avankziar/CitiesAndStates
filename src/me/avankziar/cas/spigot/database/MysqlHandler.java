package me.avankziar.cas.spigot.database;

import me.avankziar.cas.general.database.MysqlBaseHandler;
import me.avankziar.cas.spigot.CAS;

public class MysqlHandler extends MysqlBaseHandler
{	
	public MysqlHandler(CAS plugin)
	{
		super(plugin.getLogger(), plugin.getMysqlSetup());
	}
}