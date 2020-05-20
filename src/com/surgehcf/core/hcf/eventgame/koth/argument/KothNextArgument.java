 package com.surgehcf.core.hcf.eventgame.koth.argument;
 
  import me.milksales.util.command.CommandArgument;

import java.time.LocalDateTime;
 import java.time.format.TextStyle;
 import java.util.Map;
 import java.util.Map.Entry;
 import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.FastDateFormat;
 import org.bukkit.ChatColor;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandSender;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.DateFormatter;
 
 public class KothNextArgument extends CommandArgument
 {
   private final SurgeCore plugin;
   
   public KothNextArgument(SurgeCore plugin)
   {
     super("next", "View the next scheduled KOTH");
     this.plugin = plugin;
     this.permission = ("hcf.command.koth.argument." + getName());
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + getName();
   }
   
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
     long millis = System.currentTimeMillis();
     sender.sendMessage(ChatColor.GOLD + "The server time is currently " + ChatColor.YELLOW + DateFormatter.DAY_MTH_HR_MIN_AMPM.format(millis) + ChatColor.GOLD + '.');
     Map<LocalDateTime, String> scheduleMap = this.plugin.eventScheduler.getScheduleMap();
     if (scheduleMap.isEmpty()) {
       sender.sendMessage(ChatColor.RED + "There is not an event schedule for after now.");
       return true;
     }
     LocalDateTime now = LocalDateTime.now(DateFormatter.SERVER_ZONE_ID);
     for (Map.Entry<LocalDateTime, String> entry : scheduleMap.entrySet()) {
       LocalDateTime scheduleDateTime = (LocalDateTime)entry.getKey();
       if (!now.isAfter(scheduleDateTime))
       {
 
         String monthName = scheduleDateTime.getMonth().getDisplayName(TextStyle.FULL, java.util.Locale.ENGLISH);
         String weekName = scheduleDateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, java.util.Locale.ENGLISH);
         sender.sendMessage(ChatColor.DARK_AQUA + org.apache.commons.lang.WordUtils.capitalizeFully((String)entry.getValue()) + ChatColor.GRAY + " is the next event: " + ChatColor.AQUA + weekName + ' ' + scheduleDateTime.getDayOfMonth() + ' ' + monthName + ChatColor.DARK_AQUA + " (" + DateFormatter.HR_MIN_AMPM.format(TimeUnit.HOURS.toMillis(scheduleDateTime.getHour()) + TimeUnit.MINUTES.toMillis(scheduleDateTime.getMinute())) + ')');
         return true;
       } }
     sender.sendMessage(ChatColor.RED + "There is not an event scheduled after now.");
     return true;
   }
 }

