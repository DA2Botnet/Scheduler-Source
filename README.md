# BW Plugin Scheduler

Handles starting and closing bot sessions for searches

## Schedule Creator

### **Active Hours: 8 am EST - 3 am EST (12 pm PST)**
This is when most of the searches will take place (95%)

The schedule creator queries a database for a list of bots and the maximum number of daily searches. For every bot, it chooses a search limit for the day, from 0-max searches. Next, it clumps the searches into partitions, to be executed in groups throughout the day. Finally, it chooses random times throughout the day for these search groups to be executed by that bot, with a 95% probability for the time to be between 8 am and 3 am EST (Also checks to ensure maximum number of consecutive bot instances does not exceed physical capabilities). 

## Scheduler

The scheduler executes the instructions created by the Schedule Creator. It tells bots to turn on and off, and how many searches to perform in their session. 
