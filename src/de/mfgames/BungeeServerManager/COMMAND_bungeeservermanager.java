/*
 * BungeeServerAdmin v1.1
 * 
 * Copyright (C) 2019 TGamesTV
 * Copyright (C) 2019 DavidoTek
 * 
 * Licensed under MIT
 * 
 * */
package de.mfgames.BungeeServerManager;

import java.util.Collection;
import java.io.File;
import java.io.IOException;

import net.kronos.rkon.core.Rcon;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class COMMAND_bungeeservermanager extends Command {
	public COMMAND_bungeeservermanager(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		/* Check if sender has the permissions (print message and return if not) */
		if (args.length > 0) {
			if (!sender.hasPermission("bungeeserveradmin." + args[0].toLowerCase())) {
				/* Check if sender has server-specific permissions */
				if (args.length >= 2) {
					if (!sender.hasPermission("bungeeserveradmin." + args[0].toLowerCase() + "." + args[1].toLowerCase())) {
						sender.sendMessage(new TextComponent("§cYou do not have the permissions to execute this command!"));
						return;
					}
				} else {
					sender.sendMessage(new TextComponent("§cYou do not have the permissions to execute this command!"));
					return;
				}
			}
		}
		
		/* Check sub-command */
		if (args.length > 0) {
			switch(args[0].toLowerCase()) {
			default:
			case "help":
				showHelp(sender);
				break;
			case "cmd":
				executeCmd(sender, args);
				break;
			case "start":
			case "stop":
			case "restart":
				controlServer(sender, args);
				break;
			case "list":
				if(args.length > 1) {
					listServers(sender, Integer.parseInt(args[1]));
				} else {
					listServers(sender, 0);
				}
				break;
			case "reload":
				reloadPlugin(sender);
				break;
			}
			
		} else {
			showHelp(sender);
		}
	}
	
	/*
	 * showHelp handles "/bsm help"
	 * Shows help
	 * */
	private void showHelp(CommandSender sender) {
		sender.sendMessage(new TextComponent("§6§l ==== BUNGEE SERVER ADMIN " + BungeeServerManager.pver + " ==== "));
		sender.sendMessage(new TextComponent("§6/bsa help§r - Shows this"));
		sender.sendMessage(new TextComponent("§6/bsa list - Lists all servers"));
		sender.sendMessage(new TextComponent("§6/bsa cmd <SERVER> <COMMAND> [<ARGUMENTS>]§r - Execute command"));
		sender.sendMessage(new TextComponent("§6/bsa start <SERVER>§r - Start the given server"));
		sender.sendMessage(new TextComponent("§6/bsa stop <SERVER>§r - Stop the given server"));
		sender.sendMessage(new TextComponent("§6/bsa restart <SERVER>§r - Restart the given server"));
		sender.sendMessage(new TextComponent("§6/bsa reload§r - Reloads the plugin/configuration"));
	}
	
	/*
	 * executeCmd handles "/bsm cmd <SERVER> <COMMAND> [<ARGUMENTS>]"
	 * Executes command on given server
	 * */
	private void executeCmd(CommandSender sender, String[] args) {
		if (args.length >= 3) {
			String serverAddress = BungeeServerManager.getInstance().getConfiguration().getString("servers." + args[1] + ".addr");
			int serverPort = BungeeServerManager.getInstance().getConfiguration().getInt("servers." + args[1] + ".port");
			String serverPassword = BungeeServerManager.getInstance().getConfiguration().getString("servers." + args[1] + ".password");
			try {
				Rcon rcon = new Rcon(serverAddress, serverPort, serverPassword.getBytes());
				String command = args[2];
				for (int i = 3; i < args.length; i++) {
					command += " " + args[i];
				}
				String result = rcon.command(command);
				sender.sendMessage(new TextComponent("[BSA] Server \"" + args[1] + "\": " + result));
			} catch (Exception e) {
				e.printStackTrace();
				sender.sendMessage(new TextComponent("§cAn error occured!"));
			}
		} else {
			sender.sendMessage(new TextComponent("§c/bsa cmd <SERVER> <COMMAND> [<ARGUMENTS>]"));
			showHelp(sender);
		}
	}
	
	/*
	 * controlServer handles "/bsm start <SERVER>", "/bsm stop <SERVER>" and "/bsm restart <SERVER>"
	 * Starts/stops/restarts the given server
	 * */
	private void controlServer(CommandSender sender, String[] args) {
		if (args.length == 2) {
			switch (args[0]) {
			case "start":
				startServer(sender, args[1]);
				break;
			case "stop":
				stopServer(sender, args[1]);
				break;
			case "restart":
				startServer(sender, args[1]);
				stopServer(sender, args[1]);
				break;
			default:
				sender.sendMessage(new TextComponent("§c/bsa <start/stop/restart> <SERVER>"));
				showHelp(sender);
			}
		}
	}
	
	public static void startServer(CommandSender sender, String servername) {
		String startScript = BungeeServerManager.getInstance().getConfiguration().getString("servers." + servername + ".startscript");
		String serverDir = BungeeServerManager.getInstance().getConfiguration().getString("servers." + servername + ".serverdir");
		try {
			ProcessBuilder pb = new ProcessBuilder(startScript);
			pb.directory(new File(serverDir));
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
			sender.sendMessage(new TextComponent("§cAn error occured!"));
		}
	}
	
	public static void stopServer(CommandSender sender, String servername) {
		String serverAddress = BungeeServerManager.getInstance().getConfiguration().getString("servers." + servername + ".addr");
		int serverPort = BungeeServerManager.getInstance().getConfiguration().getInt("servers." + servername + ".port");
		String serverPassword = BungeeServerManager.getInstance().getConfiguration().getString("servers." + servername + ".password");
		try {
			Rcon rcon = new Rcon(serverAddress, serverPort, serverPassword.getBytes());
			String result = rcon.command("stop");
			sender.sendMessage(new TextComponent("Server +\"" + servername + "\": " + result + " (Stopping...)"));
		} catch (Exception e) {
			e.printStackTrace();
			sender.sendMessage(new TextComponent("§cAn error occured!"));
		}
	}
	
	/*
	 * listServers prints all configured servers to the sender
	 */
	private void listServers(CommandSender sender, int page) {
		Collection<String> servers = BungeeServerManager.getInstance().getConfiguration().getSection("servers").getKeys();
		sender.sendMessage(new TextComponent("§6 ==== SERVERS - PAGE " + ((int)page + (int)1) + "/" + (int)(servers.size() / 8 + 1) + " ===="));
		String a[] = new String[servers.size()]; 
		a = servers.toArray(a);
		for ( int i = 0; i < 8; i++) {
			if ( (i + page * 8) >= servers.size()) {
				break;
			}
			String s = a[i + page * 8];
			sender.sendMessage(new TextComponent("§6" + s));
		}
		sender.sendMessage(new TextComponent("§6 =========================="));
	}
	
	/*
	 * reloadPlugin handles "/bsm reload"
	 * Reloads the plugin / reloads the configuration
	 */
	private void reloadPlugin(CommandSender sender) {
		BungeeServerManager.getInstance().loadConfiguration();
		sender.sendMessage(new TextComponent("§aConfiguration reloaded"));
	}
}