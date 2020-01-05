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

import java.io.IOException;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class COMMAND_bungeeserverstats extends Command {

	public COMMAND_bungeeserverstats(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
			
		if (args.length == 0) {	// Show system stats
			/* Check if sender has the permissions (print message and return if not) */
			if (!sender.hasPermission("bungeeserveradmin.stats") && !sender.hasPermission("bungeeserveradmin.*")) {
				sender.sendMessage(new TextComponent("§cYou do not have the permissions to execute this command!"));
				return;
			}
			
			OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
			
			sender.sendMessage(new TextComponent("§6§l ==== SYSTEM INFORMATION ==== "));
			sender.sendMessage(new TextComponent("§6OS: " + System.getProperty("os.name") + " " + System.getProperty("os.arch")));
			sender.sendMessage(new TextComponent("§6CPU: " + operatingSystemMXBean.getProcessCpuLoad()));
			sender.sendMessage(new TextComponent("§6JVM: " + System.getProperty("java.vendor") + " " + System.getProperty("java.version")));
			sender.sendMessage(new TextComponent("§6JVM Memory: " + (Runtime.getRuntime().totalMemory() / 1024 / 1024) + "M"));
			// don't return: Show info
		} else if (args.length == 2 && args[0].equalsIgnoreCase("cmd")) {	// Execute command on system console
			String command = args[1];
			
			for (String cmd : BungeeServerManager.getInstance().getConfiguration().getSection("system-commands").getKeys()) {
				if (cmd.equals(command)) {	// Command was found
					String commandPerm = BungeeServerManager.getInstance().getConfiguration().getString("system-commands." + command + ".permission");
					
					if(!sender.hasPermission(commandPerm)) {
						sender.sendMessage(new TextComponent("§cYou do not have the permissions to execute this command!"));
						return;
					}
					
					String commandExec = BungeeServerManager.getInstance().getConfiguration().getString("system-commands." + command + ".execute");
					try {
						ProcessBuilder pb = new ProcessBuilder(commandExec);
						pb.start();
					} catch (IOException e) {
						e.printStackTrace();
						sender.sendMessage(new TextComponent("§cAn error occured!"));
					}
					return;
				} else if (cmd.equalsIgnoreCase(command)) {	// Similar command was found
					sender.sendMessage(new TextComponent("§cCommand §6" + command + "§c not found! Did you mean §6" + cmd + "§c?"));
					return;
				}
			}

			sender.sendMessage(new TextComponent("§cCommand §6" + command + "§c not found!"));
			
			return;
		}

		sender.sendMessage(new TextComponent("§aTo execute system command: /bss cmd <command>"));
	}

}
