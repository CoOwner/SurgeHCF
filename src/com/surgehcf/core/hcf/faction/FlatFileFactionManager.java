package com.surgehcf.core.hcf.faction;

import com.google.common.base.Preconditions;
import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.FactionMember;
import com.surgehcf.core.hcf.faction.claim.Claim;
import com.surgehcf.core.hcf.faction.event.FactionClaimChangedEvent;
import com.surgehcf.core.hcf.faction.event.FactionCreateEvent;
import com.surgehcf.core.hcf.faction.event.FactionRelationRemoveEvent;
import com.surgehcf.core.hcf.faction.event.FactionRemoveEvent;
import com.surgehcf.core.hcf.faction.event.FactionRenameEvent;
import com.surgehcf.core.hcf.faction.event.PlayerJoinedFactionEvent;
import com.surgehcf.core.hcf.faction.event.PlayerLeftFactionEvent;
import com.surgehcf.core.hcf.faction.event.cause.ClaimChangeCause;
import com.surgehcf.core.hcf.faction.struct.ChatChannel;
import com.surgehcf.core.hcf.faction.struct.Relation;
import com.surgehcf.core.hcf.faction.struct.Role;
import com.surgehcf.core.hcf.faction.type.ClaimableFaction;
import com.surgehcf.core.hcf.faction.type.EndPortalFaction;
import com.surgehcf.core.hcf.faction.type.Faction;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;
import com.surgehcf.core.hcf.faction.type.RoadFaction;
import com.surgehcf.core.hcf.faction.type.SpawnFaction;
import com.surgehcf.core.hcf.faction.type.WarzoneFaction;
import com.surgehcf.core.hcf.faction.type.WildernessFaction;
import com.surgehcf.core.hcf.faction.type.RoadFaction.EastRoadFaction;
import com.surgehcf.core.hcf.faction.type.RoadFaction.NorthRoadFaction;
import com.surgehcf.core.hcf.faction.type.RoadFaction.SouthRoadFaction;
import com.surgehcf.core.hcf.faction.type.RoadFaction.WestRoadFaction;

import me.milksales.util.Config;
import me.milksales.util.JavaUtils;
import me.milksales.util.cuboid.CoordinatePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public class FlatFileFactionManager
  implements Listener, FactionManager
{
  private final WarzoneFaction warzone;
  private final WildernessFaction wilderness;
  private final Map<CoordinatePair, Claim> claimPositionMap;
  private final Map<UUID, UUID> factionPlayerUuidMap;
  private final Map<UUID, Faction> factionUUIDMap;
  private final Map<String, UUID> factionNameMap;
  private final SurgeCore plugin;
  private Config config;
  
  public FlatFileFactionManager (SurgeCore plugin)
  {
    this.claimPositionMap = new HashMap();
    this.factionPlayerUuidMap = new ConcurrentHashMap();
    this.factionUUIDMap = new HashMap();
    this.factionNameMap = new me.milksales.base.kit.CaseInsensitiveMap();
    this.plugin = plugin;
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
    this.warzone = new WarzoneFaction();
    this.wilderness = new WildernessFaction();
    reloadFactionData();
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onPlayerJoinedFaction(PlayerJoinedFactionEvent event)
  {
    this.factionPlayerUuidMap.put(event.getUniqueID(), event.getFaction().getUniqueID());
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onPlayerLeftFaction(PlayerLeftFactionEvent event)
  {
    this.factionPlayerUuidMap.remove(event.getUniqueID());
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onFactionRename(FactionRenameEvent event)
  {
    this.factionNameMap.remove(event.getOriginalName());
    this.factionNameMap.put(event.getNewName(), event.getFaction().getUniqueID());
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onFactionClaim(FactionClaimChangedEvent event)
  {
    for (Claim claim : event.getAffectedClaims()) {
      cacheClaim(claim, event.getCause());
    }
  }
  
  @Deprecated
  public Map<String, UUID> getFactionNameMap()
  {
    return this.factionNameMap;
  }
  
  public List<Faction> getFactions()
  {
    List<Faction> asd = new ArrayList();
    for (Faction fac : this.factionUUIDMap.values()) {
      asd.add(fac);
    }
    return asd;
  }
  
  public Claim getClaimAt(World world, int x, int z)
  {
    return (Claim)this.claimPositionMap.get(new CoordinatePair(world, x, z));
  }
  
  public Claim getClaimAt(Location location)
  {
    return getClaimAt(location.getWorld(), location.getBlockX(), location.getBlockZ());
  }
  
  public Faction getFactionAt(World world, int x, int z)
  {
    World.Environment environment = world.getEnvironment();
    Claim claim = getClaimAt(world, x, z);
    if (claim != null)
    {
      Faction faction = claim.getFaction();
      if (faction != null) {
        return faction;
      }
    }
    if (environment == World.Environment.THE_END) {
      return this.warzone;
    }
    int warzoneRadius = 1000;
    if (environment == World.Environment.NETHER) {
      warzoneRadius = 450;
    }
    return (Math.abs(x) > warzoneRadius) || (Math.abs(z) > warzoneRadius) ? this.wilderness : this.warzone;
  }
  
  public Faction getFactionAt(Location location)
  {
    return getFactionAt(location.getWorld(), location.getBlockX(), location.getBlockZ());
  }
  
  public Faction getFactionAt(Block block)
  {
    return getFactionAt(block.getLocation());
  }
  
  public Faction getFaction(String factionName)
  {
    UUID uuid = (UUID)this.factionNameMap.get(factionName);
    return uuid == null ? null : (Faction)this.factionUUIDMap.get(uuid);
  }
  
  public Faction getFaction(UUID factionUUID)
  {
    return (Faction)this.factionUUIDMap.get(factionUUID);
  }
  
  public PlayerFaction getPlayerFaction(UUID playerUUID)
  {
    UUID uuid = (UUID)this.factionPlayerUuidMap.get(playerUUID);
    Faction faction = uuid == null ? null : (Faction)this.factionUUIDMap.get(uuid);
    return (faction instanceof PlayerFaction) ? (PlayerFaction)faction : null;
  }
  
  public PlayerFaction getPlayerFaction(Player player)
  {
    return getPlayerFaction(player.getUniqueId());
  }
  
  public PlayerFaction getContainingPlayerFaction(String search)
  {
    OfflinePlayer target = JavaUtils.isUUID(search) ? Bukkit.getOfflinePlayer(UUID.fromString(search)) : Bukkit.getOfflinePlayer(search);
    return (target.hasPlayedBefore()) || (target.isOnline()) ? getPlayerFaction(target.getUniqueId()) : null;
  }
  
  public Faction getContainingFaction(String search)
  {
    Faction faction = getFaction(search);
    if (faction != null) {
      return faction;
    }
    UUID playerUUID = Bukkit.getOfflinePlayer(search).getUniqueId();
    if (playerUUID != null) {
      return getPlayerFaction(playerUUID);
    }
    return null;
  }
  
  public boolean containsFaction(Faction faction)
  {
    return this.factionNameMap.containsKey(faction.getName());
  }
  
  public boolean createFaction(Faction faction)
  {
    return createFaction(faction, Bukkit.getConsoleSender());
  }
  
  public boolean createFaction(Faction faction, CommandSender sender)
  {
    if (this.factionUUIDMap.putIfAbsent(faction.getUniqueID(), faction) != null) {
      return false;
    }
    this.factionNameMap.put(faction.getName(), faction.getUniqueID());
    if (((faction instanceof PlayerFaction)) && ((sender instanceof Player)))
    {
      Player player = (Player)sender;
      PlayerFaction playerFaction = (PlayerFaction)faction;
      if (!playerFaction.setMember(player, new FactionMember(player, ChatChannel.PUBLIC, Role.LEADER))) {
        return false;
      }
    }
    FactionCreateEvent createEvent = new FactionCreateEvent(faction, sender);
    Bukkit.getPluginManager().callEvent(createEvent);
    return !createEvent.isCancelled();
  }
  
  public boolean removeFaction(Faction faction, CommandSender sender)
  {
    if (this.factionUUIDMap.remove(faction.getUniqueID()) == null) {
      return false;
    }
    this.factionNameMap.remove(faction.getName());
    FactionRemoveEvent removeEvent = new FactionRemoveEvent(faction, sender);
    Bukkit.getPluginManager().callEvent(removeEvent);
    if (removeEvent.isCancelled()) {
      return false;
    }
    if ((faction instanceof ClaimableFaction)) {
      Bukkit.getPluginManager().callEvent(new FactionClaimChangedEvent(sender, ClaimChangeCause.UNCLAIM, ((ClaimableFaction)faction).getClaims()));
    }
    PlayerFaction playerFaction;
    if ((faction instanceof PlayerFaction))
    {
      playerFaction = (PlayerFaction)faction;
      for (PlayerFaction ally : playerFaction.getAlliedFactions())
      {
        Bukkit.getPluginManager().callEvent(new FactionRelationRemoveEvent(playerFaction, ally, Relation.ENEMY));
        ally.getRelations().remove(faction.getUniqueID());
      }
      for (UUID uuid : playerFaction.getMembers().keySet()) {
        playerFaction.setMember(uuid, null, true);
      }
    }
    return true;
  }
  
  private void cacheClaim(Claim claim, ClaimChangeCause cause)
  {
    Preconditions.checkNotNull(claim, "Claim cannot be null");
    Preconditions.checkNotNull(cause, "Cause cannot be null");
    Preconditions.checkArgument(cause != ClaimChangeCause.RESIZE, "Cannot cache claims of resize type");
    World world = claim.getWorld();
    if (world == null) {
      return;
    }
    int minX = Math.min(claim.getX1(), claim.getX2());
    int maxX = Math.max(claim.getX1(), claim.getX2());
    int minZ = Math.min(claim.getZ1(), claim.getZ2());
    int maxZ = Math.max(claim.getZ1(), claim.getZ2());
    for (int x = minX; x <= maxX; x++) {
      for (int z = minZ; z <= maxZ; z++)
      {
        CoordinatePair coordinatePair = new CoordinatePair(world, x, z);
        if (cause == ClaimChangeCause.CLAIM) {
          this.claimPositionMap.put(coordinatePair, claim);
        } else if (cause == ClaimChangeCause.UNCLAIM) {
          this.claimPositionMap.remove(coordinatePair);
        }
      }
    }
  }
  
  private void cacheFaction(Faction faction)
  {
    this.factionNameMap.put(faction.getName(), faction.getUniqueID());
    this.factionUUIDMap.put(faction.getUniqueID(), faction);
    ClaimableFaction claimableFaction;
    if ((faction instanceof ClaimableFaction))
    {
      claimableFaction = (ClaimableFaction)faction;
      for (Claim claim : claimableFaction.getClaims()) {
        cacheClaim(claim, ClaimChangeCause.CLAIM);
      }
    }
    if ((faction instanceof PlayerFaction)) {
      for (FactionMember factionMember : ((PlayerFaction)faction).getMembers().values()) {
        this.factionPlayerUuidMap.put(factionMember.getUniqueId(), faction.getUniqueID());
      }
    }
  }
  
  public void reloadFactionData()
  {
    this.factionNameMap.clear();
    this.config = new Config(this.plugin, "factions");
    Object object = this.config.get("factions");
    MemorySection section;
    if ((object instanceof MemorySection))
    {
      section = (MemorySection)object;
      for (String factionName : section.getKeys(false))
      {
        Object next = this.config.get(section.getCurrentPath() + '.' + factionName);
        if ((next instanceof Faction)) {
          cacheFaction((Faction)next);
        }
      }
    }
    else if ((object instanceof List))
    {
      List<?> list = (List)object;
      for (Object next2 : list) {
        if ((next2 instanceof Faction)) {
          cacheFaction((Faction)next2);
        }
      }
    }
    Set<Faction> adding = new HashSet();
    if (!this.factionNameMap.containsKey("Warzone")) {
      adding.add(new WarzoneFaction());
    }
    if (!this.factionNameMap.containsKey("Spawn")) {
      adding.add(new SpawnFaction());
    }
    if (!this.factionNameMap.containsKey("NorthRoad"))
    {
      adding.add(new RoadFaction.NorthRoadFaction());
      adding.add(new RoadFaction.EastRoadFaction());
      adding.add(new RoadFaction.SouthRoadFaction());
      adding.add(new RoadFaction.WestRoadFaction());
    }
    if (!this.factionNameMap.containsKey("EndPortal")) {
      adding.add(new EndPortalFaction());
    }
    for (Faction added : adding)
    {
      cacheFaction(added);
      Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "Faction " + added.getName() + " not found, created.");
    }
  }
  
  public void saveFactionData()
  {
    this.config.set("factions", new ArrayList(this.factionUUIDMap.values()));
    this.config.save();
  }
}
