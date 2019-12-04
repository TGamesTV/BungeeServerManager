# BungeeServerManager

## Deutsch
BungeeServerManager ist ein Plugin zur Verwaltung von Minecraft Servers in einem BungeeCord-Netzwerk.  
Die Server können über Commands gestartet/gestoppt/neugestartet werden.
Zudem können Befehle auf den Minecraft Servern via rcon ausgeführt werden.

### Befehle
 - /bsm help - Zeigt die Hilfe an
 - /bsm reload - Läd die Config neu
 - /bsm \<start/stop\> \<SERVER\> - Startet/Stoppt den angegebenen Server
 - /bsm restart \<SERVER\> - Startet den angegebenen Server neu
 - /bsm cmd \<SERVER\> \<COMMAND\> [\<ARGUMENTS\>] - Führt einen Befehl mit optionalen Argumenten auf dem Server aus.

### Permissions
bungeeservermanager.\<BEFEHL\>[.\<SERVER\>]  
z.B. bungeeservermanager.reload oder bungeeservermanager.restart.server1

## English
BungeeServerManager is plugin for managing Minecraft servers in a Bungeecord network.  
Using commands the servers can be started/stopped/restarted.
Also commands can be executed on the Minecraft servers using rcon.

### Commands
 - /bsm help - Shows help
 - /bsm reload - Reloads the configuration
 - /bsm \<start/stop\> \<SERVER\> - Starts/stopps the given server
 - /bsm restart \<SERVER\> - Restarts the given server
 - /bsm cmd \<SERVER\> \<COMMAND\> [\<ARGUMENTS\>] - Executes the command with optional arguments on the server.

### Permissions
bungeeservermanager.\<COMMAND\>[.\<SERVER\>]  
eg. bungeeservermanager.reload or bungeeservermanager.restart.server1
