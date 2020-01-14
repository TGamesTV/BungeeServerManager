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
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class ServerState {
    public ServerState(HashMap<String, Boolean> servers, String name, String ip, int port) {
        new Thread(new Runnable() {
            public void run() {
                boolean open = false;
                try {
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

                servers.put(name, open);
            }
        }).start();
    }
}
