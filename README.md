# BungeeServerAdmin
BungeeServerAdmin is a plugin for managing Minecraft servers in a Bungeecord network.  
Using commands the servers can be started/stopped/restarted.
Also commands can be executed on the Minecraft servers using rcon.

## Commands
Command | Explaination
----------|----------
**/bsa** help | Lists all available commands
**/bsa** reload | Reloads the configuration
**/bsa** list | Lists all servers
**/bsa** \<start/stop\> \<SERVER\> | Starts/stopps the given server\*
**/bsa** restart \<SERVER\> | Restarts the given server\*
**/bsa** cmd \<SERVER\> \<COMMAND\> [\<ARGUMENTS\>] | Executes the command with optional arguments on the server\*
**/bsa** add \<NAME\> \<ADDR\> \<RCON PORT\> \<PASSWORD\> \<SERVER DIR\> \<SCRIPT\> \<ACTIVE\> \<ALWAYS STOP\> | Adds the server
**/bsa** remove \<SERVER NAME\> \<REPEAT SERVER NAME\> | Removes the server
**/bss** | Shows system information
**/bss** cmd \<command\> | Executes the command on the system (needs to be configured)

\*By typing `allservers` instead of the server name, the command is executed on all servers.

## Permissions
bungeeserveradmin.\<COMMAND\>[.\<SERVER\>]  
eg. bungeeserveradmin.reload or bungeeserveradmin.restart.server1  
**/bss** permissions: bungeeserveradmin.stats
For **/bss cmd** the permissions need to be defined in the configuration.  

## Configuration
### Example configuration
```YAML
language: en
bstats-enable: true
language: en
servers:
  server1:
    addr: 192.168.2.101
    port: 27015
    password: secure
    serverdir: ..\Server1\
    startscript: .\start.bat
    active: true
    always-stop: false
system-commands:
  runexplorer:
    execute: 'explorer'
    permission: 'bungeeserveradmin.stats.commands.explorer'
```

Parameter|Explaination
----------|----------
language|language of the plugin/commands. Go to the plugin's folder, duplicate the `i18n_en.yml` file in the `i18n` folder, rename it according to your language (e.g. `i18n_de.yml`) and translate the contens of the file.
bstats-enable|should the plugin use bStats?
language|Language. A translations file is needed.
addr|address of the server
port|rcon Port
password|rcon password
serverdir|path to the server
startscript|name of the startscript (add **./** on Linux and **.\\** on Windows, eg. `./start.sh`)
active|should the server be started when the plugin is loaded? (Defaults to false)
always-stop|also stopps the server if it has not been started by bsa (Defaults to false)
execute|command to be executed on the system
permission|permission for the command


As many servers/commands can be created as needed. Add `#!/bin/bash` at the beginning of each start script if you haven't already.

## Installation
1. Copy the BungeeServerAdmin-\*.jar file to the plugins folder of Bungeecord
2. Configure and active rcon on the Spigot servers
3. Start Bungeecord so that the BungeeServerAdmin-folder is created
4. Configure BungeeServerAdmin by editing the config.yml

## Translations
### README
German/Deutsch: [README-DE.md](README-DE.md)
