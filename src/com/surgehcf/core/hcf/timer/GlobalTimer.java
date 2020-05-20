 package com.surgehcf.core.hcf.timer;
 
 import org.bukkit.plugin.PluginManager;

import com.surgehcf.core.hcf.timer.Timer;
import com.surgehcf.core.hcf.timer.TimerRunnable;
import com.surgehcf.core.hcf.timer.event.TimerPauseEvent;
 
 public abstract class GlobalTimer extends Timer
 {
   private TimerRunnable runnable;
   
   public GlobalTimer(String name, long defaultCooldown)
   {
     super(name, defaultCooldown);
   }
   
   public boolean clearCooldown() {
     if (this.runnable != null) {
       this.runnable.cancel();
       this.runnable = null;
       return true;
     }
     return false;
   }
   
   public boolean isPaused() {
     return (this.runnable != null) && (this.runnable.isPaused());
   }
   
   public void setPaused(boolean paused) {
     if ((this.runnable != null) && (this.runnable.isPaused() != paused)) {
       TimerPauseEvent event = new TimerPauseEvent(this, paused);
       org.bukkit.Bukkit.getPluginManager().callEvent(event);
       if (!event.isCancelled()) {
         this.runnable.setPaused(paused);
       }
     }
   }
   
   public long getRemaining() {
     return this.runnable == null ? 0L : this.runnable.getRemaining();
   }
   
   public boolean setRemaining() {
     return setRemaining(this.defaultCooldown, false);
   }
   
   public boolean setRemaining(long duration, boolean overwrite) {
     boolean hadCooldown = false;
     if (this.runnable != null) {
       if (!overwrite) {
         return false;
       }
       com.surgehcf.core.hcf.timer.event.TimerExtendEvent event = new com.surgehcf.core.hcf.timer.event.TimerExtendEvent(this, this.runnable.getRemaining(), duration);
       org.bukkit.Bukkit.getPluginManager().callEvent(event);
       if (event.isCancelled()) {
         return false;
       }
       hadCooldown = this.runnable.getRemaining() > 0L;
       this.runnable.setRemaining(duration);
     }
     else {
       org.bukkit.Bukkit.getPluginManager().callEvent(new com.surgehcf.core.hcf.timer.event.TimerStartEvent(this, duration));
       this.runnable = new TimerRunnable(this, duration);
     }
     return !hadCooldown;
   }
 }


/* Location:              E:\Users\PUTTYDOTEXE\Desktop\Gabe\OLD\ZOlus\HCF (Zolus).jar!\com\exodon\hcf\timer\GlobalTimer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */