 package com.surgehcf.core.hcf.deathban.lives.argument;
 
 import org.bukkit.ChatColor;
 import org.bukkit.OfflinePlayer;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandSender;
 import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
 
 public class LivesGiveArgument extends me.milksales.util.command.CommandArgument
 {
   private final SurgeCore plugin;
   
   public LivesGiveArgument(SurgeCore plugin)
   {
     super("give", "Give lives to a player");
     this.plugin = plugin;
     this.aliases = new String[] { "transfer", "send", "pay", "add" };
     this.permission = ("hcf.command.lives.argument." + getName());
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + getName() + " <playerName> <amount>";
   }
   
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
     if (args.length < 3) {
       sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
       return true;
     }
     Integer amount = net.minecraft.util.com.google.common.primitives.Ints.tryParse(args[2]);
     if (amount == null) {
       sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a number.");
       return true;
     }
     if (amount.intValue() <= 0) {
       sender.sendMessage(ChatColor.RED + "The amount of lives must be positive.");
       return true;
     }
     OfflinePlayer target = org.bukkit.Bukkit.getOfflinePlayer(args[1]);
     if ((!target.hasPlayedBefore()) && (!target.isOnline())) {
       sender.sendMessage(ChatColor.GOLD + "Player '" + ChatColor.WHITE + args[1] + ChatColor.GOLD + "' not found.");
       return true;
     }
     Player onlineTarget = target.getPlayer();
     if ((sender instanceof Player)) {
       Player player = (Player)sender;
       int ownedLives = this.plugin.getDeathbanManager().getLives(player.getUniqueId());
       if (amount.intValue() > ownedLives) {
         sender.sendMessage(ChatColor.RED + "You tried to give " + target.getName() + ' ' + amount + " lives, but you only have " + ownedLives + '.');
         return true;
       }
       this.plugin.getDeathbanManager().takeLives(player.getUniqueId(), amount.intValue());
     }
     this.plugin.getDeathbanManager().addLives(target.getUniqueId(), amount.intValue());
     sender.sendMessage(ChatColor.YELLOW + "You have sent " + ChatColor.GOLD + target.getName() + ChatColor.YELLOW + ' ' + amount + ' ' + (amount.intValue() > 1 ? "life" : "lives") + '.');
     if (onlineTarget != null) {
       onlineTarget.sendMessage(ChatColor.GOLD + sender.getName() + ChatColor.YELLOW + " has sent you " + ChatColor.GOLD + amount + ' ' + (amount.intValue() > 1 ? "life" : "lives") + '.');
     }
     return true;
   }
   
   public java.util.List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
     return args.length == 2 ? null : java.util.Collections.emptyList();
   }
 }

