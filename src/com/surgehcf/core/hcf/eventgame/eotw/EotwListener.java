 package com.surgehcf.core.hcf.eventgame.eotw;
 
  import me.milksales.base.kit.event.KitApplyEvent;

import org.bukkit.event.EventHandler;
 import org.bukkit.event.EventPriority;
 import org.bukkit.event.entity.PlayerDeathEvent;
 import org.bukkit.event.player.PlayerKickEvent;
 import org.bukkit.event.player.PlayerQuitEvent;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.eventgame.eotw.EotwHandler;
import com.surgehcf.core.hcf.faction.event.FactionClaimChangeEvent;
 
 public class EotwListener implements org.bukkit.event.Listener
 {
   private final SurgeCore plugin;
   
   public EotwListener(SurgeCore plugin)
   {
     this.plugin = plugin;
   }
   
   @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
   public void onPlayerQuit(PlayerQuitEvent event) {
     EotwHandler.EotwRunnable runnable = this.plugin.getEotwHandler().getRunnable();
     if (runnable != null) {
       runnable.handleDisconnect(event.getPlayer());
     }
   }
   
   @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
   public void onPlayerKick(PlayerKickEvent event) {
     EotwHandler.EotwRunnable runnable = this.plugin.getEotwHandler().getRunnable();
     if (runnable != null) {
       runnable.handleDisconnect(event.getPlayer());
     }
   }
   
   @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
   public void onPlayerDeath(PlayerDeathEvent event) {
     EotwHandler.EotwRunnable runnable = this.plugin.getEotwHandler().getRunnable();
     if (runnable != null) {
       runnable.handleDisconnect(event.getEntity());
     }
   }
   
   @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
   public void onLandClaim(KitApplyEvent event) {
     if ((!event.isForce()) && (this.plugin.getEotwHandler().isEndOfTheWorld())) {
       event.setCancelled(true);
       event.getPlayer().sendMessage(org.bukkit.ChatColor.RED + "Kits cannot be applied during EOTW.");
     }
   }
   
   @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
   public void onFactionClaimChange(FactionClaimChangeEvent event) {
     if ((this.plugin.getEotwHandler().isEndOfTheWorld()) && (event.getCause() == com.surgehcf.core.hcf.faction.event.cause.ClaimChangeCause.CLAIM)) {
       com.surgehcf.core.hcf.faction.type.Faction faction = event.getClaimableFaction();
       if ((faction instanceof com.surgehcf.core.hcf.faction.type.PlayerFaction)) {
         event.setCancelled(true);
         event.getSender().sendMessage(org.bukkit.ChatColor.RED + "Player based faction land cannot be claimed during EOTW.");
       }
     }
   }
 }
