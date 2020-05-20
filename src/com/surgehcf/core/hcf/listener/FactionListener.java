package com.surgehcf.core.hcf.listener;

import com.google.common.base.Optional;
import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.eventgame.CaptureZone;
import com.surgehcf.core.hcf.eventgame.eotw.EotwHandler;
import com.surgehcf.core.hcf.eventgame.faction.CapturableFaction;
import com.surgehcf.core.hcf.eventgame.faction.KothFaction;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.FactionMember;
import com.surgehcf.core.hcf.faction.event.CaptureZoneEnterEvent;
import com.surgehcf.core.hcf.faction.event.CaptureZoneLeaveEvent;
import com.surgehcf.core.hcf.faction.event.FactionCreateEvent;
import com.surgehcf.core.hcf.faction.event.FactionRemoveEvent;
import com.surgehcf.core.hcf.faction.event.FactionRenameEvent;
import com.surgehcf.core.hcf.faction.event.PlayerClaimEnterEvent;
import com.surgehcf.core.hcf.faction.event.PlayerJoinFactionEvent;
import com.surgehcf.core.hcf.faction.event.PlayerLeaveFactionEvent;
import com.surgehcf.core.hcf.faction.event.PlayerLeftFactionEvent;
import com.surgehcf.core.hcf.faction.struct.RegenStatus;
import com.surgehcf.core.hcf.faction.struct.Role;
import com.surgehcf.core.hcf.faction.type.Faction;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;
import com.surgehcf.core.hcf.user.FactionUser;
import com.surgehcf.core.hcf.user.UserManager;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ChatColor;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class FactionListener
  implements Listener
{//§6»
  private static final long FACTION_JOIN_WAIT_MILLIS = TimeUnit.SECONDS.toMillis(30L);
  private static final String FACTION_JOIN_WAIT_WORDS = DurationFormatUtils.formatDurationWords(FACTION_JOIN_WAIT_MILLIS, true, true);
  private static final String LAND_CHANGED_META_KEY = "landChangedMessage";
  private static final long LAND_CHANGE_MSG_THRESHOLD = 225L;
  private final SurgeCore plugin;
  
  public FactionListener(SurgeCore plugin)
  {
    this.plugin = plugin;
  }
  
  public String C(String msg)
  {
    return ChatColor.translateAlternateColorCodes('&', msg);
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onFactionCreate(FactionCreateEvent event)
  {
    Faction faction = event.getFaction();
    if ((faction instanceof PlayerFaction))
    {
      CommandSender sender = event.getSender();
      
      Bukkit.broadcastMessage(C("&6» &e" + event.getFaction().getName() + "&r has been created by &a" + sender.getName() + "&r."));
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onFactionRemove(FactionRemoveEvent event)
  {
    Faction faction = event.getFaction();
    if ((faction instanceof PlayerFaction))
    {
      CommandSender sender = event.getSender();
      Bukkit.broadcastMessage(C("&6» &e" + event.getFaction().getName() + "&r has been disbanded by &a" + sender.getName() + "&r."));
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onFactionRename(FactionRenameEvent event)
  {
    Faction faction = event.getFaction();
    CommandSender sender = event.getSender();
    if ((faction instanceof PlayerFaction)) {
    	Bukkit.broadcastMessage(C("&6» &e" + event.getFaction().getName() + "&r has been renamed to &c" + event.getNewName() + "&r by &a" + event.getSender().getName() + "."));
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onFactionRenameMonitor(FactionRenameEvent event)
  {
    Faction faction = event.getFaction();
    if ((faction instanceof KothFaction)) {
      ((KothFaction)faction).getCaptureZone().setName(event.getNewName());
    }
  }
  
  private long getLastLandChangedMeta(Player player)
  {
    MetadataValue value = player.getMetadata("landChangedMessage").iterator().hasNext() ? (MetadataValue)player.getMetadata("landChangedMessage").iterator().next() : null;
    long millis = System.currentTimeMillis();
    long remaining = value == null ? 0L : value.asLong() - millis;
    if (remaining <= 0L) {
      player.setMetadata("landChangedMessage", new FixedMetadataValue(this.plugin, Long.valueOf(millis + 225L)));
    }
    return remaining;
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onCaptureZoneEnter(CaptureZoneEnterEvent event)
  {
    Player player = event.getPlayer();
    if ((getLastLandChangedMeta(player) <= 0L) && 
      (this.plugin.getUserManager().getUser(player.getUniqueId()).isCapzoneEntryAlerts())) {
      player.sendMessage("§7Now entering capture zone: §r" + event.getCaptureZone().getDisplayName() + " §7(" + event.getFaction().getName() + "§7)");
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onCaptureZoneLeave(CaptureZoneLeaveEvent event)
  {
    Player player = event.getPlayer();
    if ((getLastLandChangedMeta(player) <= 0L) && 
      (this.plugin.getUserManager().getUser(player.getUniqueId()).isCapzoneEntryAlerts())) {
      player.sendMessage("§7Now leaving capture zone: §r" + event.getCaptureZone().getDisplayName() + " §7(" + event.getFaction().getName() + "§7)");
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  private void onPlayerClaimEnter(PlayerClaimEnterEvent event)
  {
    Faction toFaction = event.getToFaction();
    if (toFaction.isSafezone())
    {
      Player player = event.getPlayer();
      player.setHealth(((CraftPlayer)player).getMaxHealth());
      player.setFoodLevel(20);
      player.setFireTicks(0);
      player.setSaturation(4.0F);
    }
    Player player = event.getPlayer();
    if (getLastLandChangedMeta(player) <= 0L)
    {
      Faction fromFaction = event.getFromFaction();
      player.sendMessage("§7Now leaving: §r" + fromFaction.getDisplayName(player) + " §7(" + (fromFaction.isDeathban() ? ChatColor.RED + "§cDeathban" : new StringBuilder().append(ChatColor.GREEN).append("§aNon-Deathban").toString()) + "§7)");
      player.sendMessage("§7Now entering: §r" + toFaction.getDisplayName(player) + " §7(" + (toFaction.isDeathban() ? ChatColor.RED + "§cDeathban" : new StringBuilder().append(ChatColor.GREEN).append("§aNon-Deathban").toString()) + "§7)");
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onPlayerLeftFaction(PlayerLeftFactionEvent event)
  {
    Optional<Player> optionalPlayer = event.getPlayer();
    if (optionalPlayer.isPresent()) {
      this.plugin.getUserManager().getUser(((Player)optionalPlayer.get()).getUniqueId()).setLastFactionLeaveMillis(System.currentTimeMillis());
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
  public void onPlayerPreFactionJoin(PlayerJoinFactionEvent event)
  {
    Faction faction = event.getFaction();
    Optional optionalPlayer = event.getPlayer();
    if (((faction instanceof PlayerFaction)) && (optionalPlayer.isPresent()))
    {
      Player player = (Player)optionalPlayer.get();
      PlayerFaction playerFaction = (PlayerFaction)faction;
      if ((!this.plugin.getEotwHandler().isEndOfTheWorld()) && (playerFaction.getRegenStatus() == RegenStatus.PAUSED))
      {
        event.setCancelled(true);
        player.sendMessage("§eYou cannot join factions that are not regenerating DTR.");
        return;
      }
      long difference = this.plugin.getUserManager().getUser(player.getUniqueId()).getLastFactionLeaveMillis() - System.currentTimeMillis() + FACTION_JOIN_WAIT_MILLIS;
      if ((difference > 0L) && (!player.hasPermission("hcf.faction.argument.staff.forcejoin")))
      {
        event.setCancelled(true);
        player.sendMessage("§eYou cannot join factions after just leaving within " + FACTION_JOIN_WAIT_WORDS + ". " + "You need to wait wait another " + DurationFormatUtils.formatDurationWords(difference, true, true) + '.');
      }
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
  public void onFactionLeave(PlayerLeaveFactionEvent event)
  {
    Faction faction = event.getFaction();
    if ((faction instanceof PlayerFaction))
    {
      Optional optional = event.getPlayer();
      if (optional.isPresent())
      {
        Player player = (Player)optional.get();
        if (this.plugin.getFactionManager().getFactionAt(player.getLocation()).equals(faction))
        {
          event.setCancelled(true);
          player.sendMessage("§eYou cannot leave your faction whilst you remain in its' territory. This is to prevent insiding!");
        }
      }
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onPlayerJoin(PlayerJoinEvent event)
  {
    Player player = event.getPlayer();
    PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
    if (playerFaction != null)
    {
      playerFaction.printDetails(player);
      playerFaction.broadcast("§eMember Online: " + ChatColor.GREEN + playerFaction.getMember(player).getRole().getAstrix() + player.getName() + ChatColor.GOLD + '.', new UUID[] {player
      
        .getUniqueId() });
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onPlayerQuit(PlayerQuitEvent event)
  {
    Player player = event.getPlayer();
    PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
    if (playerFaction != null) {
      playerFaction.broadcast("§eMember Offline: " + ChatColor.RED + playerFaction.getMember(player).getRole().getAstrix() + player.getName() + ChatColor.GOLD + '.');
    }
  }
}
