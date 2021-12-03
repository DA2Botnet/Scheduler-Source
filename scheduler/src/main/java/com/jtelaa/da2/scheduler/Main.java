package com.jtelaa.da2.scheduler;

import java.util.Properties;

/**
 * 
 */

 // TODO Comment
 // TODO Setup

public class Main {
    /** The remote cli local object */ 
    public static RemoteCLI rem_cli;

    /** The system cli local object */ 
    public static SysCLI sys_cli;
    
    /** Local configuration handler */
    public static Properties my_config;

    /** Properties file path */
    public static String properties_path = "~/scheduler_config.properties";
    public static String banners_directory = "~/banners/";

    /**
     * 
     * @param args
     */

    public static void main(String[] args) {
        // Check for first time setup
        boolean first_time = false;
        for (String arg : args) {
            if (arg.equalsIgnoreCase("setup")) {
                Log.sendManSysMessage("Loading first - time config");
                first_time = true;
            }
        }

        // Load normally if not first time
        if (first_time) {
            // Load banners
            ConsoleBanners.loadRemoteBanners("QueryGen");

            // Get config template
            Log.sendManSysMessage("Loading config template");
            ComputerControl.sendCommand("cd ~/ && curl https://raw.githubusercontent.com/DA2Botnet/Scheduler-Source/main/config_template/scheduler_config.properties > scheduler_config.properties");

            // Load searches
            SearchHandler.loadRemoteSearches();

        }

        my_config = PropertiesUtils.importConfig(properties_path); 

        my_config.setProperty("log_ip", FranchiseUtils.resolveDefaultLogIP());
        my_config.setProperty("db_ip", FranchiseUtils.resolveDefaultDBIP());

        try {
            querySettings();

        } catch (EmptySQLURLException e) {
            e.printStackTrace();

        }

        // Start Logging
        Log.loadConfig(my_config, args);
        Log.clearHistory();

        // List properties
        for (String line : PropertiesUtils.listProperties(my_config)) { Log.sendSysMessage(line); }

        // Startup
        Log.sendSysMessage("Main: Starting.....\n");
        Log.sendSysMessage(ConsoleBanners.otherBanner(banners_directory + "Rewards.txt", ConsoleBanners.EXTERNAL, ConsoleColors.CYAN_BOLD));
        Log.sendSysMessage(ConsoleBanners.otherBanner(banners_directory + "Scheduler.txt", ConsoleBanners.EXTERNAL, ConsoleColors.YELLOW));
        Log.sendSysMessage("\n\n\n");

        // Start logging client
        Log.openClient(my_config.getProperty("log_ip", my_config.getProperty("log_ip")));
        Log.openConnector();


        
    }

    public static void sendBootup(int da2_id) {

    }
}
