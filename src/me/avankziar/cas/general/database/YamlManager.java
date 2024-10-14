package me.avankziar.cas.general.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import me.avankziar.cas.general.database.Language.ISO639_2B;
import me.avankziar.cas.general.objects.Result;
import me.avankziar.cas.spigot.gui.objects.ClickFunctionType;
import me.avankziar.cas.spigot.gui.objects.ClickType;
import me.avankziar.cas.spigot.gui.objects.GuiType;
import me.avankziar.cas.spigot.gui.objects.SettingsLevel;
import me.avankziar.cas.spigot.modifiervalueentry.Bypass;

public class YamlManager
{
	private ISO639_2B languageType = ISO639_2B.GER;
	//The default language of your plugin. Mine is german.
	private ISO639_2B defaultLanguageType = ISO639_2B.GER;
	
	//Per Flatfile a linkedhashmap.
	private static LinkedHashMap<String, Language> configSpigotKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> configCityKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> configCityFlagsKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> commandsKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> languageKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> mvelanguageKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> matlanguageKeys = new LinkedHashMap<>();
	private static LinkedHashMap<GuiType, LinkedHashMap<String, Language>> guiKeys = new LinkedHashMap<>();
	
	public YamlManager()
	{
		initConfig();
		initConfigCity();
		initConfigCityFlags();
		initCommands();
		initLanguage();
		initModifierValueEntryLanguage();
		initMaterialLanguage();
		initGuiAdministration();
		initGuiNumpad();
		initGuiItemInput();
		initGuiKeyboard();
		/*initGuiSearchBuy();
		initGuiSearchSell();
		initGuiSubscribe();*/
	}
	
	public ISO639_2B getLanguageType()
	{
		return languageType;
	}

	public void setLanguageType(ISO639_2B languageType)
	{
		this.languageType = languageType;
	}
	
	public ISO639_2B getDefaultLanguageType()
	{
		return defaultLanguageType;
	}
	
	public LinkedHashMap<String, Language> getConfigSpigotKey()
	{
		return configSpigotKeys;
	}
	
	public LinkedHashMap<String, Language> getConfigCityKey()
	{
		return configCityFlagsKeys;
	}
	
	public LinkedHashMap<String, Language> getConfigCityFlagsKey()
	{
		return configCityFlagsKeys;
	}
	
	public LinkedHashMap<String, Language> getCommandsKey()
	{
		return commandsKeys;
	}
	
	public LinkedHashMap<String, Language> getLanguageKey()
	{
		return languageKeys;
	}
	
	public LinkedHashMap<String, Language> getModifierValueEntryLanguageKey()
	{
		return mvelanguageKeys;
	}
	
	public LinkedHashMap<String, Language> getMaterialLanguageKey()
	{
		return matlanguageKeys;
	}
	
	public LinkedHashMap<String, Language> getGuiKey(GuiType guiType)
	{
		return guiKeys.get(guiType);
	}
	
	public LinkedHashMap<GuiType, LinkedHashMap<String, Language>> getGuiKey()
	{
		return guiKeys;
	}
	
