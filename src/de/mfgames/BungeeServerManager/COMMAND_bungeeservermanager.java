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

import java.util.ArrayList;
import java.util.Collection;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import net.kronos.rkon.core.Rcon;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class COMMAND_bungeeservermanager extends Command {
	public COMMAND_bungeeservermanager(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		/* Check if sender has the permissions (print message and return if not) */
		if (args.length > 0) {
			if (!sender.hasPermission("bungeeserveradmin." + args[0].toLowerCase()) && !sender.hasPermission("bungeeserveradmin.*")) {
				/* Check if sender has server-specific permissions */
				if (args.length >= 2) {
					if (!sender.hasPermission("bungeeserveradmin." + args[0].toLowerCase() + "." + args[1].toLowerCase())
							&& !sender.hasPermission("bungeeserveradmin." + args[0].toLowerCase() + ".*")) {
						sender.sendMessage(new TextComponent("ï¿½cYou do not have the permissions to execute this command!"));
						return;
					}
				} else {
					sender.sendMessage(new TextComponent("ï¿½cYou do not have the permissions to execute this command!"));
					return;
				}
			}

			switch(args[0].toLowerCase()) {
			default:
			case "help":
				showHelp(sender);
				break;
			case "cmd":
				handleMultiple(sender, args, 1);
				break;
			case "start":
			case "stop":
			case "restart":
				handleMultiple(sender, args, 2);
				break;
			case "list":
				if(args.length > 1) {
					try {
						listServers(sender, Integer.parseInt(args[1]));
					} catch (NumberFormatException e) {
						sender.sendMessage(new TextComponent("ï¿½cPage must be a number!"));
					}
				} else {
					listServers(sender, 1);
				}
				break;
			case "reload":
				reloadPlugin(sender);
				break;
			case "add":
				addServer(sender, args);
				break;
			case "remove":
				removeServer(sender, args);
				break;
			}
			
		} else {
			showHelp(sender);
		}
	}
	
	/*
	 * handleMultiple handles the Server argument 'allservers' to execute the command on all servers.
	 */
	private void handleMultiple(CommandSender sender, String[] args, int cmd) {
		if (args[1].equalsIgnoreCase("allservers")) {
			Collection<String> servers = BungeeServerManager.getInstance().getConfiguration().getSection("servers").getKeys();
			for (String s : servers) {
				switch (cmd) {
				case 1:
					args[1] = s;
					executeCmd(sender, args);
					break;
				case 2:
					args[1] = s;
					controlServer(sender, args);
					break;
				}
			}
		} else {
			switch (cmd) {
			case 1:
				executeCmd(sender, args);
				break;
			case 2:
				controlServer(sender, args);
				break;
			}
		}
	}
	
	/*
	 * showHelp handles "/bsm help"
	 * Shows help
	 * */
	private void showHelp(CommandSender sender) {
		sender.sendMessage(new TextComponent("ï¿½6ï¿½l ==== BUNGEE SERVER ADMIN " + BungeeServerManager.pver + " ==== "));
		sender.sendMessage(new TextComponent("ï¿½6/bsa helpï¿½r - Shows this"));
		sender.sendMessage(new TextComponent("ï¿½6/bsa list - Lists all servers"));
		sender.sendMessage(new TextComponent("ï¿½6/bsa cmd <SERVER> <COMMAND> [<ARGUMENTS>]ï¿½r - Execute command"));
		sender.sendMessage(new TextComponent("ï¿½6/bsa start <SERVER>ï¿½r - Start the given server"));
		sender.sendMessage(new TextComponent("ï¿½6/bsa stop <SERVER>ï¿½r - Stop the given server"));
		sender.sendMessage(new TextComponent("ï¿½6/bsa restart <SERVER>ï¿½r - Restart the given server"));
		sender.sendMessage(new TextComponent("ï¿½6/bsa reloadï¿½r - Reloads the plugin/configuration"));
		sender.sendMessage(new TextComponent("ï¿½6/bsa add <name> <addr> <rconport> <passwd> <dir> <script> <active> <always-stop> ï¿½r - Add server"));
		sender.sendMessage(new TextComponent("ï¿½6/bsa remove <name> <repeat name>ï¿½r - Remove the server"));
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
				sender.sendMessage(new TextComponent("ï¿½6Server ï¿½a" + args[1] + "ï¿½6: " + result));
				rcon.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
				sender.sendMessage(new TextComponent("ï¿½cAn error occured!"));
			}
		} else {
			sender.sendMessage(new TextComponent("ï¿½c/bsa cmd <SERVER> <COMMAND> [<ARGUMENTS>]"));
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
				sender.sendMessage(new TextComponent("ï¿½c/bsa <start/stop/restart> <SERVER>"));
				showHelp(sender);
			}
		}
	}
	
	public static void startServer(CommandSender sender, String servername) {
		for (String server : BungeeServerManager.getInstance().getConfiguration().getSection("servers").getKeys()) {
			if (server.equals(servername)) {
				String startScript = BungeeServerManager.getInstance().getConfiguration().getString("servers." + servername + ".startscript");
				String serverDir = BungeeServerManager.getInstance().getConfiguration().getString("servers." + servername + ".serverdir");
				try {
					ProcessBuilder pb = new ProcessBuilder(startScript);
					pb.directory(new File(serverDir));
					pb.start();
					if (sender != null) {
						sender.sendMessage(new TextComponent("ï¿½6Server ï¿½a" + servername + "ï¿½6: Starting..."));
					}
				} catch (IOException e) {
					e.printStackTrace();
					sender.sendMessage(new TextComponent("ï¿½cCould not start ï¿½6" + servername + "ï¿½c!"));
				}
				return;
			} else if (server.equalsIgnoreCase(servername)) {
				sender.sendMessage(new TextComponent("ï¿½cServer ï¿½6" + servername + "ï¿½c not found! Did you mean ï¿½6" + server + "ï¿½c?"));
				return;
			}
		}
		
		sender.sendMessage(new TextComponent("ï¿½cServer " + servername + " not found! Type ï¿½6/bsa list ï¿½cfor a list of servers."));
	}
	
	public static void stopServer(CommandSender sender, String servername) {
		for (String server : BungeeServerManager.getInstance().getConfiguration().getSection("servers").getKeys()) {
			if (server.equals(servername)) {
				String serverAddress = BungeeServerManager.getInstance().getConfiguration().getString("servers." + servername + ".addr");
				int serverPort = BungeeServerManager.getInstance().getConfiguration().getInt("servers." + servername + ".port");
				String serverPassword = BungeeServerManager.getInstance().getConfiguration().getString("servers." + servername + ".password");
				try {
					Rcon rcon = new Rcon(serverAddress, serverPort, serverPassword.getBytes());
					String result = rcon.command("stop");
					if (sender != null) {
						sender.sendMessage(new TextComponent("ï¿½6Server ï¿½a" + servername + "ï¿½6: " + result + " (Stopping...)"));
					}
					rcon.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
					sender.sendMessage(new TextComponent("ï¿½cCould not stop ï¿½6" + servername + "ï¿½c!"));
				}
				return;
			} else if (server.equalsIgnoreCase(servername)) {
				sender.sendMessage(new TextComponent("ï¿½cServer ï¿½6" + servername + "ï¿½c not found! Did you mean ï¿½6" + server + "ï¿½c?"));
				return;
			}
		}

		sender.sendMessage(new TextComponent("ï¿½cServer " + servername + " not found! Type ï¿½6/bsa list ï¿½cfor a list of servers."));
	}
	
	/*
	 * listServers prints all configured servers to the sender
	 */
	private void listServers(CommandSender sender, int page) {
		Collection<String> servers = BungeeServerManager.getInstance().getConfiguration().getSection("servers").getKeys();
		
		
		int pages = (servers.size() - 1) / 8 + 1;
		
		if (page > pages) {
			sender.sendMessage(new TextComponent("ï¿½cThere are ï¿½6" + pages + "ï¿½c pages. You want page ï¿½6" + page + "ï¿½c!"));
			return;
		}
		
		if (page < 1) {
			sender.sendMessage(new TextComponent("ï¿½cSorry. There is no page before page 1!"));
			return;
		}


		String a[] = new String[servers.size()];
		a = servers.toArray(a);

		ArrayList<String> listedServers = new ArrayList<>();	/* All servers to list */

		for (int i = (page - 1) * 8; i < page * 8; i++) {
			if (i == a.length) {
				break;
			}
			
			String s = a[i];
			listedServers.add(s);
			
		}

		HashMap<String, Boolean> states = new HashMap<>();	/* Name, State*/
		
		for (String s : listedServers) {
			String serverAddress = BungeeServerManager.getInstance().getConfiguration().getString("servers." + s + ".addr");
			int serverPort = BungeeServerManager.getInstance().getConfiguration().getInt("servers." + s + ".port");

			new ServerState(states, s, serverAddress, serverPort, sender, listedServers, page, pages);
		}
	}
	
	/*
	 * reloadPlugin handles "/bsm reload"
	 * Reloads the plugin / reloads the configuration
	 */
	private void reloadPlugin(CommandSender sender) {
		BungeeServerManager.getInstance().loadConfiguration();
		sender.sendMessage(new TextComponent("ï¿½aConfiguration reloaded"));
	}
	
	/*
	 * addServer adds the given server to the configuration
	 * Syntax: /bsa add <name> <addr> <rconport> <passwd> <dir> <script> <active> <always-stop>
	 */
	private void addServer(CommandSender sender, String[] args) {
		if (args.length != 9) {
			showHelp(sender);
			return;
		}
		
		String serverName = args[1];
		String serverAddr = args[2];
		int rconport = 0;
		try {
			rconport = Integer.parseInt(args[3]);
		} catch (NumberFormatException e) {
			sender.sendMessage(new TextComponent("ï¿½cRcon port must be a number!"));
			return;
		}
		String serverPw = args[4];
		String serverDir = args[5];
		String serverScript = args[6];
		boolean serverActive = args[7].equalsIgnoreCase("true") ? true : false;
		boolean serverAlwStop = args[8].equalsIgnoreCase("true") ? true : false;
		
		
		Configuration config = BungeeServerManager.getInstance().getConfiguration();

		config.set("servers." + serverName + ".addr", serverAddr);
		config.set("servers." + serverName + ".port", rconport);
		config.set("servers." + serverName + ".password", serverPw);
		config.set("servers." + serverName + ".serverdir", serverDir);
		config.set("servers." + serverName + ".startscript", serverScript);
		config.set("servers." + serverName + ".active", serverActive);
		config.set("servers." + serverName + ".always-stop", serverAlwStop);
		
		BungeeServerManager.getInstance().saveConfiguration();
	}

	/*
	 * removeServer removes the given server from the configuration
	 * Syntax: /bsa remove <name>
	 */
	private void removeServer(CommandSender sender, String[] args) {
		if (args.length != 3) {
			showHelp(sender);
			return;
		}
		
		String serverName = args[1];
		String nameRepeat = args[2];
		
		if (!serverName.equals(nameRepeat)) {
			sender.sendMessage(new TextComponent("ï¿½c\"" + serverName + "\" doesn't equal \"" + nameRepeat + "\"!"));
			return;
		}

		Configuration config = BungeeServerManager.getInstance().getConfiguration();
		
		config.set("servers." + serverName, null);
		
		BungeeServerManager.getInstance().saveConfiguration();
	}
}
