/*
 * BungeeServerAdmin
 * 
 * Copyright (C) 2019 TGamesTV
 * Copyright (C) 2019 DavidoTek
 * 
 * Licensed under MIT
 * 
 * */
package de.mfgames.BungeeServerManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collection;
import java.util.concurrent.Callable;

import de.mfgames.BungeeServerManager.Metrics;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BungeeServerManager extends Plugin {

	public BungeeServerManager() {
		bsmInstance = this;
	}
	
	static BungeeServerManager bsmInstance;
	
	/*
	 * getInstance returns BungeeServerManager instance
	 */
	public static BungeeServerManager getInstance() {
		return bsmInstance;
	}
	
	
	public static String pver = "1.6";	/* Plugin Version */
	public static String pname = "BungeeServerAdmin";	/* Plugin Name */
	
	Configuration configuration;
	
	@Override
	public void onEnable() {
		registerCommands();
		loadConfiguration();
		Collection<String> servers = BungeeServerManager.getInstance().getConfiguration().getSection("servers").getKeys();
		String a[] = new String[servers.size()];
		a = servers.toArray(a);
		for (String s : a) {
			if (getInstance().getConfiguration().getBoolean("servers." + s + ".active", false)) {
				System.out.println("[" + pname + " " + pver + "] Starting server \"" + s + "\"");
				COMMAND_bungeeservermanager.startServer(null, s);
			}
		}
		
		/* bStats */
		if (getConfiguration().getBoolean("bstats-enable", true)) {
			Metrics metrics = new Metrics(this);
			metrics.addCustomChart(new Metrics.SingleLineChart("num_servers", new Callable<Integer>() {
		        @Override
		        public Integer call() throws Exception {
		            return getConfiguration().getSection("servers").getKeys().size();
		        }
		    }));
		}

		System.out.println("[" + pname + " " + pver + "] Plugin enabled!");
	}
	
	@Override
	public void onDisable() {
		Collection<String> servers = BungeeServerManager.getInstance().getConfiguration().getSection("servers").getKeys();
		String a[] = new String[servers.size()];
		a = servers.toArray(a);
		for (String s : a) {
			if (getInstance().getConfiguration().getBoolean("servers." + s + ".active", false) || getInstance().getConfiguration().getBoolean("servers." + s + ".always-stop", false)) {
				System.out.println("[" + pname + " " + pver + "] Stopping server \"" + s + "\"");
				COMMAND_bungeeservermanager.stopServer(null, s);
			}
		}
		System.out.println("[" + pname + " " + pver + "] Plugin disabled!");
	}
	
	/*
	 * registerCommands registers the commands at Bungeecord
	 */
	public void registerCommands() {
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new COMMAND_bungeeservermanager("bungeeserveradmin"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new COMMAND_bungeeservermanager("bsa"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new COMMAND_bungeeserverstats("bungeeserverstats"));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new COMMAND_bungeeserverstats("bss"));
	}
	
	/*
	 * getConfiguration returns the configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}
	
	/*
	 * loadConfiguration loads the configuration file
	 */
	public void loadConfiguration() {
		try {
			if(!getDataFolder().exists())
				getDataFolder().mkdir();
			
			File file = new File(getDataFolder(), "config.yml");
			
			if (!file.exists()) {
				InputStream in = getResourceAsStream("config.yml");
                Files.copy(in, file.toPath());
	        }

			configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/*
	 * saveConfiguration saves the configuration to the configuration file
	 */
	public void saveConfiguration() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(getDataFolder(), "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
