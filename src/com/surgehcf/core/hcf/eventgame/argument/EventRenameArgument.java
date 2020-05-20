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
 
 public class EventRenameArgument
   extends CommandArgument
 {
   private final SurgeCore plugin;
   
   public EventRenameArgument(SurgeCore plugin)
   {
     super("rename", "Renames an event");
     this.plugin = plugin;
     this.permission = ("hcf.command.event.argument." + getName());
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + getName() + " <oldName> <newName>";
   }
   
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
     if (args.length < 3) {
       sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
       return true;
     }
     Faction faction = this.plugin.getFactionManager().getFaction(args[2]);
     if (faction != null) {
       sender.sendMessage(ChatColor.RED + "There is already a faction named " + args[2] + '.');
       return true;
     }
     faction = this.plugin.getFactionManager().getFaction(args[1]);
     if (!(faction instanceof EventFaction)) {
       sender.sendMessage(ChatColor.RED + "There is not an event faction named '" + args[1] + "'.");
       return true;
     }
     String oldName = faction.getName();
     faction.setName(args[2], sender);
     sender.sendMessage(ChatColor.YELLOW + "Renamed event " + ChatColor.WHITE + oldName + ChatColor.YELLOW + " to " + ChatColor.WHITE + faction.getName() + ChatColor.YELLOW + '.');
     return true;
   }
   
   public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
     if (args.length != 2) {
       return Collections.emptyList();
     }
     return (List)this.plugin.getFactionManager();
   }
 }
