 package com.surgehcf.core.hcf.eventgame.argument;
 
 import org.bukkit.ChatColor;
 import org.bukkit.command.CommandSender;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.eventgame.EventTimer;
import com.surgehcf.core.hcf.faction.type.Faction;
 
 public class EventCancelArgument extends me.milksales.util.command.CommandArgument
 {
   private final SurgeCore plugin;
   
   public EventCancelArgument(SurgeCore plugin)
   {
     super("cancel", "Cancels a running event", new String[] { "stop", "end" });
     this.plugin = plugin;
     this.permission = ("hcf.command.event.argument." + getName());
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + getName();
   }
   
   public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
     EventTimer eventTimer = this.plugin.getTimerManager().eventTimer;
     Faction eventFaction = eventTimer.getEventFaction();
     if (!eventTimer.clearCooldown()) {
       sender.sendMessage(ChatColor.RED + "There is not a running event.");
       return true;
     }
     return true;
   }
 }

