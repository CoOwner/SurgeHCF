 package com.surgehcf.core.hcf.eventgame.argument;
 
  import me.milksales.util.command.CommandArgument;

import org.apache.commons.lang.time.FastDateFormat;
 import org.bukkit.ChatColor;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandSender;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.DateFormatter;
import com.surgehcf.core.hcf.eventgame.EventTimer;
import com.surgehcf.core.hcf.eventgame.faction.EventFaction;
 
 public class EventUptimeArgument extends CommandArgument
 {
   private final SurgeCore plugin;
   
   public EventUptimeArgument(SurgeCore plugin)
   {
     super("uptime", "Check the uptime of an event");
     this.plugin = plugin;
     this.permission = ("hcf.command.event.argument." + getName());
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + getName();
   }
   
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
     EventTimer eventTimer = this.plugin.getTimerManager().eventTimer;
     if (eventTimer.getRemaining() <= 0L) {
       sender.sendMessage(ChatColor.RED + "There is not a running event.");
       return true;
     }
     EventFaction eventFaction = eventTimer.getEventFaction();
     sender.sendMessage(ChatColor.YELLOW + "Up-time of " + eventTimer.getName() + " timer" + (eventFaction == null ? "" : new StringBuilder().append(": ").append(ChatColor.BLUE).append('(').append(eventFaction.getDisplayName(sender)).append(ChatColor.BLUE).append(')').toString()) + ChatColor.YELLOW + " is " + ChatColor.GRAY + org.apache.commons.lang.time.DurationFormatUtils.formatDurationWords(eventTimer.getUptime(), true, true) + ChatColor.YELLOW + ", started at " + ChatColor.GOLD + DateFormatter.HR_MIN_AMPM_TIMEZONE.format(eventTimer.getStartStamp()) + ChatColor.YELLOW + '.');
     return true;
   }
 }

