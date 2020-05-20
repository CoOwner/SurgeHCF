package com.surgehcf.core.hcf.timer;

import java.util.Iterator;
import java.util.Collection;

import org.bukkit.plugin.Plugin;

import java.util.HashSet;

import me.milksales.util.Config;

import org.bukkit.plugin.java.JavaPlugin;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.eventgame.EventTimer;
import com.surgehcf.core.hcf.timer.Timer;
import com.surgehcf.core.hcf.timer.type.ArcherTimer;
import com.surgehcf.core.hcf.timer.type.EnderPearlTimer;
import com.surgehcf.core.hcf.timer.type.LogoutTimer;
import com.surgehcf.core.hcf.timer.type.NotchAppleTimer;
import com.surgehcf.core.hcf.timer.type.PvpClassWarmupTimer;
import com.surgehcf.core.hcf.timer.type.PvpProtectionTimer;
import com.surgehcf.core.hcf.timer.type.SOTWTimer;
import com.surgehcf.core.hcf.timer.type.SpawnTagTimer;
import com.surgehcf.core.hcf.timer.type.StuckTimer;
import com.surgehcf.core.hcf.timer.type.TeleportTimer;

import java.util.Set;

import org.bukkit.event.Listener;

public class TimerManager implements Listener
{
    public final LogoutTimer logoutTimer;
    public final EnderPearlTimer enderPearlTimer;
    public final NotchAppleTimer notchAppleTimer;
    public final PvpProtectionTimer pvpProtectionTimer;
    public final PvpClassWarmupTimer pvpClassWarmupTimer;
    public final StuckTimer stuckTimer;
    public final SpawnTagTimer spawnTagTimer;
    public final SOTWTimer sotw;
    public final TeleportTimer teleportTimer;
    public final EventTimer eventTimer;
    public final ArcherTimer archerTimer;
    private final Set<Timer> timers;
    private final JavaPlugin plugin;
    private Config config;
    
    public TimerManager(final SurgeCore plugin) {
        this.timers = new HashSet<Timer>();
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
        this.registerTimer(this.sotw = new SOTWTimer());
        this.registerTimer(this.archerTimer = new ArcherTimer(plugin));
        this.registerTimer(this.enderPearlTimer = new EnderPearlTimer(plugin));
        this.registerTimer(this.logoutTimer = new LogoutTimer());
        this.registerTimer(this.notchAppleTimer = new NotchAppleTimer(plugin));
        this.registerTimer(this.stuckTimer = new StuckTimer());
        this.registerTimer(this.pvpProtectionTimer = new PvpProtectionTimer(plugin));
        this.registerTimer(this.spawnTagTimer = new SpawnTagTimer(plugin));
        this.registerTimer(this.teleportTimer = new TeleportTimer(plugin));
        this.registerTimer(this.eventTimer = new EventTimer(plugin));
        this.registerTimer(this.pvpClassWarmupTimer = new PvpClassWarmupTimer(plugin));
        this.reloadTimerData();
    }
    
    public Collection<Timer> getTimers() {
        return this.timers;
    }
    
    public void registerTimer(final Timer timer) {
        this.timers.add(timer);
        if (timer instanceof Listener) {
            this.plugin.getServer().getPluginManager().registerEvents((Listener)timer, (Plugin)this.plugin);
        }
    }
    
    public void unregisterTimer(final Timer timer) {
        this.timers.remove(timer);
    }
    
    public void reloadTimerData() {
        this.config = new Config(this.plugin, "timers");
        for (final Timer timer : this.timers) {
            timer.load(this.config);
        }
    }
    
    public void saveTimerData() {
        for (final Timer timer : this.timers) {
            timer.onDisable(this.config);
        }
        this.config.save();
    }
}