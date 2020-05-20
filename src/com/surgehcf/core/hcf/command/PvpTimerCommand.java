 package com.surgehcf.core.hcf.command;
 
  import com.google.common.collect.ImmutableList;
import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.timer.TimerManager;
import com.surgehcf.core.hcf.timer.type.PvpProtectionTimer;

import java.util.List;
 import java.util.Set;

import org.bukkit.ChatColor;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandExecutor;
 import org.bukkit.command.CommandSender;
 import org.bukkit.entity.Player;
 
 public class PvpTimerCommand implements CommandExecutor, org.bukkit.command.TabCompleter
 {
   public PvpTimerCommand(SurgeCore plugin)
   {
     this.plugin = plugin;
   }
   
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
     if (!(sender instanceof Player)) {
       sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
       return true;
     }
     Player player = (Player)sender;
     PvpProtectionTimer pvpTimer = this.plugin.getTimerManager().pvpProtectionTimer;
     if (args.length < 1) {
       printUsage(sender, label, pvpTimer);
       return true;
     }
     if ((args[0].equalsIgnoreCase("enable")) || (args[0].equalsIgnoreCase("remove")) || (args[0].equalsIgnoreCase("off"))) {
       if (pvpTimer.getRemaining(player) > 0L) {
         sender.sendMessage(ChatColor.RED + "Your " + pvpTimer.getDisplayName() + ChatColor.RED + " timer is now off.");
         pvpTimer.clearCooldown(player);
         return true;
       }
       if (pvpTimer.getLegible().remove(player.getUniqueId())) {
         player.sendMessage(ChatColor.YELLOW + "You will no longer be legible for your " + pvpTimer.getDisplayName() + ChatColor.YELLOW + " when you leave spawn.");
         return true;
       }
       sender.sendMessage(ChatColor.RED + "Your " + pvpTimer.getDisplayName() + ChatColor.RED + " timer is currently not active.");
       return true;
     }
     
     if ((!args[0].equalsIgnoreCase("remaining")) && (!args[0].equalsIgnoreCase("time")) && (!args[0].equalsIgnoreCase("left")) && (!args[0].equalsIgnoreCase("check"))) {
       printUsage(sender, label, pvpTimer);
       return true;
     }
     long remaining = pvpTimer.getRemaining(player);
     if (remaining <= 0L) {
       sender.sendMessage(ChatColor.RED + "Your " + pvpTimer.getDisplayName() + ChatColor.RED + " timer is currently not active.");
       return true;
     }
     sender.sendMessage(ChatColor.YELLOW + "Your " + pvpTimer.getDisplayName() + ChatColor.YELLOW + " timer is active for another " + ChatColor.BOLD + SurgeCore.getRemaining(remaining, true, false) + ChatColor.YELLOW + (pvpTimer.isPaused(player) ? " and is currently paused" : "") + '.');
     return true;
   }
   
   public java.util.List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
   {
     return args.length == 1 ? me.milksales.util.BukkitUtils.getCompletions(args, COMPLETIONS) : java.util.Collections.emptyList();
   }
   
   private void printUsage(CommandSender sender, String label, PvpProtectionTimer pvpTimer) {
     sender.sendMessage(ChatColor.AQUA + pvpTimer.getName() + " Help");
     sender.sendMessage(ChatColor.GRAY + "/" + label + " enable - Removes your " + pvpTimer.getDisplayName() + ChatColor.GRAY + " timer.");
     sender.sendMessage(ChatColor.GRAY + "/" + label + " time - Check remaining " + pvpTimer.getDisplayName() + ChatColor.GRAY + " time.");
     sender.sendMessage(ChatColor.GRAY + "/lives - Life and deathban related commands.");
   }
   
 
   private static final List<String> COMPLETIONS = ImmutableList.of("enable", "time");
   private final SurgeCore plugin;
 }