	/*
	 * The main methode to set all paths in the yamls.
	 */
	public void setFileInput(YamlConfiguration yml, LinkedHashMap<String, Language> keyMap, String key, ISO639_2B languageType)
	{
		if(!keyMap.containsKey(key))
		{
			return;
		}
		if(key.startsWith("#"))
		{
			//Comments
			String k = key.replace("#", "");
			if(yml.get(k) == null)
			{
				//return because no aktual key are present
				return;
			}
			if(yml.getComments(k) != null && !yml.getComments(k).isEmpty())
			{
				//Return, because the comments are already present, and there could be modified. F.e. could be comments from a admin.
				return;
			}
			if(keyMap.get(key).languageValues.get(languageType).length == 1)
			{
				if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
				{
					String s = ((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", "");
					yml.setComments(k, Arrays.asList(s));
				}
			} else
			{
				List<Object> list = Arrays.asList(keyMap.get(key).languageValues.get(languageType));
				ArrayList<String> stringList = new ArrayList<>();
				if(list instanceof List<?>)
				{
					for(Object o : list)
					{
						if(o instanceof String)
						{
							stringList.add(((String) o).replace("\r\n", ""));
						}
					}
				}
				yml.setComments(k, (List<String>) stringList);
			}
			return;
		}
		if(yml.get(key) != null)
		{
			return;
		}
		if(keyMap.get(key).languageValues.get(languageType).length == 1)
		{
			if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
			{
				yml.set(key, ((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", ""));
			} else
			{
				yml.set(key, keyMap.get(key).languageValues.get(languageType)[0]);
			}
		} else
		{
			List<Object> list = Arrays.asList(keyMap.get(key).languageValues.get(languageType));
			ArrayList<String> stringList = new ArrayList<>();
			if(list instanceof List<?>)
			{
				for(Object o : list)
				{
					if(o instanceof String)
					{
						stringList.add(((String) o).replace("\r\n", ""));
					} else
					{
						stringList.add(o.toString().replace("\r\n", ""));
					}
				}
			}
			yml.set(key, (List<String>) stringList);
		}
	}
	
	private void addComments(LinkedHashMap<String, Language> mapKeys, String path, Object[] o)
	{
		mapKeys.put(path, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, o));
	}
	
	private void addConfig(String path, Object[] c, Object[] o)
	{
		configSpigotKeys.put(path, new Language(new ISO639_2B[] {ISO639_2B.GER}, c));
		addComments(configSpigotKeys, "#"+path, o);
	}
	
	private void addConfigCity(String path, Object[] c, Object[] o)
	{
		configCityKeys.put(path, new Language(new ISO639_2B[] {ISO639_2B.GER}, c));
		addComments(configCityKeys, "#"+path, o);
	}
	
	private void addConfigCityFlags(String path, Object[] c, Object[] o)
	{
		configCityFlagsKeys.put(path, new Language(new ISO639_2B[] {ISO639_2B.GER}, c));
		addComments(configCityFlagsKeys, "#"+path, o);
	}
	
	public void initConfig() //INFO:Config
	{
		addConfig("useIFHAdministration",
				new Object[] {
				true},
				new Object[] {
				"Boolean um auf das IFH Interface Administration zugreifen soll.",
				"Wenn 'true' eingegeben ist, aber IFH Administration ist nicht vorhanden, so werden automatisch die eigenen Configwerte genommen.",
				"Boolean to access the IFH Interface Administration.",
				"If 'true' is entered, but IFH Administration is not available, the own config values are automatically used."});
		addConfig("IFHAdministrationPath", 
				new Object[] {
				"vss"},
				new Object[] {
				"",
				"Diese Funktion sorgt dafür, dass das Plugin auf das IFH Interface Administration zugreifen kann.",
				"Das IFH Interface Administration ist eine Zentrale für die Daten von Sprache, Servername und Mysqldaten.",
				"Diese Zentralisierung erlaubt für einfache Änderung/Anpassungen genau dieser Daten.",
				"Sollte das Plugin darauf zugreifen, werden die Werte in der eigenen Config dafür ignoriert.",
				"",
				"This function ensures that the plugin can access the IFH Interface Administration.",
				"The IFH Interface Administration is a central point for the language, server name and mysql data.",
				"This centralization allows for simple changes/adjustments to precisely this data.",
				"If the plugin accesses it, the values in its own config are ignored."});
		
		addConfig("Language",
				new Object[] {
				"ENG"},
				new Object[] {
				"",
				"Die eingestellte Sprache. Von Haus aus sind 'ENG=Englisch' und 'GER=Deutsch' mit dabei.",
				"Falls andere Sprachen gewünsch sind, kann man unter den folgenden Links nachschauen, welchs Kürzel für welche Sprache gedacht ist.",
				"Siehe hier nach, sowie den Link, welche dort auch für Wikipedia steht.",
				"https://github.com/Avankziar/RootAdministration/blob/main/src/main/java/me/avankziar/roota/general/Language.java",
				"",
				"The set language. By default, ENG=English and GER=German are included.",
				"If other languages are required, you can check the following links to see which abbreviation is intended for which language.",
				"See here, as well as the link, which is also there for Wikipedia.",
				"https://github.com/Avankziar/RootAdministration/blob/main/src/main/java/me/avankziar/roota/general/Language.java"});
		addConfig("ServerName",
				new Object[] {
				"hub"},
				new Object[] {
				"",
				"Der Server steht für den Namen des Spigotservers, wie er in BungeeCord/Waterfall/Velocity config.yml unter dem Pfad 'servers' angegeben ist.",
				"Sollte kein BungeeCord/Waterfall oder andere Proxys vorhanden sein oder du nutzt IFH Administration, so kannst du diesen Bereich ignorieren.",
				"",
				"The server stands for the name of the spigot server as specified in BungeeCord/Waterfall/Velocity config.yml under the path 'servers'.",
				"If no BungeeCord/Waterfall or other proxies are available or you are using IFH Administration, you can ignore this area."});
		
		addConfig("Mysql.Status",
				new Object[] {
				false},
				new Object[] {
				"",
				"'Status' ist ein simple Sicherheitsfunktion, damit nicht unnötige Fehler in der Konsole geworfen werden.",
				"Stelle diesen Wert auf 'true', wenn alle Daten korrekt eingetragen wurden.",
				"",
				"'Status' is a simple security function so that unnecessary errors are not thrown in the console.",
				"Set this value to 'true' if all data has been entered correctly."});
		addComments(configSpigotKeys, "#Mysql", 
				new Object[] {
				"",
				"Mysql ist ein relationales Open-Source-SQL-Databaseverwaltungssystem, das von Oracle entwickelt und unterstützt wird.",
				"'My' ist ein Namenkürzel und 'SQL' steht für Structured Query Language. Eine Programmsprache mit der man Daten auf einer relationalen Datenbank zugreifen und diese verwalten kann.",
				"Link https://www.mysql.com/de/",
				"Wenn du IFH Administration nutzt, kann du diesen Bereich ignorieren.",
				"",
				"Mysql is an open source relational SQL database management system developed and supported by Oracle.",
				"'My' is a name abbreviation and 'SQL' stands for Structured Query Language. A program language that can be used to access and manage data in a relational database.",
				"Link https://www.mysql.com",
				"If you use IFH Administration, you can ignore this section."});
		addConfig("Mysql.Host",
				new Object[] {
				"127.0.0.1"},
				new Object[] {
				"",
				"Der Host, oder auch die IP. Sie kann aus einer Zahlenkombination oder aus einer Adresse bestehen.",
				"Für den Lokalhost, ist es möglich entweder 127.0.0.1 oder 'localhost' einzugeben. Bedenke, manchmal kann es vorkommen,",
				"das bei gehosteten Server die ServerIp oder Lokalhost möglich ist.",
				"",
				"The host, or IP. It can consist of a number combination or an address.",
				"For the local host, it is possible to enter either 127.0.0.1 or >localhost<.",
				"Please note that sometimes the serverIp or localhost is possible for hosted servers."});
		addConfig("Mysql.Port",
				new Object[] {
				3306},
				new Object[] {
				"",
				"Ein Port oder eine Portnummer ist in Rechnernetzen eine Netzwerkadresse,",
				"mit der das Betriebssystem die Datenpakete eines Transportprotokolls zu einem Prozess zuordnet.",
				"Ein Port für Mysql ist standart gemäß 3306.",
				"",
				"In computer networks, a port or port number ",
				"is a network address with which the operating system assigns the data packets of a transport protocol to a process.",
				"A port for Mysql is standard according to 3306."});
		addConfig("Mysql.DatabaseName",
				new Object[] {
				"mydatabase"},
				new Object[] {
				"",
				"Name der Datenbank in Mysql.",
				"",
				"Name of the database in Mysql."});
		addConfig("Mysql.SSLEnabled",
				new Object[] {
				false},
				new Object[] {
				"",
				"SSL ist einer der drei Möglichkeiten, welcher, solang man nicht weiß, was es ist, es so lassen sollte wie es ist.",
				"",
				"SSL is one of the three options which, as long as you don't know what it is, you should leave it as it is."});
		addConfig("Mysql.AutoReconnect",
				new Object[] {
				true},
				new Object[] {
				"",
				"AutoReconnect ist einer der drei Möglichkeiten, welcher, solang man nicht weiß, was es ist, es so lassen sollte wie es ist.",
				"",
				"AutoReconnect is one of the three options which, as long as you don't know what it is, you should leave it as it is."});
		addConfig("Mysql.VerifyServerCertificate",
				new Object[] {
				false},
				new Object[] {
				"",
				"VerifyServerCertificate ist einer der drei Möglichkeiten, welcher, solang man nicht weiß, was es ist, es so lassen sollte wie es ist.",
				"",
				"VerifyServerCertificate is one of the three options which, as long as you don't know what it is, you should leave it as it is."});
		addConfig("Mysql.User",
				new Object[] {
				"admin"},
				new Object[] {
				"",
				"Der User, welcher auf die Mysql zugreifen soll.",
				"",
				"The user who should access the Mysql."});
		addConfig("Mysql.Password",
				new Object[] {
				"not_0123456789"},
				new Object[] {
				"",
				"Das Passwort des Users, womit er Zugang zu Mysql bekommt.",
				"",
				"The user's password, with which he gets access to Mysql."});
		
		configSpigotKeys.put("Enable.SignStorage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		/*configSpigotKeys.put("Enable.Auction"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));*/
		configSpigotKeys.put("EnableMechanic.Modifier"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("EnableMechanic.ValueEntry"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("ValueEntry.OverrulePermission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		
		addConfig("CleanUpTask.Player.Active",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn 'true' dann ist der Aufräumtask für Datenbankeinträge für Spieler aktiv.",
				"",
				"If 'true' then the cleanup task for database entries for players is active."});
		addConfig("CleanUpTask.Player.DeleteAfterXDaysOffline",
				new Object[] {
				365},
				new Object[] {
				"",
				"Löscht alle Spieler, die nach X Tage nicht online sind.",
				"",
				"Deletes all players who are not online after X days."});
		
		configSpigotKeys.put("Mechanic.CountPerm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"ADDUP"}));
		addConfig("SignStorage.SignQuantityInitializationLine",
				new Object[] {
				"[VssStorage]"},
				new Object[] {
				"",
				"Zeile 0 welche zum initialisieren genutzt wird um ein SignQuantityStorage zu gründen.",
				"",
				"Line which is used for initialization to create a SignQuantityStorage."});
		addConfig("SignStorage.SignVariousInitializationLine",
				new Object[] {
				"[VssStorage]"},
				new Object[] {
				"",
				"Zeile 0 welche zum initialisieren genutzt wird um ein SignVariousStorage zu gründen.",
				"",
				"Line which is used for initialization to create a SignVariousStorage."});
		addConfig("SignStorage.SignCopyLine",
				new Object[] {
				"[Copy]"},
				new Object[] {
				"",
				"Zeile 1 welche zum Kopieren des angegeben Lager genutzt wird. Übernommen wird alles außer eingelagerte Items.",
				"",
				"Line 1 which is used to copy the specified storage. Everything is copied except stored items."});
		addConfig("SignStorage.SignMoveLine",
				new Object[] {
				"[Move]"},
				new Object[] {
				"",
				"Zeile 1 welche zum Verlegen des angegeben Lagers genutzt wird. Altes Lagerschild ist somit inaktiv. Wird aber nicht abgebaut.",
				"",
				"Line 1 which is used to relocate the specified storage. The old storage sign is therefore inactive. But will not be removed."});
		addConfig("SignStorage.DefaultStartItemStorageQuantity",
				new Object[] {
				17280},
				new Object[] {
				"",
				"Die Anzahl an Standartlagerplatz für ein neuer Lagers.",
				"",
				"The number of standard storage spaces for a new storage."});
		addConfig("SignStorage.CostToAdd1Storage",
				new Object[] {
				"dollar;100.0",
				"token;9.0",
				"vault;100.0"},
				new Object[] {
				"",
				"Die Kosten pro Währung um den Lagerplatz eines Lagers um 1 zu erhöhen.",
				"",
				"The cost per currency to increase the storage space of a storage by 1."});
		addConfig("SignStorage.ForbiddenWorld",
				new Object[] {
				"hubs", "spawns"},
				new Object[] {
				"",
				"Die Welten, wo es verboten ist, Lager zu erstellen.",
				"",
				"The worlds where it is forbidden to create storages."});
		addConfig("SignStorage.Sign.Line4CalculateInStack",
				new Object[] {
				false},
				new Object[] {
				"",
				"Wenn 'true' dann wird die 4 Zeile des Schildes des Lagers in Stacks berechnet, ansteller einzelner Items.",
				"",
				"If 'true' then the 4 line of the storages sign is calculated in stacks instead of individual items."});
		addConfig("SignStorage.Sign.DefaultOutput",
				new Object[] {
				64},
				new Object[] {
				"",
				"Die Standartanzahl an Items, welche durch einen Linksklick aus einem Lager herausgenommen werden kann.",
				"",
				"The standard number of items that can be removed from a storage by left-clicking."});
		addConfig("SignStorage.Sign.DefaultShiftOutput",
				new Object[] {
				576},
				new Object[] {
				"",
				"Die Standartanzahl an Items, welche durch einen Shift-Linksklick aus einem Lager herausgenommen werden kann.",
				"",
				"The standard number of items that can be removed from a storagee with a shift-left click."});
		addConfig("SignStorage.Sign.DefaultInput",
				new Object[] {
				64},
				new Object[] {
				"",
				"Die Standartanzahl an Items, welche durch einen Rechtsklick ins Lager deponiert werden können.",
				"",
				"The standard number of items that can be deposited in the storage by right-clicking."});
		addConfig("SignStorage.Sign.DefaultShiftInput",
				new Object[] {
				576},
				new Object[] {
				"",
				"Die Standartanzahl an Items, welche durch einen Shift-Rechtsklick ins Lager deponiert werden können.",
				"",
				"The standard number of items that can be deposited in the storage by shift-right clicking."});
		addConfig("SignStorage.ItemHologram.CanSpawn",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn 'true' dann können ItemHologram spawnen.",
				"",
				"If 'true' then ItemHologram can spawn."});
		addConfig("SignStorage.ItemHologram.RunTimerInSeconds",
				new Object[] {
				2},
				new Object[] {
				"",
				"Wie lange die Wiederholung der Schedular des ItemHologram läuft.",
				"",
				"How long the repetition of the ItemHologram's Schedular runs."});
		addConfig("SignStorage.ItemHologram.VisibilityTimeInSeconds",
				new Object[] {
				3},
				new Object[] {
				"",
				"Wie lange das ItemHologram sichtbar ist. Mit dem Schedular könnte die Sichtbarkeit länger sein.",
				"",
				"How long the ItemHologram is visible. With the Schedular, the visibility could be longer."});
		addConfig("SignStorage.CanStoreShulker",
				new Object[] {
				false},
				new Object[] {
				"",
				"Erlaubt den Shop Shulker zu verkaufen.",
				"",
				"Allows the store to sell Shulker."});
		addConfig("SignStorage.StorageUseMaterialAsStorageName",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn 'true' dann wird das Material als Shopname verwendet, wenn kein Displayname festgelegt wurde.",
				"",
				"If 'true' then the material is used as the store name if no display name has been defined."});
		addConfig("SignStorage.Gui.ForceSettingsLevel",
				new Object[] {
				false},
				new Object[] {
				"",
				"Wenn 'true' dann wird bei jedem öffnen des Shop & AdministrationGui das Settingslevel auf den unten angegeben Wert gesetzt.",
				"",
				"If 'true' then the settings level is set to the value specified below each time the Shop & AdministrationGui is opened."});
		addConfig("SignStorage.Gui.ClickCooldown",
				new Object[] {
				500},
				new Object[] {
				"",
				"Cooldown beim Klicken in den Guis in Millisekunden.",
				"",
				"Cooldown when clicking in the guis in milliseconds."});
		addConfig("SignStorage.Gui.ToBeForcedSettingsLevel",
				new Object[] {
				SettingsLevel.BASE.toString()},
				new Object[] {
				"",
				"SettingsLevel welches gesetzt wird, wenn es geforced wird.",
				"",
				"SettingsLevel which is set when it is forced."});
		addConfig("SignStorage.Gui.FillNotDefineGuiSlots",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn 'true' werden alle leeren Slots in einem Gui mit Füllitems aufgefüllt.",
				"",
				"If 'true', all empty slots in a gui are filled with filler items."});
		addConfig("SignStorage.Gui.FillerItemMaterial",
				new Object[] {
				Material.LIGHT_GRAY_STAINED_GLASS_PANE.toString()},
				new Object[] {
				"",
				"Füllmaterial für die Guis",
				"",
				"Filling material for the guis"});
		/*addConfig("SignShop.Search.Radius",
				new Object[] {
				"PROXY"},
				new Object[] {
				"",
				"Der 'Suchradius' für die Suchbefehle. Er determiniert, in welchem Bezugsradius er suchen soll.",
				"PROXY - sucht auf allen Server nach den Shops.",
				"SERVER - sucht auf dem Server wo sich der Spieler befindet nach den Shops.",
				"WORLD - sucht auf der Welt wo sich der Spieler befindet nach den Shops.",
				"Ganzzahl - bspw. 50 sucht innerhalb von +50 und -50 Blöcken in allen Richtungen nach den Shops.",
				"",
				"The 'search radius' for the search commands. It determines the reference radius in which it should search.",
				"PROXY - searches for the stores on all servers.",
				"SERVER - searches for stores on the server where the player is located.",
				"WORLD - searches for stores in the world where the player is located.",
				"Integer - e.g. 50 searches for the stores within +50 and -50 blocks in all directions."});
		addConfig("SignShop.Search.SortType",
				new Object[] {
				"PRICE"},
				new Object[] {
				"",
				"Die Sortierungsmethode für die Reihenfolge. Möglich sind: PRICE oder RANDOM",
				"",
				"The sorting method for the sequence. The following are possible: PRICE or RANDOM"});
		addConfig("SignShop.Search.DoAfterGuiClick",
				new Object[] {
				"LOCATION"},
				new Object[] {
				"",
				"Determiniert, was bei dem Klicken auf die Shops von den Suchbefehlen Guis gemacht wird.",
				"Bei LOCATION wird nur die Location des Shops und ein paar andere Daten per Chat geschrieben. Bei TELEPORT wird der Spieler teleportiert.",
				"",
				"Determines what is done by the search commands Guis when clicking on the stores.",
				"With LOCATION, only the location of the store and a few other details are written via chat. With TELEPORT the player is teleported."});*/
	}
	
	public void initConfigCity() //INFO:ConfigCity
	{
		addConfigCity("MinimumDistanceBetweenCities", new Object[] {
				100}, new Object[] {
				"",
				"Die Minimum Distanze zwischen Städte, wenn eine neue gegründet wird.",
				"",
				"The minimum distance between cities when a new one is founded."});
	}
	
	public void initConfigCityFlags() //INFO:ConfigCityFlags
	{
		String path = "PLAYER_VS_PLAYER";
		addConfigCityFlags(path+".Displayname", new Object[] {
				"Player vs. Player"}, new Object[] {
				"",
				"Anzeigename des CityAttributes.",
				"",
				"Display name of the CityAttribute."});
		addConfigCityFlags(path+".DefaultResultIfNotSpecified", new Object[] {
				Result.ALLOW.toString()}, new Object[] {
				"",
				"Das Resultat, wenn man das CityAttribute noch nicht in der City hat. Möglichkeiten: ALLOW, DENY.",
				"",
				"The result if you do not yet have the CityAttribute in the city. Possibilities: ALLOW, DENY."});
		addConfigCityFlags(path+".CooldownToChangeActiveStatus", new Object[] {
				"0000-14-00:00"}, new Object[] {
				"",
				"Zeitformat ab wann man das CityAttribute wieder aktiv schalten kann. In 'yyyy-dd-HH:mm'.",
				"",
				"Time format from when the CityAttribute can be activated again. In 'yyyy-dd-HH:mm'."});
		addConfigCityFlags(path+".BuyCosts", new Object[] {
				10000.0}, new Object[] {
				"",
				"Kosten, die beim Kauf des CityAttributes entstehen.",
				"",
				"Costs incurred when purchasing the CityAttribute."});
		addConfigCityFlags(path+".BuyableAtCityLevel", new Object[] {
				1}, new Object[] {
				"",
				"Level der City, ab wann man das CityAttribute kaufen kann. Niedrigstes CityLevel ist 0 bis maximal 9.",
				"",
				"Level of the city, from when you can buy the city attribute. The lowest city level is 0 to a maximum of 9."});
		addConfigCityFlags(path+".ActivationCosts", new Object[] {
				1000.0}, new Object[] {
				"",
				"Kosten um das CityAttribute wieder zu aktivieren, nachdem es deaktiviert worden war.",
				"",
				"Costs to reactivate the CityAttribute after it had been deactivated."});
		addConfigCityFlags(path+".AdministrativeExpensedIfActive", new Object[] {
				1000.0}, new Object[] {
				"",
				"Die Verwaltungskosten des CityAttributes, wenn es aktiv ist.",
				"",
				"The administrative costs of the CityAttribute when it is active."});
		addConfigCityFlags(path+".AdministrativeExpensedIfDeactive", new Object[] {
				1.0}, new Object[] {
				"",
				"Die Verwaltungskosten des CityAttributes, wenn es inaktiv ist.",
				"",
				"The administrative costs of the CityAttribute when it is inactive."});
		addCityAttributes("PLAYER_VS_MOBS", "Player vs. Mobs", Result.ALLOW, "0000-14-00:00", 10000.0, 2, 1000.0, 1000.0, 1.0);
	}
	
	private void addCityAttributes(String uniquename, String displayname,
			Result defaultResultIfNotSpecified,
			String cooldownToChangeActiveStatus,
			double buyCosts, int buyableAtCityLevel,
			double activationCosts, double administrativeExpensesIfActive, double administrativeExpensesIfDeactive)
	{
		configCityFlagsKeys.put(uniquename+".Displayname", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				displayname}));
		configCityFlagsKeys.put(uniquename+".DefaultResultIfNotSpecified", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				defaultResultIfNotSpecified.toString()}));
		configCityFlagsKeys.put(uniquename+".CooldownToChangeActiveStatus", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				cooldownToChangeActiveStatus}));
		configCityFlagsKeys.put(uniquename+".BuyCosts", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				buyCosts}));
		configCityFlagsKeys.put(uniquename+".BuyableAtCityLevel", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				buyableAtCityLevel}));
		configCityFlagsKeys.put(uniquename+".ActivationCosts", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				activationCosts}));
		configCityFlagsKeys.put(uniquename+".AdministrativeExpensedIfActive", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				administrativeExpensesIfActive}));
		configCityFlagsKeys.put(uniquename+".AdministrativeExpensedIfDeactive", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				administrativeExpensesIfDeactive}));
	}
	
	//INFO:Commands
	public void initCommands()
	{
		comBypass();
		commandsInput("vss", "vss", "vss.cmd.vss", 
				"/vss [pagenumber]", "/vss ", false,
				"&c/vss &f| Infoseite für alle Befehle.",
				"&c/vss &f| Info page for all commands.",
				"&bBefehlsrecht für &f/vss",
				"&bCommandright for &f/vss",
				"&eBasisbefehl für das VSS Plugin.",
				"&eGroundcommand for the VSS Plugin.");
		String basePermission = "vss.cmd";
		argumentInput("vss_storage", "storage", basePermission,
				"/vss storage", "/vss storage ", false,
				"&c/vss storage &f| Zwischenbefehl.",
				"&c/vss storage &f| Intermediate command.",
				"&bBefehlsrecht für &f/vss storage",
				"&bCommandright for &f/vss storage",
				"&eBefehl für den Zwischenbefehl.",
				"&eCommand for the intermediate command.");
		argumentInput("vss_debug", "debug", basePermission,
				"/vss debug <xxx>", "/vss debug ", false,
				"&c/vss debug <xxx> &f| Zu Testzwecken des Plugin. Benutzung auf eigene Gefahr.",
				"&c/vss debug <xxx> &f| For testing the plugin. Use at your own risk.",
				"&bBefehlsrecht für &f/vss debug",
				"&bCommandright for &f/vss debug",
				"&eZu Testzwecken des Plugin. Benutzung auf eigene Gefahr.",
				"&eFor testing the plugin. Use at your own risk.");
		argumentInput("vss_storage_delete", "delete", basePermission,
				"/vss storage delete <xxx:yyy...>", "/vss storage delete ", false,
				"&c/vss storage delete <xxx:yyy...> &f| Löscht alle Shops nach den Parameter(xxx). Param. sind id, player, server, world, item, radius.",
				"&c/vss storage delete <xxx:yyy...> &f| Deletes all stores after the parameter(xxx). Param. are id, player, server, world, item, radius.",
				"&bBefehlsrecht für &f/vss storage delete",
				"&bCommandright for &f/vss storage delete",
				"&eBefehl zum Löschen von Shops über Parameterangaben.",
				"&eCommand to delete stores via parameter specifications.");
		argumentInput("vss_storage_breaktoggle", "breaktoggle", basePermission,
				"/vss storage breaktoggle", "/vss storage breaktoggle ", false,
				"&c/vss storage breaktoggle &f| Togglet ob man Shops direkt löschen durch das Abbauen kann.",
				"&c/vss storage breaktoggle &f| Togglet whether you can delete stores directly by dismantling.",
				"&bBefehlsrecht für &f/vss storage breaktoggle",
				"&bCommandright for &f/vss storage breaktoggle",
				"&eBefehl für die direkte",
				"&eCommand for the intermediate command.");
		argumentInput("vss_storage_toggle", "toggle", basePermission,
				"/vss storage toggle", "/vss storage toggle ", false,
				"&c/vss storage toggle &f| Togglet ob man fremde Shops durch das Gui administrieren kann.",
				"&c/vss storage toggle &f| Toggle whether you can administrate foreign stores through the gui.",
				"&bBefehlsrecht für &f/vss storage toggle",
				"&bCommandright for &f/vss storage toggle",
				"&eBefehl zum togglet ob man fremde Shops durch das Gui administrieren kann.",
				"&eCommand to toggle whether you can administrate foreign stores through the gui.");
		argumentInput("vss_storage_searchbuy", "searchbuy", basePermission+".signstorage",
				"/vss storage searchbuy [Material] [Displayname...]", "/vss storage searchbuy", false,
				"&c/vss storage searchbuy [Material] [Displayname...] &f| Sucht alle Shops nach den angegebenen Parameter für Items zum kaufen.",
				"&c/vss storage searchbuy [Material] [Displayname...] &f| Searches all stores for the specified parameters for items to buy.",
				"&bBefehlsrecht für &f/vss storage searchbuy",
				"&bCommandright for &f/vss storage searchbuy",
				"&eSucht alle Shops nach den angegebenen Parameter für Items zum kaufen.",
				"&eSearches all stores for the specified parameters for items to buy.");
		argumentInput("vss_storage_searchsell", "searchsell", basePermission+".signstorage",
				"/vss storage searchsell [Material] [Displayname...]", "/vss storage searchsell", false,
				"&c/vss storage searchsell [Material] [Displayname...] &f| Sucht alle Shops nach den angegebenen Parameter für Items zum verkaufen.",
				"&c/vss storage searchsell [Material] [Displayname...] &f| Searches all stores for the specified parameters for items to sell.",
				"&bBefehlsrecht für &f/vss storage searchsell",
				"&bCommandright for &f/vss storage searchsell",
				"&eSucht alle Shops nach den angegebenen Parameter für Items zum verkaufen.",
				"&eSearches all stores for the specified parameters for items to sell.");
	}
	
	private void comBypass() //INFO:ComBypass
	{
		List<Bypass.Permission> list = new ArrayList<Bypass.Permission>(EnumSet.allOf(Bypass.Permission.class));
		for(Bypass.Permission ept : list)
		{
			commandsKeys.put("Bypass."+ept.toString()
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"vss."+ept.toString().toLowerCase().replace("_", ".")}));
		}
		
		List<Bypass.Counter> list2 = new ArrayList<Bypass.Counter>(EnumSet.allOf(Bypass.Counter.class));
		for(Bypass.Counter ept : list2)
		{
			commandsKeys.put("Count."+ept.toString()
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"vss."+ept.toString().toLowerCase().replace("_", ".")}));
		}
	}
	
	private void commandsInput(String path, String name, String basePermission, 
			String suggestion, String commandString, boolean putUpCmdPermToValueEntrySystem,
			String helpInfoGerman, String helpInfoEnglish,
			String dnGerman, String dnEnglish,
			String exGerman, String exEnglish)
	{
		commandsKeys.put(path+".Name"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				name}));
		commandsKeys.put(path+".Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				basePermission}));
		commandsKeys.put(path+".Suggestion"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				suggestion}));
		commandsKeys.put(path+".CommandString"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				commandString}));
		commandsKeys.put(path+".HelpInfo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				helpInfoGerman,
				helpInfoEnglish}));
		commandsKeys.put(path+".ValueEntry.PutUpCommandPerm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				putUpCmdPermToValueEntrySystem}));
		commandsKeys.put(path+".ValueEntry.Displayname"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				dnGerman,
				dnEnglish}));
		commandsKeys.put(path+".ValueEntry.Explanation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				exGerman,
				exEnglish}));
	}
	
	private void argumentInput(String path, String argument, String basePermission, 
			String suggestion, String commandString, boolean putUpCmdPermToValueEntrySystem,
			String helpInfoGerman, String helpInfoEnglish,
			String dnGerman, String dnEnglish,
			String exGerman, String exEnglish)
	{
		commandsKeys.put(path+".Argument"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				argument}));
		commandsKeys.put(path+".Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				basePermission+"."+argument}));
		commandsKeys.put(path+".Suggestion"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				suggestion}));
		commandsKeys.put(path+".CommandString"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				commandString}));
		commandsKeys.put(path+".HelpInfo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				helpInfoGerman,
				helpInfoEnglish}));
		commandsKeys.put(path+".ValueEntry.PutUpCommandPerm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				putUpCmdPermToValueEntrySystem}));
		commandsKeys.put(path+".ValueEntry.Displayname"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				dnGerman,
				dnEnglish}));
		commandsKeys.put(path+".ValueEntry.Explanation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				exGerman,
				exEnglish}));
	}
	
	public void initLanguage() //INFO:Languages
	{
		languageKeys.put("InputIsWrong",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDeine Eingabe ist fehlerhaft! Klicke hier auf den Text, um weitere Infos zu bekommen!",
						"&cYour input is incorrect! Click here on the text to get more information!"}));
		languageKeys.put("NoPermission",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast dafür keine Rechte!",
						"&cYou dont not have the rights!"}));
		languageKeys.put("NoPlayerExist",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Spieler existiert nicht!",
						"&cThe player does not exist!"}));
		languageKeys.put("InputIsEmpty",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDie Eingabe darf nicht leer sein!",
						"&cThe input must not be empty!"}));
		languageKeys.put("NoNumber",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Argument &f%value% &cmuss eine ganze Zahl sein.",
						"&cThe argument &f%value% &must be an integer."}));
		languageKeys.put("NoDouble",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Argument &f%value% &cmuss eine Gleitpunktzahl sein!",
						"&cThe argument &f%value% &must be a floating point number!"}));
		languageKeys.put("IsNegativ",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Argument &f%value% &cmuss eine positive Zahl sein!",
						"&cThe argument &f%value% &must be a positive number!"}));
		languageKeys.put("NotEnought",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast nicht genug Geld!",
						"&cYou dont have enough money!"}));
		languageKeys.put("OnCooldown",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Befehl ist in Cooldown!",
						"&cThe command is in cooldown!"}));
		languageKeys.put("GeneralHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKlick mich!",
						"&eClick me!"}));
		languageKeys.put("Headline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6VSS&7]&e=====",
						"&e=====&7[&6VSS&7]&e====="}));
		languageKeys.put("Next", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e&nnächste Seite &e==>",
						"&e&nnext page &e==>"}));
		languageKeys.put("Past", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e<== &nvorherige Seite",
						"&e<== &nprevious page"}));
		languageKeys.put("IsTrue", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&a✔",
						"&a✔"}));
		languageKeys.put("IsFalse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&c✖",
						"&c✖"}));
		languageKeys.put("AccountNotExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Account %value% existiert nicht!",
						"&cThe account %value% doesnt exist!"}));
		languageKeys.put("NoWithdrawRights",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast für das angegeben Konto keine Abheberechte!",
						"&cYou have no withdrawal rights for the specified account!"}));
		
		languageKeys.put("Cmd.OtherCmd",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBitte nutze den Befehl mit einem weiteren Argument aus der Tabliste!",
						"&cPlease use the command with another argument from the tab list!"}));
	}
	
	public void initModifierValueEntryLanguage() //INFO:ModifierValueEntryLanguages
	{
		mvelanguageKeys.put(Bypass.Permission.STORAGE_CREATION.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eShop-Erstellungsrecht",
						"&eShop creation right"}));
		mvelanguageKeys.put(Bypass.Permission.STORAGE_CREATION.toString()+".Explanation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&ePermission, welche erlaubt SchilderShop",
						"&eerstellen zu können.",
						"&ePermission, which allows",
						"&eto create SchilderShop."}));
		mvelanguageKeys.put(Bypass.Permission.STORAGE_GUI_BYPASS.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eShop-Adminstrationbypass",
						"&eShop administration bypass"}));
		mvelanguageKeys.put(Bypass.Permission.STORAGE_GUI_BYPASS.toString()+".Explanation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&ePermission, welche spezifische",
						"&eAdministrative Rechte für Shops gibt.",
						"&ePermission, which gives specific",
						"&eadministrative rights for shops."}));
		mvelanguageKeys.put(Bypass.Counter.STORAGE_CREATION_AMOUNT_.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eAnzahl zu erstellende Shops",
						"&eNumber of stores to be created"}));
		mvelanguageKeys.put(Bypass.Counter.STORAGE_CREATION_AMOUNT_.toString()+".Explanation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eZählpermission, welche die Anzahl",
						"&ezu erstellender definiert.",
						"&eCounting mission, which defines",
						"&ethe number to be created."}));
		mvelanguageKeys.put(Bypass.Counter.COST_ADDING_STORAGE.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKosten für die Vergrößerung des Shoplagerraums",
						"&eCosts for the enlargement of the store storage room"}));
		mvelanguageKeys.put(Bypass.Counter.COST_ADDING_STORAGE.toString()+".Explanation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eZählpermission, welche die Kosten für die",
						"&eVergrößerung des Shoplagerraums definiert.",
						"&eCount mission that defines the cost",
						"&eof increasing the store storage space."}));
	}
	
	public void initMaterialLanguage() //INFO:MaterialLanguages
	{		
		List<Material> signList = 
				Arrays.asList(new Material[] {
						Material.ACACIA_SIGN,
						Material.ACACIA_WALL_SIGN,
						Material.ACACIA_HANGING_SIGN,
						Material.BIRCH_SIGN,
						Material.BIRCH_WALL_SIGN,
						Material.BIRCH_HANGING_SIGN,
						Material.CHERRY_SIGN,
						Material.CHERRY_WALL_SIGN,
						Material.CHERRY_HANGING_SIGN,
						Material.CRIMSON_SIGN,
						Material.CRIMSON_WALL_SIGN,
						Material.CRIMSON_HANGING_SIGN,
						Material.DARK_OAK_SIGN,
						Material.DARK_OAK_WALL_SIGN,
						Material.DARK_OAK_HANGING_SIGN,
						Material.JUNGLE_SIGN,
						Material.JUNGLE_WALL_SIGN,
						Material.JUNGLE_HANGING_SIGN,
						Material.MANGROVE_SIGN,
						Material.MANGROVE_WALL_SIGN,
						Material.MANGROVE_HANGING_SIGN,
						Material.OAK_SIGN,
						Material.OAK_WALL_SIGN,
						Material.OAK_HANGING_SIGN,
						Material.SPRUCE_SIGN,
						Material.SPRUCE_WALL_SIGN,
						Material.SPRUCE_HANGING_SIGN,
						Material.WARPED_SIGN,
						Material.WARPED_WALL_SIGN,
						Material.WARPED_HANGING_SIGN
				});
		for(Material m : signList)
		{
			String c = "";
			switch(m)
			{
			default:
			case OAK_SIGN:
			case OAK_WALL_SIGN:
			case BIRCH_SIGN:
			case BIRCH_WALL_SIGN:
			case JUNGLE_SIGN:
			case JUNGLE_WALL_SIGN:
				c = "&0"; break;
			case SPRUCE_SIGN:
			case SPRUCE_WALL_SIGN:
			case SPRUCE_HANGING_SIGN:
			case ACACIA_SIGN:
			case ACACIA_WALL_SIGN:
			case ACACIA_HANGING_SIGN:
			case DARK_OAK_SIGN:
			case DARK_OAK_WALL_SIGN:
			case DARK_OAK_HANGING_SIGN:
			case MANGROVE_SIGN:
			case MANGROVE_WALL_SIGN:
			case MANGROVE_HANGING_SIGN:
			case CRIMSON_SIGN:
			case CRIMSON_WALL_SIGN:
			case CRIMSON_HANGING_SIGN:
				c = "&f"; break;
			case WARPED_SIGN:
			case WARPED_WALL_SIGN:
			case WARPED_HANGING_SIGN:
				c = "&e"; break;
			}
			matlanguageKeys.put(m.toString()+"_SignStartColor",
					new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
							c}));
		}
	}
	
	public void initGuiAdministration() //INFO:GuiAdministration
	{
		LinkedHashMap<String, Language> admin = new LinkedHashMap<>();
		String path = "4"; //InfoItem, wie es ist
		admin.put(path+".IsInfoItem",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		path = "13"; //InfoZumShop
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.GOLD_ORE.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cInfo zum Lager &f%displayname%",
						"&cInfo from Storage &f%displayname%"}));
		admin.put(path+".Lore."+SettingsLevel.BASE.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEigentümer: &f%owner%",
						"&cLagername: &f%signshopname%",
						"&cLager Aktuelle Items: &f%itemstoragecurrent%",
						"&cLager Gesamter Itemsplatz: &f%itemstoragetotal%",
						
						"&cOwner: &f%owner%",
						"&cStoragename: &f%signshopname%",
						"&cStorage Items Actual: &f%itemstoragecurrent%",
						"&cStorage Items Total: &f%itemstoragetotal%"}));
		admin.put(path+".Lore."+SettingsLevel.ADVANCED.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEigentümer: &f%owner%",
						"&cLagername: &f%signshopname%",
						"&cErstellungsdatum: &f%creationdate%",
						"&cLeuchtendes Schild: &f%glow%",
						"&cLager Aktuelle Items: &f%itemstoragecurrent%",
						"&cLager Gesamter Itemsplatz: &f%itemstoragetotal%",
						
						"&cOwner: &f%owner%",
						"&cStoragename: &f%signshopname%",
						"&cCreationdatum: &f%creationdate%",
						"&cLuminous shield: &f%glow%",
						"&cStorage Items Actual: &f%itemstoragecurrent%",
						"&cStorage Items Total: &f%itemstoragetotal%"}));
		admin.put(path+".Lore."+SettingsLevel.EXPERT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEigentümer: &f%owner%",
						"&cLagername: &f%signshopname%",
						"&cErstellungsdatum: &f%creationdate%",
						"&cLeuchtendes Schild: &f%glow%",
						"&cListenTyp: &f%listtype%",
						"&cItemHologram: &f%hologram%",
						"&cLager Aktuelle Items: &f%itemstoragecurrent%",
						"&cLager Gesamter Itemsplatz: &f%itemstoragetotal%",
						"&cLocation: &f%server%-%world%-&7%x%&f/&7%y%&f/&7%z%",
						"&cAccount: &f%accountid% - %accountname%",
						"&cUnlimitiert: &f%unlimited%",
						
						"&cOwner: &f%owner%",
						"&cStoragename: &f%signshopname%",
						"&cCreationdatum: &f%creationdate%",
						"&cLuminous shield: &f%glow%",
						"&cListType: &f%listtype%",
						"&cItemHologram: &f%hologram%",
						"&cStorage Items Actual: &f%itemstoragecurrent%",
						"&cStorage Items Total: &f%itemstoragetotal%",
						"&cLocation: &f%server%-%world%-&7%x%&f/&7%y%&f/&7%z%",
						"&cAccount: &f%accountid% - %accountname%",
						"&cUnlimited: &f%unlimited%"}));
		admin.put(path+".Lore."+SettingsLevel.MASTER.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cId: &f%id%",
						"&cEigentümer: &f%owner%",
						"&cLagername: &f%signshopname%",
						"&cErstellungsdatum: &f%creationdate%",
						"&cLeuchtendes Schild: &f%glow%",
						"&cListenTyp: &f%listtype%",
						"&cItemHologram: &f%hologram%",
						"&cLager Aktuelle Items: &f%itemstoragecurrent%",
						"&cLager Gesamter Itemsplatz: &f%itemstoragetotal%",
						"&cLocation: &f%server%-%world%-&7%x%&f/&7%y%&f/&7%z%",
						"&cAccount: &f%accountid% - %accountname%",
						"&cUnlimitiert: &f%unlimited%",
						
						"&cId: &f%id%",
						"&cOwner: &f%owner%",
						"&cStoragename: &f%signshopname%",
						"&cCreationdatum: &f%creationdate%",
						"&cLuminous shield: &f%glow%",
						"&cListType: &f%listtype%",
						"&cItemHologram: &f%hologram%",
						"&cStorage Items Actual: &f%itemstoragecurrent%",
						"&cStorage Items Total: &f%itemstoragetotal%",
						"&cLocation: &f%server%>%world%>&7%x%&f/&7%y%&f/&7%z%",
						"&cAccount: &f%accountid% - %accountname%",
						"&cUnlimited: &f%unlimited%"}));
		path = "6"; //ItemStack clear
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.ADVANCED.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.COMPOSTER.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cItem zurücksetzen",
						"&cReset item"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dSetzt das Item des Lagers zurück.",
						"&bFunktioniert nur, wenn alle Items",
						"&baus dem Lagerraum entfernt worden sind.",
						"&bDanach kann man ein neues Item setzen.",
						"&bResets the item of the storage.",
						"&bWorks only when all items have been",
						"&bremoved from the storage room.",
						"&bAfter that you can set a new item."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ITEM_CLEAR.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ITEM_CLEAR.toString()}));
		path = "0"; //SettingsLevelToggle
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		admin.put(path+".Material."+SettingsLevel.NOLEVEL.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.OAK_WOOD.toString()}));
		admin.put(path+".Material."+SettingsLevel.BASE.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.OAK_WOOD.toString()}));
		admin.put(path+".Material."+SettingsLevel.ADVANCED.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.STONE.toString()}));
		admin.put(path+".Material."+SettingsLevel.EXPERT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.DIAMOND.toString()}));
		admin.put(path+".Material."+SettingsLevel.MASTER.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.NETHERITE_INGOT.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dSwitcht die Gui-Level-Ansicht",
						"&dSwitch the Gui level view"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cLinksklick &bfür das Basis Level.",
						"&cRechtsklick &bfür das Fortgeschrittene Level.",
						"&cShift Linksklick &bfür das Experten Level.",
						"&cShift Rechtsklick &bfür das Meister Level.",
						"&cLeftclick &bfor the basic level.",
						"&cRightclick &bfor the advanced level.",
						"&cShift-Leftclick  &bfor the expert level.",
						"&cShift-Rightclick &bfor the master level."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETTINGSLEVEL_SETTO_BASE.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETTINGSLEVEL_SETTO_ADVANCED.toString()}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETTINGSLEVEL_SETTO_EXPERT.toString()}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETTINGSLEVEL_SETTO_MASTER.toString()}));
		path = "9"; //SetAccount
		admin.put(path+".IFHDepend",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.EXPERT.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.OAK_DOOR.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dEingabe des Wirtschaftsaccount per ID",
						"&dEnter the economyaccount per ID"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%accountid% - %accountname%",
						"&cQ &bzum stellen auf den Default Account.",
						"&cLinks-/Rechtsklick &bzum öffnen des Numpad Gui.",
						"&bAtm.: &f%accountid% - %accountname%",
						"&cQ &bto set to the default account.",
						"&cLeft/right click &to open the Numpad Gui."}));
		admin.put(path+".ClickFunction."+ClickType.DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETACCOUNT_DEFAULT.toString()}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETACCOUNT_OPEN_NUMPAD.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETACCOUNT_OPEN_NUMPAD.toString()}));
		path = "18"; //AddStorage
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.CHEST.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dErhöht den Lageraum des Lagers",
						"&dIncreases the storage space of the storage"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%itemstoragetotal% | %itemstoragetotaldoublechest% DKs",
						"&cLinksklick &berhöht den Lagerraum um 1 Item.",
						"&cRechtsklick &berhöht den Lagerraum um 8 Items.",
						"&cQ &berhöht den Lagerraum um 16 Items.",
						"&cShift Linksklick &berhöht den Lagerraum um 32 Items.",
						"&cShift Rechtsklick &berhöht den Lagerraum um 64 Items",
						"&cCtrl Q &berhöht den Lagerraum um 576 Items",
						"&c1 &berhöht den Lagerraum um 1728 Items",
						"&c2 &berhöht den Lagerraum um 3456 Items (1 DK)",
						"&c3 &berhöht den Lagerraum um 6912 Items (2 DK)",
						"&c4 &berhöht den Lagerraum um 17280 Items (5 DK)",
						"&c5 &berhöht den Lagerraum um 34560 Items (10 DK)",
						"&bAtm.: &f%itemstoragetotal% | %itemstoragetotaldoublechest% double chests",
						"&cLeftclick &bincreases the storage space by 1 item.",
						"&cRightclick &bincreases the storage space by 8 items.",
						"&cQ &bincreases the storage space by 16 item.",
						"&cShift-Leftclick  &bincreases the storage space by 32 items.",
						"&cShift-Rightclick &bincreases the storage space by 64 items.",
						"&cCtrl Q &bincreases the storage space by 576 items.",
						"&c1 &bincreases the storage space by 1728 items.",
						"&c2 &bincreases the storage space by 3456 items.",
						"&c3 &bincreases the storage space by 6912 items.",
						"&c3 &bincreases the storage space by 17280 items.",
						"&c3 &bincreases the storage space by 34560 items."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDSTORAGE_1.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDSTORAGE_8.toString()}));
		admin.put(path+".ClickFunction."+ClickType.DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDSTORAGE_16.toString()}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDSTORAGE_32.toString()}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDSTORAGE_64.toString()}));
		admin.put(path+".ClickFunction."+ClickType.CTRL_DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDSTORAGE_576.toString()}));
		admin.put(path+".ClickFunction."+ClickType.NUMPAD_1.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDSTORAGE_1728.toString()}));
		admin.put(path+".ClickFunction."+ClickType.NUMPAD_2.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDSTORAGE_3456.toString()}));
		admin.put(path+".ClickFunction."+ClickType.NUMPAD_3.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDSTORAGE_6912.toString()}));
		admin.put(path+".ClickFunction."+ClickType.NUMPAD_4.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDSTORAGE_17280.toString()}));
		admin.put(path+".ClickFunction."+ClickType.NUMPAD_5.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDSTORAGE_34560.toString()}));
		path = "2"; //SetShop Name
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.EXPERT.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.BARREL.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dUmbenennung des Lagers",
						"&dRename of the storage"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%signshopname%",
						"&cLinks-/Rechtsklick &bzum öffnen des Tastatur Gui.",
						"&bAtm.: &f%signshopname%",
						"&cLeft/right click &to open the Keyboard Gui."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETSIGNSHOPNAME_OPENKEYBOARD.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETSIGNSHOPNAME_OPENKEYBOARD.toString()}));
		path = "11"; //Setglowing
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.ADVANCED.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.GLOW_INK_SAC.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dLeuchtfunktion des Schild",
						"&dGlow function of the sign"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%glow%",
						"&cLinksklick &banstellen der Leuchtfunktion des Schildes.",
						"&cRechtsklick &babstellen der Leuchtfunktion des Schildes.",
						"&bAtm.: &f%glow%",
						"&cLeft click &bturn on the luminous function of the sign.",
						"&cRight click &bturn off the luminous function of the sign."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETGLOWING.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETUNGLOWING.toString()}));
		path = "30"; //SetListType
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.EXPERT.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.LECTERN.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dSetzen des Listfunktion des Lagers",
						"&dSetting the list function of the storage"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%listtype%",
						"&bStellt den Zugang zum Lager auf eine",
						"&bspezielle Liste von Spieler ein.",
						"&bSollten Spieler nicht auf der jeweiligen Liste sein,",
						"&bhaben Sie kein Zugriff auf das Lager!",
						"&cRechtsklick &bnur für Mitglieder.",
						"&cLinksklick &bfür die Whitelist.",
						"&bAtm.: &f%listtype%",
						"&bSets access to the storage to a",
						"&bspecial list of players.",
						"&bIf players are not on the respective list,",
						"&byou will not have access to the storage!",
						"&cRight click &bonly for member.",
						"&cLeft click &bfor the Whitelist."}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETLISTEDTYPE_MEMBER.toString()}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETLISTEDTYPE_WHITELIST.toString()}));
		path = "31"; //AddPlayerToList
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.EXPERT.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.SKELETON_SKULL.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&d+ oder - von Spieler auf den Listen",
						"&dAdd or remove players from the lists"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bÖffnet einer der Keyboard Guis,",
						"&bum Spieler zu den jeweiligen Listen hinzuzufügen",
						"&boder zu entfernen. Hier können Mitglieder,",
						"&bWhitelist sowie eine",
						"&bbenutzerdefinierte Liste bearbeitet werden.",
						"&cRechtsklick &böffnet das Keyboard Gui für die Whitelist.",
						"&cLinksklick &böffnet das Keyboard Gui für die Mitglieder.",
						
						"&bOpens one of the Keyboard Guis",
						"&bto add or remove players",
						"&bfrom the respective lists.",
						"&bMembers, whitelist",
						"&band a custom list can be edited here.",
						"&cRight click &bopens the keyboard gui for the whitelist.",
						"&cLeft click &bopens the keyboard Gui for the members.",}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_OPENKEYBOARD_WHITELIST.toString()}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_OPENKEYBOARD_MEMBER.toString()}));
		path = "32"; //PlayersOnList
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.EXPERT.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.ZOMBIE_HEAD.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dSpieler auf den jeweiligen Listen",
						"&dPlayers on the respective lists"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bÖffnet einen Chateintrag,",
						"&bvon Spieler die auf den jeweiligen Listen sind.",
						"&cRechtsklick &bfür die Whitelist.",
						"&cLinksKlick &bfür die Mitglieder.",
						
						"&bOpens a chatentry",
						"&bof players from the respective lists.",
						"&cRight click &bfor the whitelist.",
						"&cLeft click &bfor the members.",}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_LISTEDTYPE_PLAYER_OPENLIST_WHITELIST.toString()}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_LISTEDTYPE_PLAYER_OPENLIST_MEMBER.toString()}));
		path = "20"; //ToggleItemHologram
		admin.put(path+".Permission",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						".item.hologram"}));
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.EXPERT.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.END_CRYSTAL.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dToggel ItemHologram",
						"&dToggle ItemHologram"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%hologram%",
						"&bToggelt das ItemHologram.",
						"&bWenn aktiv, so erscheint mit einem Item in der Hand",
						"&bund einem Linksklick auf das Shopschild das ItemHologram.",
						"&cLinksklick &baktiviert das Hologram.",
						"&cRechtsklick &bdeaktiviert das Hologram.",
						"&bAtm.: &f%hologram%",
						"&bToggles the ItemHologram.",
						"&bIf active, with an item in hand",
						"&band a left click on the store sign, the ItemHologram will appear.",
						"&cLeftclick &bactivates the hologram.",
						"&cRightclick &bdeactivates the hologram."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETITEMHOLOGRAM_ACTIVE.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETITEMHOLOGRAM_DEACTIVE.toString()}));
		path = "36"; //Input
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.ADVANCED.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.ENDER_CHEST.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dEingabe des Item Input per Linksklick",
						"&d"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%iteminput%",
						"&cLinks/Rechtsklick &bSetzt die Anzahl an Items,",
						"&bwelche mit Linksklick ins Lager gelagert werden.",
						"&cQ &bsetzt den Standartwert ein.",
						"&bAtm.: &f%iteminput%",
						"&cLeft/right-click &bsets the number of items that are",
						"&bstored in the warehouse by left-clicking.",
						"&cQ &sets the default value."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETINPUT_OPEN_NUMPAD.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETINPUT_OPEN_NUMPAD.toString()}));
		admin.put(path+".ClickFunction."+ClickType.DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETINPUT_DEFAULT.toString()}));
		path = "45"; //ShiftInput
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.ADVANCED.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.ENDER_CHEST.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dEingabe des Item Input per Shift-Linksklick",
						"&d"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%itemshiftinput%",
						"&cLinks/Rechtsklick &bSetzt die Anzahl an Items,",
						"&bwelche mit Shift-Linksklick ins Lager gelagert werden.",
						"&cQ &bsetzt den Standartwert ein.",
						"&bAtm.: &f%itemshiftinput%",
						"&cLeft/right-click &bSets the number of items that are",
						"&bstored in the warehouse with shift-left-click.",
						"&cQ &sets the default value."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETSHIFTINPUT_OPEN_NUMPAD.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETSHIFTINPUT_OPEN_NUMPAD.toString()}));
		admin.put(path+".ClickFunction."+ClickType.DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETSHIFTINPUT_DEFAULT.toString()}));
		path = "37"; //Output
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.ADVANCED.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.CHEST.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dEingabe des Item Output per Rechtsklick",
						"&d"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%itemoutput%",
						"&cLinks/Rechtsklick &bSetzt die Anzahl an Items,",
						"&bwelche mit Rechtsklick vom Lager geholt werden.",
						"&cQ &bsetzt den Standartwert ein.",
						"&bAtm.: &f%itemoutput%",
						"&cLeft/right-click &bSets the number of items that are",
						"&bretrieved from the warehouse by right-clicking.",
						"&cQ &sets the default value."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETOUTPUT_OPEN_NUMPAD.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETOUTPUT_OPEN_NUMPAD.toString()}));
		admin.put(path+".ClickFunction."+ClickType.DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETOUTPUT_DEFAULT.toString()}));
		path = "46"; //ShiftOutput
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.ADVANCED.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.CHEST.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dEingabe des Item Output per Shift-Rechtsklick",
						"&d"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%itemshiftoutput%",
						"&cLinks/Rechtsklick &bSetzt die Anzahl an Items, ",
						"&bwelche mit Shift-Rechtsklick vom Lager geholt werden.",
						"&cQ &bsetzt den Standartwert ein.",
						"&bAtm.: &f%itemshiftoutput%",
						"&cLeft/right-click &bSets the number of items that are",
						"&bretrieved from the warehouse with shift-right-click.",
						"&cQ &sets the default value."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETSHIFTOUTPUT_OPEN_NUMPAD.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETSHIFTOUTPUT_OPEN_NUMPAD.toString()}));
		admin.put(path+".ClickFunction."+ClickType.DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETSHIFTOUTPUT_DEFAULT.toString()}));
		path = "49"; //Unlimited Toggle
		admin.put(path+".Permission",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						".unlimited.sell"}));
		admin.put(path+".CanSell",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.EXPERT.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.GOLD_BLOCK.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dToggel Unlimitiert",
						"&dToggle unlimited"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz. &f%unlimited%",
						"&bToggelt das unlimitierte Lager.",
						"&bSollte der unlimitierter Verkauf angeschaltet sein,",
						"&bkönnen unbegrenzt Items ge- und entlagert werden.",
						"&bAtm. &f%unlimited%",
						"&bToggles the unlimited storage",
						"&bIf the unlimited storage is turned on,",
						"&bunlimited number of items can be called or stored."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_UNLIMITED_TOGGLE.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_UNLIMITED_TOGGLE.toString()}));
		path = "8"; //Delete All but with no items in storage
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.BARRIER.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dLöschung des Lagers",
						"&dDeleting of the storage"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cShift Links/Rechtsklick &blöscht das Lager.",
						"&bFunktioniert nur, wenn alle Items",
						"&baus dem Lagerraum entfernt worden sind.",
						"&cShift Left/rightclick &bdelete the storage.",
						"&bWorks only when all items have been",
						"&bremoved from the storage room."}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_DELETE_WITHOUT_ITEMS_IN_STORAGE.toString()}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_DELETE_WITHOUT_ITEMS_IN_STORAGE.toString()}));
		path = "17"; //Delete All
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.EXPERT.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.TNT.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dLöschung des Lagers",
						"&dDeleting of the storages"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cShift Links/Rechtsklick &blöscht das Lager.",
						"&cAchtung!",
						"&bDurch das Klicken wird unverzüglich der gesamte Shop",
						"&bmit allen noch verbliebenen Items gelöscht!",
						"&cAchtung!",
						"&cShift Left/rightclick &bopen the storage.",
						"&cAttention!",
						"&bClicking immediately deletes the entire",
						"&bstore with all remaining items!",
						"&cAttention"}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_DELETE_ALL.toString()}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_DELETE_ALL.toString()}));
		guiKeys.put(GuiType.ADMINISTRATION, admin);
		
		//------------------------------------
		/*admin.put(path+".IsInfoItem",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".Permission",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".CanBuy",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".CanSell",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".UseASH",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						false}));
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&d",
						"&d"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&b",
						"&b",
						"&b",
						"&b"}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
		admin.put(path+".ClickFunction."+ClickType.DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
		admin.put(path+".ClickFunction."+ClickType.CTRL_DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
		admin.put(path+".ClickFunction."+ClickType.SWAP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));*/
	}
	
	public void initGuiNumpad() //INFO:GuiNumpad
	{
		LinkedHashMap<String, Language> numpad = new LinkedHashMap<>();
		Language lNSL = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				SettingsLevel.NOLEVEL.toString()});
		Language lNMat = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				Material.PLAYER_HEAD.toString()});
		Language lNLo = new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&bZz.: &f%numtext%",
				"&bFügt am Ende ein Zeichen an.",
				"&bAtm.: &f%numtext%",
				"&bAdds a character at the end."});
		String path = "12"; //7
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/9e198fd831cb61f3927f21cf8a7463af5ea3c7e43bd3e8ec7d2948631cce879"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f7 &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_7.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_7.toString()}));
		path = "13"; //8
		numpad.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		numpad.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.PLAYER_HEAD.toString()}));
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/84ad12c2f21a1972f3d2f381ed05a6cc088489fcfdf68a713b387482fe91e2"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f8 &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_8.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_8.toString()}));
		path = "14"; //9
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/9f7aa0d97983cd67dfb67b7d9d9c641bc9aa34d96632f372d26fee19f71f8b7"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f9 &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_9.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_9.toString()}));
		path = "21"; //4
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/d198d56216156114265973c258f57fc79d246bb65e3c77bbe8312ee35db6"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f4 &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_4.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_4.toString()}));
		path = "22"; //5
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/7fb91bb97749d6a6eed4449d23aea284dc4de6c3818eea5c7e149ddda6f7c9"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f5 &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_5.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_5.toString()}));
		path = "23"; //6
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/9c613f80a554918c7ab2cd4a278752f151412a44a73d7a286d61d45be4eaae1"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f6 &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_6.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_6.toString()}));
		path = "30"; //1
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/d2a6f0e84daefc8b21aa99415b16ed5fdaa6d8dc0c3cd591f49ca832b575"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f1 &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_1.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_1.toString()}));
		path = "31"; //2
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/96fab991d083993cb83e4bcf44a0b6cefac647d4189ee9cb823e9cc1571e38"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f2 &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_2.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_2.toString()}));
		path = "32"; //3
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/cd319b9343f17a35636bcbc26b819625a9333de3736111f2e932827c8e749"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f3 &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_3.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_3.toString()}));
		path = "40"; //0
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/6d68343bd0b129de93cc8d3bba3b97a2faa7ade38d8a6e2b864cd868cfab"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f0 &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_0.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_0.toString()}));
		path = "38"; //.
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/95f6e3383d128f17d73cf39af7b579889779c4e5f38d2c1ef85dba2f462f6840"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f. &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_DECIMAL.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_DECIMAL.toString()}));
		path = "42"; //:
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/ac4c26963f8538c11eac6a8e437d27682dffea4bcc0f04afd5cda6d1d567"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f: &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_COLON.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_COLON.toString()}));
		path = "48"; //RemoveOnce
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/864f779a8e3ffa231143fa69b96b14ee35c16d669e19c75fd1a7da4bf306c"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eUm ein Zeichen zurücksetzen",
						"&eReset by one character"}));
		numpad.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%numtext%",
						"&bSetzt die Eingabe um ein Zeichen zurück.",
						"&bAtm.: &f%numtext%",
						"&bResets the input by one character."}));
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_REMOVEONCE.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_REMOVEONCE.toString()}));
		path = "50"; //Clear
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/118a2dd5bef0b073b13271a7eeb9cfea7afe8593c57a93821e43175572461812"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eZurücksetzen",
						"&eReset"}));
		numpad.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%numtext%",
						"&bSetzt die Eingabe zurück.",
						"&bAtm.: &f%numtext%",
						"&bResets the input."}));
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_CLEAR.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_CLEAR.toString()}));
		path = "45"; //Cancel
		numpad.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		numpad.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.RED_BANNER.toString()}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cZurück zum Administrations-Gui",
						"&cBack to the administration gui"}));
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_CANCEL.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_CANCEL.toString()}));
		LinkedHashMap<String, Language> numpad_ACCOUNT = new LinkedHashMap<>();
		numpad_ACCOUNT.putAll(numpad);
		LinkedHashMap<String, Language> numpad_ITEMINPUT = new LinkedHashMap<>();
		numpad_ITEMINPUT.putAll(numpad);
		LinkedHashMap<String, Language> numpad_ITEMSHIFTINPUT = new LinkedHashMap<>();
		numpad_ITEMSHIFTINPUT.putAll(numpad);
		LinkedHashMap<String, Language> numpad_ITEMOUTPUT = new LinkedHashMap<>();
		numpad_ITEMOUTPUT.putAll(numpad);
		LinkedHashMap<String, Language> numpad_ITEMSHIFTOUTPUT = new LinkedHashMap<>();
		numpad_ITEMSHIFTOUTPUT.putAll(numpad);
		//---------------
		path = "53"; //TakeOver
		String sSL = path+".SettingLevel";
		Language lSL = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				SettingsLevel.NOLEVEL.toString()});
		String sMat = path+".Material";
		Language lMat = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				Material.GREEN_BANNER.toString()});
		String sDN = path+".Displayname";
		Language lDN = new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&aÜbernahme der Eingabe",
				"&cAcceptance of the input"});
		String sLo = path+".Lore";
		Language lLo = new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&bZz.: &f%numtext%", "&bÜbernimmt die Eingabe und kehre zum Administration Gui zurück.",
				"&bAtm.: &f%numtext%", "&bAccept the input and return to the Administration Gui."});
		String sLC = path+".ClickFunction."+ClickType.LEFT.toString();
		Language lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETACCOUNT_TAKEOVER.toString()});
		String sRC = path+".ClickFunction."+ClickType.RIGHT.toString();
		Language lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETACCOUNT_TAKEOVER.toString()});
		numpad_ACCOUNT.put(sSL, lSL);
		numpad_ACCOUNT.put(sMat, lMat);
		numpad_ACCOUNT.put(sDN, lDN);
		numpad_ACCOUNT.put(sLo, lLo);
		numpad_ACCOUNT.put(sLC, lLC);
		numpad_ACCOUNT.put(sRC, lRC);
		guiKeys.put(GuiType.NUMPAD_ACCOUNT, numpad_ACCOUNT);
		//-------------
		sLC = path+".ClickFunction."+ClickType.LEFT.toString();
		lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETINPUT_TAKEOVER.toString()});
		sRC = path+".ClickFunction."+ClickType.RIGHT.toString();
		lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETINPUT_TAKEOVER.toString()});
		numpad_ITEMINPUT.put(sSL, lSL);
		numpad_ITEMINPUT.put(sMat, lMat);
		numpad_ITEMINPUT.put(sDN, lDN);
		numpad_ITEMINPUT.put(sLo, lLo);
		numpad_ITEMINPUT.put(sLC, lLC);
		numpad_ITEMINPUT.put(sRC, lRC);
		guiKeys.put(GuiType.NUMPAD_ITEMINPUT, numpad_ITEMINPUT);
		//-------------
		sLC = path+".ClickFunction."+ClickType.LEFT.toString();
		lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETSHIFTINPUT_TAKEOVER.toString()});
		sRC = path+".ClickFunction."+ClickType.RIGHT.toString();
		lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETSHIFTINPUT_TAKEOVER.toString()});
		numpad_ITEMSHIFTINPUT.put(sSL, lSL);
		numpad_ITEMSHIFTINPUT.put(sMat, lMat);
		numpad_ITEMSHIFTINPUT.put(sDN, lDN);
		numpad_ITEMSHIFTINPUT.put(sLo, lLo);
		numpad_ITEMSHIFTINPUT.put(sLC, lLC);
		numpad_ITEMSHIFTINPUT.put(sRC, lRC);
		guiKeys.put(GuiType.NUMPAD_ITEMSHIFTINPUT, numpad_ITEMSHIFTINPUT);
		//-------------
		sLC = path+".ClickFunction."+ClickType.LEFT.toString();
		lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETOUTPUT_TAKEOVER.toString()});
		sRC = path+".ClickFunction."+ClickType.RIGHT.toString();
		lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETOUTPUT_TAKEOVER.toString()});
		numpad_ITEMOUTPUT.put(sSL, lSL);
		numpad_ITEMOUTPUT.put(sMat, lMat);
		numpad_ITEMOUTPUT.put(sDN, lDN);
		numpad_ITEMOUTPUT.put(sLo, lLo);
		numpad_ITEMOUTPUT.put(sLC, lLC);
		numpad_ITEMOUTPUT.put(sRC, lRC);
		guiKeys.put(GuiType.NUMPAD_ITEMOUTPUT, numpad_ITEMOUTPUT);
		//-------------
		sLC = path+".ClickFunction."+ClickType.LEFT.toString();
		lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETSHIFTOUTPUT_TAKEOVER.toString()});
		sRC = path+".ClickFunction."+ClickType.RIGHT.toString();
		lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETSHIFTOUTPUT_TAKEOVER.toString()});
		numpad_ITEMSHIFTOUTPUT.put(sSL, lSL);
		numpad_ITEMSHIFTOUTPUT.put(sMat, lMat);
		numpad_ITEMSHIFTOUTPUT.put(sDN, lDN);
		numpad_ITEMSHIFTOUTPUT.put(sLo, lLo);
		numpad_ITEMSHIFTOUTPUT.put(sLC, lLC);
		numpad_ITEMSHIFTOUTPUT.put(sRC, lRC);
		guiKeys.put(GuiType.NUMPAD_ITEMSHIFTOUTPUT, numpad_ITEMSHIFTOUTPUT);
		//-------------
		/*lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETBUY_TAKEOVER.toString()});
		lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETBUY_TAKEOVER.toString()});
		numpad_BUY.put(sSL, lSL);
		numpad_BUY.put(sMat, lMat);
		numpad_BUY.put(sDN, lDN);
		numpad_BUY.put(sLo, lLo);
		numpad_BUY.put(sLC, lLC);
		numpad_BUY.put(sRC, lRC);
		guiKeys.put(GuiType.NUMPAD_BUY, numpad_BUY);*/
	}
	
	public void initGuiItemInput() //INFO:GuiItemInput
	{
		LinkedHashMap<String, Language> iinput = new LinkedHashMap<>();
		String path = "22"; //Information
		iinput.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		iinput.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.PAPER.toString()}));
		iinput.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dKlicke auf ein Item zum Hinzufügen des Shops.",
						"&dClick on an item to add the store."}));
		guiKeys.put(GuiType.ITEM_INPUT, iinput);
	}
	
	public void initGuiKeyboard() //INFO:GuiKeyBoard
	{
		LinkedHashMap<String, Language> keyboard = new LinkedHashMap<>();
		Language lBSL = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				SettingsLevel.NOLEVEL.toString()});
		Language lBMat = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				Material.PLAYER_HEAD.toString()});
		Language lBLo = new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&bZz.: &f%numtext%",
				"&bFügt am Ende ein Zeichen an.",
				"&bAtm.: &f%numtext%",
				"&bAdds a character at the end."});
		Language lB2Lo = new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&bZz.: &f%numtext%",
				"&cLinksklick &bfügt den Kleinbuchstabe am Ende hinzu.",
				"&cRechtsklick &bfügt den Großbuchstabe am Ende hinzu.",
				"&bAtm.: &f%numtext%",
				"&cLeftclick &badds the lowercase letter at the end.",
				"&cRightclick &badds the capital letter at the end."});
		String path = ""; //7
		path = "18"; //Q
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/ff72cceb4a565478de5b0b0e727946e549834e36f6e0ec8f7dd7f6327b15a"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fq/Q &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_Q_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_Q_CAPITAL.toString()}));
		path = "19"; //W
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/79cbc465525e16a89441d789b72f554e8ff4ea5b393447aef3ff193f0465058"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fw/W &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_W_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_W_CAPITAL.toString()}));
		path = "20"; //E
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/ced9f431a997fce0d8be1844f62090b1783ac569c9d2797528349d37c215fcc"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fe/E &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_E_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_E_CAPITAL.toString()}));
		path = "21"; //R
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/3cb88225ee4ab39f7cbf581f22cbf08bdcc33884f1ff747689312841516c345"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fr/R &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_R_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_R_CAPITAL.toString()}));
		path = "22"; //T
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/fc2fcbc24e7382ac112bb2c0d5eca27e9f48ffca5a157e502617a96d636f5c3"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&ft/T &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_T_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_T_CAPITAL.toString()}));
		path = "23"; //Y
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/a71071bef733f477021b3291dc3d47f0bdf0be2da1b165a119a8ff1594567"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fy/Y &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_Y_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_Y_CAPITAL.toString()}));
		path = "24"; //U
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/9fdc4f321c78d67484135ae464af4fd925bd57d459383a4fe9d2f60a3431a79"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fu/U &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_U_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_U_CAPITAL.toString()}));
		path = "25"; //I
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/c148a8865bc4afe0747f3415138b96bbb4e8bbb7261f45e5d11d7219f368e4"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fi/I &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_I_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_I_CAPITAL.toString()}));
		path = "26"; //O
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/1c27235de3a55466b627459f1233596ab6a22c435cfc89a4454b47d32b199431"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fo/O &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_O_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_O_CAPITAL.toString()}));
		path = "27"; //A
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/17dd34924d2b6a213a5ed46ae5783f95373a9ef5ce5c88f9d736705983b97"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fa/A &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_A_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_A_CAPITAL.toString()}));
		path = "28"; //S
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/af22d7cd53d5bfe61eafbc2fb1ac94443eec24f455292139ac9fbdb83d0d09"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fs/S &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_S_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_S_CAPITAL.toString()}));
		path = "29"; //D
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/59aa69229ffdfa182889bf3097d32215c1b2159d987103b1d5843646faac"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fd/D &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_D_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_D_CAPITAL.toString()}));
		path = "30"; //F
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/9d714bafb0b5ab9cfa7db02efc8927aed1ef29797a595da066efc5c3efdc9"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&ff/F &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_F_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_F_CAPITAL.toString()}));
		path = "31"; //G
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/58c336dedfe197b434b5ab67988cbe9c2c9f285ec1871fdd1ba434855b"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fg/G &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_G_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_G_CAPITAL.toString()}));
		path = "32"; //H
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/bde4a89be2197f86d2e6166a0ac541ccc21dce28b7854b788d329a39daec32"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fh/H &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_H_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_H_CAPITAL.toString()}));
		path = "33"; //J
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/18c9dc3d38a56282e1d92337198fb19ea641b61a8c4e57fb4e27c1ba6a4b24c"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fj/J &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_J_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_J_CAPITAL.toString()}));
		path = "34"; //K
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/12bfeb246f649b86f212feea87a9c216a655565d4b7992e80326b3918d923bd"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fk/K &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_K_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_K_CAPITAL.toString()}));
		path = "35"; //L
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/cc58321d4bffbec2ddf66bf38cf2f9e9ddf3fa2f1387dc7d30c62b4d010c8"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fl/L &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_L_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_L_CAPITAL.toString()}));
		path = "36"; //Z
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/c992c753bf9c625853ce2a0b7b174b89a6ec26bb5c3ccb473b6a2012496312"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fz/Z &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_Z_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_Z_CAPITAL.toString()}));
		path = "37"; //X
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/c38ab145747b4bd09ce0354354948ce69ff6f41d9e098c6848b80e187e919"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fx/X &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_X_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_X_CAPITAL.toString()}));
		path = "38"; //C
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/56b1486e1f576bc921b8f9f59fe6122ce6ce9dd70d75e2c92fdb8ab9897b5"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fc/C &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_C_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_C_CAPITAL.toString()}));
		path = "39"; //V
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/2dd0143d8e449ad1ba97e1981712cee0f3fc297dbc17c83b05eea3338d659"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fv/V &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_V_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_V_CAPITAL.toString()}));
		path = "40"; //B
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/9eca98befd0d7efca9b11ebf4b2da459cc19a378114b3cdde67d4067afb896"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fb/B &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_B_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_B_CAPITAL.toString()}));
		path = "41"; //N
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/612c7afea48e53325e5129038a45aec51afe256abca941b6bc8206fae1cef"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fn/N &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_N_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_N_CAPITAL.toString()}));
		path = "42"; //M
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/90376dc5e3c981b52960578afe4bfc41c1778789bcd80ec2c2d2fd460e5a51a"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fm/M &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_M_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_M_CAPITAL.toString()}));
		path = "43"; //P
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/c584dc7ecf36b4f044f8262527985718bf24a9daef012de92e1e76d4586d96"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fp/P &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_P_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_P_CAPITAL.toString()}));
		path = "44"; //_
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/979a465183a3ba63fe6ae272bc1bf1cd15f2c209ebbfcc5c521b9514682a43"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f_ &7Numpad"}));
		keyboard.put(path+".Lore", lBLo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD__.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD__.toString()}));
		path = "49"; //0-1
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/6d68343bd0b129de93cc8d3bba3b97a2faa7ade38d8a6e2b864cd868cfab"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f0/1 &7Numpad"}));
		keyboard.put(path+".Lore", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%numtext%",
						"&cLinksklick &bfügt eine 0 hinzu.",
						"&cRechtsklick &bfügt eine 1 hinzu.",
						"&bAtm.: &f%numtext%",
						"&cLeftclick &badds a 0.",
						"&cRightclick &badds a 1."}));
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_0.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_1.toString()}));
		path = "50"; //2-3
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/96fab991d083993cb83e4bcf44a0b6cefac647d4189ee9cb823e9cc1571e38"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f2/3 &7Numpad"}));
		keyboard.put(path+".Lore",  
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%numtext%",
						"&cLinksklick &bfügt eine 2 hinzu.",
						"&cRechtsklick &bfügt eine 3 hinzu.",
						"&bAtm.: &f%numtext%",
						"&cLeftclick &badds a 2.",
						"&cRightclick &badds a 3."}));
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_2.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_3.toString()}));
		path = "51"; //4-5
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/d198d56216156114265973c258f57fc79d246bb65e3c77bbe8312ee35db6"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f4/5 &7Numpad"}));
		keyboard.put(path+".Lore",  
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%numtext%",
						"&cLinksklick &bfügt eine 4 hinzu.",
						"&cRechtsklick &bfügt eine 5 hinzu.",
						"&bAtm.: &f%numtext%",
						"&cLeftclick &badds a 4.",
						"&cRightclick &badds a 5."}));
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_4.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_5.toString()}));
		path = "52"; //6-7
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/9c613f80a554918c7ab2cd4a278752f151412a44a73d7a286d61d45be4eaae1"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f6/7 &7Numpad"}));
		keyboard.put(path+".Lore",  
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%numtext%",
						"&cLinksklick &bfügt eine 6 hinzu.",
						"&cRechtsklick &bfügt eine 7 hinzu.",
						"&bAtm.: &f%numtext%",
						"&cLeftclick &badds a 6.",
						"&cRightclick &badds a 7."}));
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_6.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_7.toString()}));
		path = "53"; //8-9
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/84ad12c2f21a1972f3d2f381ed05a6cc088489fcfdf68a713b387482fe91e2"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f8/9 &7Numpad"}));
		keyboard.put(path+".Lore",  
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%numtext%",
						"&cLinksklick &bfügt eine 8 hinzu.",
						"&cRechtsklick &bfügt eine 9 hinzu.",
						"&bAtm.: &f%numtext%",
						"&cLeftclick &badds a 8.",
						"&cRightclick &badds a 9."}));
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_8.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_9.toString()}));
		path = "46"; //RemoveOnce
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/864f779a8e3ffa231143fa69b96b14ee35c16d669e19c75fd1a7da4bf306c"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eUm ein Zeichen zurücksetzen",
						"&eReset by one character"}));
		keyboard.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%numtext%",
						"&bSetzt die Eingabe um ein Zeichen zurück.",
						"&bAtm.: &f%numtext%",
						"&bResets the input by one character."}));
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_REMOVEONCE.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_REMOVEONCE.toString()}));
		path = "47"; //Clear
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/118a2dd5bef0b073b13271a7eeb9cfea7afe8593c57a93821e43175572461812"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eZurücksetzen",
						"&eReset"}));
		keyboard.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%numtext%",
						"&bSetzt die Eingabe zurück.",
						"&bAtm.: &f%numtext%",
						"&bResets the input."}));
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_CLEAR.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_CLEAR.toString()}));
		path = "45"; //Cancel
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.RED_BANNER.toString()}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cZurück zum Administrations-Gui",
						"&cBack to the administration gui"}));
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_CANCEL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_CANCEL.toString()}));
		LinkedHashMap<String, Language> keyboard_signshopname = new LinkedHashMap<>();
		keyboard_signshopname.putAll(keyboard);
		LinkedHashMap<String, Language> keyboard_blacklist = new LinkedHashMap<>();
		keyboard_blacklist.putAll(keyboard);
		LinkedHashMap<String, Language> keyboard_whitelist = new LinkedHashMap<>();
		keyboard_whitelist.putAll(keyboard);
		LinkedHashMap<String, Language> keyboard_member = new LinkedHashMap<>();
		keyboard_member.putAll(keyboard);
		LinkedHashMap<String, Language> keyboard_custom = new LinkedHashMap<>();
		keyboard_custom.putAll(keyboard);
		path = "4"; //TakeOver
		keyboard_signshopname.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		keyboard_signshopname.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.GREEN_BANNER.toString()}));
		keyboard_signshopname.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aÜbernahme der Eingabe",
						"&cAcceptance of the input"}));
		keyboard_signshopname.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%numtext%", "&bÜbernimmt die Eingabe und","&bkehre zum Administration Gui zurück.",
						"&bAtm.: &f%numtext%", "&bAccept the input and","&breturn to the Administration Gui."}));
		keyboard_signshopname.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETSIGNSHOPNAME_TAKEOVER.toString()}));
		keyboard_signshopname.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETSIGNSHOPNAME_TAKEOVER.toString()}));
		guiKeys.put(GuiType.KEYBOARD_SIGNSTORAGENAME, keyboard_signshopname);
		//-------------
		Language lSL = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				SettingsLevel.NOLEVEL.toString()});
		Language lMat = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				Material.PLAYER_HEAD.toString()});
		Language lLo_WL = new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&bZz.: &f%numtext%",
				"&bZz. auf der Whiteliste: &f%isonwhitelist%",
				"&cLinksklick &bfügt den Spieler",
				"&bzur Whitelist dieses Lagers hinzu.",
				"&cRechtsklick &bfügt den Spieler",
				"&bzur Whitelist aller Lagern dieser Welt hinzu.",
				"&cShift-Linksklick &bentfernt den Spieler",
				"&bvon der Whiteliste dieses Lagers.",
				"&cShift-Rechtsklick &bentfernt den Spieler",
				"&bvon der Whiteliste aller Lagern dieser Welt.",
				"&bAtm.: &f%numtext%",
				"&bAtm. on the whitelist: &f%isonwhitelist%",
				"&cLeftclick &badds the player to the",
				"&bwhitelist of this storage.",
				"&cRightclick &badds the player to the",
				"&bwhitelist of all storages in the world.",
				"&cShift-Leftclick &bremoves the player",
				"&bfrom the whitelist of this storage.",
				"&cShift-Rightclick &bremoves the player",
				"&bfrom the whitelist of all storages in the world."});
		Language lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_WHITELIST.toString()});
		Language lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_WHITELIST_WORLD.toString()});
		Language lSLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_WHITELIST_REMOVE.toString()});
		Language lSRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_WHITELIST_REMOVE_WORLD.toString()});
		path = "0"; //PlayerHead
		keyboard_whitelist.put(path+".SettingLevel", lSL);
		keyboard_whitelist.put(path+".Material", lMat);
		keyboard_whitelist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {0}));
		keyboard_whitelist.put(path+".Lore", lLo_WL);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "1"; //PlayerHead
		keyboard_whitelist.put(path+".SettingLevel", lSL);
		keyboard_whitelist.put(path+".Material", lMat);
		keyboard_whitelist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {1}));
		keyboard_whitelist.put(path+".Lore", lLo_WL);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "2"; //PlayerHead
		keyboard_whitelist.put(path+".SettingLevel", lSL);
		keyboard_whitelist.put(path+".Material", lMat);
		keyboard_whitelist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {2}));
		keyboard_whitelist.put(path+".Lore", lLo_WL);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "3"; //PlayerHead
		keyboard_whitelist.put(path+".SettingLevel", lSL);
		keyboard_whitelist.put(path+".Material", lMat);
		keyboard_whitelist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {3}));
		keyboard_whitelist.put(path+".Lore", lLo_WL);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "4"; //PlayerHead
		keyboard_whitelist.put(path+".SettingLevel", lSL);
		keyboard_whitelist.put(path+".Material", lMat);
		keyboard_whitelist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {4}));
		keyboard_whitelist.put(path+".Lore", lLo_WL);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "5"; //PlayerHead
		keyboard_whitelist.put(path+".SettingLevel", lSL);
		keyboard_whitelist.put(path+".Material", lMat);
		keyboard_whitelist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {5}));
		keyboard_whitelist.put(path+".Lore", lLo_WL);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "6"; //PlayerHead
		keyboard_whitelist.put(path+".SettingLevel", lSL);
		keyboard_whitelist.put(path+".Material", lMat);
		keyboard_whitelist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {6}));
		keyboard_whitelist.put(path+".Lore", lLo_WL);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "7"; //PlayerHead
		keyboard_whitelist.put(path+".SettingLevel", lSL);
		keyboard_whitelist.put(path+".Material", lMat);
		keyboard_whitelist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {7}));
		keyboard_whitelist.put(path+".Lore", lLo_WL);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "8"; //PlayerHead
		keyboard_whitelist.put(path+".SettingLevel", lSL);
		keyboard_whitelist.put(path+".Material", lMat);
		keyboard_whitelist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {8}));
		keyboard_whitelist.put(path+".Lore", lLo_WL);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		guiKeys.put(GuiType.KEYBOARD_WHITELIST, keyboard_whitelist);
		//-------------
		Language lLo_M = new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&bZz.: &f%numtext%",
				"&bZz. ist ein Mitglied: &f%ismember%",
				"&cLinksklick &bfügt den Spieler",
				"&bzur Mitgliedern dieses Lagers hinzu.",
				"&cRechtsklick &bfügt den Spieler",
				"&bzur Mitgliedern aller Lagern dieser Welt hinzu.",
				"&cShift-Linksklick &bentfernt den",
				"&bSpieler von den Mitgliedern dieses Lagers.",
				"&cShift-Rechtsklick &bentfernt den Spieler",
				"&bvon den Mitgliedern aller Lagern dieser Welt.",
				"&bAtm.: &f%numtext%",
				"&bAtm. is a member: &f%ismember%",
				"&cLeftclick &badds the player to",
				"&bthe members of this storage.",
				"&cRightclick &badds the player to",
				"&bthe members of all storages in the world.",
				"&cShift-Leftclick &bremoves the",
				"&bplayer from the members of this storage.",
				"&cShift-Rightclick &bremoves the player",
				"&bfrom the members of all storages in the world."});
		lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_MEMBER.toString()});
		lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_MEMBER_WORLD.toString()});
		lSLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_MEMBER_REMOVE.toString()});
		lSRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_MEMBER_REMOVE_WORLD.toString()});
		path = "0"; //PlayerHead
		keyboard_member.put(path+".SettingLevel", lSL);
		keyboard_member.put(path+".Material", lMat);
		keyboard_member.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {0}));
		keyboard_member.put(path+".Lore", lLo_M);
		keyboard_member.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "1"; //PlayerHead
		keyboard_member.put(path+".SettingLevel", lSL);
		keyboard_member.put(path+".Material", lMat);
		keyboard_member.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {1}));
		keyboard_member.put(path+".Lore", lLo_M);
		keyboard_member.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "2"; //PlayerHead
		keyboard_member.put(path+".SettingLevel", lSL);
		keyboard_member.put(path+".Material", lMat);
		keyboard_member.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {2}));
		keyboard_member.put(path+".Lore", lLo_M);
		keyboard_member.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "3"; //PlayerHead
		keyboard_member.put(path+".SettingLevel", lSL);
		keyboard_member.put(path+".Material", lMat);
		keyboard_member.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {3}));
		keyboard_member.put(path+".Lore", lLo_M);
		keyboard_member.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "4"; //PlayerHead
		keyboard_member.put(path+".SettingLevel", lSL);
		keyboard_member.put(path+".Material", lMat);
		keyboard_member.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {4}));
		keyboard_member.put(path+".Lore", lLo_M);
		keyboard_member.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "5"; //PlayerHead
		keyboard_member.put(path+".SettingLevel", lSL);
		keyboard_member.put(path+".Material", lMat);
		keyboard_member.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {5}));
		keyboard_member.put(path+".Lore", lLo_M);
		keyboard_member.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "6"; //PlayerHead
		keyboard_member.put(path+".SettingLevel", lSL);
		keyboard_member.put(path+".Material", lMat);
		keyboard_member.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {6}));
		keyboard_member.put(path+".Lore", lLo_M);
		keyboard_member.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "7"; //PlayerHead
		keyboard_member.put(path+".SettingLevel", lSL);
		keyboard_member.put(path+".Material", lMat);
		keyboard_member.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {7}));
		keyboard_member.put(path+".Lore", lLo_M);
		keyboard_member.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "8"; //PlayerHead
		keyboard_member.put(path+".SettingLevel", lSL);
		keyboard_member.put(path+".Material", lMat);
		keyboard_member.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {8}));
		keyboard_member.put(path+".Lore", lLo_M);
		keyboard_member.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		guiKeys.put(GuiType.KEYBOARD_MEMBER, keyboard_member);
	}
	
	/*private void initGuiSearchBuy() //INFO:GuiSearchBuy
	{
		LinkedHashMap<String, Language> sbuy = new LinkedHashMap<>();
		sbuy.put("Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e%signshopname% &f- &e%owner%",
						"&e%signshopname% &f- &e%owner%"}));
		sbuy.put("Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"",
						"&eItems: &f%itemstoragecurrent% / %itemstoragetotal%",
						"&eKosten: &f%buyraw1%",
						"",
						"&eItems: &f%itemstoragecurrent% / %itemstoragetotal%",
						"&eCosts: &f%buyraw1%",}));
		sbuy.put("ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SEARCH_BUY.toString()}));
		sbuy.put("ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SEARCH_BUY.toString()}));
		guiKeys.put(GuiType.SEARCH_BUY, sbuy);
	}
	
	private void initGuiSearchSell() //INFO:GuiSearchSell
	{
		LinkedHashMap<String, Language> ssell = new LinkedHashMap<>();
		ssell.put("Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e%signshopname% &f- &e%owner%",
						"&e%signshopname% &f- &e%owner%"}));
		ssell.put("Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"",
						"&eItems: &f%itemstoragecurrent% / %itemstoragetotal%",
						"&eKosten: &f%sellraw1%",
						"",
						"&eItems: &f%itemstoragecurrent% / %itemstoragetotal%",
						"&eCosts: &f%sellraw1%",}));
		ssell.put("ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SEARCH_SELL.toString()}));
		ssell.put("ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SEARCH_SELL.toString()}));
		guiKeys.put(GuiType.SEARCH_SELL, ssell);
	}
	
	private void initGuiSubscribe() //INFO:GuiSubscribe
	{
		LinkedHashMap<String, Language> subs = new LinkedHashMap<>();
		subs.put("Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e%signshopname% &f- &e%owner%",
						"&e%signshopname% &f- &e%owner%"}));
		subs.put("Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"",
						"&eMaterial: &f%material%",
						"&eDisplayname: &f%displayname%",
						"&eItems: &f%itemstoragecurrent% / %itemstoragetotal%",
						"&eKaufkosten: &f%buyraw1%",
						"&eVerkaufskosten: &f%sellraw1%",
						"",
						"&eMaterial: &f%material%",
						"&eDisplayname: &f%displayname%",
						"&eItems: &f%itemstoragecurrent% / %itemstoragetotal%",
						"&eBuycosts: &f%buyraw1%",
						"&eSellcosts: &f%sellraw1%"}));
		subs.put("ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SUBSCRIBED.toString()}));
		subs.put("ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SUBSCRIBED.toString()}));
		String path = "45"; //Past
		subs.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.PLAYER_HEAD.toString()}));
		subs.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"http://textures.minecraft.net/texture/e35e42fc7060c223acc965f7c5996f272644af40a4723a372f5903f8e9f188e7"}));
		subs.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7Klicke hier für die vorherige Seite!",
						"&7Click here for the past page!"}));
		subs.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SUBSCRIBED_PAST.toString()}));
		subs.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SUBSCRIBED_PAST.toString()}));
		path = "53"; //Next
		subs.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.PLAYER_HEAD.toString()}));
		subs.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"http://textures.minecraft.net/texture/aee0f82fb33f6cfa5169b9f5eafe4dc1c73618c9783b131adada411d8f605505"}));
		subs.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7Klicke hier für die nächste Seite!",
						"&7Click here for the next page!"}));
		subs.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SUBSCRIBED_NEXT.toString()}));
		subs.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SUBSCRIBED_NEXT.toString()}));
		guiKeys.put(GuiType.SUBSCIBED, subs);
	}*/
}