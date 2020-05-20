 package com.surgehcf.core.hcf.faction.event;
 
 import org.bukkit.event.Event;

import com.surgehcf.core.hcf.faction.type.Faction;
 
 public abstract class FactionEvent extends Event
 {
   protected final Faction faction;
   
   public FactionEvent(Faction faction)
   {
     this.faction = ((Faction)com.google.common.base.Preconditions.checkNotNull(faction, "Faction cannot be null"));
   }
   
   FactionEvent(Faction faction, boolean async) {
     super(async);
     this.faction = ((Faction)com.google.common.base.Preconditions.checkNotNull(faction, "Faction cannot be null"));
   }
   
   public Faction getFaction() {
     return this.faction;
   }
 }

