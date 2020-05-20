 package com.surgehcf.core.hcf.command;
 
 import org.bukkit.ChatColor;
 import org.bukkit.OfflinePlayer;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandSender;
 import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.CooldownTimers;
import com.surgehcf.core.hcf.deathban.Deathban;
import com.surgehcf.core.hcf.faction.struct.Relation;
import com.surgehcf.core.hcf.user.FactionUser;
 
 public class SurgeCommand implements org.bukkit.command.CommandExecutor
 {
   private final SurgeCore plugin;
   
   public SurgeCommand(SurgeCore plugin)
   {
     this.plugin = plugin;
     plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + "revive <playerName>";
   }
   
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
     Player p = (Player)sender;
     if (args.length < 2) {
       sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
       return true;
     }
     if (CooldownTimers.isOnCooldown("Rogue_cooldown", p)) {
       p.sendMessage(ChatColor.YELLOW + "You still have an " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Revive" + ChatColor.YELLOW + " cooldown for another " + SurgeCore.getRemaining(CooldownTimers.getCooldownForPlayerLong("Rogue_cooldown", p), true) + ChatColor.YELLOW + '.');
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
       Player player = (Player)sender;
       java.util.UUID playerUUID = player.getUniqueId();
       int selfLives = this.plugin.getDeathbanManager().getLives(playerUUID);
       CooldownTimers.addCooldown("Rogue_cooldown", p, 5400);
       com.surgehcf.core.hcf.faction.type.PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
       relation = playerFaction == null ? Relation.ENEMY : playerFaction.getFactionRelation(this.plugin.getFactionManager().getPlayerFaction(targetUUID));
       sender.sendMessage(ChatColor.YELLOW + "You have revived " + relation.toChatColour() + target.getName() + ChatColor.YELLOW + '.');
       org.bukkit.Bukkit.broadcastMessage("�7(�6*�7) �6" + p.getName() + " �eused their �9Pot �erank to revive �c" + target.getName() + "�e!");
     }
     factionTarget.removeDeathban();
     return true;
   }
   
   public java.util.List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
     if (args.length != 2) {
       return java.util.Collections.emptyList();
     }
     java.util.List<String> results = new java.util.ArrayList();
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