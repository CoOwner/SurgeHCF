 package com.surgehcf.core.hcf.eventgame.argument;
 
  import me.milksales.util.command.CommandArgument;

import java.util.ArrayList;
 import java.util.Collections;
 import java.util.List;

import org.bukkit.ChatColor;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandSender;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.eventgame.EventType;
import com.surgehcf.core.hcf.eventgame.faction.KothFaction;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.type.Faction;
 
 public class EventCreateArgument extends CommandArgument
 {
   private final SurgeCore plugin;
   
   public EventCreateArgument(SurgeCore plugin)
   {
     super("create", "Defines a new event", new String[] { "make", "define" });
     this.plugin = plugin;
     this.permission = ("hcf.command.event.argument." + getName());
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + getName() + " <eventName> <Conquest|KOTH>";
   }
   
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
     if (args.length < 3) {
       sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
       return true;
     }
     Faction faction = this.plugin.getFactionManager().getFaction(args[1]);
     if (faction != null) {
       sender.sendMessage(ChatColor.RED + "There is already a faction named " + args[1] + '.');
       return true;
     }
     String upperCase2;
     String upperCase = upperCase2 = args[2].toUpperCase();
     switch (upperCase2) {
     case "CONQUEST": 
       faction = new com.surgehcf.core.hcf.eventgame.faction.ConquestFaction(args[1]);
       break;
     
     case "KOTH": 
       faction = new KothFaction(args[1]);
       break;
     
     default: 
       sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
       return true;
     }
     
     this.plugin.getFactionManager().createFaction(faction, sender);
     sender.sendMessage(ChatColor.YELLOW + "Created event faction " + ChatColor.WHITE + faction.getDisplayName(sender) + ChatColor.YELLOW + " with type " + org.apache.commons.lang.WordUtils.capitalizeFully(args[2]) + '.');
     return true;
   }
   
   public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
     if (args.length != 3) {
       return Collections.emptyList();
     }
     EventType[] eventTypes = EventType.values();
     List<String> results = new ArrayList(eventTypes.length);
     for (EventType eventType : eventTypes) {
       results.add(eventType.name());
     }
     return results;
   }
 }
