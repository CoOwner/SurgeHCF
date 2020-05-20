 package com.surgehcf.core.hcf.faction.event;
 
  import com.google.common.base.Optional;
import com.surgehcf.core.hcf.faction.event.FactionEvent;
import com.surgehcf.core.hcf.faction.event.cause.FactionLeaveCause;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;

import java.util.UUID;

import org.bukkit.Bukkit;
 import org.bukkit.entity.Player;
 import org.bukkit.event.HandlerList;
 
 public class PlayerLeftFactionEvent
   extends FactionEvent
 {
   private static final HandlerList handlers = new HandlerList();
   private final UUID uniqueID;
   private final FactionLeaveCause cause;
   private Optional<Player> player;
   
   public PlayerLeftFactionEvent(Player player, PlayerFaction playerFaction, FactionLeaveCause cause)
   {
     super(playerFaction);
     this.player = Optional.of(player);
     this.uniqueID = player.getUniqueId();
     this.cause = cause;
   }
   
   public PlayerLeftFactionEvent(UUID playerUUID, PlayerFaction playerFaction, FactionLeaveCause cause)
   {
     super(playerFaction);
     this.uniqueID = playerUUID;
     this.cause = cause;
   }
   
   public static HandlerList getHandlerList()
   {
     return handlers;
   }
   
   public PlayerFaction getFaction()
   {
     return (PlayerFaction)super.getFaction();
   }
   
   public Optional<Player> getPlayer()
   {
     if (this.player == null) {
       this.player = Optional.fromNullable(Bukkit.getPlayer(this.uniqueID));
     }
     return this.player;
   }
   
   public UUID getUniqueID()
   {
     return this.uniqueID;
   }
   
   public FactionLeaveCause getCause()
   {
     return this.cause;
   }
   
   public HandlerList getHandlers()
   {
     return handlers;
   }
 }

