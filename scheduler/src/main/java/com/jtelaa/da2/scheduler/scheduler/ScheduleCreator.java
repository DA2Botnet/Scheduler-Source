package com.jtelaa.da2.scheduler.scheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Random;


public class ScheduleCreator {
    private int MAX_SEARCHES;
    private final Random random = new Random();
    private ArrayList<String> botIDs;

    /**
     * Enables schedule creation for BW Search bots
     * @see com.jtelaa.da2.scheduler.scheduler.ScheduleCreator#CreateSchedule
     * @param ms (Max Searches): maximum number of daily searches for every bot
     */
    public ScheduleCreator(int ms) {
        MAX_SEARCHES = ms;
        // Add database query for bot IDs
    }


    
/**
 * Creates a schedule:
 * Queries a database for a list of bots
 * For every bot, it chooses a search limit for the day, from 0-max searches. 
 * Next, it clumps the searches into partitions, to be executed in groups throughout the day. 
 * Finally, it chooses random times throughout the day for these search groups to be executed by that bot, with a 95% probability for the time to be between 8 am and 3 am EST. 
 * 
 * Also checks to ensure maximum number of consecutive bot instances does not exceed physical capabilities.
 * @see README.md
 */
    public void CreateSchedule() {

        LocalDate today = java.time.LocalDate.now();
        LocalDateTime early = today.atTime(8, 0);
        long start_time = early.toEpochSecond(ZoneOffset.ofHours(-5));  // gets unix time for current day at 8:00 am

        for (String botID : botIDs) {
            int searches = random.nextInt(MAX_SEARCHES + 1);
            ArrayList<Integer> partitions = new ArrayList<Integer>();
            int num_partitions = random.nextInt(searches + 1);
            while (searches > 0) { // generates generates n partitions from s searches
                int partition_value = random.nextInt(searches - num_partitions + 1);
                searches -= partition_value;
                partitions.add(partition_value);
            }

            for (int partition : partitions) {
                long partition_time;
                if (random.nextInt(100) < 96) {
                    partition_time = start_time + random.nextInt(68400); // active time
                }else {
                    partition_time = start_time + 68400 + random.nextInt(18000); // inactive time
                }
                
                // Write partition time and number of searches to database / file

                
            }

        }
    }

    /**
     * Sets Maximum daily searches for schedule creator
     * @param ms (Max Searches): maximum number of daily searches for every bot
     */
    public void setMaxSearches(int ms) {
        MAX_SEARCHES = ms;
    }

    /**
     * @return Current daily maximum number of searches, per bot
     */
    public int maxSearches() {return MAX_SEARCHES;}

    /**
     * Re-queries database for a new list of search bots
     */
    public void refreshBotList() {
        // re-query database for new bots
        
    }
}
