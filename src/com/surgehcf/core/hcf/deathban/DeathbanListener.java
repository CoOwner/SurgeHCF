 package com.surgehcf.core.hcf.deathban;
 
  import java.util.UUID;
 import java.util.concurrent.ConcurrentMap;
 import java.util.concurrent.TimeUnit;

import net.minecraft.util.com.google.common.cache.Cache;
 import net.minecraft.util.com.google.common.cache.CacheBuilder;

import org.apache.commons.lang.time.DurationFormatUtils;
 import org.bukkit.ChatColor;
 import org.bukkit.entity.Player;
 import org.bukkit.event.EventHandler;
 import org.bukkit.event.EventPriority;
 import org.bukkit.event.Listener;
 import org.bukkit.event.entity.PlayerDeathEvent;
 import org.bukkit.event.player.PlayerLoginEvent;
 import org.bukkit.event.player.PlayerLoginEvent.Result;
 import org.bukkit.scheduler.BukkitRunnable;

import com.surgehcf.SurgeCore;
import com.surgehcf.cmds.LocationFormat;
import com.surgehcf.core.hcf.deathban.Deathban;
import com.surgehcf.core.hcf.deathban.DeathbanListener;
import com.surgehcf.core.hcf.eventgame.eotw.EotwHandler;
import com.surgehcf.core.hcf.user.FactionUser;
import com.surgehcf.core.hcf.user.UserManager;
 
 public class DeathbanListener
   implements Listener
 {
   public DeathbanListener(SurgeCore plugin)
   {
     this.plugin = plugin;
     this.lastAttemptedJoinMap = CacheBuilder.newBuilder().expireAfterWrite(LIFE_USE_DELAY_MILLIS, TimeUnit.MILLISECONDS).build().asMap();
   }
   
 
   @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
   public void onPlayerLogin(PlayerLoginEvent event)
   {
     Player player = event.getPlayer();
     FactionUser user = this.plugin.getUserManager().getUser(player.getUniqueId());
     Deathban deathban = user.getDeathban();
     if ((deathban == null) || (!deathban.isActive())) {
       return;
     }
     if (player.hasPermission("hcf.deathban.bypass"))
     {
       return;
     }
     if (this.plugin.getEotwHandler().isEndOfTheWorld()) {
       event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "§eSurgeHCF §6» §rThank-you for playing EOTW!");
     }
     else {
       UUID uuid = player.getUniqueId();
       int lives = this.plugin.getDeathbanManager().getLives(uuid);
       String formattedDuration = SurgeCore.getRemaining(deathban.getRemaining(), true, false);
       String reason = deathban.getReason();
       String prefix = "§eYou are currently death-banned" + (reason != null ? ": §r" + reason + ".\n" : ".") + ChatColor.YELLOW + "\n§eRemaining §6» §r" + formattedDuration + "\n" + "§eLocation §6» §r" + LocationFormat.formatLocation(deathban.getDeathPoint()) + "\n\n" + ChatColor.RED + "§rYou currently have §e" + (lives <= 0 ? "no" : Integer.valueOf(lives)) + " lives§r.";
       if (lives > 0)
       {
         long millis = System.currentTimeMillis();
         Long lastAttemptedJoinMillis = (Long)this.lastAttemptedJoinMap.get(uuid);
         if ((lastAttemptedJoinMillis != null) && (lastAttemptedJoinMillis.longValue() - System.currentTimeMillis() < LIFE_USE_DELAY_MILLIS))
         {
           this.lastAttemptedJoinMap.remove(uuid);
           user.removeDeathban();
           lives = this.plugin.getDeathbanManager().takeLives(uuid, 1);
           event.setResult(PlayerLoginEvent.Result.ALLOWED);
           new LoginMessageRunnable(player, ChatColor.YELLOW + "You have used a life bypass your death. You now have " + ChatColor.LIGHT_PURPLE + lives + ChatColor.YELLOW + " lives.").runTask(this.plugin);
         }
         else
         {
           this.lastAttemptedJoinMap.put(uuid, Long.valueOf(millis + LIFE_USE_DELAY_MILLIS));
           event.disallow(PlayerLoginEvent.Result.KICK_OTHER, prefix + ChatColor.GOLD + "\n" + "§rYou may use a life by reconnecting within " + ChatColor.YELLOW + LIFE_USE_DELAY_WORDS + ChatColor.GOLD + '.');
         }
         return;
       }
       event.disallow(PlayerLoginEvent.Result.KICK_OTHER, prefix + "\n§cYou can purchase lives at " + "surgehcf.buycraft.net" + " to bypass this.");
     }
   }
   
   @EventHandler(ignoreCancelled=true, priority=EventPriority.LOW)
   public void onPlayerDeath(PlayerDeathEvent event) {
     final Player player = event.getEntity();
     final Deathban deathban = this.plugin.getDeathbanManager().applyDeathBan(player, event.getDeathMessage());
     final String durationString = SurgeCore.getRemaining(deathban.getRemaining(), true, false);
     if (player.hasPermission("rank.staff")) {
       return;
     }
     
 
 
 
 
 
 
 
 
     new BukkitRunnable()
     {
       public void run()
       {
         if (DeathbanListener.this.plugin.getEotwHandler().isEndOfTheWorld()) {
           player.kickPlayer(ChatColor.RED + "§eSurgeHCF §6» §rThank-you for playing EOTW!");
         }
         else
					
           player.kickPlayer("§eYou have been death-banned" + (deathban.getReason() != null ? ": §r" + deathban.getReason() + ".\n" : ".") + ChatColor.YELLOW + "\n§eRemaining §6» §r" + SurgeCore.getRemaining(deathban.getRemaining(), true) + "\n" + "§eLocation §6» §r" + LocationFormat.formatLocation(deathban.getDeathPoint()) + "\n\n§aReconnect for more information."); } }
     
 
       .runTaskLater(this.plugin, 1L);
   }
   
 
   private static final long LIFE_USE_DELAY_MILLIS = TimeUnit.SECONDS.toMillis(30L);
   private static final String LIFE_USE_DELAY_WORDS = DurationFormatUtils.formatDurationWords(LIFE_USE_DELAY_MILLIS, true, true);
   private static final String DEATH_BAN_BYPASS_PERMISSION = "hcf.deathban.bypass";
   private final ConcurrentMap<Object, Object> lastAttemptedJoinMap;
   private final SurgeCore plugin;
   
   private static class LoginMessageRunnable extends BukkitRunnable { private final Player player;
     private final String message;
     
     public LoginMessageRunnable(Player player, String message) { this.player = player;
       this.message = message;
     }
     
     public void run() {
       this.player.sendMessage(this.message);
     }
   }
 }