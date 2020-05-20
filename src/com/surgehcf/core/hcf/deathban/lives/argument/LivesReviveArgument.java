 package com.surgehcf.core.hcf.deathban.lives.argument;
 
  import java.util.List;

import org.bukkit.ChatColor;
 import org.bukkit.OfflinePlayer;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandSender;
 import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.deathban.Deathban;
import com.surgehcf.core.hcf.faction.struct.Relation;
import com.surgehcf.core.hcf.user.FactionUser;
 
 public class LivesReviveArgument extends me.milksales.util.command.CommandArgument
 {
   private static final String REVIVE_BYPASS_PERMISSION = "hcf.revive.bypass";
   private static final String PROXY_CHANNEL_NAME = "BungeeCord";
   private final SurgeCore plugin;
   
   public LivesReviveArgument(SurgeCore plugin)
   {
     super("revive", "Revive a death-banned player");
     this.plugin = plugin;
     this.permission = ("hcf.command.lives.argument." + getName());
     plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + getName() + " <playerName>";
   }
   
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
     if (args.length < 2) {
       sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
       return true;
     }
     OfflinePlayer target = org.bukkit.Bukkit.getOfflinePlayer(args[1]);
     if ((!target.hasPlayedBefore()) && (!target.isOnline())) {
       sender.sendMessage(ChatColor.GOLD + "Player '" + ChatColor.WHITE + args[1] + ChatColor.GOLD + "' not found.");
       return true;
     }
     java.util.UUID targetUUID = target.getUniqueId();
     FactionUser factionTarget = this.plugin.getUserManager().getUser(targetUUID);
     Deathban deathban = factionTarget.getDeathban();
     if ((deathban == null) || (!deathban.isActive())) {
       sender.sendMessage(ChatColor.RED + target.getName() + " is not death-banned.");
       return true;
     }
     Relation relation = Relation.ENEMY;
     if ((sender instanceof Player)) {
       if ((!sender.hasPermission("hcf.revive.bypass")) && (this.plugin.getEotwHandler().isEndOfTheWorld())) {
         sender.sendMessage(ChatColor.RED + "You cannot revive players during EOTW.");
         return true;
       }
       if (!sender.hasPermission("hcf.revive.bypass")) {
         Player player = (Player)sender;
         java.util.UUID playerUUID = player.getUniqueId();
         int selfLives = this.plugin.getDeathbanManager().getLives(playerUUID);
         if (selfLives <= 0) {
           sender.sendMessage(ChatColor.RED + "You do not have any lives.");
           return true;
         }
         this.plugin.getDeathbanManager().setLives(playerUUID, selfLives - 1);
         com.surgehcf.core.hcf.faction.type.PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
         relation = playerFaction == null ? Relation.ENEMY : playerFaction.getFactionRelation(this.plugin.getFactionManager().getPlayerFaction(targetUUID));
         sender.sendMessage(ChatColor.YELLOW + "You have revived " + relation.toChatColour() + target.getName() + ChatColor.YELLOW + '.');
       }
       else {
         sender.sendMessage(ChatColor.YELLOW + "You have revived " + relation.toChatColour() + target.getName() + ChatColor.YELLOW + '.');
       }
     }
     else {
       sender.sendMessage(ChatColor.YELLOW + "You have revived " + com.surgehcf.core.hcf.CoreConfiguration.ENEMY_COLOUR + target.getName() + ChatColor.YELLOW + '.');
     }
     factionTarget.removeDeathban();
     return true;
   }
   
   public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
     if (args.length != 2) {
       return java.util.Collections.emptyList();
     }
     List<String> results = new java.util.ArrayList();
     java.util.Collection<FactionUser> factionUsers = this.plugin.getUserManager().getUsers().values();
     for (FactionUser factionUser : factionUsers) {
       Deathban deathban = factionUser.getDeathban();
       if (deathban != null)
         if (deathban.isActive())
         {
 
           OfflinePlayer offlinePlayer = org.bukkit.Bukkit.getOfflinePlayer(factionUser.getUserUUID());
           String offlineName = offlinePlayer.getName();
           if (offlineName != null)
           {
 
             results.add(offlinePlayer.getName()); }
         }
     }
     return results;
   }
 }