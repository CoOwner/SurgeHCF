 package com.surgehcf.core.hcf.faction.argument;
 
  import me.milksales.util.command.CommandArgument;

import java.util.ArrayList;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Set;

import org.apache.commons.lang.StringUtils;
 import org.bukkit.ChatColor;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandSender;
 import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.type.Faction;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;
 
 
 public class FactionInvitesArgument
   extends CommandArgument
 {
   private final SurgeCore plugin;
   
   public FactionInvitesArgument(SurgeCore plugin)
   {
     super("invites", "View faction invitations.");
     this.plugin = plugin;
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + getName();
   }
   
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
     if (!(sender instanceof Player)) {
       sender.sendMessage(ChatColor.RED + "Only players can have faction invites.");
       return true;
     }
     List<String> receivedInvites = new ArrayList();
     for (Faction faction : this.plugin.getFactionManager().getFactions()) {
       if ((faction instanceof PlayerFaction)) {
         PlayerFaction targetPlayerFaction = (PlayerFaction)faction;
         if (targetPlayerFaction.getInvitedPlayerNames().contains(sender.getName()))
         {
 
           receivedInvites.add(targetPlayerFaction.getDisplayName(sender)); }
       }
     }
     PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(((Player)sender).getUniqueId());
     String delimiter = ChatColor.WHITE + ", " + ChatColor.GRAY;
     if (playerFaction != null) {
       Set<String> sentInvites = playerFaction.getInvitedPlayerNames();
       sender.sendMessage(ChatColor.YELLOW + "Sent by " + playerFaction.getDisplayName(sender) + ChatColor.YELLOW + " (" + sentInvites.size() + ')' + ChatColor.YELLOW + ": " + ChatColor.GRAY + (sentInvites.isEmpty() ? "Your faction has not invited anyone." : new StringBuilder().append(StringUtils.join((Iterator)sentInvites, delimiter)).append('.').toString()));
     }
     sender.sendMessage(ChatColor.YELLOW + "Requested (" + receivedInvites.size() + ')' + ChatColor.YELLOW + ": " + ChatColor.GRAY + (receivedInvites.isEmpty() ? "No factions have invited you." : new StringBuilder().append(StringUtils.join((Iterator)receivedInvites, new StringBuilder().append(ChatColor.WHITE).append(delimiter).toString())).append('.').toString()));
     return true;
   }
 }

