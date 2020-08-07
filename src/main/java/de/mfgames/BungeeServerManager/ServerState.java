/*
 * ServerState class
 *
 * Copyright (C) 2019 DavidoTek
 *
 * Licensed under MIT
 *
 * */

package de.mfgames.BungeeServerManager;

import javax.net.SocketFactory;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerState {
    public ServerState(HashMap<String, Boolean> servers, String name, String ip, int port, CommandSender sender, ArrayList<String> listedServers, int page, int pages) {
        new Thread(new Runnable() {
            public void run() {
                boolean open = false;
                try {	/* Test if port is open */
                    open = true;
                    Socket socket = SocketFactory.getDefault().createSocket();
                    try {
                        socket.setSoTimeout(5000);
                        socket.connect(new InetSocketAddress(ip, port));
                        socket.close();
                    } catch (ConnectException e) {
                        open = false;
                    }
                } catch (UnknownHostException e) {
                    System.out.println("Unknown host: " + ip + ":" + port);
                } catch (IOException e) {
                    System.out.println("IOException while connecting to " + ip + ":" + port);
                }

                servers.put(name, open);	/* Add server state to HashMap */

                if (servers.size() >= listedServers.size()) {	/* All servers checked? */
                    sender.sendMessage(new TextComponent("§6 ==== SERVERS - PAGE " + page + "/" + pages + " ===="));

                    for (String s : listedServers) {
                        if (servers.get(s)) {
                            sender.sendMessage(new TextComponent("§6" + s + " §r[§aONLINE§r]"));
                        } else {
                            sender.sendMessage(new TextComponent("§6" + s + " §r[§cOFFLINE§r]"));
                        }
                    }

                    sender.sendMessage(new TextComponent("§6 ============================"));
                }
            }
        }).start();
    }
}
