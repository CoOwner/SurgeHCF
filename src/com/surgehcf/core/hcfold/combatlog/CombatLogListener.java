package com.surgehcf.core.hcfold.combatlog;

import me.milksales.util.InventoryUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.type.Faction;
import com.surgehcf.core.hcf.timer.TimerManager;
import com.surgehcf.core.hcf.timer.type.PvpProtectionTimer;
import com.surgehcf.core.hcf.timer.type.TeleportTimer;
import com.surgehcf.core.hcfold.combatlog.CombatLogEntry;
import com.surgehcf.core.hcfold.combatlog.CombatLogListener;
import com.surgehcf.core.hcfold.combatlog.LoggerDeathEvent;
import com.surgehcf.core.hcfold.combatlog.LoggerEntity;
import com.surgehcf.core.hcfold.combatlog.LoggerSpawnEvent;

public class CombatLogListener
  implements Listener
{
  private static final int NEARBY_SPAWN_RADIUS = 64;
  private static final Set<UUID> SAFE_DISCONNECTS = new HashSet();
  private static final Map<UUID, CombatLogEntry> LOGGERS = new HashMap();
  private final SurgeCore plugin;
  
  public CombatLogListener(SurgeCore plugin)
  {
    this.plugin = plugin;
  }
  
  public static void safelyDisconnect(Player player, String reason)
  {
    if (SAFE_DISCONNECTS.add(player.getUniqueId())) {
      player.kickPlayer(reason);
    }
  }
  
  public static void removeCombatLoggers()
  {
    Iterator<CombatLogEntry> iterator = LOGGERS.values().iterator();
    while (iterator.hasNext())
    {
      CombatLogEntry entry = (CombatLogEntry)iterator.next();
      entry.task.cancel();
      entry.loggerEntity.getBukkitEntity().remove();
      iterator.remove();
    }
    SAFE_DISCONNECTS.clear();
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onPlayerQuitSafe(PlayerQuitEvent event)
  {
    SAFE_DISCONNECTS.remove(event.getPlayer().getUniqueId());
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
  public void onLoggerInteract(EntityInteractEvent event)
  {
    Collection<CombatLogEntry> entries = LOGGERS.values();
    Iterator<CombatLogEntry> var3 = entries.iterator();
    while (var3.hasNext())
    {
      CombatLogEntry entry = (CombatLogEntry)var3.next();
      if (entry.loggerEntity.getBukkitEntity().equals(event.getEntity()))
      {
        event.setCancelled(true);
        break;
      }
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onLoggerDeath(LoggerDeathEvent event)
  {
    CombatLogEntry entry = (CombatLogEntry)LOGGERS.remove(event.getLoggerEntity().getPlayerUUID());
    if (entry != null) {
      entry.task.cancel();
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
  public void onPlayerSpawnLocation(PlayerSpawnLocationEvent event)
  {
    CombatLogEntry combatLogEntry = (CombatLogEntry)LOGGERS.remove(event.getPlayer().getUniqueId());
    if (combatLogEntry != null)
    {
      CraftLivingEntity loggerEntity = combatLogEntry.loggerEntity.getBukkitEntity();
      Player player = event.getPlayer();
      event.setSpawnLocation(loggerEntity.getLocation());
      player.setFallDistance(loggerEntity.getFallDistance());
      player.setHealth(Math.min(((CraftPlayer)player).getMaxHealth(), loggerEntity.getHealth()));
      
      player.setRemainingAir(loggerEntity.getRemainingAir());
      loggerEntity.remove();
      combatLogEntry.task.cancel();
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
  public void onPlayerQuit(PlayerQuitEvent event)
  {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    PlayerInventory inventory = player.getInventory();
    if ((player.getGameMode() != GameMode.CREATIVE) && (!player.isDead()) && (!SAFE_DISCONNECTS.contains(uuid)))
    {
      if ((InventoryUtils.isEmpty(inventory)) || (this.plugin.getTimerManager().pvpProtectionTimer.getRemaining(uuid) > 0L)) {
        return;
      }
      if (this.plugin.getTimerManager().teleportTimer.getNearbyEnemies(player, 64) <= 0) {
        return;
      }
      Location location = player.getLocation();
      if (this.plugin.getFactionManager().getFactionAt(location).isSafezone()) {
        return;
      }
      if (LOGGERS.containsKey(player.getUniqueId())) {
        return;
      }
      World world = location.getWorld();
      LoggerEntity loggerEntity = new LoggerEntity(world, location, player);
      LoggerSpawnEvent calledEvent = new LoggerSpawnEvent(loggerEntity);
      Bukkit.getPluginManager().callEvent(calledEvent);
      LOGGERS.put(uuid, new CombatLogEntry(loggerEntity, new LoggerRemovable(uuid, loggerEntity).runTaskLater(this.plugin, 600L)));
      CraftLivingEntity craftEntity = loggerEntity.getBukkitEntity();
      if (craftEntity != null)
      {
        CraftLivingEntity craftLivingEntity = craftEntity;
        EntityEquipment entityEquipment = craftLivingEntity.getEquipment();
        entityEquipment.setItemInHand(inventory.getItemInHand());
        entityEquipment.setArmorContents(inventory.getArmorContents());
        craftLivingEntity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 1));
      }
    }
  }
  
  private static class LoggerRemovable
    extends BukkitRunnable
  {
    private final UUID uuid;
    private final LoggerEntity loggerEntity;
    
    public LoggerRemovable(UUID uuid, LoggerEntity loggerEntity)
    {
      this.uuid = uuid;
      this.loggerEntity = loggerEntity;
    }
    
    public void run()
    {
      if (CombatLogListener.LOGGERS.remove(this.uuid) != null) {
        this.loggerEntity.dead = true;
      }
    }
  }
}
