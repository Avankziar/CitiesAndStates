package me.avankziar.cas.general.database;

public interface MemoryHandable
{
	/**
	 * Set in the mysql and in the ram if the object is on the same server;
	 */
	void create();
	/**
	 * Save the object in the hashmap in the ram.
	 */
	void saveRAM();
	
	/**
	 * Save the object in the mysql.
	 */
	void saveMysql();
}