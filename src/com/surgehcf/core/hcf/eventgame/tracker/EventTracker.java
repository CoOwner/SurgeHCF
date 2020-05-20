package com.surgehcf.core.hcf.eventgame.tracker;

import org.bukkit.entity.Player;

import com.surgehcf.core.hcf.eventgame.CaptureZone;
import com.surgehcf.core.hcf.eventgame.EventTimer;
import com.surgehcf.core.hcf.eventgame.EventType;
import com.surgehcf.core.hcf.eventgame.faction.EventFaction;

@Deprecated
public abstract interface EventTracker
{
  public abstract EventType getEventType();
  
  public abstract void tick(EventTimer paramEventTimer, EventFaction paramEventFaction);
  
  public abstract void onContest(EventFaction paramEventFaction, EventTimer paramEventTimer);
  
  public abstract boolean onControlTake(Player paramPlayer, CaptureZone paramCaptureZone);
  
  public abstract boolean onControlLoss(Player paramPlayer, CaptureZone paramCaptureZone, EventFaction paramEventFaction);
  
  public abstract void stopTiming();
}


