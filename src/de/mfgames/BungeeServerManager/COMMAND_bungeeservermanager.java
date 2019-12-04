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
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("help")) {
				showHelp(sender);
			}
		} else {
			showHelp(sender);
		}
	}
	
	private void showHelp(CommandSender sender) {
		sender.sendMessage(new TextComponent("§6 ==== BUNGEE SERVER MANAGER " + BungeeServerManager.pver + " ==== "));
		sender.sendMessage(new TextComponent("§6 §l/bsm help$r§6 - Shows this"));
	}
}
