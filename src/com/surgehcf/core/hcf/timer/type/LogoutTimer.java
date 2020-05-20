 package com.surgehcf.core.hcf.timer.type;
 
 import org.bukkit.ChatColor;
 import org.bukkit.Location;
 import org.bukkit.entity.Player;
 import org.bukkit.event.EventHandler;
 import org.bukkit.event.EventPriority;
 import org.bukkit.event.player.PlayerKickEvent;
 import org.bukkit.event.player.PlayerMoveEvent;
 import org.bukkit.event.player.PlayerQuitEvent;
 import org.bukkit.event.player.PlayerTeleportEvent;
 
 public class LogoutTimer extends com.surgehcf.core.hcf.timer.PlayerTimer implements org.bukkit.event.Listener
 {
   public LogoutTimer()
   {
     super("Logout", java.util.concurrent.TimeUnit.SECONDS.toMillis(30L), false);
   }
   
   public String getScoreboardPrefix() {
     return ChatColor.RED.toString();
   }
   
   private void checkMovement(Player player, Location from, Location to) {
     if ((from.getBlockX() == to.getBlockX()) && (from.getBlockZ() == to.getBlockZ())) {
       return;
     }
     if (getRemaining(player) > 0L) {
       player.sendMessage(ChatColor.RED + "You moved a block, " + getDisplayName() + ChatColor.RED + " timer cancelled.");
       clearCooldown(player);
     }
   }
   
   @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
   public void onPlayerMove(PlayerMoveEvent event) {
     checkMovement(event.getPlayer(), event.getFrom(), event.getTo());
   }
   
   @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
   public void onPlayerTeleport(PlayerTeleportEvent event) {
     checkMovement(event.getPlayer(), event.getFrom(), event.getTo());
   }
   
   @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
   public void onPlayerKick(PlayerKickEvent event) {
     java.util.UUID uuid = event.getPlayer().getUniqueId();
     if (getRemaining(event.getPlayer().getUniqueId()) > 0L) {
       clearCooldown(uuid);
     }
   }
   
   @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
   public void onPlayerQuit(PlayerQuitEvent event) {
     java.util.UUID uuid = event.getPlayer().getUniqueId();
     if (getRemaining(event.getPlayer().getUniqueId()) > 0L) {
       clearCooldown(uuid);
     }
   }
   
   @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
   public void onPlayerDamage(org.bukkit.event.entity.EntityDamageEvent event) {
     org.bukkit.entity.Entity entity = event.getEntity();
     if ((entity instanceof Player)) {
       Player player = (Player)entity;
       if (getRemaining(player) > 0L) {
         player.sendMessage(ChatColor.RED + "You were damaged, " + getDisplayName() + ChatColor.RED + " timer ended.");
         clearCooldown(player);
       }
     }
   }
   
   public void onExpire(java.util.UUID userUUID)
   {
     Player player = org.bukkit.Bukkit.getPlayer(userUUID);
     if (player == null) {
       return;
     }
     com.surgehcf.core.hcfold.combatlog.CombatLogListener.safelyDisconnect(player, ChatColor.GREEN + "You have been safely logged out.");
   }
   
   public void run(Player player) {
     long remainingMillis = getRemaining(player);
     if (remainingMillis > 0L) {
       player.sendMessage(getDisplayName() + ChatColor.BLUE + " timer is disconnecting you in " + ChatColor.BOLD + com.surgehcf.SurgeCore.getRemaining(remainingMillis, true, false) + ChatColor.BLUE + '.');
     }
   }
 }


/* Location:              E:\Users\PUTTYDOTEXE\Desktop\Gabe\OLD\ZOlus\HCF (Zolus).jar!\com\exodon\hcf\timer\type\LogoutTimer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */