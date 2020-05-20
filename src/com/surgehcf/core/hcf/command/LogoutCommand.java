 package com.surgehcf.core.hcf.command;
 
 import org.bukkit.ChatColor;
 import org.bukkit.command.CommandSender;
 import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.timer.type.LogoutTimer;
 
 public class LogoutCommand implements org.bukkit.command.CommandExecutor, org.bukkit.command.TabCompleter
 {
   private final SurgeCore plugin;
   
   public LogoutCommand(SurgeCore plugin)
   {
     this.plugin = plugin;
   }
   
   public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
     if (!(sender instanceof Player)) {
       sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
       return true;
     }
     Player player = (Player)sender;
     LogoutTimer logoutTimer = this.plugin.getTimerManager().logoutTimer;
     if (!logoutTimer.setCooldown(player, player.getUniqueId())) {
       sender.sendMessage(ChatColor.RED + "Your " + logoutTimer.getDisplayName() + ChatColor.RED + " timer is already active.");
       return true;
     }
     sender.sendMessage(ChatColor.RED + "Your " + logoutTimer.getDisplayName() + ChatColor.RED + " timer has started.");
     return true;
   }
   
   public java.util.List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
     return java.util.Collections.emptyList();
   }
 }