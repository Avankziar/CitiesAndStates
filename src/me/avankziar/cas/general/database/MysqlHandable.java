package me.avankziar.cas.general.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface MysqlHandable
{
	public boolean create(Connection conn, String tablename);
	
	public boolean update(Connection conn, String tablename, String whereColumn, Object... whereObject);
	
	public ArrayList<Object> get(Connection conn, String tablename, String orderby, String limit, String whereColumn, Object... whereObject);
	
	public ArrayList<Object> get(Connection conn, String tablename, String sql, Object... whereObject);
	
	default void log(Logger logger, Level level, String log, Exception e)
	{
		logger.log(level, log, e);
	}
	
	default void log(Level level, String log, Exception e)
	{
		MysqlBaseHandler.getLogger().log(level, log, e);
	}
}