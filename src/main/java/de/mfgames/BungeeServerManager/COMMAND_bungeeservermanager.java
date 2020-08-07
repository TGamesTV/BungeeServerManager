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
                        sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(1, "§cYou do not have the permissions to execute this command!")));
                        return;
                    }
                } else {
                    sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(1, "§cYou do not have the permissions to execute this command!")));
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
                            sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(3, "§cPage must be a number!")));
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
        sender.sendMessage(new TextComponent("§6§l ==== BUNGEE SERVER ADMIN " + BungeeServerManager.pver + " ==== "));
        sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(4, "§6/bsa help§r - Shows this")));
        sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(5, "§6/bsa list - Lists all servers")));
        sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(6, "§6/bsa cmd <SERVER> <COMMAND> [<ARGUMENTS>]§r - Execute command")));
        sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(7, "§6/bsa start <SERVER>§r - Start the given server")));
        sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(8, "§6/bsa stop <SERVER>§r - Stop the given server")));
        sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(9, "§6/bsa restart <SERVER>§r - Restart the given server")));
        sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(10, "§6/bsa reload§r - Reloads the plugin/configuration")));
        sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(11, "§6/bsa add <name> <addr> <rconport> <passwd> <dir> <script> <active> <always-stop> §r - Add server")));
        sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(12, "§6/bsa remove <name> <repeat name>§r - Remove the server")));
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
                sender.sendMessage(new TextComponent("§6Server §a" + args[1] + "§6: " + result));
                rcon.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(2, "§cAn error occured!")));
            }
        } else {
            sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(14, "§c/bsa cmd <SERVER> <COMMAND> [<ARGUMENTS>]")));
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
                    sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(15, "§c/bsa <start/stop/restart> <SERVER>")));
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
                        sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(17, "§6Server §a%1§6: Starting..." , servername)));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(18, "§cCould not start §6%1§c!" , servername)));
                }
                return;
            } else if (server.equalsIgnoreCase(servername)) {
                sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(16, "§cServer §6%1§c not found! Did you mean §6%2§c?", servername, server)));
                return;
            }
        }

        sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(19, "§cServer %1 not found! Type §6/bsa list §cfor a list of servers.", servername)));
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
                        sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(20, "§6Server §a%1§6: %2 (Stopping...)", servername, result)));
                    }
                    rcon.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(21, "§cCould not stop §6%1§c!", servername)));
                }
                return;
            } else if (server.equalsIgnoreCase(servername)) {
                sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(16, "§cServer §6%1§c not found! Did you mean §6%2§c?", servername, server)));
                return;
            }
        }

        sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(19, "§cServer %1 not found! Type §6/bsa list §cfor a list of servers.", servername)));
    }

    /*
     * listServers prints all configured servers to the sender
     */
    private void listServers(CommandSender sender, int page) {
        Collection<String> servers = BungeeServerManager.getInstance().getConfiguration().getSection("servers").getKeys();


        int pages = (servers.size() - 1) / 8 + 1;

        if (page > pages) {
            sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(22, "§cThere are §6%1§c pages. You want page §6%2§c!", pages, page)));
            return;
        }

        if (page < 1) {
            sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(23, "§cSorry. There is no page before page 1!")));
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
        sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(24, "§aConfiguration reloaded")));
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
            sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(25, "§cRcon port must be a number!")));
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
            sender.sendMessage(new TextComponent(BungeeServerManager.getInstance().translator.translateString(26, "§c\"%1\" doesn't equal \"%2\"!", serverName, nameRepeat)));
            return;
        }

        Configuration config = BungeeServerManager.getInstance().getConfiguration();

        config.set("servers." + serverName, null);

        BungeeServerManager.getInstance().saveConfiguration();
    }
}
