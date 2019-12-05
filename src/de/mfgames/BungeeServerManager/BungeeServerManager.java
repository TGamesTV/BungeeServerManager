/*
 * BungeeServerManager v1.0
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
	
	
	public static String pver = "1.0";	/* Plugin Version */
	
	Configuration configuration;
	
	@Override
	public void onEnable() {
		registerCommands();
		loadConfiguration();
	}
	
	@Override
	public void onDisable() {
		
	}
	
	/*
	 * registerCommands registers the commands at Bungeecord
	 */
	public void registerCommands() {
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new COMMAND_bungeeservermanager("bungeeservermanager"));
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
