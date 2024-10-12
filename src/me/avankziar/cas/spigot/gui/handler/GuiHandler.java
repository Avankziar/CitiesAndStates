package me.avankziar.cas.spigot.gui.handler;

import me.avankziar.cas.spigot.CAS;

public class GuiHandler
{
	private static CAS plugin = CAS.getPlugin();
	public static String CITY_ID = "city_id";
	public static String PLAYER_UUID = "player_uuid";
	public static String PAGE = "page";
	
	/*public static void openAdministration(_SignQStorage ssh, Player player, SettingsLevel settingsLevel, boolean closeInv)
	{
		GuiType gt = GuiType.ADMINISTRATION;
		GUIApi gui = new GUIApi(plugin.pluginname, gt.toString(), null, 6, ssh.getSignStorageName(), 
				settingsLevel == null ? SettingsLevel.BASE : settingsLevel);
		_SignQStorage ssh2 = (_SignQStorage) plugin.getMysqlHandler().getData(MysqlType._SIGNQSTORAGE, "`id` = ?", ssh.getId());
		openGui(ssh2, player, gt, gui, settingsLevel, closeInv);
	}
	
	public static void openAdministration(_SignQStorage ssh, Player player, SettingsLevel settingsLevel, Inventory inv, boolean closeInv)
	{
		GuiType gt = GuiType.ADMINISTRATION;
		GUIApi gui = new GUIApi(plugin.pluginname, inv, gt.toString(), 
				settingsLevel == null ? SettingsLevel.BASE : settingsLevel);
		_SignQStorage ssh2 = (_SignQStorage) plugin.getMysqlHandler().getData(MysqlType._SIGNQSTORAGE, "`id` = ?", ssh.getId());
		openGui(ssh2, player, gt, gui, settingsLevel, closeInv);
	}
	
	public static void openInputInfo(_SignQStorage ssh, Player player, SettingsLevel settingsLevel, boolean closeInv)
	{
		GuiType gt = GuiType.ITEM_INPUT;
		GUIApi gui = new GUIApi(plugin.pluginname, gt.toString(), null, 6, "VSSID:"+String.valueOf(ssh.getId()), settingsLevel);
		_SignQStorage ssh2 = (_SignQStorage) plugin.getMysqlHandler().getData(MysqlType._SIGNQSTORAGE, "`id` = ?", ssh.getId());
		openGui(ssh2, player, gt, gui, settingsLevel, closeInv);
	}
	
	public static void openKeyOrNumInput(_SignQStorage ssh, Player player, GuiType gt, SettingsLevel settingsLevel, String keyboardOrNumpad, boolean closeInv)
	{
		GUIApi gui = new GUIApi(plugin.pluginname, gt.toString(), null, 6, ssh.getSignStorageName()+keyboardOrNumpad, settingsLevel);
		_SignQStorage ssh2 = (_SignQStorage) plugin.getMysqlHandler().getData(MysqlType._SIGNQSTORAGE, "`id` = ?", ssh.getId());
		openGui(ssh2, player, gt, gui, settingsLevel, closeInv);
	}
	
	private static void openGui(_SignQStorage sst, Player player, GuiType gt, GUIApi gui, SettingsLevel settingsLevel, boolean closeInv)
	{
		if(plugin.getIFHEco() != null)
		{
			Account ac = plugin.getIFHEco().getAccount(sst.getAccountId());
			if(ac == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.AccountNotExist")
						.replace("%player%", Utility.convertUUIDToName(sst.getOwner().toString()))));
			}
		} else if(plugin.getVaultEco() != null)
		{
			if(!plugin.getVaultEco().hasAccount(Bukkit.getOfflinePlayer(sst.getOwner())))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("GuiHandler.AccountNotExist")
						.replace("%player%", Utility.convertUUIDToName(sst.getOwner().toString()))));
			}
		}
		boolean fillNotDefineGuiSlots = ConfigHandler.fillNotDefineGuiSlots();
		Material filler = ConfigHandler.fillerMaterial();
		YamlConfiguration y = plugin.getYamlHandler().getGui(gt);
		for(int i = 0; i < 54; i++)
		{
			if(y.get(i+".IsInfoItem") != null && y.getBoolean(i+".IsInfoItem"))
			{
				ItemStack is = sst.getItemStack();
				LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
				map.put(CITY_ID, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.INTEGER, sst.getId()));
				gui.add(i, is, settingsLevel, true, map, getClickFunction(y, String.valueOf(i)));
				continue;
			}
			if(y.get(i+".Material") == null && y.get(i+".Material."+settingsLevel.toString()) == null)
			{
				if(fillNotDefineGuiSlots)
				{
					filler(gui, sst.getId(), i, filler);
				}
				continue;
			}			
			SettingsLevel itemSL = SettingsLevel.valueOf(y.getString(i+".SettingLevel"));
			if(y.get(i+".SettingLevel") == null)
			{
				itemSL = SettingsLevel.NOLEVEL;
			}
			if(settingsLevel.getOrdinal() < itemSL.getOrdinal())
			{
				if(fillNotDefineGuiSlots)
				{
					filler(gui, sst.getId(), i, filler);
				}
				continue;
			}
			if(y.get(i+".Permission") != null)
			{
				if(!ModifierValueEntry.hasPermission(player, Bypass.Permission.STORAGE_GUI_BYPASS, y.getString(i+".Permission")))
				{
					if(fillNotDefineGuiSlots)
					{
						filler(gui, sst.getId(), i, filler);
					}
					continue;
				}
			}
			if(y.get(i+".IFHDepend") != null)
			{
				if(y.getBoolean(i+".IFHDepend"))
				{
					if(plugin.getIFHEco() == null)
					{
						if(fillNotDefineGuiSlots)
						{
							filler(gui, sst.getId(), i, filler);
						}
						continue;
					}
				}
			}
			Material mat = null;
			ItemStack is = null;
			if(y.get(i+".Material."+settingsLevel.toString()) != null)
			{
				mat = Material.valueOf(y.getString(i+".Material."+settingsLevel.toString()));
				if(mat == Material.PLAYER_HEAD && y.getString(i+"."+settingsLevel.toString()+".PlayerHeadTexture") != null)
				{
					is = getSkull(y.getString(i+"."+settingsLevel.getName()+".PlayerHeadTexture"));
				}
			} else
			{
				try
				{
					mat = Material.valueOf(y.getString(i+".Material"));
					if(mat == Material.PLAYER_HEAD && y.getString(i+".HeadTexture") != null)
					{
						is = getSkull(y.getString(i+".HeadTexture"));
					}
				} catch(Exception e)
				{
					if(fillNotDefineGuiSlots)
					{
						filler(gui, sst.getId(), i, filler);
					}
					continue;
				}
			}
			String playername = null;
			UUID otheruuid = null;
			if(y.get(i+".PlayerSearchNum") != null)
			{
				if(sst.getNumText().isBlank() || sst.getNumText().isEmpty())
				{
					if(fillNotDefineGuiSlots)
					{
						filler(gui, sst.getId(), i, filler);
					}
					continue;
				}
				int num = y.getInt(i+".PlayerSearchNum");
				ArrayList<Object> l = plugin.getMysqlHandler().getList(
						MysqlType.PLAYERDATA, "`player_name` ASC", num, 1, "`player_name` like ?", "%"+sst.getNumText()+"%");
				if(l == null || l.isEmpty())
				{
					if(fillNotDefineGuiSlots)
					{
						filler(gui, sst.getId(), i, filler);
					}
					continue;
				}
				PlayerData pd = PlayerData.convert(l).get(0);
				playername = pd.getName();
				otheruuid = pd.getUUID();
				is = new ItemStack(Material.PLAYER_HEAD);
				ItemMeta im = is.getItemMeta();
				if(!(im instanceof SkullMeta))
				{
					if(fillNotDefineGuiSlots)
					{
						filler(gui, sst.getId(), i, filler);
					}
					continue;
				}
				SkullMeta sm = (SkullMeta) im;
				try
				{
					sm.setOwningPlayer(Bukkit.getOfflinePlayer(pd.getUUID()));
				} catch(Exception e)
				{
					PlayerProfile profile = Bukkit.createPlayerProfile(pd.getUUID(), "null");
					sm.setOwnerProfile(profile);
				}
				
				is.setItemMeta(sm);
			}
			int amount = 1;
			if(y.get(i+".Amount") != null)
			{
				amount = y.getInt(i+".Amount");
			}
			ArrayList<String> lore = null;
			if(y.get(i+".Lore."+settingsLevel.toString()) != null)
			{
				lore = (ArrayList<String>) y.getStringList(i+".Lore."+settingsLevel.toString());
			} else
			{
				if(y.get(i+".Lore") != null)
				{
					lore = (ArrayList<String>) y.getStringList(i+".Lore");
				}
			}
			if(lore != null)
			{
				lore = (ArrayList<String>) getLorePlaceHolder(sst, player, lore, playername);
			}
			
			if(y.get(i+".InfoLore") != null && y.getBoolean(i+".InfoLore"))
			{
				if(lore == null)
				{
					lore = new ArrayList<>();
				}
				ArrayList<String> infoLore = getStringPlaceHolder(sst.getItemStack(), sst.getOwner());
				for(String s : infoLore)
				{
					lore.add(ChatApi.tl(s));
				}
			}
			String displayname = y.get(i+".Displayname") != null 
					? y.getString(i+".Displayname") 
					: (playername != null ? playername 
					: (CAS.getPlugin().getEnumTl() != null
							  ? CAS.getPlugin().getEnumTl().getLocalization(mat)
							  : is.getType().toString()));
			displayname = getStringPlaceHolder(sst, player, displayname, playername);
			if(is == null)
			{
				is = new ItemStack(mat, amount);
			} else
			{
				is.setAmount(amount);
			}
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(displayname);
			if(lore != null)
			{
				im.setLore(lore);
			}
			is.setItemMeta(im);
			LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
			map.put(CITY_ID, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.INTEGER, sst.getId()));
			if(otheruuid != null)
			{
				map.put(PLAYER_UUID, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING, otheruuid.toString()));
			}
			gui.add(i, is, settingsLevel, true, map, getClickFunction(y, String.valueOf(i)));
		}
		new BukkitRunnable()
		{			
			@Override
			public void run()
			{
				if(closeInv)
				{
					player.closeInventory();
				}
				gui.open(player, gt, sst.getId());
			}
		}.runTask(plugin);
		
	}
	
	private static void openListGui(ArrayList<_SignQStorage> list, Player player, GuiType gt, GUIApi gui, boolean closeInv, int page, String whereQuery)
	{
		boolean fillNotDefineGuiSlots = ConfigHandler.fillNotDefineGuiSlots();
		Material filler = ConfigHandler.fillerMaterial();
		YamlConfiguration y = plugin.getYamlHandler().getGui(gt);
		int i = 0;
		for(_SignQStorage ssh : list)
		{
			ArrayList<String> lore = null;
			if(y.get("Lore") != null)
			{
				lore = (ArrayList<String>) y.getStringList("Lore");
			}
			if(lore != null)
			{
				lore = (ArrayList<String>) getLorePlaceHolder(ssh, player, lore, player.getName());
			}
			String displayname = y.get("Displayname") != null 
					? y.getString("Displayname") 
					: (CAS.getPlugin().getEnumTl() != null
							  ? CAS.getPlugin().getEnumTl().getLocalization(ssh.getMaterial())
							  : ssh.getMaterial().toString());
			displayname = getStringPlaceHolder(ssh, player, displayname, player.getName());
			ItemStack is = new ItemStack(ssh.getMaterial() == null ? Material.WIND_CHARGE : ssh.getMaterial());
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(displayname);
			if(lore != null)
			{
				im.setLore(lore);
			}
			is.setItemMeta(im);
			LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
			map.put(CITY_ID, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.INTEGER, ssh.getId()));
			gui.add(i, is, SettingsLevel.NOLEVEL, true, map, getClickFunction(y, null));
			i++;
		}
		for(int j = 0; j < 53; j++)
		{
			if(y.get(i+".Material") == null)
			{
				if(fillNotDefineGuiSlots)
				{
					filler(gui, 0, i, filler);
				}
				continue;
			}
			Material mat = null;
			ItemStack is = null;
			try
			{
				mat = Material.valueOf(y.getString(i+".Material"));
				if(mat == Material.PLAYER_HEAD && y.getString(i+".HeadTexture") != null)
				{
					is = getSkull(y.getString(i+".HeadTexture"));
				}
			} catch(Exception e)
			{
				if(fillNotDefineGuiSlots)
				{
					filler(gui, 0, i, filler);
				}
				continue;
			}
			int amount = 1;
			ArrayList<String> lore = null;
			if(y.get(i+".Lore") != null)
			{
				lore = (ArrayList<String>) y.getStringList(i+".Lore");
			}
			String displayname = y.get(i+".Displayname") != null 
					? y.getString(i+".Displayname")
					: (CAS.getPlugin().getEnumTl() != null
							  ? CAS.getPlugin().getEnumTl().getLocalization(mat)
							  : is.getType().toString());
			if(is == null)
			{
				is = new ItemStack(mat, amount);
			} else
			{
				is.setAmount(amount);
			}
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(displayname);
			if(lore != null)
			{
				im.setLore(lore);
			}
			is.setItemMeta(im);
			LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
			map.put(CITY_ID, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.INTEGER, 0));
			if(y.getString(i+".Pagination").equalsIgnoreCase("Next"))
			{
				map.put(PAGE, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.INTEGER, page+1));
				gui.add(i, is, SettingsLevel.BASE, true, map, getClickFunction(y, String.valueOf(i)));
			} else if(y.getString(i+".Pagination").equalsIgnoreCase("Past"))
			{
				if(page > 0)
				{
					map.put(PAGE, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.INTEGER, page-1));
					gui.add(i, is, SettingsLevel.BASE, true, map, getClickFunction(y, String.valueOf(i)));
				}
			} else
			{
				if(fillNotDefineGuiSlots)
				{
					filler(gui, 0, i, filler);
				}
			}
		}
		new BukkitRunnable()
		{			
			@Override
			public void run()
			{
				if(closeInv)
				{
					player.closeInventory();
				}
				gui.open(player, gt, 0);
			}
		}.runTask(plugin);
	}
	
	private static void filler(GUIApi gui, int sshId, int i, Material mat)
	{
		ItemStack is = new ItemStack(mat, 1);
		ItemMeta im = is.getItemMeta();
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		im.addItemFlags(ItemFlag.HIDE_DESTROYS);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		im.setDisplayName(ChatApi.tl("&0"));
		im.setLore(new ArrayList<>());
		is.setItemMeta(im);
		LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
		map.put(CITY_ID, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.INTEGER, sshId));
		gui.add(i, is, SettingsLevel.NOLEVEL, true, map, new ClickFunction[0]);
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack getSkull(String paramString) 
	{
		ItemStack is = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta paramSkullMeta = (SkullMeta) is.getItemMeta();
	    try 
	    {
	    	UUID uuid = UUID.randomUUID();
	        PlayerProfile playerProfile = Bukkit.createPlayerProfile(uuid, "null");
	        playerProfile.getTextures().setSkin(new URL(paramString));
	        paramSkullMeta.setOwnerProfile(playerProfile);
	    } catch (IllegalArgumentException|SecurityException|java.net.MalformedURLException illegalArgumentException) {
	      illegalArgumentException.printStackTrace();
	    }
	    is.setItemMeta(paramSkullMeta);
	    return is;
	}
	
	public static List<String> getLorePlaceHolder(_SignQStorage ssh, Player player, List<String> lore, String playername)
	{
		List<String> list = new ArrayList<>();
		for(String s : lore)
		{
			String a = getStringPlaceHolder(ssh, player, s, playername);
			if(plugin.getIFHEco() != null)
			{
				Account ac = plugin.getIFHEco().getAccount(ssh.getAccountId());
				if(ac != null)
				{
					int dg = ac == null ? 0 : plugin.getIFHEco().getDefaultGradationQuantity(ac.getCurrency());
					boolean useSI = ac == null ? false : plugin.getIFHEco().getDefaultUseSIPrefix(ac.getCurrency());
					boolean useSy = ac == null ? false : plugin.getIFHEco().getDefaultUseSymbol(ac.getCurrency());
					String ts = ac == null ? "." : plugin.getIFHEco().getDefaultThousandSeperator(ac.getCurrency());
					String ds = ac == null ? "," : plugin.getIFHEco().getDefaultDecimalSeperator(ac.getCurrency());
					a = getStringPlaceHolderIFH(ssh, player, a, ac, dg, useSI, useSy, ts, ds, playername);
				}
			} else
			{
				a = getStringPlaceHolderVault(ssh, player, a, playername);
			}
			list.add(a);
		}
		return list;
	}
	
	private static ArrayList<String> getStringPlaceHolder(ItemStack is, UUID uuid)
	{
		if(is == null)
		{
			return new ArrayList<>();
		}
		ItemMeta im = is.getItemMeta();
		ArrayList<String> list = new ArrayList<>();
		YamlConfiguration y = plugin.getYamlHandler().getLang();
		list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.Owner") 
				+ (Utility.convertUUIDToName(uuid.toString()) == null 
				? "/" : Utility.convertUUIDToName(uuid.toString()))));
		PotionType ptd = PotionType.WATER;
		PotionMeta pmd = null;
		if(im instanceof PotionMeta)
		{
			pmd = (PotionMeta) im;
			ptd = pmd.getBasePotionType();
		}
		list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.Displayname") 
				+ (is.getItemMeta().hasDisplayName() 
				? is.getItemMeta().getDisplayName() 
				: (ptd != null && pmd != null
					? plugin.getEnumTl().getLocalization(ptd, pmd)
					: (plugin.getEnumTl() != null
					  ? plugin.getEnumTl().getLocalization(is.getType())
					  : is.getType())))));
		list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.Material") +
				(plugin.getEnumTl() != null 
				? plugin.getEnumTl().getLocalization(is.getType())
				: is.getType().toString())));
		if(im instanceof Damageable)
		{
			Damageable dam = (Damageable) im;
			int dama = getMaxDamage(is.getType())-dam.getDamage();
			if(dama > 0)
			{
				list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.Damageable") + dama));
			}			
		}
		if(im instanceof Repairable)
		{
			Repairable rep = (Repairable) im;
			if(rep.hasRepairCost())
			{
				list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.Repairable") + rep.getRepairCost()));
			}
		}
		if(im.getItemFlags().size() > 0)
		{
			list.add(y.getString("GuiHandler.InfoLore.ItemFlag"));
			for(ItemFlag itf : im.getItemFlags())
			{
				list.add(ChatApi.tl("&7"+
						(plugin.getEnumTl() != null 
						? plugin.getEnumTl().getLocalization(itf)
						: itf.toString())));
			}
		}		
		if(Material.ENCHANTED_BOOK != is.getType())
		{
			if(im.hasEnchants())
			{
				list.add(y.getString("GuiHandler.InfoLore.Enchantment"));
				for(Entry<Enchantment, Integer> en : is.getEnchantments().entrySet())
				{
					int level = en.getValue();
					list.add(ChatApi.tl("&7"+
							(plugin.getEnumTl() != null 
							? plugin.getEnumTl().getLocalization(en.getKey())
							: en.getKey().getKey().getKey())
					+" "+GuiHandler.IntegerToRomanNumeral(level)));
				}
			}
		} else
		{
			if(im instanceof EnchantmentStorageMeta)
			{
				EnchantmentStorageMeta esm = (EnchantmentStorageMeta) im;
				if(esm.hasStoredEnchants())
				{
					list.add(y.getString("GuiHandler.InfoLore.StorageEnchantment"));
					for(Entry<Enchantment, Integer> en : esm.getStoredEnchants().entrySet())
					{
						int level = en.getValue();
						list.add(ChatApi.tl("&7"+
								(plugin.getEnumTl() != null 
								? plugin.getEnumTl().getLocalization(en.getKey())
								: en.getKey().getKey().getKey())
						+" "+GuiHandler.IntegerToRomanNumeral(level)));
					}
				}
			}
		}
		if(im instanceof PotionMeta)
		{
			PotionMeta pm = (PotionMeta) im;
			if(pm.hasCustomEffects())
			{
				for(PotionEffect pe : pm.getCustomEffects())
				{
					int level = pe.getAmplifier()+1;
					long dur = pe.getDuration()*50;
					String color = GuiHandler.getPotionColor(pe);
					if(pe.getType() == PotionEffectType.INSTANT_HEALTH 
							|| pe.getType() == PotionEffectType.INSTANT_DAMAGE)
					{
						list.add(ChatApi.tl(color+
								(plugin.getEnumTl() != null 
								? CAS.getPlugin().getEnumTl().getLocalization(pe.getType())
								: pe.getType().toString())
								+" "+GuiHandler.IntegerToRomanNumeral(level)));
					} else
					{
						list.add(ChatApi.tl(color+
								(plugin.getEnumTl() != null 
								? CAS.getPlugin().getEnumTl().getLocalization(pe.getType())
								: pe.getType())
								+" "+GuiHandler.IntegerToRomanNumeral(level)+" >> "+TimeHandler.getDateTime(dur, "mm:ss")));
					}
				}
			} else
			{
				for(PotionEffect pe : pm.getBasePotionType().getPotionEffects())
				{
					int level = pe.getAmplifier()+1;
					long dur = pe.getDuration()*50;
					String color = GuiHandler.getPotionColor(pe);
					if(pe.getType() == PotionEffectType.INSTANT_HEALTH || pe.getType() == PotionEffectType.INSTANT_DAMAGE)
					{
						list.add(ChatApi.tl(color+
								(plugin.getEnumTl() != null 
								? CAS.getPlugin().getEnumTl().getLocalization(pe.getType())
								: pe.getType())
								+" "+GuiHandler.IntegerToRomanNumeral(level)));
					} else
					{
						list.add(ChatApi.tl(color+
								(plugin.getEnumTl() != null 
								? plugin.getEnumTl().getLocalization(pe.getType())
								: pe.getType().toString())
								+" "+GuiHandler.IntegerToRomanNumeral(level)+" >> "+TimeHandler.getDateTime(dur, "mm:ss")));
					}
				}
			}
		}
		if(im instanceof SkullMeta)
		{
			SkullMeta sm = (SkullMeta) im;
			if(sm.getOwningPlayer() != null)
			{
				list.add(ChatApi.tl("&7"+sm.getOwningPlayer().getName()));
			}			
		}
		if(im instanceof AxolotlBucketMeta)
		{
			AxolotlBucketMeta abm = (AxolotlBucketMeta) im;
			try
			{
				if(abm.getVariant() != null)
				{
					list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.AxolotlBucketMeta") + abm.getVariant().toString()));
				}
			} catch(Exception e) {}
		}
		if(im instanceof BannerMeta)
		{
			BannerMeta bm = (BannerMeta) im;
			list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.BannerMeta")));
			for(Pattern pa : bm.getPatterns())
			{
				list.add(ChatApi.tl("&7"+
						(plugin.getEnumTl() != null 
						? plugin.getEnumTl().getLocalization(pa.getColor(), pa.getPattern())
						: pa.getColor().toString()+"_"+pa.getPattern().toString())));
			}
		}
		if(im instanceof BlockStateMeta)
		{
			BlockStateMeta bsm = (BlockStateMeta) im;
			if(bsm.getBlockState() instanceof ShulkerBox)
			{
				ShulkerBox sh = (ShulkerBox) bsm.getBlockState();
				LinkedHashMap<String, Integer> lhm = new LinkedHashMap<>(); //B64, itemamount
				for(ItemStack its : sh.getSnapshotInventory())
				{
					if(its == null || its.getType() == Material.AIR)
					{
						continue;
					}
					ItemStack c = its.clone();
					c.setAmount(1);
					String b64 = new Base64Handler(c).toBase64();
					int amount = its.getAmount() + (lhm.containsKey(b64) ? lhm.get(b64) : 0);
					lhm.put(b64, amount);
				}
				for(Entry<String, Integer> e : lhm.entrySet())
				{
					ItemStack ist = new Base64Handler(e.getKey()).fromBase64();
					list.add(ChatApi.tl("&7"+
							(plugin.getEnumTl() != null 
							? CAS.getPlugin().getEnumTl().getLocalization(ist.getType())
							: ist.getType().toString())+ " x"+e.getValue()));
				}
			}
		}
		if(im instanceof BookMeta)
		{
			BookMeta bm = (BookMeta) im;
			if(bm.getTitle() != null)
			{
				list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.BookMeta.Title") + bm.getTitle()));
			}
			if(bm.getAuthor() != null)
			{
				list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.BookMeta.Author") + bm.getAuthor()));
			}
			list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.BookMeta.Page") + bm.getPageCount()));
			if(bm.getGeneration() != null)
			{
				list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.BookMeta.Generation") 
						+ (plugin.getEnumTl() != null 
						? CAS.getPlugin().getEnumTl().getLocalization(bm.getGeneration())
						: bm.getGeneration().toString())));
			}
		}
		if(im instanceof LeatherArmorMeta)
		{
			LeatherArmorMeta lam = (LeatherArmorMeta) im;
			list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.LeatherArmorMeta")+ 
					String.format("#%02x%02x%02x", lam.getColor().getRed(), lam.getColor().getGreen(), lam.getColor().getBlue())
					.toUpperCase()));
		}
		if(im instanceof SpawnEggMeta)
		{
			SpawnEggMeta sem = (SpawnEggMeta) im;
			try
			{
				if(sem.getSpawnedEntity().getEntityType() != null)
				{
					list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.SpawnEggMeta") 
							+ (plugin.getEnumTl() != null 
							? CAS.getPlugin().getEnumTl().getLocalization(sem.getSpawnedEntity().getEntityType())
							: sem.getSpawnedEntity().getEntityType().toString())));
				}				
			} catch(Exception e)
			{
				list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.SpawnEggMeta") 
						+ getSpawnEggType(is.getType())));
			}
			
		}
		if(im instanceof SuspiciousStewMeta)
		{
			SuspiciousStewMeta ssm = (SuspiciousStewMeta) im;
			for(PotionEffect pe : ssm.getCustomEffects())
			{
				int level = pe.getAmplifier()+1;
				long dur = pe.getDuration();
				String color = getPotionColor(pe);
				list.add(ChatApi.tl(color+
						(plugin.getEnumTl() != null 
						? plugin.getEnumTl().getLocalization(pe.getType())
						: pe.getType())
				+" "+GuiHandler.IntegerToRomanNumeral(level)+" >> "+TimeHandler.getDateTime(dur, "mm:ss")));
			}
		}
		if(im instanceof TropicalFishBucketMeta)
		{
			TropicalFishBucketMeta tfbm = (TropicalFishBucketMeta) im;
			list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.TropicalFishBucketMeta") 
					+ (plugin.getEnumTl() != null 
					? CAS.getPlugin().getEnumTl().getLocalization(tfbm.getBodyColor(), tfbm.getPattern(), tfbm.getPatternColor())
					: tfbm.getBodyColor().toString()+"_"+tfbm.getPattern().toString()+"_"+tfbm.getPatternColor().toString())));
		}
		return list;
	}
	
	public static String getPotionColor(PotionEffect pe)
	{
		String color = "";
		if(pe.getType() == PotionEffectType.ABSORPTION || pe.getType() == PotionEffectType.CONDUIT_POWER
				|| pe.getType() == PotionEffectType.RESISTANCE || pe.getType() == PotionEffectType.DOLPHINS_GRACE
				|| pe.getType() == PotionEffectType.HASTE || pe.getType() == PotionEffectType.FIRE_RESISTANCE
				|| pe.getType() == PotionEffectType.INSTANT_HEALTH || pe.getType() == PotionEffectType.HEALTH_BOOST
				|| pe.getType() == PotionEffectType.HERO_OF_THE_VILLAGE || pe.getType() == PotionEffectType.STRENGTH
				|| pe.getType() == PotionEffectType.INVISIBILITY || pe.getType() == PotionEffectType.JUMP_BOOST
				|| pe.getType() == PotionEffectType.LUCK || pe.getType() == PotionEffectType.NIGHT_VISION
				|| pe.getType() == PotionEffectType.REGENERATION || pe.getType() == PotionEffectType.SATURATION
				|| pe.getType() == PotionEffectType.SLOW_FALLING || pe.getType() == PotionEffectType.SPEED
				|| pe.getType() == PotionEffectType.WATER_BREATHING)
		{
			color = "&9";
		} else if(pe.getType() == PotionEffectType.BAD_OMEN || pe.getType() == PotionEffectType.BLINDNESS
				|| pe.getType() == PotionEffectType.BLINDNESS || pe.getType() == PotionEffectType.DARKNESS
				|| pe.getType() == PotionEffectType.INSTANT_DAMAGE || pe.getType() == PotionEffectType.HUNGER
				|| pe.getType() == PotionEffectType.LEVITATION || pe.getType() == PotionEffectType.POISON
				|| pe.getType() == PotionEffectType.SLOWNESS || pe.getType() == PotionEffectType.MINING_FATIGUE
				|| pe.getType() == PotionEffectType.SLOW_FALLING || pe.getType() == PotionEffectType.UNLUCK
				|| pe.getType() == PotionEffectType.WEAKNESS || pe.getType() == PotionEffectType.WITHER)
		{
			color = "&c";
		} else if(pe.getType() == PotionEffectType.GLOWING)
		{
			color = "&7";
		}
		return color;
	}
	
	public static int getMaxDamage(Material material)
	{
		int damage = 0;
		switch(material)
		{
		case WOODEN_AXE: //Fallthrough
		case WOODEN_HOE:
		case WOODEN_PICKAXE:
		case WOODEN_SHOVEL:
		case WOODEN_SWORD:
			damage = 60;
			break;
		case LEATHER_BOOTS:
			damage = 65;
			break;
		case LEATHER_CHESTPLATE:
			damage = 80;
			break;
		case LEATHER_HELMET:
			damage = 55;
			break;
		case LEATHER_LEGGINGS:
			damage = 75;
			break;
		case STONE_AXE:
		case STONE_HOE:
		case STONE_PICKAXE:
		case STONE_SHOVEL:
		case STONE_SWORD:
			damage = 132;
			break;
		case CHAINMAIL_BOOTS:
			damage = 196;
			break;
		case CHAINMAIL_CHESTPLATE:
			damage = 241;
			break;
		case CHAINMAIL_HELMET:
			damage = 166;
			break;
		case CHAINMAIL_LEGGINGS:
			damage = 226;
			break;
		case GOLDEN_AXE:
		case GOLDEN_HOE:
		case GOLDEN_PICKAXE:
		case GOLDEN_SHOVEL:
		case GOLDEN_SWORD:
			damage = 33;
			break;
		case GOLDEN_BOOTS:
			damage = 91;
			break;
		case GOLDEN_CHESTPLATE:
			damage = 112;
			break;
		case GOLDEN_HELMET:
			damage = 77;
			break;
		case GOLDEN_LEGGINGS:
			damage = 105;
			break;
		case IRON_AXE:
		case IRON_HOE:
		case IRON_PICKAXE:
		case IRON_SHOVEL:
		case IRON_SWORD:
			damage = 251;
			break;
		case IRON_BOOTS:
			damage = 195;
			break;
		case IRON_CHESTPLATE:
			damage = 40;
			break;
		case IRON_HELMET:
			damage = 165;
			break;
		case IRON_LEGGINGS:
			damage = 225;
			break;
		case DIAMOND_AXE:
		case DIAMOND_HOE:
		case DIAMOND_PICKAXE:
		case DIAMOND_SHOVEL:
		case DIAMOND_SWORD:
			damage = 1562;
			break;
		case DIAMOND_BOOTS:
			damage = 429;
			break;
		case DIAMOND_CHESTPLATE:
			damage = 528;
			break;
		case DIAMOND_HELMET:
			damage = 363;
			break;
		case DIAMOND_LEGGINGS:
			damage = 495;
			break;
		case NETHERITE_AXE:
		case NETHERITE_HOE:
		case NETHERITE_PICKAXE:
		case NETHERITE_SHOVEL:
		case NETHERITE_SWORD:
			damage = 2031;
			break;
		case NETHERITE_BOOTS:
			damage = 482;
			break;
		case NETHERITE_CHESTPLATE:
			damage = 592;
			break;
		case NETHERITE_HELMET:
			damage = 408;
			break;
		case NETHERITE_LEGGINGS:
			damage = 556;
			break;
		case SHIELD:
			damage = 337;
			break;
		case TURTLE_HELMET:
			damage = 276;
			break;
		case TRIDENT:
			damage = 251;
			break;
		case FISHING_ROD:
			damage = 65;
			break;
		case CARROT_ON_A_STICK:
			damage = 26;
			break;
		case WARPED_FUNGUS_ON_A_STICK:
			damage = 100;
			break;
		case ELYTRA:
			damage = 432;
			break;
		case SHEARS:
			damage = 238;
			break;
		case BOW:
			damage = 385;
			break;
		case CROSSBOW:
			damage = 326;
			break;
		case FLINT_AND_STEEL:
			damage = 65;
			break;
		default:
			damage = 0;
			break;
		}
		return damage;
	}
	
	//thanks https://stackoverflow.com/questions/12967896/converting-integers-to-roman-numerals-java
	public static String IntegerToRomanNumeral(int input) 
	{
	    if (input < 1 || input > 3999)
	        return String.valueOf(input);
	    String s = "";
	    while (input >= 1000) {
	        s += "M";
	        input -= 1000;        }
	    while (input >= 900) {
	        s += "CM";
	        input -= 900;
	    }
	    while (input >= 500) {
	        s += "D";
	        input -= 500;
	    }
	    while (input >= 400) {
	        s += "CD";
	        input -= 400;
	    }
	    while (input >= 100) {
	        s += "C";
	        input -= 100;
	    }
	    while (input >= 90) {
	        s += "XC";
	        input -= 90;
	    }
	    while (input >= 50) {
	        s += "L";
	        input -= 50;
	    }
	    while (input >= 40) {
	        s += "XL";
	        input -= 40;
	    }
	    while (input >= 10) {
	        s += "X";
	        input -= 10;
	    }
	    while (input >= 9) {
	        s += "IX";
	        input -= 9;
	    }
	    while (input >= 5) {
	        s += "V";
	        input -= 5;
	    }
	    while (input >= 4) {
	        s += "IV";
	        input -= 4;
	    }
	    while (input >= 1) {
	        s += "I";
	        input -= 1;
	    }    
	    return s;
	}
	
	public static String getStringPlaceHolder(_SignQStorage ssh, Player player, String text, String playername)
	{
		String s = text;
		if(text.contains("%owner%"))
		{
			if(ssh.getOwner() == null)
			{
				s = s.replace("%owner%", "/");
			} else
			{
				s = s.replace("%owner%", Utility.convertUUIDToName(ssh.getOwner().toString()) == null 
						? "/" : Utility.convertUUIDToName(ssh.getOwner().toString()));
			}
		}
		if(text.contains("%material%"))
		{
			s = s.replace("%material%", (ssh.getMaterial() == null ? "/" : ssh.getMaterial().toString()));
		}
		if(text.contains("%isonwhitelist%"))
		{
			if(playername != null)
			{
				UUID uuid = Utility.convertNameToUUID(playername);
				s = s.replace("%isonwhitelist%", 
						uuid == null ? "/" :
							getBoolean(plugin.getMysqlHandler().exist(MysqlType._STORAGEACCESSTYPE,
								"`player_uuid` = ? AND `sign_shop_id` = ? AND `listed_type` = ?",
								uuid.toString(), ssh.getId(), _ListedType.WHITELIST.toString()))
						);
			} else
			{
				s = s.replace("%isonwhitelist%", "/");
			}
		}
		if(text.contains("%ismember%"))
		{
			if(playername != null)
			{
				UUID uuid = Utility.convertNameToUUID(playername);
				s = s.replace("%ismember%", 
						uuid == null ? "/" :
							getBoolean(plugin.getMysqlHandler().exist(MysqlType._STORAGEACCESSTYPE,
								"`player_uuid` = ? AND `sign_shop_id` = ? AND `listed_type` = ?",
								uuid.toString(), ssh.getId(), _ListedType.MEMBER.toString()))
						);
			} else
			{
				s = s.replace("%ismember%", "/");
			}
		}
		if(text.contains("%id%"))
		{
			s = s.replace("%id%", String.valueOf(ssh.getId()));
		}
		if(text.contains("%numtext%"))
		{
			s = s.replace("%numtext%", "'"+ssh.getNumText()+"'");
		}
		if(text.contains("%player%"))
		{
			s = s.replace("%player%", player.getName());
		}
		if(text.contains("%displayname%"))
		{
			s = s.replace("%displayname%", ssh.getDisplayName() == null ? "/" : ssh.getDisplayName());
		}
		if(text.contains("%signshopname%"))
		{
			s = s.replace("%signshopname%", ssh.getSignStorageName());
		}
		if(text.contains("%server%"))
		{
			s = s.replace("%server%", ssh.getServer());
		}
		if(text.contains("%world%"))
		{
			s = s.replace("%world%", ssh.getWorld());
		}
		if(text.contains("%x%"))
		{
			s = s.replace("%x%", String.valueOf(ssh.getX()));
		}
		if(text.contains("%y%"))
		{
			s = s.replace("%y%", String.valueOf(ssh.getY()));
		}
		if(text.contains("%z%"))
		{
			s = s.replace("%z%", String.valueOf(ssh.getZ()));
		}
		if(text.contains("%accountid%"))
		{
			s = s.replace("%accountid%", String.valueOf(ssh.getAccountId()));
		}
		if(text.contains("%creationdate%"))
		{
			s = s.replace("%creationdate%", TimeHandler.getDateTime(ssh.getCreationDateTime()));
		}
		if(text.contains("%itemstoragecurrent%"))
		{
			s = s.replace("%itemstoragecurrent%", String.valueOf(ssh.getItemStorageCurrent()));
		}
		if(text.contains("%itemstoragetotal%"))
		{
			s = s.replace("%itemstoragetotal%", String.valueOf(ssh.getItemStorageTotal()));
		}
		if(text.contains("%itemstoragetotalstack%"))
		{
			s = s.replace("%itemstoragetotalstack%", SignQuantityHandler.formatDouble((double) ssh.getItemStorageTotal()/64.0));
		}
		if(text.contains("%itemstoragetotaldoublechest%"))
		{
			s = s.replace("%itemstoragetotaldoublechest%", SignQuantityHandler.formatDouble((double) ssh.getItemStorageTotal()/3456.0));
		}
		if(text.contains("%iteminput%"))
		{
			s = s.replace("%iteminput%", String.valueOf(ssh.getItemInput()));
		}
		if(text.contains("%itemshiftinput%"))
		{
			s = s.replace("%itemshiftinput%", String.valueOf(ssh.getItemShiftInput()));
		}
		if(text.contains("%itemoutput%"))
		{
			s = s.replace("%itemoutput%", String.valueOf(ssh.getItemOutput()));
		}
		if(text.contains("%itemshiftoutput%"))
		{
			s = s.replace("%itemshiftoutput%", String.valueOf(ssh.getItemShiftOutput()));
		}
		if(text.contains("%unlimited%"))
		{
			s = s.replace("%unlimited%", getBoolean(ssh.isUnlimited()));
		}
		if(text.contains("%glow%"))
		{
			s = s.replace("%glow%", getBoolean(ssh.isSignGlowing()));
		}
		if(text.contains("%listtype%"))
		{
			s = s.replace("%listtype%", 
					plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.ListedType."+ssh.getListedType().toString()));
		}
		if(text.contains("%hologram%"))
		{
			s = s.replace("%hologram%", getBoolean(ssh.isItemHologram()));
		}
		return ChatApi.tl(s);
	}
	
	private static String getStringPlaceHolderIFH(_SignQStorage ssh, Player player, String text,
			Account ac, int dg, boolean useSI, boolean useSy, String ts, String ds, String playername)
	{
		String s = text;
		if(text.contains("%accountname%"))
		{
			s = s.replace("%accountname%", (ac == null || ac.getID() == 0) ? "/" : ac.getAccountName());
		}
		return ChatApi.tl(s);
	}
	
	private static String getStringPlaceHolderVault(_SignQStorage ssh, Player player, String text, String playername)
	{
		String s = text;
		if(text.contains("%accountname%"))
		{
			String n = Utility.convertUUIDToName(ssh.getOwner().toString());
			s = s.replace("%accountname%", n != null ? n : "/");
		}
		return ChatApi.tl(s);
	}
	
	private static String getBoolean(boolean boo)
	{
		return boo ? plugin.getYamlHandler().getLang().getString("IsTrue") : plugin.getYamlHandler().getLang().getString("IsFalse");
	}
	
	private static ClickFunction[] getClickFunction(YamlConfiguration y, String pathBase)
	{
		ArrayList<ClickFunction> ctar = new ArrayList<>();
		List<ClickType> list = new ArrayList<ClickType>(EnumSet.allOf(ClickType.class));
		for(ClickType ct : list)
		{
			if(pathBase == null)
			{
				if(y.get("ClickFunction."+ct.toString()) == null)
				{
					continue;
				}
				ClickFunctionType cft = null;
				try
				{
					cft = ClickFunctionType.valueOf(y.getString("ClickFunction."+ct.toString()));
				} catch(Exception e)
				{
					continue;
				}
				ctar.add(new ClickFunction(ct, cft));
			} else
			{
				if(y.get(pathBase+".ClickFunction."+ct.toString()) == null)
				{
					continue;
				}
				ClickFunctionType cft = null;
				try
				{
					cft = ClickFunctionType.valueOf(y.getString(pathBase+".ClickFunction."+ct.toString()));
				} catch(Exception e)
				{
					continue;
				}
				ctar.add(new ClickFunction(ct, cft));
			}
		}
		return ctar.toArray(new ClickFunction[ctar.size()]);
	}
	
	public static String getSpawnEggType(Material mat)
	{
		String s = "";
		switch(mat)
		{
		default: break;
		case ALLAY_SPAWN_EGG:
		case AXOLOTL_SPAWN_EGG:
		case BAT_SPAWN_EGG:
		case BEE_SPAWN_EGG:
		case BLAZE_SPAWN_EGG:
		case CAT_SPAWN_EGG:
		case CAVE_SPIDER_SPAWN_EGG:
		case CHICKEN_SPAWN_EGG:
		case COD_SPAWN_EGG:
		case COW_SPAWN_EGG:
		case CREEPER_SPAWN_EGG:
		case DOLPHIN_SPAWN_EGG:
		case DONKEY_SPAWN_EGG:
		case DROWNED_SPAWN_EGG:
		case ELDER_GUARDIAN_SPAWN_EGG:
		case ENDERMAN_SPAWN_EGG:
		case ENDERMITE_SPAWN_EGG:
		case EVOKER_SPAWN_EGG:
		case FOX_SPAWN_EGG:
		case FROG_SPAWN_EGG:
		case FROGSPAWN:
		case GHAST_SPAWN_EGG:
		case GLOW_SQUID_SPAWN_EGG:
		case GOAT_SPAWN_EGG:
		case GUARDIAN_SPAWN_EGG:
		case HOGLIN_SPAWN_EGG:
		case HORSE_SPAWN_EGG:
		case HUSK_SPAWN_EGG:
		case LLAMA_SPAWN_EGG:
		case MAGMA_CUBE_SPAWN_EGG:
		case MOOSHROOM_SPAWN_EGG:
		case MULE_SPAWN_EGG:
		case OCELOT_SPAWN_EGG:
		case PANDA_SPAWN_EGG:
		case PARROT_SPAWN_EGG:
		case PHANTOM_SPAWN_EGG:
		case PIG_SPAWN_EGG:
		case PIGLIN_BRUTE_SPAWN_EGG:
		case PIGLIN_SPAWN_EGG:
		case PILLAGER_SPAWN_EGG:
		case POLAR_BEAR_SPAWN_EGG:
		case PUFFERFISH_SPAWN_EGG:
		case RABBIT_SPAWN_EGG:
		case RAVAGER_SPAWN_EGG:
		case SALMON_SPAWN_EGG:
		case SHEEP_SPAWN_EGG:
		case SHULKER_SPAWN_EGG:
		case SILVERFISH_SPAWN_EGG:
		case SKELETON_HORSE_SPAWN_EGG:
		case SKELETON_SPAWN_EGG:
		case SLIME_SPAWN_EGG:
		case SPIDER_SPAWN_EGG:
		case SQUID_SPAWN_EGG:
		case STRAY_SPAWN_EGG:
		case STRIDER_SPAWN_EGG:
		case TADPOLE_SPAWN_EGG:
		case TRADER_LLAMA_SPAWN_EGG:
		case TROPICAL_FISH_SPAWN_EGG:
		case TURTLE_SPAWN_EGG:
		case VEX_SPAWN_EGG:
		case VILLAGER_SPAWN_EGG:
		case VINDICATOR_SPAWN_EGG:
		case WANDERING_TRADER_SPAWN_EGG:
		case WARDEN_SPAWN_EGG:
		case WITCH_SPAWN_EGG:
		case WITHER_SKELETON_SPAWN_EGG:
		case WOLF_SPAWN_EGG:
		case ZOGLIN_SPAWN_EGG:
		case ZOMBIE_HORSE_SPAWN_EGG:
		case ZOMBIE_SPAWN_EGG:
		case ZOMBIE_VILLAGER_SPAWN_EGG:
		case ZOMBIFIED_PIGLIN_SPAWN_EGG:
		case ARMADILLO_SPAWN_EGG:
		case BOGGED_SPAWN_EGG:
		case BREEZE_SPAWN_EGG:
		case CAMEL_SPAWN_EGG:
		case ENDER_DRAGON_SPAWN_EGG:
		case IRON_GOLEM_SPAWN_EGG:
		case SNIFFER_SPAWN_EGG:
		case SNOW_GOLEM_SPAWN_EGG:
		case WITHER_SPAWN_EGG:
			s = (plugin.getEnumTl() != null 
				? CAS.getPlugin().getEnumTl().getLocalization(mat)
				: mat.toString()); break;
		}
		return s;
	}*/
}