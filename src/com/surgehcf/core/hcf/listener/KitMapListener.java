package com.surgehcf.core.hcf.listener;

import me.milksales.base.kit.event.KitApplyEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.surgehcf.SurgeCore;

public class KitMapListener
  implements Listener
{
  final SurgeCore plugin;
  
  public KitMapListener(SurgeCore plugin)
  {
    this.plugin = plugin;
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
  public void onCreatureSpawn(CreatureSpawnEvent event) {}
  
  @EventHandler
  public void onJoin(PlayerJoinEvent e) {}
  
  @EventHandler
  public void onDeath(PlayerDeathEvent e) {}
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
  public void onPlayerDropItem(PlayerDropItemEvent event) {}
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onKitApplyMonitor(KitApplyEvent event) {}
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
  public void onItemSpawn(ItemSpawnEvent event) {}
}
