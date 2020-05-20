package com.surgehcf.core.hcf.scoreboard;

import com.google.common.base.Optional;
import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.event.FactionRelationCreateEvent;
import com.surgehcf.core.hcf.faction.event.FactionRelationRemoveEvent;
import com.surgehcf.core.hcf.faction.event.PlayerJoinedFactionEvent;
import com.surgehcf.core.hcf.faction.event.PlayerLeftFactionEvent;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;
import com.surgehcf.core.hcf.scoreboard.PlayerBoard;
import com.surgehcf.core.hcf.scoreboard.provider.TimerSidebarProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

public class ScoreboardHandler
  implements Listener
{
  private final Map<UUID, PlayerBoard> playerBoards;
  private final TimerSidebarProvider timerSidebarProvider;
  private final SurgeCore plugin;
  
  public ScoreboardHandler(SurgeCore plugin)
  {
    this.playerBoards = new HashMap();
    this.plugin = plugin;
    this.timerSidebarProvider = new TimerSidebarProvider(plugin);
    Bukkit.getPluginManager().registerEvents(this, plugin);
    for (Player players : Bukkit.getOnlinePlayers())
    {
      PlayerBoard playerBoard;
      setPlayerBoard(players.getUniqueId(), playerBoard = new PlayerBoard(plugin, players));
      playerBoard.addUpdates(Arrays.asList(Bukkit.getOnlinePlayers()));
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.LOWEST)
  public void onPlayerJoin(PlayerJoinEvent event)
  {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    for (PlayerBoard board : this.playerBoards.values()) {
      board.addUpdate(player);
    }
    PlayerBoard board2 = new PlayerBoard(this.plugin, player);
    board2.addUpdates(Arrays.asList(Bukkit.getOnlinePlayers()));
    setPlayerBoard(uuid, board2);
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.LOWEST)
  public void onPlayerQuit(PlayerQuitEvent event)
  {
    ((PlayerBoard)this.playerBoards.remove(event.getPlayer().getUniqueId())).remove();
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onPlayerJoinedFaction(PlayerJoinedFactionEvent event)
  {
    Optional<Player> optional = event.getPlayer();
    Player player;
    if (optional.isPresent())
    {
      player = (Player)optional.get();
      Collection<Player> players = this.plugin.getFactionManager().getPlayerFaction(player.getUniqueId()).getOnlinePlayers();
      getPlayerBoard(event.getUniqueID()).addUpdates(players);
      for (Player target : players) {
        getPlayerBoard(target.getUniqueId()).addUpdate(player);
      }
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onPlayerLeftFaction(PlayerLeftFactionEvent event)
  {
    Optional<Player> optional = event.getPlayer();
    Player player;
    if (optional.isPresent())
    {
      player = (Player)optional.get();
      Collection<Player> players = event.getFaction().getOnlinePlayers();
      getPlayerBoard(event.getUniqueID()).addUpdates(players);
      for (Player target : players) {
        getPlayerBoard(target.getUniqueId()).addUpdate(player);
      }
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onFactionAllyCreate(FactionRelationCreateEvent event)
  {
    Set<Player> updates = new HashSet(event.getSenderFaction().getOnlinePlayers());
    updates.addAll(event.getTargetFaction().getOnlinePlayers());
    for (PlayerBoard board : this.playerBoards.values()) {
      board.addUpdates(updates);
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onFactionAllyRemove(FactionRelationRemoveEvent event)
  {
    Set<Player> updates = new HashSet(event.getSenderFaction().getOnlinePlayers());
    updates.addAll(event.getTargetFaction().getOnlinePlayers());
    for (PlayerBoard board : this.playerBoards.values()) {
      board.addUpdates(updates);
    }
  }
  
  public PlayerBoard getPlayerBoard(UUID uuid)
  {
    return (PlayerBoard)this.playerBoards.get(uuid);
  }
  
  public void setPlayerBoard(UUID uuid, PlayerBoard board)
  {
    this.playerBoards.put(uuid, board);
    board.setSidebarVisible(true);
    board.setDefaultSidebar(this.timerSidebarProvider, 2L);
  }
  
  public void clearBoards()
  {
    Iterator<PlayerBoard> iterator = this.playerBoards.values().iterator();
    while (iterator.hasNext())
    {
      ((PlayerBoard)iterator.next()).remove();
      iterator.remove();
    }
  }
}
