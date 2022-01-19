package de.sc.utils;

import com.formdev.flatlaf.intellijthemes.FlatAllIJThemes;
import de.sc.views.MainView;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigManager {
    private final String userHome;
    private final File configFolder;
    private final File configFile;

    private Properties config;
    private MainView window;
    //Constructor of ConfigManager
    public ConfigManager(MainView window){
        //Set the main Window object
        this.window = window;
        //set users home path
        userHome = System.getProperty("user.home");
        //set config folder
        //.config/subnet/
        configFolder = new File(Paths.get(userHome, ".config", "subnet").toString());

        //Create config folder if not existing
        if(!configFolder.exists()){
            configFolder.mkdirs();
        }
        //set the config file. Properties
        configFile = new File(configFolder, "config.config");
        //create config file if not existing
        if(!configFile.exists()){
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //sett the config Properties variable
        config = new Properties();

        //Read in the config variable from the saved config file.
        try (FileInputStream fis = new FileInputStream(configFile.getAbsolutePath())) {
            config.load(fis);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //load the look and feel from config.
        if(config.getProperty("theme") != null && !getLAFClassByName(config.getProperty("theme")).equals("undefined")){
            setTheme(config.getProperty("theme"));
        }

    }

    //Getting Look and feel Class name from Theme name
    public String getLAFClassByName(String name){
        String clazz = "undefined";
        //Loop through Themes
        for (FlatAllIJThemes.FlatIJLookAndFeelInfo info : FlatAllIJThemes.INFOS) {
            //check if name is equal
            if(name.equals(info.getName())){
                //setting the class name
                clazz = info.getClassName();
                //break out of loop after found
                break;
            }
        }
        //return class name if found / if not return undefined.
        return clazz;
    }

    //Set theme and look and feel.
    public void setTheme(String name){
        //Try to set the Look and feel.
        try {
            //Set the components look and feel.
            UIManager.setLookAndFeel( getLAFClassByName(name));
            //set the Default font to Century Gothic
            UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("Century Gothic", Font.BOLD, 14));
            //save the current theme to config.
            saveProperty("theme", name);
        } catch( Exception ex ) {
            //Print error message
            System.err.println( "Failed to initialize LaF" );
        }
        //Update all components
        for(Window window : Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(window);
        }
    }
    public void saveProperty(String key, String value) throws IOException {
        //start stream to config file.
        FileOutputStream outputStrem = new FileOutputStream(configFile);
        //put the key value pair in the config.
        config.setProperty(key, value);
        //store the config into the stream and write it back n the file.
        config.store(outputStrem, "Config file | Subnet Calculator");
    }
    //Getter for the config Properties file
    public Properties getConfig() {
        return config;
    }

    //Getter for the config Folder
    public File getConfigFolder() {
        return configFolder;
    }
}
