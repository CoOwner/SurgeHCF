package com.surgehcf.core.hcf.pvpclass;

import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.util.HashSet;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.surgehcf.core.hcf.pvpclass.PvpClass;

import java.util.Set;

public abstract class PvpClass
{
    public static final long DEFAULT_MAX_DURATION;
    protected final Set<PotionEffect> passiveEffects;
    protected final String name;
    protected final long warmupDelay;
    
    public PvpClass(final String name, final long warmupDelay) {
        this.passiveEffects = new HashSet<PotionEffect>();
        this.name = name;
        this.warmupDelay = warmupDelay;
    }
    
    public String getName() {
        return this.name;
    }
    
    public long getWarmupDelay() {
        return this.warmupDelay;
    }
    
    public boolean onEquip(final Player player) {
        for (final PotionEffect effect : this.passiveEffects) {
            player.addPotionEffect(effect, true);
        }
        player.sendMessage(ChatColor.YELLOW + "Class " + ChatColor.LIGHT_PURPLE + this.name + ChatColor.YELLOW + " has been equipped.");
        return true;
    }
    
    public void onUnequip(final Player player) {
        for (final PotionEffect effect : this.passiveEffects) {
            for (final PotionEffect active : player.getActivePotionEffects()) {
                if (active.getDuration() > PvpClass.DEFAULT_MAX_DURATION && active.getType().equals((Object)effect.getType()) && active.getAmplifier() == effect.getAmplifier()) {
                    ((CraftPlayer)player).removePotionEffect(effect.getType());
                    break;
                }
                if (this.name != "Reaper") {
                    continue;
                }
                ((CraftPlayer)player).removePotionEffect(PotionEffectType.SPEED);
            }
        }
        player.sendMessage(ChatColor.YELLOW + "Class " + ChatColor.LIGHT_PURPLE + this.name + ChatColor.YELLOW + " has been un-equipped.");
    }
    
    public abstract boolean isApplicableFor(final Player p0);
    
    static {
        DEFAULT_MAX_DURATION = TimeUnit.MINUTES.toMillis(8L);
    }
}