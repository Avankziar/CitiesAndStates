package me.avankziar.cas.spigot.gui.listener;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.cas.spigot.CAS;
import me.avankziar.cas.spigot.gui.events.UpperGuiClickEvent;
import me.avankziar.cas.spigot.gui.handler.GuiHandler;
import me.avankziar.cas.spigot.gui.objects.ClickFunctionType;
import me.avankziar.cas.spigot.gui.objects.ClickType;
import me.avankziar.cas.spigot.gui.objects.GuiType;
import me.avankziar.cas.spigot.handler.ConfigHandler;

public class UpperListener implements Listener
{
	private CAS plugin;
	private long dur = 1000;
	
	public UpperListener(CAS plugin)
	{
		this.plugin = plugin;
		dur = ConfigHandler.getGuiClickCooldown();
	}
	
	private LinkedHashMap<UUID, Long> cooldown = new LinkedHashMap<>();;
	
	public boolean isOnCooldown(UUID uuid)
	{
		Long c = cooldown.get(uuid);
		return c == null ? false : c.longValue() > System.currentTimeMillis();
	}
	
	public void setCooldown(UUID uuid, long duration, TimeUnit timeUnit)
	{
		cooldown.put(uuid, timeUnit.convert(duration, TimeUnit.MILLISECONDS)+System.currentTimeMillis());
	}
	
	public void removeCooldown(UUID uuid)
	{
		cooldown.remove(uuid);
	}
	
	@EventHandler
	public void onUpperGui(UpperGuiClickEvent event) throws IOException
	{
		if(!event.getPluginName().equals(plugin.pluginname))
		{
			return;
		}
		if(!(event.getEvent().getWhoClicked() instanceof Player))
		{
			return;
		}
		Player player = (Player) event.getEvent().getWhoClicked();
		GuiType gut = null;
		try
		{
			gut = GuiType.valueOf(event.getInventoryIdentifier());
		} catch(Exception e)
		{
			return;
		}
		if(!event.getValuesInteger().containsKey(GuiHandler.CITY_ID))
		{
			return;
		}
		if(isOnCooldown(player.getUniqueId()))
		{
			return;
		}
		setCooldown(player.getUniqueId(), dur, TimeUnit.MILLISECONDS);
		//int sshID = event.getValuesInteger().get(GuiHandler.CITY_ID);
		//_SignQStorage ssh = (_SignQStorage) plugin.getMysqlHandler().getData(MysqlType._SIGNQSTORAGE, "`id` = ?", sshID);
		UUID ou = null;
		if(event.getValuesString().containsKey(GuiHandler.PLAYER_UUID))
		{
			ou = UUID.fromString(event.getValuesString().get(GuiHandler.PLAYER_UUID));
		}
		/*boolean teleport_OR_location = event.getValuesString().containsKey(GuiHandler.SEARCH_TELEPORT_OR_LOCATION)
				? Boolean.valueOf(event.getValuesString().get(GuiHandler.SEARCH_TELEPORT_OR_LOCATION))
				: false;
		int page = event.getValuesInteger().containsKey(GuiHandler.PAGE)
				? event.getValuesInteger().get(GuiHandler.PAGE)
				: -1;
		String where = event.getValuesString().containsKey(GuiHandler.SEARCH_TELEPORT_OR_LOCATION)
				? event.getValuesString().get(GuiHandler.SEARCH_TELEPORT_OR_LOCATION)
				: "";*/
		ClickType ct = getClickFunctionType(event.getEvent().getClick(), event.getEvent().getHotbarButton());
		if(ct == null)
		{
			return;
		}
		ClickFunctionType cfct = null;
		try
		{
			cfct = ClickFunctionType.valueOf(event.getFunction(ct));
		} catch(Exception e)
		{
			return;
		}
		if(cfct == null)
		{
			return;
		}
		final GuiType gt = gut;
		final ClickFunctionType cft = cfct;
		final UUID otheruuid = ou;
		switch(gt)
		{
		case ITEM_INPUT:
			break;
		case ADMINISTRATION:
		case NUMPAD_ACCOUNT:
		case KEYBOARD_MEMBER:
		case KEYBOARD_SIGNSTORAGENAME:
		case KEYBOARD_WHITELIST:
		case NUMPAD_ITEMINPUT:
		case NUMPAD_ITEMOUTPUT:
		case NUMPAD_ITEMSHIFTINPUT:
		case NUMPAD_ITEMSHIFTOUTPUT:
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					//AdminstrationFunctionQuantityHandler.doClickFunktion(gt, cft, player, 0,
					//		event.getEvent().getClickedInventory(), event.getSettingsLevel(), otheruuid);
				}
			}.runTaskAsynchronously(plugin);
			break;
		}
	}
	
	private ClickType getClickFunctionType(org.bukkit.event.inventory.ClickType ct, int hotbarButton)
	{
		switch(ct)
		{
		default: return null;
		case LEFT: return ClickType.LEFT;
		case RIGHT: return ClickType.RIGHT;
		case DROP: return ClickType.DROP;
		case SHIFT_LEFT: return ClickType.SHIFT_LEFT;
		case SHIFT_RIGHT: return ClickType.SHIFT_RIGHT;
		case CONTROL_DROP: return ClickType.CTRL_DROP;
		case NUMBER_KEY:
			if(hotbarButton < 0)
			{
				return null;
			}
			int i = hotbarButton+1;
			switch(i)
			{
			default: return null;
			case 1: return ClickType.NUMPAD_1;
			case 2: return ClickType.NUMPAD_2;
			case 3: return ClickType.NUMPAD_3;
			case 4: return ClickType.NUMPAD_4;
			case 5: return ClickType.NUMPAD_5;
			case 6: return ClickType.NUMPAD_6;
			case 7: return ClickType.NUMPAD_7;
			case 8: return ClickType.NUMPAD_8;
			case 9: return ClickType.NUMPAD_9;
			}
		}
	}
}