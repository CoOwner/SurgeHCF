package com.surgehcf.core.hcf.timer.type;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.eventgame.eotw.EotwHandler;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.claim.Claim;
import com.surgehcf.core.hcf.faction.event.FactionClaimChangedEvent;
import com.surgehcf.core.hcf.faction.event.PlayerClaimEnterEvent;
import com.surgehcf.core.hcf.faction.event.cause.ClaimChangeCause;
import com.surgehcf.core.hcf.faction.type.ClaimableFaction;
import com.surgehcf.core.hcf.faction.type.Faction;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;
import com.surgehcf.core.hcf.faction.type.RoadFaction;
import com.surgehcf.core.hcf.timer.PlayerTimer;
import com.surgehcf.core.hcf.timer.Timer;
import com.surgehcf.core.hcf.timer.TimerRunnable;
import com.surgehcf.core.hcf.timer.event.TimerClearEvent;
import com.surgehcf.core.hcf.visualise.VisualBlock;
import com.surgehcf.core.hcf.visualise.VisualType;
import com.surgehcf.core.hcf.visualise.VisualiseHandler;

import me.milksales.util.BukkitUtils;
import me.milksales.util.Config;
import me.milksales.util.GenericUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import net.minecraft.util.com.google.common.cache.Cache;
import net.minecraft.util.com.google.common.cache.CacheBuilder;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PvpProtectionTimer
extends PlayerTimer
implements Listener {
    private static final String PVP_COMMAND = "/pvp enable";
    private static final long ITEM_PICKUP_DELAY = TimeUnit.SECONDS.toMillis(30);
    private static final long ITEM_PICKUP_MESSAGE_DELAY = 1250;
    private static final String ITEM_PICKUP_MESSAGE_META_KEY = "pickupMessageDelay";
    public final Set<UUID> legible = new HashSet<UUID>();
    private final ConcurrentMap<Object, Object> itemUUIDPickupDelays;
    private final SurgeCore plugin;

    public PvpProtectionTimer(SurgeCore plugin) {
        super("Immunity", TimeUnit.MINUTES.toMillis(30));
        this.plugin = plugin;
        this.itemUUIDPickupDelays = CacheBuilder.newBuilder().expireAfterWrite(ITEM_PICKUP_DELAY + 5000, TimeUnit.MILLISECONDS).build().asMap();
    }

    @Override
    public String getScoreboardPrefix() {
        return ChatColor.GREEN.toString();
    }

    @Override
    public void onExpire(UUID userUUID) {
        Player player = Bukkit.getPlayer((UUID)userUUID);
        if (player == null) {
            return;
        }
        if (this.getRemaining(player) <= 0) {
            this.plugin.getVisualiseHandler().clearVisualBlocks(player, VisualType.CLAIM_BORDER, null);
            player.sendMessage(ChatColor.RED.toString() + "You no longer have " + this.getDisplayName() + (Object)ChatColor.RED + '.');
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onTimerStop(TimerClearEvent event) {
        Optional<UUID> optionalUserUUID;
        if (event.getTimer().equals(this) && (optionalUserUUID = event.getUserUUID()).isPresent()) {
            this.onExpire((UUID)optionalUserUUID.get());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onClaimChange(final FactionClaimChangedEvent event) {
        if (event.getCause() != ClaimChangeCause.CLAIM) {
            return;
        }
        final Collection<Claim> claims = event.getAffectedClaims();
        for (final Claim claim : claims) {
            final Collection<Player> players = (Collection<Player>)claim.getPlayers();
            for (final Player player : players) {
                if (this.getRemaining(player) > 0L) {
                    Location location = player.getLocation();
                    location.setX(claim.getMinimumX() - 1);
                    location.setY(0);
                    location.setZ(claim.getMinimumZ() - 1);
                    location = BukkitUtils.getHighestLocation(location, location);
                    if (!player.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN)) {
                        continue;
                    }
                    player.sendMessage(ChatColor.RED + "Land was claimed where you were standing. As you still have your " + this.getName() + " timer, you were teleported away.");
                }
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        this.clearCooldown(player);
        if (this.legible.add(player.getUniqueId())) {
            player.sendMessage((Object)ChatColor.YELLOW + "Once you leave Spawn your 30 minutes of " + this.getDisplayName() + (Object)ChatColor.YELLOW + " will start.");
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        World world = player.getWorld();
        Location location = player.getLocation();
        Iterator iterator = event.getDrops().iterator();
        while (iterator.hasNext()) {
            this.itemUUIDPickupDelays.put(world.dropItemNaturally(location, (ItemStack)iterator.next()).getUniqueId(), System.currentTimeMillis() + ITEM_PICKUP_DELAY);
            iterator.remove();
        }
        this.clearCooldown(player);
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        long remaining = this.getRemaining(player);
        if (remaining > 0) {
            event.setCancelled(true);
            player.sendMessage((Object)ChatColor.RED + "You cannot empty buckets as your " + this.getDisplayName() + (Object)ChatColor.RED + " timer is active [" + (Object)ChatColor.BOLD + SurgeCore.getRemaining(remaining, true, false) + (Object)ChatColor.RED + " remaining]");
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onBlockIgnite(BlockIgniteEvent event) {
        Player player = event.getPlayer();
        if (player == null) {
            return;
        }
        long remaining = this.getRemaining(player);
        if (remaining > 0) {
            event.setCancelled(true);
            player.sendMessage((Object)ChatColor.RED + "You cannot ignite blocks as your " + this.getDisplayName() + (Object)ChatColor.RED + " timer is active [" + (Object)ChatColor.BOLD + SurgeCore.getRemaining(remaining, true, false) + (Object)ChatColor.RED + " remaining]");
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onItemPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        long remaining = this.getRemaining(player);
        if (remaining > 0) {
            UUID itemUUID = event.getItem().getUniqueId();
            Long delay = (Long)this.itemUUIDPickupDelays.get(itemUUID);
            if (delay == null) {
                return;
            }
            long millis = System.currentTimeMillis();
            if (delay - millis > 0) {
                MetadataValue value;
                event.setCancelled(true);
                MetadataValue metadataValue = value = player.getMetadata("pickupMessageDelay").iterator().hasNext() ? (MetadataValue)player.getMetadata("pickupMessageDelay").iterator().next() : null;
                if (value != null && value.asLong() - millis <= 0) {
                    player.setMetadata("pickupMessageDelay", (MetadataValue)new FixedMetadataValue((Plugin)this.plugin, (Object)(millis + 1250)));
                    player.sendMessage((Object)ChatColor.RED + "You cannot pick this item up for another " + (Object)ChatColor.BOLD + DurationFormatUtils.formatDurationWords((long)remaining, (boolean)true, (boolean)true) + (Object)ChatColor.RED + " as your " + this.getDisplayName() + (Object)ChatColor.RED + " timer is active [" + (Object)ChatColor.BOLD + SurgeCore.getRemaining(remaining, true, false) + (Object)ChatColor.RED + " remaining]");
                }
            } else {
                this.itemUUIDPickupDelays.remove(itemUUID);
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        TimerRunnable runnable = (TimerRunnable)this.cooldowns.get(player.getUniqueId());
        if (runnable != null && runnable.getRemaining() > 0) {
            runnable.setPaused(true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerSpawnLocation(PlayerSpawnLocationEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            if (!this.plugin.getEotwHandler().isEndOfTheWorld() && this.legible.add(player.getUniqueId())) {
                player.sendMessage((Object)ChatColor.AQUA + "Once you leave Spawn your 30 minutes of " + this.getName() + (Object)ChatColor.AQUA + " will start.");
            }
        } else if (this.isPaused(player) && this.getRemaining(player) > 0 && !this.plugin.getFactionManager().getFactionAt(event.getSpawnLocation()).isSafezone()) {
            this.setPaused(player, player.getUniqueId(), false);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlayerClaimEnterMonitor(PlayerClaimEnterEvent event) {
        Player player = event.getPlayer();
        if (event.getTo().getWorld().getEnvironment() == World.Environment.THE_END) {
            this.clearCooldown(player);
            return;
        }
        Faction toFaction = event.getToFaction();
        Faction fromFaction = event.getFromFaction();
        if (fromFaction.isSafezone() && !toFaction.isSafezone()) {
            if (this.legible.remove(player.getUniqueId())) {
                this.setCooldown(player, player.getUniqueId());
                player.sendMessage((Object)ChatColor.GREEN + "Your " + this.getDisplayName() + (Object)ChatColor.GREEN + " timer has started.");
                return;
            }
            if (this.getRemaining(player) > 0) {
                this.setPaused(player, player.getUniqueId(), false);
                player.sendMessage((Object)ChatColor.RED + "Your " + this.getDisplayName() + (Object)ChatColor.RED + " timer is no longer paused.");
            }
        } else if (!fromFaction.isSafezone() && toFaction.isSafezone() && this.getRemaining(player) > 0) {
            player.sendMessage((Object)ChatColor.GREEN + "Your " + this.getDisplayName() + (Object)ChatColor.GREEN + " timer is now paused.");
            this.setPaused(player, player.getUniqueId(), true);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onPlayerClaimEnter(PlayerClaimEnterEvent event) {
        long remaining;
        Player player = event.getPlayer();
        Faction toFaction = event.getToFaction();
        if (toFaction instanceof ClaimableFaction && (remaining = this.getRemaining(player)) > 0) {
            PlayerFaction playerFaction;
            if (event.getEnterCause() == PlayerClaimEnterEvent.EnterCause.TELEPORT && toFaction instanceof PlayerFaction && (playerFaction = this.plugin.getFactionManager().getPlayerFaction(player)) != null && playerFaction.equals(toFaction)) {
                player.sendMessage((Object)ChatColor.YELLOW + "You have entered your own claim, therefore your " + this.getDisplayName() + (Object)ChatColor.YELLOW + " has been removed.");
                this.clearCooldown(player);
                return;
            }
            if (!toFaction.isSafezone() && !(toFaction instanceof RoadFaction)) {
                event.setCancelled(true);
                player.sendMessage((Object)ChatColor.RED + "You cannot enter " + toFaction.getDisplayName((CommandSender)player) + (Object)ChatColor.RED + " whilst your " + this.getDisplayName() + (Object)ChatColor.RED + " timer is active [" + (Object)ChatColor.BOLD + SurgeCore.getRemaining(remaining, true, false) + (Object)ChatColor.RED + " remaining]. " + "Use '" + (Object)ChatColor.GOLD + "/pvp enable" + (Object)ChatColor.RED + "' to remove this timer.");
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player attacker = BukkitUtils.getFinalAttacker((EntityDamageEvent)event, (boolean)true);
            if (attacker == null) {
                return;
            }
            Player player = (Player)entity;
            long remaining = this.getRemaining(player);
            if (remaining > 0) {
                event.setCancelled(true);
                attacker.sendMessage((Object)ChatColor.RED + player.getName() + " has their " + this.getDisplayName() + (Object)ChatColor.RED + " timer for another " + (Object)ChatColor.BOLD + SurgeCore.getRemaining(remaining, true, false) + (Object)ChatColor.RED + '.');
                return;
            }
            remaining = this.getRemaining(attacker);
            if (remaining > 0) {
                event.setCancelled(true);
                attacker.sendMessage((Object)ChatColor.RED + "You cannot attack players whilst your " + this.getDisplayName() + (Object)ChatColor.RED + " timer is active [" + (Object)ChatColor.BOLD + SurgeCore.getRemaining(remaining, true, false) + (Object)ChatColor.RED + " remaining]. Use '" + (Object)ChatColor.GOLD + "/pvp enable" + (Object)ChatColor.RED + "' to allow pvp.");
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
    public void onPotionSplash(PotionSplashEvent event) {
        ThrownPotion potion = event.getPotion();
        if (potion.getShooter() instanceof Player && BukkitUtils.isDebuff((ThrownPotion)potion)) {
            for (LivingEntity livingEntity : event.getAffectedEntities()) {
                if (!(livingEntity instanceof Player) || this.getRemaining((Player)livingEntity) <= 0) continue;
                event.setIntensity(livingEntity, 0.0);
            }
        }
    }

    public Set<UUID> getLegible() {
        return this.legible;
    }

    @Override
    public long getRemaining(UUID playerUUID) {
        return this.plugin.getEotwHandler().isEndOfTheWorld() ? 0 : super.getRemaining(playerUUID);
    }

    @Override
    public boolean setCooldown(@Nullable Player player, UUID playerUUID, long duration, boolean overwrite) {
        return !this.plugin.getEotwHandler().isEndOfTheWorld() && super.setCooldown(player, playerUUID, duration, overwrite);
    }

    @Override
    public TimerRunnable clearCooldown(UUID playerUUID) {
        TimerRunnable runnable = super.clearCooldown(playerUUID);
        if (runnable != null) {
            this.legible.remove(playerUUID);
            return runnable;
        }
        return null;
    }

    @Override
    public void load(Config config) {
        super.load(config);
        Object object = config.get("pvp-timer-legible");
        if (object instanceof List) {
            this.legible.addAll(GenericUtils.createList((Object)object, String.class).stream().map(UUID::fromString).collect(Collectors.toList()));
        }
    }

    @Override
    public void onDisable(Config config) {
        super.onDisable(config);
        config.set("pvp-timer-legible", new ArrayList<UUID>(this.legible).stream().map(UUID::toString).collect(Collectors.toList()));
    }
}