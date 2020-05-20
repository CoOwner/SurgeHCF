 package com.surgehcf.core.hcf.deathban.lives.argument;
 
 import org.bukkit.ChatColor;
 import org.bukkit.OfflinePlayer;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandSender;

import com.surgehcf.SurgeCore;
 
 public class LivesCheckArgument extends me.milksales.util.command.CommandArgument
 {
   private final SurgeCore plugin;
   
   public LivesCheckArgument(SurgeCore plugin)
   {
     super("check", "Check how much lives a player has");
     this.plugin = plugin;
     this.permission = ("hcf.command.lives.argument." + getName());
   }
   
 
   public String getUsage(String label) { return '/' + label + ' ' + getName() + " <playerName>"; }
   
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
     OfflinePlayer target;
     if (args.length > 1) {
       target = org.bukkit.Bukkit.getOfflinePlayer(args[1]);
     }
     else {
       if (!(sender instanceof org.bukkit.entity.Player)) {
         sender.sendMessage("§eUsage §6» §r" + getUsage(label));
         return true;
       }
       target = (OfflinePlayer)sender;
     }
     if ((!target.hasPlayedBefore()) && (!target.isOnline())) {
       sender.sendMessage("§cError §6» §rThe player §e" + args[1] + " §rwas not found.");
       return true;
     }
     int targetLives = this.plugin.getDeathbanManager().getLives(target.getUniqueId());
     sender.sendMessage("§eThe player §d" + target.getName() + " §ehas §4\u2764§c" + targetLives + (targetLives == 1 ? " life" : " lives") + '.');
     return true;
   }
   
   public java.util.List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
     return args.length == 2 ? null : java.util.Collections.emptyList();
   }
 }
