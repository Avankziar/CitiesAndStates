package me.avankziar.cas.spigot.handler;

import org.bukkit.entity.EntityType;

public class EntityHandler
{
	public static boolean isLivestock(EntityType et)
	{
		switch(et)
		{
		default:
			return false;
		case BEE:
		case CAMEL:
		case CAT:
		case CHICKEN:
		case COW:
		case DONKEY:
		case FOX:
		case GOAT:
		case HORSE:
		case LLAMA:
		case MOOSHROOM:
		case MULE:
		case PARROT:
		case PIG:
		case RABBIT:
		case SHEEP:
		case STRIDER:
		case WOLF:
			return true;
		}
	}
}