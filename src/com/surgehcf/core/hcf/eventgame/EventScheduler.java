 package com.surgehcf.core.hcf.eventgame;
 
  import java.io.BufferedReader;
 import java.io.FileInputStream;
 import java.io.FileNotFoundException;
 import java.io.IOException;
 import java.nio.charset.StandardCharsets;
 import java.time.LocalDateTime;
 import java.util.Map;
 import java.util.concurrent.TimeUnit;

import net.minecraft.util.com.google.common.primitives.Ints;

 import org.bukkit.command.ConsoleCommandSender;

import com.surgehcf.SurgeCore;
 
 public class EventScheduler
 {
   private static final String FILE_NAME = "event-schedules.txt";
   
   public EventScheduler(SurgeCore plugin)
   {
     this.scheduleMap = new java.util.LinkedHashMap();
     this.plugin = plugin;
     reloadSchedules();
   }
   
   private static LocalDateTime getFromString(String input) {
     if (!input.contains(",")) {
       return null;
     }
     String[] args = input.split(",");
     if (args.length != 5) {
       return null;
     }
     Integer year = Ints.tryParse(args[0]);
     if (year == null) {
       return null;
     }
     Integer month = Ints.tryParse(args[1]);
     if (month == null) {
       return null;
     }
     Integer day = Ints.tryParse(args[2]);
     if (day == null) {
       return null;
     }
     Integer hour = Ints.tryParse(args[3]);
     if (hour == null) {
       return null;
     }
     Integer minute = Ints.tryParse(args[4]);
     if (minute == null) {
       return null;
     }
     return LocalDateTime.of(year.intValue(), month.intValue(), day.intValue(), hour.intValue(), minute.intValue());
   }
   
   private void reloadSchedules() {
     try { BufferedReader bufferedReader = new BufferedReader(new java.io.InputStreamReader(new FileInputStream(new java.io.File(this.plugin.getDataFolder(), "event-schedules.txt")), StandardCharsets.UTF_8));Throwable localThrowable3 = null;
       try { String currentLine;
         while ((currentLine = bufferedReader.readLine()) != null) {
           if (!currentLine.startsWith("#"))
           {
 
             currentLine = currentLine.trim();
             String[] args = currentLine.split(":");
             if (args.length == 2)
             {
 
               LocalDateTime localDateTime = getFromString(args[0]);
               if (localDateTime != null)
               {
 
                 this.scheduleMap.put(localDateTime, args[1]);
               }
             }
           }
         }
       }
       catch (Throwable localThrowable1)
       {
         localThrowable3 = localThrowable1;throw localThrowable1;
 
 
 
 
 
 
 
       }
       finally
       {
 
 
 
 
 
 
         if (bufferedReader != null) if (localThrowable3 != null) try { bufferedReader.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else bufferedReader.close();
       }
     } catch (FileNotFoundException ex2) { org.bukkit.Bukkit.getConsoleSender().sendMessage("Could not find file event-schedules.txt.");
     }
     catch (IOException ex) {
       ex.printStackTrace();
     }
   }
   
   public Map<LocalDateTime, String> getScheduleMap() {
     long millis = System.currentTimeMillis();
     if (millis - QUERY_DELAY > this.lastQuery) {
       reloadSchedules();
       this.lastQuery = millis;
     }
     return this.scheduleMap;
   }
   
 
   private static final long QUERY_DELAY = TimeUnit.SECONDS.toMillis(60L);
   private final Map<LocalDateTime, String> scheduleMap;
   private final SurgeCore plugin;
   private long lastQuery;
 }

