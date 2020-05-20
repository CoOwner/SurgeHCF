 package com.surgehcf.core.hcf.eventgame.koth.argument;
 
  import me.milksales.util.BukkitUtils;
 import me.milksales.util.command.CommandArgument;

import java.time.DayOfWeek;
 import java.time.LocalDateTime;
 import java.time.Month;
 import java.time.format.DateTimeFormatter;
 import java.time.format.TextStyle;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Locale;
 import java.util.Map;
 import java.util.Map.Entry;

import org.apache.commons.lang.WordUtils;
 import org.bukkit.ChatColor;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandSender;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.DateFormatter;
import com.surgehcf.core.hcf.eventgame.EventScheduler;
 
 public class KothScheduleArgument extends CommandArgument
 {
   private static final String TIME_UNTIL_PATTERN = "d'd' H'h' mm'm'";
   
   public KothScheduleArgument(SurgeCore plugin)
   {
     super("schedule", "View the schedule for KOTH arenas");
     this.plugin = plugin;
     this.aliases = new String[] { "info", "i", "time" };
     this.permission = ("hcf.command.koth.argument." + getName());
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + getName();
   }
   
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
     LocalDateTime now = LocalDateTime.now(DateFormatter.SERVER_ZONE_ID);
     int currentDay = now.getDayOfYear();
     Map<LocalDateTime, String> scheduleMap = this.plugin.eventScheduler.getScheduleMap();
     List<String> shownEvents = new ArrayList();
     for (Map.Entry<LocalDateTime, String> entry : scheduleMap.entrySet()) {
       LocalDateTime scheduleDateTime = (LocalDateTime)entry.getKey();
       if (scheduleDateTime.isAfter(now)) {
         int dayDifference = scheduleDateTime.getDayOfYear() - currentDay;
         if (dayDifference <= 1)
         {
 
           String eventName = (String)entry.getValue();
           String monthName = scheduleDateTime.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
           String weekName = scheduleDateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
           ChatColor colour = dayDifference == 0 ? ChatColor.GREEN : ChatColor.AQUA;
           shownEvents.add("  " + colour + WordUtils.capitalizeFully(eventName) + ": " + ChatColor.YELLOW + weekName + ' ' + scheduleDateTime.getDayOfMonth() + ' ' + monthName + ChatColor.RED + " (" + HHMMA.format(scheduleDateTime) + ')' + ChatColor.GRAY + " - " + ChatColor.GOLD + org.apache.commons.lang.time.DurationFormatUtils.formatDuration(now.until(scheduleDateTime, java.time.temporal.ChronoUnit.MILLIS), "d'd' H'h' mm'm'"));
         }
       } }
     if (shownEvents.isEmpty()) {
       sender.sendMessage(ChatColor.RED + "There are no event schedules defined.");
       return true;
     }
     String monthName2 = WordUtils.capitalizeFully(now.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
     String weekName2 = WordUtils.capitalizeFully(now.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
     sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
     sender.sendMessage(ChatColor.GRAY + "Server time is currently " + ChatColor.WHITE + weekName2 + ' ' + now.getDayOfMonth() + ' ' + monthName2 + ' ' + HHMMA.format(now) + ChatColor.GRAY + '.');
     sender.sendMessage((String[])shownEvents.toArray(new String[shownEvents.size()]));
     sender.sendMessage(ChatColor.GRAY + "For more info about King of the Hill, use " + ChatColor.WHITE + '/' + label + " help" + ChatColor.GRAY + '.');
     sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
     return true;
   }
   
 
   private static final DateTimeFormatter HHMMA = DateTimeFormatter.ofPattern("h:mma");
   private final SurgeCore plugin;
 }


