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

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeServerManager extends Plugin {

	public static String pver = "1.0";	/* Plugin Version */
	
	@Override
	public void onEnable() {
		registerCommands();
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public void registerCommands() {
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new COMMAND_bungeeservermanager("bungeeservermanager"));
	}
}
