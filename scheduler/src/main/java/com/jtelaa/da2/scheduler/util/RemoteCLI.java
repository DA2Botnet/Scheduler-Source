package com.jtelaa.da2.scheduler.util;

import com.jtelaa.da2.lib.cli.Cases;
import com.jtelaa.da2.lib.cli.LocalCLI;
import com.jtelaa.da2.lib.console.ConsoleBanners;
import com.jtelaa.da2.lib.console.ConsoleColors;
import com.jtelaa.da2.lib.control.Command;
import com.jtelaa.da2.lib.control.ComputerControl;
import com.jtelaa.da2.lib.log.Log;
import com.jtelaa.da2.lib.misc.MiscUtil;
import com.jtelaa.da2.scheduler.Main;

public class RemoteCLI extends LocalCLI {

    private boolean run_as_local;

    public RemoteCLI() { run_as_local = false; }
    public RemoteCLI(boolean run_as_local) { this.run_as_local = run_as_local; }

    @Override
    public void run() {
        if (!run_as_local && Main.my_config.getProperty("remote_cli", "false").equalsIgnoreCase("true")) {
            Log.sendMessage("CLI: Preparing Remote CLI");

            while (!run) {
                MiscUtil.waitasec();

            }

            Log.sendMessage("CLI: Starting Local CLI");

            runRX();

        } else {
            MiscUtil.waitasec();

        }
    }

    @Override
    public synchronized String terminal(Command command) {
        Command[] commands = command.split(" ");
        Command cmd = commands[0];

        String response = "";

        // Shutdown
        if (Cases.exit(cmd)) {
            run = false;
            return "Shutting Down CLI";

        // CMD
        } else if (Cases.command(cmd)) {
            ComputerControl.sendCommand(command.addBlankUser().addBlankControlID().modifyforSys());

        } else if (Cases.checkCase(cmd, new String[] {"title"})) {
            response += ConsoleColors.CLEAR.getEscape() + ConsoleColors.LINES.getEscape();
            response += ConsoleBanners.otherBanner("com/jtelaa/bwbot/querygen/misc/Rewards.txt", ConsoleColors.CYAN_BOLD) + "\n";
            response += ConsoleBanners.otherBanner("com/jtelaa/bwbot/querygen/misc/QueryGen.txt", ConsoleColors.YELLOW) + "\n";
            
        } else if (Cases.help(cmd)) {
            response += ConsoleColors.CLEAR.getEscape() + ConsoleColors.LINES.getEscape();
            response += ConsoleBanners.otherBanner("com/jtelaa/bwbot/querygen/misc/Rewards.txt", ConsoleColors.CYAN_BOLD) + "\n";
            response += ConsoleBanners.otherBanner("com/jtelaa/bwbot/querygen/misc/QueryGen.txt", ConsoleColors.YELLOW) + "\n";

            String help = (
                "Query Generator CLI Help:\n"
                + ConsoleColors.YELLOW_UNDERLINED.getEscape() + "cmd" + ConsoleColors.RESET.getEscape() + " -> pass through a command to the systems OS\n"
                + ConsoleColors.YELLOW_UNDERLINED.getEscape() + "dump x" + ConsoleColors.RESET.getEscape() + " -> remove x queries from queue and print them (default 100)\n"
                + ConsoleColors.YELLOW_UNDERLINED.getEscape() + "clear x" + ConsoleColors.RESET.getEscape() + " -> clear x queries from the queue and clear bot queue (default all)\n"
                + ConsoleColors.YELLOW_UNDERLINED.getEscape() + "size x" + ConsoleColors.RESET.getEscape() + " -> change the size to x (default print the queue size)\n"
                + ConsoleColors.YELLOW_UNDERLINED.getEscape() + "add x y" + ConsoleColors.RESET.getEscape() + " -> add ip x to the request queue y times(s)\n"  
            );

            response += help + ConsoleColors.LINES_SHORT.getEscape();

        }

        return response;
        
    }
}