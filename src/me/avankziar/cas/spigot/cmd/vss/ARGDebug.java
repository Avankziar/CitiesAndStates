package me.avankziar.cas.spigot.cmd.vss;

import java.io.IOException;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.cas.general.cmdtree.ArgumentConstructor;
import me.avankziar.cas.spigot.CAS;
import me.avankziar.cas.spigot.cmdtree.ArgumentModule;

public class ARGDebug extends ArgumentModule
{
	private CAS plugin;
	
	public ARGDebug(CAS plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		String identifier = args[1];
		switch(identifier)
		{
		default:
			break;
		case "blockface":
			Block b = player.getTargetBlock(null, 10);
			if(!(b.getState() instanceof org.bukkit.block.Sign))
			{
				player.sendMessage("No Sign found!");
				return;
			}
			if(b.getBlockData() instanceof org.bukkit.block.data.type.WallSign)
			{
				org.bukkit.block.data.type.WallSign ws = (org.bukkit.block.data.type.WallSign) b.getBlockData();
				Block behind = b.getRelative(ws.getFacing().getOppositeFace());
				player.sendMessage("Behind Block Type = "+behind.getType());
			} else
			{
				Block under = b.getRelative(BlockFace.DOWN);
				player.sendMessage("Bottom Block Type = "+under.getType());
			}
			break;
		}
	}
}