 package com.surgehcf.core.hcf.eventgame.tracker;
 
  import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
 import org.bukkit.entity.Player;

import com.surgehcf.core.hcf.eventgame.CaptureZone;
import com.surgehcf.core.hcf.eventgame.EventTimer;
import com.surgehcf.core.hcf.eventgame.EventType;
import com.surgehcf.core.hcf.eventgame.faction.EventFaction;
import com.surgehcf.core.hcf.eventgame.tracker.EventTracker;
 
 @Deprecated
 public class KothTracker implements EventTracker
 {
   private final com.surgehcf.SurgeCore plugin;
   
   public KothTracker(com.surgehcf.SurgeCore plugin)
   {
     this.plugin = plugin;
   }
   
   public EventType getEventType()
   {
     return EventType.KOTH;
   }
   
   public void tick(EventTimer eventTimer, EventFaction eventFaction)
   {
     CaptureZone captureZone = ((com.surgehcf.core.hcf.eventgame.faction.KothFaction)eventFaction).getCaptureZone();
     long remainingMillis = captureZone.getRemainingCaptureMillis();
     if (remainingMillis <= 0L) {
       this.plugin.getTimerManager().eventTimer.handleWinner(captureZone.getCappingPlayer());
       eventTimer.clearCooldown();
       return;
     }
     if (remainingMillis == captureZone.getDefaultCaptureMillis()) {
       return;
     }
     int remainingSeconds = (int)(remainingMillis / 1000L);
     if ((remainingSeconds > 0) && (remainingSeconds % 30 == 0)) {
       org.bukkit.Bukkit.broadcastMessage(ChatColor.YELLOW + "[" + eventFaction.getEventType().getDisplayName() + "] " + ChatColor.GOLD + "Someone is controlling " + ChatColor.LIGHT_PURPLE + captureZone.getDisplayName() + ChatColor.GOLD + ". " + ChatColor.RED + '(' + com.surgehcf.core.hcf.DateFormatter.KOTH_FORMAT.format(remainingMillis) + ')');
     }
   }
   
   public void onContest(EventFaction eventFaction, EventTimer eventTimer)
   {
     org.bukkit.Bukkit.broadcastMessage(ChatColor.YELLOW + "[" + eventFaction.getEventType().getDisplayName() + "] " + ChatColor.LIGHT_PURPLE + eventFaction.getName() + ChatColor.GOLD + " can now be contested. " + ChatColor.RED + '(' + com.surgehcf.core.hcf.DateFormatter.KOTH_FORMAT.format(eventTimer.getRemaining()) + ')');
   }
   
   public boolean onControlTake(Player player, CaptureZone captureZone)
   {
     player.sendMessage(ChatColor.RESET + "(Surge) " + ChatColor.AQUA + "You are now in control of " + ChatColor.LIGHT_PURPLE + captureZone.getDisplayName() + ChatColor.GOLD + '.');
     return true;
   }
   
   public boolean onControlLoss(Player player, CaptureZone captureZone, EventFaction eventFaction)
   {
     player.sendMessage(ChatColor.RESET + "(Surge) " + ChatColor.AQUA + "You are no longer in control of " + ChatColor.LIGHT_PURPLE + captureZone.getDisplayName() + ChatColor.GOLD + '.');
     long remainingMillis = captureZone.getRemainingCaptureMillis();
     if ((remainingMillis > 0L) && (captureZone.getDefaultCaptureMillis() - remainingMillis > MINIMUM_CONTROL_TIME_ANNOUNCE)) {
       org.bukkit.Bukkit.broadcastMessage(ChatColor.YELLOW + "[" + eventFaction.getEventType().getDisplayName() + "] " + ChatColor.LIGHT_PURPLE + player.getName() + ChatColor.GOLD + " has lost control of " + ChatColor.LIGHT_PURPLE + captureZone.getDisplayName() + ChatColor.GOLD + '.' + ChatColor.RED + " (" + com.surgehcf.core.hcf.DateFormatter.KOTH_FORMAT.format(captureZone.getRemainingCaptureMillis()) + ')');
     }
     return true;
   }
   
 
 
 
 
 
   private static final long MINIMUM_CONTROL_TIME_ANNOUNCE = TimeUnit.SECONDS.toMillis(10L);
   public static final long DEFAULT_CAP_MILLIS = TimeUnit.MINUTES.toMillis(5L);
   
   public void stopTiming() {}
 }

