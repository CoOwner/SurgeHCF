 package com.surgehcf.core.hcf.timer.type;
 
  import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
 import org.bukkit.entity.Player;
 import org.bukkit.event.EventHandler;
 import org.bukkit.event.EventPriority;
 import org.bukkit.event.entity.EntityDamageEvent;
 import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.surgehcf.core.hcf.timer.GlobalTimer;

import org.bukkit.event.entity.FoodLevelChangeEvent;
 
 public class SOTWTimer extends GlobalTimer implements org.bukkit.event.Listener
 {
   public SOTWTimer()
   {
     super("SOTW", TimeUnit.MINUTES.toMillis(30L));
   }
   
   public void run() {
     long remainingMillis = getRemaining();
     if (remainingMillis > 0L)
       org.bukkit.Bukkit.broadcastMessage(ChatColor.YELLOW + "SOTW will start in " + ChatColor.RED + com.surgehcf.SurgeCore.getRemaining(getRemaining(), true));
   }
   
   @EventHandler(priority=EventPriority.HIGHEST)
   public void onDamage(EntityDamageEvent e) {
     if (((e.getEntity() instanceof Player)) && (e.getCause() != EntityDamageEvent.DamageCause.VOID) && (getRemaining() > 0L))
       e.setCancelled(true);
   }
   
   @EventHandler(priority=EventPriority.MONITOR)
   public void onFoodLevelChange(FoodLevelChangeEvent e) {
     if (((e.getEntity() instanceof Player)) && (getRemaining() > 0L)) {
       e.setCancelled(true);
       e.setFoodLevel(20);
     }
   }
   
   public String getScoreboardPrefix() {
     return ChatColor.GOLD.toString();
   }
 }


/* Location:              E:\Users\PUTTYDOTEXE\Desktop\Gabe\OLD\ZOlus\HCF (Zolus).jar!\com\exodon\hcf\timer\type\SOTWTimer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */