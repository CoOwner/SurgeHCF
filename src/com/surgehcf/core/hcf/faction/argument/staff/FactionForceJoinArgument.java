 package com.surgehcf.core.hcf.faction.argument.staff;
 
  import java.util.List;

import org.bukkit.ChatColor;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandSender;
 import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;
 
 public class FactionForceJoinArgument extends me.milksales.util.command.CommandArgument
 {
   private final SurgeCore plugin;
   
   public FactionForceJoinArgument(SurgeCore plugin)
   {
     super("forcejoin", "Forcefully join a faction.");
     this.plugin = plugin;
     this.permission = ("hcf.command.faction.argument." + getName());
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + getName() + " <factionName>";
   }
   
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
     if (!(sender instanceof Player)) {
       sender.sendMessage(ChatColor.RED + "Only players can join factions.");
       return true;
     }
     if (args.length < 2) {
       sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
       return true;
     }
     Player player = (Player)sender;
     PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
     if (playerFaction != null) {
       sender.sendMessage(ChatColor.RED + "You are already in a faction.");
       return true;
     }
     com.surgehcf.core.hcf.faction.type.Faction faction = this.plugin.getFactionManager().getContainingFaction(args[1]);
     if (faction == null) {
       sender.sendMessage(ChatColor.RED + "Faction named or containing member with IGN or UUID " + args[1] + " not found.");
       return true;
     }
     if (!(faction instanceof PlayerFaction)) {
       sender.sendMessage(ChatColor.RED + "You can only join player factions.");
       return true;
     }
     playerFaction = (PlayerFaction)faction;
     if (playerFaction.setMember(player, new com.surgehcf.core.hcf.faction.FactionMember(player, com.surgehcf.core.hcf.faction.struct.ChatChannel.PUBLIC, com.surgehcf.core.hcf.faction.struct.Role.MEMBER), true)) {
       playerFaction.broadcast(ChatColor.GOLD.toString() + ChatColor.BOLD + sender.getName() + " has forcefully joined the faction.");
     }
     return true;
   }
   
   public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
     if ((args.length != 2) || (!(sender instanceof Player))) {
       return java.util.Collections.emptyList();
     }
     if (args[1].isEmpty()) {
       return null;
     }
     Player player = (Player)sender;
     List<String> results = new java.util.ArrayList(this.plugin.getFactionManager().getFactionNameMap().keySet());
     for (Player target : org.bukkit.Bukkit.getOnlinePlayers()) {
       if ((player.canSee(target)) && (!results.contains(target.getName()))) {
         results.add(target.getName());
       }
     }
     return results;
   }
 }

