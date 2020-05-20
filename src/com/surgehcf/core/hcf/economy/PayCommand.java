 package com.surgehcf.core.hcf.economy;
 
 import org.bukkit.ChatColor;
 import org.bukkit.OfflinePlayer;
 import org.bukkit.command.CommandSender;
 import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
 
 public class PayCommand implements org.bukkit.command.CommandExecutor, org.bukkit.command.TabCompleter
 {
   private final SurgeCore plugin;
   
   public PayCommand(SurgeCore plugin)
   {
     this.plugin = plugin;
   }
   
   public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
     if (args.length < 2) {
       sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <playerName> <amount>");
       return true;
     }
     Integer amount = net.minecraft.util.com.google.common.primitives.Ints.tryParse(args[1]);
     if (amount == null) {
       sender.sendMessage(ChatColor.RED + "'" + args[1] + "' is not a valid number.");
       return true;
     }
     if (amount.intValue() <= 0) {
       sender.sendMessage(ChatColor.RED + "You must send money in positive quantities.");
       return true;
     }
     if (amount.intValue() >= 30000) {
       sender.sendMessage(ChatColor.RED + "You cannot send more than " + 30000 + ".");
       return true;
     }
     Player senderPlayer = (Player)sender;
     int senderBalance = senderPlayer != null ? this.plugin.getEconomyManager().getBalance(senderPlayer.getUniqueId()) : 1024;
     if (senderBalance < amount.intValue()) {
       sender.sendMessage(ChatColor.RED + "You tried to pay " + '$' + amount + ", but you only have " + '$' + senderBalance + " in your bank account.");
       return true;
     }
     OfflinePlayer target = org.bukkit.Bukkit.getOfflinePlayer(args[0]);
     if (sender.equals(target)) {
       sender.sendMessage(ChatColor.RED + "You cannot send money to yourself.");
       return true;
     }
     Player targetPlayer = target.getPlayer();
     if ((!target.hasPlayedBefore()) && (targetPlayer == null)) {
       sender.sendMessage(ChatColor.GOLD + "Player '" + ChatColor.WHITE + args[0] + ChatColor.GOLD + "' not found.");
       return true;
     }
     if (targetPlayer == null) {
       return false;
     }
     if (senderPlayer != null) {
       this.plugin.getEconomyManager().subtractBalance(senderPlayer.getUniqueId(), amount.intValue());
     }
     this.plugin.getEconomyManager().addBalance(targetPlayer.getUniqueId(), amount.intValue());
     targetPlayer.sendMessage(ChatColor.YELLOW + sender.getName() + " has sent you " + ChatColor.GOLD + '$' + amount + ChatColor.YELLOW + '.');
     sender.sendMessage(ChatColor.YELLOW + "You have sent " + ChatColor.GREEN + '$' + amount + ChatColor.YELLOW + " to " + target.getName() + '.');
     return true;
   }
   
   public java.util.List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
     return args.length == 1 ? null : java.util.Collections.emptyList();
   }
 }
