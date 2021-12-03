package com.jtelaa.da2.scheduler;

import java.util.Properties;

import com.jtelaa.da2.scheduler.util.RemoteCLI;
import com.jtelaa.da2.scheduler.util.SysCLI;
import com.jtelaa.da2.lib.config.PropertiesUtils;
import com.jtelaa.da2.lib.console.ConsoleBanners;
import com.jtelaa.da2.lib.console.ConsoleColors;
import com.jtelaa.da2.lib.control.ComputerControl;
import com.jtelaa.da2.lib.franchise.FranchiseUtils;
import com.jtelaa.da2.lib.log.Log;
import com.jtelaa.da2.lib.net.NetTools;
import com.jtelaa.da2.lib.sql.DA2SQLQueries;
import com.jtelaa.da2.lib.sql.EmptySQLURLException;
import com.jtelaa.da2.lib.sql.SQL;

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

     /**
     * 
     * @return
     */

    private static void querySettings() throws EmptySQLURLException {
        String database = my_config.getProperty("database");
        String table_name = my_config.getProperty("table");
        String id_type = my_config.getProperty("key");
        String db_ip = my_config.getProperty("db_ip");
        String user = my_config.getProperty("db_user");
        String passwd = my_config.getProperty("db_passwd");

        DA2SQLQueries.connectionURL = SQL.getConnectionURL(db_ip, database, user, passwd);

        // TODO Implement other config        

        // Get ID
        int id = DA2SQLQueries.getID(database, table_name, id_type, "IP", NetTools.getLocalIP());
        my_config.setProperty("id", id + "");

        PropertiesUtils.exportConfig(properties_path, my_config);

    }
}
