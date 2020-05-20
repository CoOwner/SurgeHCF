 package com.surgehcf.core.hcf.timer.type;
 
  import java.util.HashMap;
 import java.util.UUID;

import org.bukkit.Bukkit;
 import org.bukkit.ChatColor;
 import org.bukkit.entity.Entity;
 import org.bukkit.entity.Player;
 import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.timer.event.TimerExpireEvent;
 
 public class ArcherTimer extends com.surgehcf.core.hcf.timer.PlayerTimer implements org.bukkit.event.Listener
 {
   private final SurgeCore plugin;
   
   public String getScoreboardPrefix()
   {
     return ChatColor.GRAY + "" + ChatColor.RED.toString();
   }
   
   public ArcherTimer(SurgeCore plugin) {
     super("Archer Mark", java.util.concurrent.TimeUnit.SECONDS.toMillis(10L));
     this.plugin = plugin;
   }
   
   public void run() {}
   
   @org.bukkit.event.EventHandler
   public void onExpire(TimerExpireEvent e)
   {
     if ((e.getUserUUID().isPresent()) && (e.getTimer().equals(this))) {
       UUID userUUID = (UUID)e.getUserUUID().get();
       Player player = Bukkit.getPlayer(userUUID);
       if (player == null) {
         return;
       }
       Bukkit.getPlayer((UUID)com.surgehcf.core.hcf.pvpclass.archer.ArcherClass.tagged.get(userUUID)).sendMessage(ChatColor.YELLOW + "Your archer mark on " + ChatColor.AQUA + player.getName() + ChatColor.YELLOW + " has expired.");
       player.sendMessage(ChatColor.YELLOW + "You're no longer archer marked.");
       com.surgehcf.core.hcf.pvpclass.archer.ArcherClass.tagged.remove(player.getUniqueId());
       for (Player players : Bukkit.getOnlinePlayers()) {
         this.plugin.getScoreboardHandler().getPlayerBoard(players.getUniqueId()).addUpdates(java.util.Arrays.asList(Bukkit.getOnlinePlayers()));
       }
     }
   }
   
   @org.bukkit.event.EventHandler
   public void onHit(EntityDamageByEntityEvent e) {
     if (((e.getEntity() instanceof Player)) && ((e.getDamager() instanceof Player))) {
       Player entity = (Player)e.getEntity();
       Entity damager = e.getDamager();
       if (getRemaining(entity) > 0L) {
         Double damage = Double.valueOf(e.getDamage() * 0.3D);
         e.setDamage(e.getDamage() + damage.doubleValue());
       }
     }
     if (((e.getEntity() instanceof Player)) && ((e.getDamager() instanceof org.bukkit.entity.Arrow))) {
       Player entity = (Player)e.getEntity();
       Entity damager = ((org.bukkit.entity.Arrow)e.getDamager()).getShooter();
       if (((damager instanceof Player)) && (getRemaining(entity) > 0L)) {
         if (((UUID)com.surgehcf.core.hcf.pvpclass.archer.ArcherClass.tagged.get(entity.getUniqueId())).equals(damager.getUniqueId())) {
           setCooldown(entity, entity.getUniqueId());
         }
         Double damage = Double.valueOf(e.getDamage() * 0.3D);
         e.setDamage(e.getDamage() + damage.doubleValue());
       }
     }
   }
 }


/* Location:              E:\Users\PUTTYDOTEXE\Desktop\Gabe\OLD\ZOlus\HCF (Zolus).jar!\com\exodon\hcf\timer\type\ArcherTimer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */