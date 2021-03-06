 package com.surgehcf.core.hcf.faction.event;
 
 import org.bukkit.event.Cancellable;
 import org.bukkit.event.Event;
 import org.bukkit.event.HandlerList;

import com.surgehcf.core.hcf.faction.struct.Relation;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;
 
 
 public class FactionRelationCreateEvent
   extends Event
   implements Cancellable
 {
   public FactionRelationCreateEvent(PlayerFaction senderFaction, PlayerFaction targetFaction, Relation relation)
   {
     this.senderFaction = senderFaction;
     this.targetFaction = targetFaction;
     this.relation = relation;
   }
   
   public static HandlerList getHandlerList() {
     return handlers;
   }
   
   public PlayerFaction getSenderFaction() {
     return this.senderFaction;
   }
   
   public PlayerFaction getTargetFaction() {
     return this.targetFaction;
   }
   
   public Relation getRelation() {
     return this.relation;
   }
   
   public boolean isCancelled() {
     return this.cancelled;
   }
   
   public void setCancelled(boolean cancel) {
     this.cancelled = cancel;
   }
   
   public HandlerList getHandlers() {
     return handlers;
   }
   
 
   private static final HandlerList handlers = new HandlerList();
   private final PlayerFaction senderFaction;
   private final PlayerFaction targetFaction;
   private final Relation relation;
   private boolean cancelled;
 }
