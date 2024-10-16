package me.avankziar.cas.spigot.handler;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventarHandler 
{
	public static boolean hasItem(Player player, Material mat, int amount)
	{
		Inventory inv = player.getInventory();
		int count = 0;
		for(ItemStack is : inv.getStorageContents())
		{
			if(is == null || is.getType() == Material.AIR || is.getType() != mat)
			{
				continue;
			}
			if(amount > count + is.getAmount())
			{
				count += is.getAmount();
			} else if(amount == count + is.getAmount())
			{
				count += is.getAmount();
				break;
			} else if(amount < count + is.getAmount())
			{
				count = amount;
				break;
			}
		}
		return count >= amount;
	}
	
	public static void removeItem(Player player, Material mat, int amount)
	{
		Inventory inv = player.getInventory();
		int count = 0;
		for(ItemStack is : inv.getStorageContents())
		{
			if(is == null || is.getType() == Material.AIR || is.getType() != mat)
			{
				continue;
			}
			if(amount > count + is.getAmount())
			{
				count += is.getAmount();
				is.setAmount(0);
			} else if(amount == count + is.getAmount())
			{
				is.setAmount(0);
				break;
			} else if(amount < count + is.getAmount())
			{
				int dif = amount - count;
				is.setAmount(is.getAmount()-dif);
				break;
			}
		}
	}
}
