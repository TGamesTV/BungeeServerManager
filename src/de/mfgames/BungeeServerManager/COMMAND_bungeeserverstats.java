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


import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class COMMAND_bungeeserverstats extends Command {

	public COMMAND_bungeeserverstats(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		/* Check if sender has the permissions (print message and return if not) */
		if (!sender.hasPermission("bungeeservermanager.stats")) {
			sender.sendMessage(new TextComponent("§cYou do not have the permissions to execute this command!"));
			return;
		}
		
		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		
		sender.sendMessage(new TextComponent("§6§l ==== SYSTEM STATISTICS ==== "));
		sender.sendMessage(new TextComponent("§6System CPU usage: " + operatingSystemMXBean.getSystemLoadAverage()));
		
	}

}
