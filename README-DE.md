# BungeeServerAdmin
BungeeServerAdmin ist ein Plugin zur Verwaltung von Minecraft Servers in einem BungeeCord-Netzwerk.  
Die Server können über Commands gestartet/gestoppt/neugestartet werden.
Zudem können Befehle auf den Minecraft Servern via rcon ausgeführt werden.

## Befehle
Befehl | Erklärung
----------|----------
**/bsa** help | Zeigt alle verfügbaren Befehle
**/bsa** reload | Läd die Config neu
**/bsa** list | Listet alle Server auf
**/bsa** \<start/stop\> \<SERVER\> | Startet/Stoppt den angegebenen Server\*
**/bsa** restart \<SERVER\> | Startet den angegebenen Server neu\*
**/bsa** cmd \<SERVER\> \<COMMAND\> [\<ARGUMENTS\>] | Führt einen Befehl mit optionalen Argumenten auf dem Server aus\*
**/bsa** add \<NAME\> \<ADDR\> \<RCON PORT\> \<PASSWORD\> \<SERVER DIR\> \<SCRIPT\> \<ACTIVE\> \<ALWAYS STOP\> | Fügt den Server hinzu
**/bsa** remove \<SERVER NAME\> \<SERVER NAME WIEDERHOLEN\> | Server entfernen
**/bss** | Zeigt System Informationen an
**/bss** cmd \<command\> | Führt den Befehlt auf dem System aus (Muss konfiguriert werden)

\*Wenn statt dem Servernamen "allservers" eingegeben wird, wird der Befehl auf allen Servern ausgeführt.

## Rechte
bungeeserveradmin.\<BEFEHL\>[.\<SERVER\>]  
z.B. bungeeserveradmin.reload oder bungeeserveradmin.restart.server1  
**/bss** permissions: bungeeserveradmin.stats  
Für **/bss cmd** müssen die Befehle in der Config definiert werden.  

## Konfiguration
### Beispiel Konfiguration
```YAML
language: en
bstats-enable: true
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

Parameter|Erklärung
----------|----------
language|Sprache des Plugins/der Befehle. Öffne den Plugin Konfigurations Ordner, Duplizierte die Datei `i18n_en.yml` im Ordner `i18n`, nenn die Datei entsprechend um (e.g. `i18n_de.yml`) und übersetze den Inhalt der Datei.
bstats-enable|Soll das Plugin bStats verwenden?
addr|Adresse des Servers
port|rcon Port
password|rcon password
serverdir|Pfad zum Server
startscript|Names des Startscripts (Füge **./** unter Linux und **.\\** unter Windows hinzu, z.B. `./start.sh`)
active|Server beim Laden des Plugins starten? (Falls nicht gesetzt: false)
always-stop|Stoppt den Server auch dann, wenn er nicht von BSA gestartet wurde (Falls nicht gesetzt: false)
execute|Befehl, der auf dem System ausgeführt werden soll
permission|Berechtigung für den Befehl

Es können beliebig viele Server/Befehle angegeben werden.

## Installation
1. BungeeServerAdmin-\*.jar Datei in den Plugins Ordner von Bungeecord schieben
2. rcon bei den Spigot Servern konfigurieren und aktivieren
3. Bungeecord starten, damit der BungeeServerAdmin-Ordner erstellt wird
4. BungeeServerAdmin konfigurieren: config.yml bearbeiten

## Übersetzung
### README
Englisch/English: [README.md](README.md)
