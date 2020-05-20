 package com.surgehcf.core.hcf.eventgame.argument;
 
  import me.milksales.util.command.CommandArgument;

import java.util.Collections;
 import java.util.List;

import org.bukkit.ChatColor;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandSender;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.eventgame.faction.EventFaction;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.type.Faction;
 
 public class EventDeleteArgument
   extends CommandArgument
 {
   private final SurgeCore plugin;
   
   public EventDeleteArgument(SurgeCore plugin)
   {
     super("delete", "Deletes an event");
     this.plugin = plugin;
     this.aliases = new String[] { "remove", "del" };
     this.permission = ("hcf.command.event.argument." + getName());
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + getName() + " <eventName>";
   }
   
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
     if (args.length < 2) {
       sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
       return true;
     }
     Faction faction = this.plugin.getFactionManager().getFaction(args[1]);
     if (!(faction instanceof EventFaction)) {
       sender.sendMessage(ChatColor.RED + "There is not an event faction named '" + args[1] + "'.");
       return true;
     }
     if (this.plugin.getFactionManager().removeFaction(faction, sender)) {
       sender.sendMessage(ChatColor.YELLOW + "Deleted event faction " + ChatColor.WHITE + faction.getDisplayName(sender) + ChatColor.YELLOW + '.');
     }
     return true;
   }
   
   public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
     if (args.length != 2) {
       return Collections.emptyList();
     }
     return (List)this.plugin.getFactionManager();
   }
 }
