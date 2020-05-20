package com.surgehcf.core.hcf.eventgame.eotw;

import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collection;

import org.bukkit.Bukkit;

import java.util.HashSet;

import org.bukkit.entity.Player;

import java.util.Set;

import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.CoreConfiguration;
import com.surgehcf.core.hcf.eventgame.eotw.EotwHandler;
import com.surgehcf.core.hcf.faction.claim.Claim;
import com.surgehcf.core.hcf.faction.type.ClaimableFaction;
import com.surgehcf.core.hcf.faction.type.Faction;
import com.surgehcf.core.hcf.listener.BorderListener;

import org.apache.commons.lang.time.DurationFormatUtils;

import java.util.concurrent.TimeUnit;

import org.bukkit.plugin.Plugin;
import org.bukkit.World;

public class EotwHandler
{
    public static final int BORDER_DECREASE_MINIMUM = 2000;
    public static final int BORDER_DECREASE_AMOUNT = 200;
    public static final long BORDER_DECREASE_TIME_MILLIS;
    public static final int BORDER_DECREASE_TIME_SECONDS;
    public static final String BORDER_DECREASE_TIME_WORDS;
    public static final String BORDER_DECREASE_TIME_ALERT_WORDS;
    public static final long EOTW_WARMUP_WAIT_MILLIS;
    public static final int EOTW_WARMUP_WAIT_SECONDS;
    private static final long EOTW_CAPPABLE_WAIT;
    private final SurgeCore plugin;
    private EotwRunnable runnable;
    
    public EotwHandler(final SurgeCore plugin) {
        this.plugin = plugin;
    }
    
    public EotwRunnable getRunnable() {
        return this.runnable;
    }
    
    public boolean isEndOfTheWorld() {
        return this.isEndOfTheWorld(true);
    }
    
    public void setEndOfTheWorld(final boolean yes) {
        if (yes == this.isEndOfTheWorld(false)) {
            return;
        }
        if (yes) {
            (this.runnable = new EotwRunnable(CoreConfiguration.BORDER_SIZES.get(World.Environment.NORMAL))).runTaskTimer((Plugin)this.plugin, 1L, 100L);
        }
        else if (this.runnable != null) {
            this.runnable.cancel();
            this.runnable = null;
        }
    }
    
    public boolean isEndOfTheWorld(final boolean ignoreWarmup) {
        return this.runnable != null && (!ignoreWarmup || this.runnable.getElapsedMilliseconds() > 0L);
    }
    
    static {
        BORDER_DECREASE_TIME_MILLIS = TimeUnit.MINUTES.toMillis(5L);
        BORDER_DECREASE_TIME_SECONDS = (int)(EotwHandler.BORDER_DECREASE_TIME_MILLIS / 1000L);
        BORDER_DECREASE_TIME_WORDS = DurationFormatUtils.formatDurationWords(EotwHandler.BORDER_DECREASE_TIME_MILLIS, true, true);
        BORDER_DECREASE_TIME_ALERT_WORDS = DurationFormatUtils.formatDurationWords(EotwHandler.BORDER_DECREASE_TIME_MILLIS / 2L, true, true);
        EOTW_WARMUP_WAIT_MILLIS = TimeUnit.SECONDS.toMillis(30L);
        EOTW_WARMUP_WAIT_SECONDS = (int)(EotwHandler.EOTW_WARMUP_WAIT_MILLIS / 1000L);
        EOTW_CAPPABLE_WAIT = TimeUnit.MINUTES.toMillis(45L);
    }
    
    public static final class EotwRunnable extends BukkitRunnable
    {
        private static final PotionEffect WITHER;
        private final Set<Player> outsideBorder;
        private boolean hasInformedStarted;
        private long startStamp;
        private int borderSize;
        
        public EotwRunnable(final int borderSize) {
            this.outsideBorder = new HashSet<Player>();
            this.hasInformedStarted = false;
            this.borderSize = borderSize;
            this.startStamp = System.currentTimeMillis() + EotwHandler.EOTW_WARMUP_WAIT_MILLIS;
        }
        
        public void handleDisconnect(final Player player) {
            this.outsideBorder.remove(player);
        }
        
        public long getTimeUntilStarting() {
            final long difference = System.currentTimeMillis() - this.startStamp;
            return (difference > 0L) ? 0L : Math.abs(difference);
        }
        
        public long getTimeUntilCappable() {
            return EotwHandler.EOTW_CAPPABLE_WAIT - this.getElapsedMilliseconds();
        }
        
        public long getElapsedMilliseconds() {
            return System.currentTimeMillis() - this.startStamp;
        }
        
