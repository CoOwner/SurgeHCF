 package com.surgehcf.core.hcf.eventgame.faction;
 
  import me.milksales.util.cuboid.Cuboid;

import org.bukkit.ChatColor;
 import org.bukkit.Location;
 import org.bukkit.command.CommandSender;

import com.surgehcf.core.hcf.eventgame.EventType;
import com.surgehcf.core.hcf.faction.type.ClaimableFaction;
 
 public abstract class EventFaction extends ClaimableFaction
 {
   public EventFaction(String name)
   {
     super(name);
     setDeathban(true);
   }
   
   public EventFaction(java.util.Map<String, Object> map) {
     super(map);
     setDeathban(true);
   }
   
   public String getDisplayName(com.surgehcf.core.hcf.faction.type.Faction faction)
   {
     if (getEventType() == EventType.KOTH) {
       return ChatColor.LIGHT_PURPLE.toString() + getName() + ' ' + getEventType().getDisplayName();
     }
     return ChatColor.DARK_PURPLE + getEventType().getDisplayName();
   }
   
   public String getDisplayName(CommandSender sender)
   {
     if (getEventType() == EventType.KOTH) {
       return ChatColor.LIGHT_PURPLE.toString() + getName() + ' ' + getEventType().getDisplayName();
     }
     return ChatColor.DARK_PURPLE + getEventType().getDisplayName();
   }
   
   public void setClaim(Cuboid cuboid, CommandSender sender) {
     removeClaims(getClaims(), sender);
     Location min = cuboid.getMinimumPoint();
     min.setY(0);
     Location max = cuboid.getMaximumPoint();
     max.setY(256);
     addClaim(new com.surgehcf.core.hcf.faction.claim.Claim(this, min, max), sender);
   }
   
   public abstract EventType getEventType();
   
   public abstract java.util.List<com.surgehcf.core.hcf.eventgame.CaptureZone> getCaptureZones();
 }

