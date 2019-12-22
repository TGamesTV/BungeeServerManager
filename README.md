# BungeeServerAdmin

## Deutsch
BungeeServerAdmin ist ein Plugin zur Verwaltung von Minecraft Servers in einem BungeeCord-Netzwerk.  
Die Server können über Commands gestartet/gestoppt/neugestartet werden.
Zudem können Befehle auf den Minecraft Servern via rcon ausgeführt werden.

### Befehle
/bungeeserveradmin oder /bsa
 - /bsa help - Zeigt die Hilfe an
 - /bsa reload - LÃ¤d die Config neu
 - /bsa list - Listet alle Server auf
 - /bsa \<start/stop\> \<SERVER\> - Startet/Stoppt den angegebenen Server
 - /bsa restart \<SERVER\> - Startet den angegebenen Server neu
 - /bsa cmd \<SERVER\> \<COMMAND\> [\<ARGUMENTS\>] - Führt einen Befehl mit optionalen Argumenten auf dem Server aus.  
Wenn statt dem Servernamen "allservers" eingegeben wird, wird der Befehl auf allen Servern ausgeführt.

### Permissions
bungeeserveradmin.\<BEFEHL\>[.\<SERVER\>]  
z.B. bungeeserveradmin.reload oder bungeeserveradmin.restart.server1

### Konfiguration
#### Beispiel Konfiguration
```YAML
bstats-enable: true
servers:
  server1:
    addr: 192.168.2.101
    port: 27015
    password: secure
    serverdir: ..\Server1\
    startscript: start.bat
    active: true
    always-stop: false
```

Parameter|ErklÃ¤rung
----------|----------
bstats-enable|Soll das Plugin bStats verwenden?
addr|Adresse des Servers
port|rcon Port
password|rcon Passwort
serverdir|Pfad zum Server
startscript|Names des Startscripts
active|Server beim Laden des Plugins starten? (Falls nicht gesetzt: false)
always-stop|Stoppt den Server auch dann, wenn er nicht von BSA gestartet wurde (Falls nicht gesetzt: false)


Es können beliebig viele Server angegeben werden.

### Installation
1. BungeeServerAdmin-\*.jar Datei in den Plugins Ordner von Bungeecord schieben
2. rcon bei den Spigot Servern konfigurieren und aktivieren
3. Bungeecord starten, damit der BungeeServerAdmin-Ordner erstellt wird
4. BungeeServerAdmin konfigurieren: config.yml bearbeiten


## English
BungeeServerAdmin is plugin for managing Minecraft servers in a Bungeecord network.  
Using commands the servers can be started/stopped/restarted.
Also commands can be executed on the Minecraft servers using rcon.

### Commands
/bungeeserveradmin or /bsa
 - /bsa help - Shows help
 - /bsa reload - Reloads the configuration
 - /bsa list - Lists all servers
 - /bsa \<start/stop\> \<SERVER\> - Starts/stopps the given server
 - /bsa restart \<SERVER\> - Restarts the given server
 - /bsa cmd \<SERVER\> \<COMMAND\> [\<ARGUMENTS\>] - Executes the command with optional arguments on the server.  
By typing "allservers" instead of the server name, the command is executed on all servers.

### Permissions
bungeeserveradmin.\<COMMAND\>[.\<SERVER\>]  
eg. bungeeserveradmin.reload or bungeeserveradmin.restart.server1

### Configuration
#### Example configuration
```YAML
bstats-enable: true
servers:
  server1:
    addr: 192.168.2.101
    port: 27015
    password: secure
    serverdir: ..\Server1\
    startscript: start.bat
    active: true
    always-stop: false
```

Parameter|Explaination
----------|----------
bstats-enable|Should the plugin use bStats?
addr|address of the server
port|rcon Port
password|rcon password
serverdir|path to the server
startscript|name of the startscript
active|should the server be started when the plugin is loaded? (Defaults to false)
always-stop|also stopps the server if it has not been started by bsa (Defaults to false)


As many servers can be created as needed.

### Installation
1. Copy the BungeeServerAdmin-\*.jar file to the plugins folder of Bungeecord
2. Configure and active rcon on the Spigot servers
3. Start Bungeecord so that the BungeeServerAdmin-folder is created
4. Configure BungeeServerAdmin by editing the config.yml
