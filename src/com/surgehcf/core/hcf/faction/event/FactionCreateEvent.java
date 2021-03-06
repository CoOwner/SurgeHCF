 package com.surgehcf.core.hcf.faction.event;
 
 import org.bukkit.command.CommandSender;
 import org.bukkit.event.Cancellable;
 import org.bukkit.event.HandlerList;

import com.surgehcf.core.hcf.faction.event.FactionEvent;
import com.surgehcf.core.hcf.faction.type.Faction;
 
 public class FactionCreateEvent
   extends FactionEvent
   implements Cancellable
 {
   public FactionCreateEvent(Faction faction, CommandSender sender)
   {
     super(faction);
     this.sender = sender;
   }
   
   public static HandlerList getHandlerList() {
     return handlers;
   }
   
   public CommandSender getSender() {
     return this.sender;
   }
   
   public boolean isCancelled() {
     return this.cancelled;
   }
   
   public void setCancelled(boolean cancelled) {
     this.cancelled = cancelled;
   }
   
   public HandlerList getHandlers() {
     return handlers;
   }
   
 
   private static final HandlerList handlers = new HandlerList();
   private final CommandSender sender;
   private boolean cancelled;
 }

