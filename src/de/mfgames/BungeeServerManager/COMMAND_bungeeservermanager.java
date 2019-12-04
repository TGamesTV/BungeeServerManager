package de.mfgames.BungeeServerManager;

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
		if (!sender.hasPermission("bungeeservermanager." + args[0].toLowerCase())) {
			sender.sendMessage(new TextComponent("§cYou do not have the permissions to execute this command!"));
			return;
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
		sender.sendMessage(new TextComponent("§6 ==== BUNGEE SERVER MANAGER " + BungeeServerManager.pver + " ==== "));
		sender.sendMessage(new TextComponent("§6§l/bsm help$r§6 - Shows this"));
		sender.sendMessage(new TextComponent("§6§l/bsm cmd <SERVER> <COMMAND> [<ARGUMENTS>]$r§6 - Execute command on server"));
		sender.sendMessage(new TextComponent("§6§l/bsm start <SERVER>$r§6 - Start the given server"));
		sender.sendMessage(new TextComponent("§6§l/bsm stop <SERVER>$r§6 - Stop the given server"));
		sender.sendMessage(new TextComponent("§6§l/bsm restart <SERVER>$r§6 - Restart the given server"));
		sender.sendMessage(new TextComponent("§6§l/bsm reload$r§6 - Reloads the plugin/configuration"));
	}
	
	/*
	 * executeCmd handles "/bsm cmd <SERVER> <COMMAND> [<ARGUMENTS>]"
	 * Executes command on given server
	 * */
	private void executeCmd(CommandSender sender, String[] args) {
		if (args.length >= 3) {
			
		} else {
			sender.sendMessage(new TextComponent("§c/bsm cmd <SERVER> <COMMAND> [<ARGUMENTS>]"));
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
				// Start <SERVER>
				break;
			case "stop":
				// Stop <SERVER>
				break;
			case "restart":
				// Restart <SERVER>
				break;
			default:
				sender.sendMessage(new TextComponent("§c/bsm <start/stop/restart> <SERVER>"));
				showHelp(sender);
			}
		}
	}
	
	/*
	 * reloadPlugin handles "/bsm reload"
	 * Reloads the plugin / reloads the configuration
	 */
	private void reloadPlugin(CommandSender sender) {
		BungeeServerManager.getInstance().loadConfiguration();
	}
}