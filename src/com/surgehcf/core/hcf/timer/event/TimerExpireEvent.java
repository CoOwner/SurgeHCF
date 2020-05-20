 package com.surgehcf.core.hcf.timer.event;
 
  import com.google.common.base.Optional;
import com.surgehcf.core.hcf.timer.Timer;

import java.util.UUID;

import org.bukkit.event.Event;
 import org.bukkit.event.HandlerList;
 
 
 
 
 
 public class TimerExpireEvent
   extends Event
 {
   public TimerExpireEvent(Timer timer)
   {
     this.userUUID = Optional.absent();
     this.timer = timer;
   }
   
   public TimerExpireEvent(UUID userUUID, Timer timer) {
     this.userUUID = Optional.fromNullable(userUUID);
     this.timer = timer;
   }
   
   public static HandlerList getHandlerList() {
     return handlers;
   }
   
   public Optional<UUID> getUserUUID() {
     return this.userUUID;
   }
   
   public Timer getTimer() {
     return this.timer;
   }
   
   public HandlerList getHandlers() {
     return handlers;
   }
   
 
   private static final HandlerList handlers = new HandlerList();
   private final Optional<UUID> userUUID;
   private final Timer timer;
 }


/* Location:              E:\Users\PUTTYDOTEXE\Desktop\Gabe\OLD\ZOlus\HCF (Zolus).jar!\com\exodon\hcf\timer\event\TimerExpireEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */