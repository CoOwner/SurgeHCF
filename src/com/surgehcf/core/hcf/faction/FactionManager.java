package com.surgehcf.core.hcf.faction;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.surgehcf.core.hcf.faction.claim.Claim;
import com.surgehcf.core.hcf.faction.type.Faction;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;

public abstract interface FactionManager
{
  public static final long MAX_DTR_REGEN_MILLIS = TimeUnit.HOURS.toMillis(3L);
  public static final String MAX_DTR_REGEN_WORDS = DurationFormatUtils.formatDurationWords(MAX_DTR_REGEN_MILLIS, true, true);
  
  public abstract Map<String, ?> getFactionNameMap();
  
  public abstract Collection<Faction> getFactions();
  
  public abstract Claim getClaimAt(Location paramLocation);
  
  public abstract Claim getClaimAt(World paramWorld, int paramInt1, int paramInt2);
  
  public abstract Faction getFactionAt(Location paramLocation);
  
  public abstract Faction getFactionAt(Block paramBlock);
  
  public abstract Faction getFactionAt(World paramWorld, int paramInt1, int paramInt2);
  
  public abstract Faction getFaction(String paramString);
  
  public abstract Faction getFaction(UUID paramUUID);
  
  @Deprecated
  public abstract PlayerFaction getContainingPlayerFaction(String paramString);
  
  @Deprecated
  public abstract PlayerFaction getPlayerFaction(Player paramPlayer);
  
  public abstract PlayerFaction getPlayerFaction(UUID paramUUID);
  
  public abstract Faction getContainingFaction(String paramString);
  
  public abstract boolean containsFaction(Faction paramFaction);
  
  public abstract boolean createFaction(Faction paramFaction);
  
  public abstract boolean createFaction(Faction paramFaction, CommandSender paramCommandSender);
  
  public abstract boolean removeFaction(Faction paramFaction, CommandSender paramCommandSender);
  
  public abstract void reloadFactionData();
  
  public abstract void saveFactionData();
}
