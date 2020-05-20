package com.surgehcf.core.hcf.listener;

import java.util.UUID;

import net.minecraft.util.gnu.trove.map.TObjectLongMap;
import net.minecraft.util.gnu.trove.map.hash.TObjectLongHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffectType;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.timer.PlayerTimer;
import com.surgehcf.core.hcf.timer.TimerManager;

public class PortalListener
  implements Listener
{
  private static final long PORTAL_MESSAGE_DELAY_THRESHOLD = 2500L;
  private final Location endExit;
  private final TObjectLongMap<UUID> messageDelays;
  private final SurgeCore plugin;
  
  public PortalListener(SurgeCore plugin)
  {
    this.endExit = new Location(Bukkit.getWorld("world"), 0.0D, 67.5D, 500.0D);
    this.messageDelays = new TObjectLongHashMap();
    this.plugin = plugin;
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
  public void onEntityPortal(EntityPortalEvent event)
  {
    if ((event.getEntity() instanceof EnderDragon)) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
  public void onPlayerPortal(PlayerPortalEvent event)
  {
    if (event.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) {
      return;
    }
    World toWorld = event.getTo().getWorld();
    if ((toWorld != null) && (toWorld.getEnvironment() == World.Environment.THE_END))
    {
      event.useTravelAgent(false);
      event.setTo(toWorld.getSpawnLocation());
      return;
    }
    World fromWorld = event.getFrom().getWorld();
    if ((fromWorld != null) && (fromWorld.getEnvironment() == World.Environment.THE_END))
    {
      event.useTravelAgent(false);
      event.setTo(this.endExit);
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
  public void onWorldChanged(PlayerChangedWorldEvent event)
  {
    Player player = event.getPlayer();
    World from = event.getFrom();
    World to = player.getWorld();
    if ((from.getEnvironment() != World.Environment.THE_END) && (to.getEnvironment() == World.Environment.THE_END) && (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))) {
      ((CraftPlayer)player).removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
  public void onPortalEnter(PlayerPortalEvent event)
  {
    if (event.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) {
      return;
    }
    Location to = event.getTo();
    World toWorld = to.getWorld();
    if (toWorld == null) {
      return;
    }
    if (toWorld.getEnvironment() == World.Environment.THE_END)
    {
      Player player = event.getPlayer();
      PlayerTimer timer = this.plugin.getTimerManager().spawnTagTimer;
      long remaining;
      if ((remaining = timer.getRemaining(player)) > 0L)
      {
        message(player, ChatColor.YELLOW + "You cannot enter the End whilst your " + timer.getDisplayName() + ChatColor.RED + " timer is active [" + ChatColor.BOLD + SurgeCore.getRemaining(remaining, true, false) + ChatColor.RED + " remaining]");
        event.setCancelled(true);
        return;
      }
      timer = this.plugin.getTimerManager().pvpProtectionTimer;
      if ((remaining = timer.getRemaining(player)) > 0L)
      {
        message(player, ChatColor.YELLOW + "You cannot enter the End whilst your " + timer.getDisplayName() + ChatColor.RED + " timer is active [" + ChatColor.BOLD + SurgeCore.getRemaining(remaining, true, false) + ChatColor.RED + " remaining]");
        event.setCancelled(true);
        return;
      }
      event.useTravelAgent(false);
      event.setTo(toWorld.getSpawnLocation().add(0.5D, 0.0D, 0.5D));
    }
  }
  
  private void message(Player player, String message)
  {
    long last = this.messageDelays.get(player.getUniqueId());
    long millis = System.currentTimeMillis();
    if ((last != this.messageDelays.getNoEntryValue()) && (last + 2500L - millis > 0L)) {
      return;
    }
    this.messageDelays.put(player.getUniqueId(), millis);
    player.sendMessage(message);
  }

    @EventHandler
    public void teleportToSpawn(final PlayerJoinEvent e) {
    final Player p = e.getPlayer();
    final Location loc = p.getLocation();
    if (loc.getBlock().getType() == Material.PORTAL) {
        p.teleport(loc.getWorld().getSpawnLocation());
        p.sendMessage(ChatColor.GREEN + "You have been teleported to spawn to protect you from a nether portal trap.");
    }
  }
}
    