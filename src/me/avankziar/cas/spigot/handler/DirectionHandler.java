package me.avankziar.cas.spigot.handler;

import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class DirectionHandler
{
	public static BlockFace getDirection(Vector vector)
	{
		BlockFace dir = BlockFace.SELF;
	    float minAngle = Float.MAX_VALUE;
	    float angle;
	    for (BlockFace tested : BlockFace.values()) 
	    {
	        if (tested != BlockFace.SELF) 
	        {
	            angle = vector.angle(tested.getDirection());
	            if (!Float.isNaN(angle) && angle < minAngle) 
	            {
	                minAngle = angle;
	                dir = tested;
	            }
	        }
	    }
	    return dir;
	}
}