        public void run() {
            final long elapsedMillis = this.getElapsedMilliseconds();
            final int elapsedSeconds = (int)Math.round(elapsedMillis / 1000.0);
            if (!this.hasInformedStarted && elapsedSeconds >= 0) {
                for (final Faction faction : SurgeCore.getPlugin().getFactionManager().getFactions()) {
                    if (faction instanceof ClaimableFaction) {
                        final ClaimableFaction claimableFaction = (ClaimableFaction)faction;
                        claimableFaction.removeClaims(claimableFaction.getClaims(), (CommandSender)Bukkit.getConsoleSender());
                    }
                }
                this.hasInformedStarted = true;
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(ChatColor.RED + "███████");
                Bukkit.broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█████" + ChatColor.RED + "█" + " §6§lEOTW §dhas commenced!");
                Bukkit.broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█" + ChatColor.RED + "█████" + " §ePvP is now globally §cenabled§e!");
                Bukkit.broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "████" + ChatColor.RED + "██" + " §eAll safezones now have deathbans §cenabled§e!");
                Bukkit.broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█" + ChatColor.RED + "█████" + " §eThe border will start shrinking to §b1000 §eblocks.");
                Bukkit.broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█████" + ChatColor.RED + "█" + " §eAll factions are now §craidable§e.");
                Bukkit.broadcastMessage(ChatColor.RED + "███████");
                Bukkit.broadcastMessage("");
                return;
            }
            if (elapsedMillis < 0L && elapsedMillis >= -EotwHandler.EOTW_WARMUP_WAIT_MILLIS) {
            	Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(ChatColor.RED + "███████");
                Bukkit.broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█████" + ChatColor.RED + "█" + "");
                Bukkit.broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█" + ChatColor.RED + "█████" + " §6§lEOTW §dis starting soon!");
                Bukkit.broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "████" + ChatColor.RED + "██" + " §eTime until EOTW: §7"  + SurgeCore.getRemaining(Math.abs(elapsedMillis), true, false));
                Bukkit.broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█" + ChatColor.RED + "█████" + " §7More information will be broadcasted when SOTW starts!");
                Bukkit.broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█████" + ChatColor.RED + "█" + "");
                Bukkit.broadcastMessage(ChatColor.RED + "███████");
                Bukkit.broadcastMessage("");
                return;
            }
            final Iterator<Player> iterator = this.outsideBorder.iterator();
            while (iterator.hasNext()) {
                final Player player = iterator.next();
                if (BorderListener.isWithinBorder(player.getLocation())) {
                    iterator.remove();
                }
                else {
                    player.sendMessage(ChatColor.RED + "You are currently outside of the border during EOTW, so you were withered.");
                    player.addPotionEffect(EotwRunnable.WITHER, true);
                }
            }
            final int newBorderSize = this.borderSize - 200;
            if (elapsedSeconds % EotwHandler.BORDER_DECREASE_TIME_SECONDS == 0) {
                final Map<World.Environment, Integer> border_SIZES = CoreConfiguration.BORDER_SIZES;
                final World.Environment normal = World.Environment.NORMAL;
                final int borderSize = newBorderSize;
                this.borderSize = borderSize;
                border_SIZES.put(normal, borderSize);
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(ChatColor.RED + "███████");
                Bukkit.broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█████" + ChatColor.RED + "█" + "");
                Bukkit.broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█" + ChatColor.RED + "█████" + " §6§lEOTW - §dBorder has been §cSHRUNK§d!");
                Bukkit.broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "████" + ChatColor.RED + "██" + " §eNew Border Size: §7" + newBorderSize + " blocks");
                Bukkit.broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█" + ChatColor.RED + "█████" + " §eNext Border Size: §7" + (newBorderSize - 200) + " blocks");
                Bukkit.broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█████" + ChatColor.RED + "█" + "");
                Bukkit.broadcastMessage(ChatColor.RED + "███████");
                Bukkit.broadcastMessage("");
                
                for (final Player player2 : Bukkit.getOnlinePlayers()) {
                    if (!BorderListener.isWithinBorder(player2.getLocation())) {
                        this.outsideBorder.add(player2);
                    }
                }
            }
            else if (elapsedSeconds % (EotwHandler.BORDER_DECREASE_TIME_SECONDS / 2) == 0) {
            	Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(ChatColor.RED + "███████");
                Bukkit.broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█████" + ChatColor.RED + "█" + "");
                Bukkit.broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█" + ChatColor.RED + "█████" + " §6§lEOTW - §dBorder will be shrunk in §c" + EotwHandler.BORDER_DECREASE_TIME_ALERT_WORDS + "§7.");
                Bukkit.broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "████" + ChatColor.RED + "██" + " §eCurrent Border Size: §7" + this.borderSize + " blocks");
                Bukkit.broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█" + ChatColor.RED + "█████" + " §eNext Border Size: §7" + newBorderSize + " blocks");
                Bukkit.broadcastMessage(ChatColor.RED + "█" + ChatColor.DARK_RED + "█████" + ChatColor.RED + "█" + "");
                Bukkit.broadcastMessage(ChatColor.RED + "███████");
                Bukkit.broadcastMessage("");
            }
        }
        
        static {
            WITHER = new PotionEffect(PotionEffectType.WITHER, 200, 0);
        }
    }
}