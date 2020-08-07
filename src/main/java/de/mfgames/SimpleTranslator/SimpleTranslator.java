/*
 * SimpleTranslator
 * Translator class for Bungeecord plugins
 * 
 * Copyright (C) 2020 DavidoTek
 * 
 * */

package de.mfgames.SimpleTranslator;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;

public class SimpleTranslator {

    String language;

    Configuration translationFile;

    Plugin pluginInstance;

    /*
     * SimpleTranslator
     * @param lang     Language to be translated to
     * @param instance Instance of your main plugin class
     * */
    public SimpleTranslator(String lang, Plugin instance) {
        language = lang.toLowerCase();
        pluginInstance = instance;

        /* Copy reference translation from resources to the plugins data folder */
        File i18nFolder = new File(pluginInstance.getDataFolder(), "i18n");

        try {
            if(!i18nFolder.exists())
                i18nFolder.mkdir();

            File file = new File(i18nFolder, "i18n_en.yml");

            if (!file.exists()) {
                InputStream filein = pluginInstance.getResourceAsStream("i18n/i18n_en.yml");
                Files.copy(filein, file.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!loadTranslationsFile(lang)) {
            System.out.println("[SimpleTranslator] Could not find a translation for " + lang);
            language = "en";
        }
    }

    /*
     * Translate the text
     * @param paragraphid Unique identifierer for the paragraph
     * @param reftext     Reference/Untranslated text (in english)
     * @param values      Placeholder values (%1, %2, ...)
     * */
    public String translateString(int paragraphid, String reftext, Object ... values) {
        String i18ntext = reftext;

        if (!language.equals("en")) {
            i18ntext = translationFile.getString(String.valueOf(paragraphid), reftext);
        }

        int i = 1;
        for (Object value : values) {
            String indexString = "%" + String.valueOf(i);
            i18ntext = i18ntext.replace(indexString, String.valueOf(value));
            i++;
        }

        return i18ntext;
    }

    /*
     * Load a translation file
     * @param lang Language
     * */
    boolean loadTranslationsFile(String lang) {
        try {
            File i18nFolder = new File(pluginInstance.getDataFolder(), "i18n");
            if (!i18nFolder.exists())
                return false;

            File file = new File(i18nFolder, "i18n_" + lang + ".yml");

            if (!file.exists())
                return false;

            translationFile = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
