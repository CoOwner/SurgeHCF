 package com.surgehcf.core.hcf.command;
 
  import me.milksales.util.BukkitUtils;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandExecutor;
 import org.bukkit.command.CommandSender;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.timer.TimerManager;
import com.surgehcf.core.hcf.timer.type.SOTWTimer;
 
 public class SOTWCommand implements CommandExecutor
 {
   public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args)
   {
     if (args.length == 0) {
       cs.sendMessage(BukkitUtils.STRAIGHT_LINE_DEFAULT);
       cs.sendMessage(org.bukkit.ChatColor.AQUA + SurgeCore.getPlugin().getTimerManager().sotw.getDisplayName() + net.md_5.bungee.api.ChatColor.AQUA + " Help");
       cs.sendMessage(org.bukkit.ChatColor.GRAY + "/" + cmd.getName() + " start - Starts the " + SurgeCore.getPlugin().getTimerManager().sotw.getDisplayName() + net.md_5.bungee.api.ChatColor.GRAY + ".");
       cs.sendMessage(org.bukkit.ChatColor.GRAY + "/" + cmd.getName() + " stop - Stops the " + SurgeCore.getPlugin().getTimerManager().sotw.getDisplayName() + net.md_5.bungee.api.ChatColor.GRAY + ".");
       cs.sendMessage(org.bukkit.ChatColor.GRAY + "/" + cmd.getName() + " pause - Pauses the " + SurgeCore.getPlugin().getTimerManager().sotw.getDisplayName() + net.md_5.bungee.api.ChatColor.GRAY + ".");
       cs.sendMessage(org.bukkit.ChatColor.GRAY + "/" + cmd.getName() + " unpause - Un-pause the " + SurgeCore.getPlugin().getTimerManager().sotw.getDisplayName() + net.md_5.bungee.api.ChatColor.GRAY + ".");
       cs.sendMessage(org.bukkit.ChatColor.GRAY + "/" + cmd.getName() + " set <time> - Set the  " + SurgeCore.getPlugin().getTimerManager().sotw.getDisplayName() + net.md_5.bungee.api.ChatColor.GRAY + ".");
       cs.sendMessage(BukkitUtils.STRAIGHT_LINE_DEFAULT);
       return true;
     }
     if (args.length == 2) {
       long duration = me.milksales.util.JavaUtils.parse(org.apache.commons.lang.StringUtils.join((Object[])args, ' ', 1, args.length));
       if (duration == -1L) {
         cs.sendMessage(org.bukkit.ChatColor.RED + "Invalid duration, use the correct format: 10m 1s");
         return true;
       }
       SurgeCore.getPlugin().getTimerManager().sotw.setRemaining(duration, true);
       SurgeCore.getPlugin().getTimerManager().sotw.setPaused(false);
       cs.sendMessage("§eSurge §6» §rSOTW set to §e" + SurgeCore.getRemaining(duration, true, true));
       return true;
     }
     if (args.length == 1) {
       if (args[0].equalsIgnoreCase("help")) {
         cs.sendMessage(BukkitUtils.STRAIGHT_LINE_DEFAULT);
         cs.sendMessage(org.bukkit.ChatColor.AQUA + SurgeCore.getPlugin().getTimerManager().sotw.getDisplayName() + net.md_5.bungee.api.ChatColor.AQUA + " Help");
         cs.sendMessage(org.bukkit.ChatColor.GRAY + "/" + cmd.getName() + " start - Starts the " + SurgeCore.getPlugin().getTimerManager().sotw.getDisplayName() + net.md_5.bungee.api.ChatColor.GRAY + ".");
         cs.sendMessage(org.bukkit.ChatColor.GRAY + "/" + cmd.getName() + " stop - Stops the " + SurgeCore.getPlugin().getTimerManager().sotw.getDisplayName() + net.md_5.bungee.api.ChatColor.GRAY + ".");
         cs.sendMessage(org.bukkit.ChatColor.GRAY + "/" + cmd.getName() + " pause - Pauses the " + SurgeCore.getPlugin().getTimerManager().sotw.getDisplayName() + net.md_5.bungee.api.ChatColor.GRAY + ".");
         cs.sendMessage(org.bukkit.ChatColor.GRAY + "/" + cmd.getName() + " unpause - Un-pause the " + SurgeCore.getPlugin().getTimerManager().sotw.getDisplayName() + net.md_5.bungee.api.ChatColor.GRAY + ".");
         cs.sendMessage(org.bukkit.ChatColor.GRAY + "/" + cmd.getName() + " set <time> - Set the  " + SurgeCore.getPlugin().getTimerManager().sotw.getDisplayName() + net.md_5.bungee.api.ChatColor.GRAY + ".");
         cs.sendMessage(BukkitUtils.STRAIGHT_LINE_DEFAULT);
         return true;
       }
       if (args[0].equalsIgnoreCase("start")) {
         SurgeCore.getPlugin().getTimerManager().sotw.setRemaining(TimeUnit.HOURS.toMillis(2L), true);
         SurgeCore.getPlugin().getTimerManager().sotw.setPaused(false);
         cs.sendMessage(net.md_5.bungee.api.ChatColor.YELLOW + "SOTW started.");
				 Bukkit.getServer().broadcastMessage("§eSurge §6» §rStart of the World has §a§lstarted§r.");
         return true;
       }
       if (SurgeCore.getPlugin().getTimerManager().sotw.getRemaining() <= 0L) {
         cs.sendMessage(org.bukkit.ChatColor.RED + "SOTW Timer hasn't started yet!");
				 cs.sendMessage("§dUsage: §7/sotw start");
         return true;
       }
       if ((args[0].equalsIgnoreCase("end")) || (args[0].equalsIgnoreCase("stop"))) {
         SurgeCore.getPlugin().getTimerManager().sotw.setRemaining(TimeUnit.SECONDS.toMillis(0L), true);
         SurgeCore.getPlugin().getTimerManager().sotw.setPaused(false);
         cs.sendMessage(net.md_5.bungee.api.ChatColor.YELLOW + "SOTW stopped.");
                 Bukkit.getServer().broadcastMessage("§eSurge §6» §rSOTW has been forcefully §cended §7(PvP is now ENABLED)");
         return true;
       }
       if (args[0].equalsIgnoreCase("pause")) {
         SurgeCore.getPlugin().getTimerManager().sotw.setPaused(true);
         cs.sendMessage(net.md_5.bungee.api.ChatColor.YELLOW + "SOTW paused.");
				 Bukkit.getServer().broadcastMessage("§eSurge §6» §rSOTW has been §cpaused §7(" + DurationFormatUtils.formatDurationWords(SurgeCore.getPlugin().getTimerManager().sotw.getRemaining(), true, true) + " remaining)");
         return true;
       }
       if (args[0].equalsIgnoreCase("unpause")) {
         SurgeCore.getPlugin().getTimerManager().sotw.setPaused(false);
         Bukkit.getServer().broadcastMessage("§eSurge §6» §rSOTW has been §aunpaused §7(" + DurationFormatUtils.formatDurationWords(SurgeCore.getPlugin().getTimerManager().sotw.getRemaining(), true, true) + " remaining)");
         return true;
       }
       
       cs.sendMessage(net.md_5.bungee.api.ChatColor.RED + "/" + cmd.getName() + " help.");
     }
     
     return false;
   }
 }

