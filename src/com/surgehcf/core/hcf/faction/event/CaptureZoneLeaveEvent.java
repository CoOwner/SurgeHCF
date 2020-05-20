 package com.surgehcf.core.hcf.faction.event;
 
  import com.google.common.base.Preconditions;
import com.surgehcf.core.hcf.eventgame.CaptureZone;
import com.surgehcf.core.hcf.eventgame.faction.CapturableFaction;
import com.surgehcf.core.hcf.faction.event.FactionEvent;

import org.bukkit.entity.Player;
 import org.bukkit.event.Cancellable;
 import org.bukkit.event.HandlerList;
 
 
 
 public class CaptureZoneLeaveEvent
   extends FactionEvent
   implements Cancellable
 {
   public CaptureZoneLeaveEvent(Player player, CapturableFaction capturableFaction, CaptureZone captureZone)
   {
     super(capturableFaction);
     Preconditions.checkNotNull(player, "Player cannot be null");
     Preconditions.checkNotNull(captureZone, "Capture zone cannot be null");
     this.captureZone = captureZone;
     this.player = player;
   }
   
   public static HandlerList getHandlerList() {
     return handlers;
   }
   
   public CapturableFaction getFaction()
   {
     return (CapturableFaction)super.getFaction();
   }
   
   public CaptureZone getCaptureZone() {
     return this.captureZone;
   }
   
   public Player getPlayer() {
     return this.player;
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
   private final CaptureZone captureZone;
   private final Player player;
   private boolean cancelled;
 }


