# BungeeServerManager

## Deutsch
BungeeServerManager ist ein Plugin zur Verwaltung von Minecraft Servers in einem BungeeCord-Netzwerk.  
Die Server können über Commands gestartet/gestoppt/neugestartet werden.
Zudem können Befehle auf den Minecraft Servern via rcon ausgeführt werden.

### Befehle
/bungeeservermanager oder /bsm
 - /bsm help - Zeigt die Hilfe an
 - /bsm reload - Läd die Config neu
 - /bsm list - Listet alle Server auf
 - /bsm \<start/stop\> \<SERVER\> - Startet/Stoppt den angegebenen Server
 - /bsm restart \<SERVER\> - Startet den angegebenen Server neu
 - /bsm cmd \<SERVER\> \<COMMAND\> [\<ARGUMENTS\>] - Führt einen Befehl mit optionalen Argumenten auf dem Server aus.

### Permissions
bungeeservermanager.\<BEFEHL\>[.\<SERVER\>]  
z.B. bungeeservermanager.reload oder bungeeservermanager.restart.server1

### Konfiguration
#### Beispiel Konfiguration
```YAML
servers:
  server1:
    addr: 192.168.2.101
    port: 27015
    password: secure
    serverdir: ..\Server1\
    startscript: start.bat
    active: true
```

Parameter|Erklärung
----------|----------
addr|Adresse des Servers
port|rcon Port
password|rcon Passwort
serverdir|Pfad zum Server
startscript|Names des Startscripts
active|Server beim Laden des Plugins starten?


Es können beliebig viele Server angegeben werden.

### Installation
1. BungeeServerManager-\*.jar Datei in den Plugins Ordner von Bungeecord schieben
2. rcon bei den Spigot Servern konfigurieren und aktivieren
3. Bungeecord starten, damit der BungeeServerManager-Ordner erstellt wird
4. BungeeServerManager konfigurieren: config.yml bearbeiten


## English
BungeeServerManager is plugin for managing Minecraft servers in a Bungeecord network.  
Using commands the servers can be started/stopped/restarted.
Also commands can be executed on the Minecraft servers using rcon.

### Commands
/bungeeservermanager or /bsm
 - /bsm help - Shows help
 - /bsm reload - Reloads the configuration
 - /bsm list - Lists all servers
 - /bsm \<start/stop\> \<SERVER\> - Starts/stopps the given server
 - /bsm restart \<SERVER\> - Restarts the given server
 - /bsm cmd \<SERVER\> \<COMMAND\> [\<ARGUMENTS\>] - Executes the command with optional arguments on the server.

### Permissions
bungeeservermanager.\<COMMAND\>[.\<SERVER\>]  
eg. bungeeservermanager.reload or bungeeservermanager.restart.server1

### Configuration
#### Example configuration
```YAML
servers:
  server1:
    addr: 192.168.2.101
    port: 27015
    password: secure
    serverdir: ..\Server1\
    startscript: start.bat
    active: true
```

Parameter|Explaination
----------|----------
addr|address of the server
port|rcon Port
password|rcon password
serverdir|path to the server
startscript|name of the startscript
active|should the server be started when the plugin is loaded?


As many servers can be created as needed.

### Installation
1. Copy the BungeeServerManager-\*.jar file to the plugins folder of Bungeecord
2. Configure and active rcon on the Spigot servers
3. Start Bungeecord so that the BungeeServerManager-folder is created
4. Configure BungeeServerManager by editing the config.yml
