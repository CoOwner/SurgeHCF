package com.surgehcf.core.hcf.faction.claim;

import com.google.common.base.Predicate;
import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.CoreConfiguration;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.FactionMember;
import com.surgehcf.core.hcf.faction.claim.Claim;
import com.surgehcf.core.hcf.faction.claim.ClaimHandler;
import com.surgehcf.core.hcf.faction.claim.ClaimSelection;
import com.surgehcf.core.hcf.faction.struct.Role;
import com.surgehcf.core.hcf.faction.type.ClaimableFaction;
import com.surgehcf.core.hcf.faction.type.Faction;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;
import com.surgehcf.core.hcf.faction.type.RoadFaction;
import com.surgehcf.core.hcf.faction.type.WildernessFaction;
import com.surgehcf.core.hcf.visualise.VisualBlock;
import com.surgehcf.core.hcf.visualise.VisualType;
import com.surgehcf.core.hcf.visualise.VisualiseHandler;

import me.milksales.util.ItemBuilder;
import me.milksales.util.cuboid.Cuboid;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import net.minecraft.util.com.google.common.base.Preconditions;
import net.minecraft.util.com.google.common.cache.CacheBuilder;
import net.minecraft.util.com.google.common.cache.CacheLoader;
import net.minecraft.util.com.google.common.cache.LoadingCache;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ClaimHandler {
    public static final int MIN_CLAIM_HEIGHT = 0;
    public static final int MAX_CLAIM_HEIGHT = 256;
    public static final long PILLAR_BUFFER_DELAY_MILLIS = 200;
    public static final ItemStack CLAIM_WAND = new ItemBuilder(Material.GOLD_AXE).displayName(ChatColor.GREEN.toString() + "Claim Wand").lore(new String[]{(Object)ChatColor.BLUE + "Left or Right Click " + (Object)ChatColor.GREEN + "a Block" + (Object)ChatColor.BLUE + " to:", (Object)ChatColor.GRAY + "Set the first and second position of ", (Object)ChatColor.GRAY + "your Claim selection.", "", (Object)ChatColor.BLUE + "Right Click " + (Object)ChatColor.GREEN + "the Air" + (Object)ChatColor.BLUE + " to:", (Object)ChatColor.GRAY + "Clear your current Claim selection.", "", (Object)ChatColor.BLUE + "Shift " + (Object)ChatColor.BLUE + "Left Click " + (Object)ChatColor.GREEN + "the Air or a Block" + (Object)ChatColor.BLUE + " to:", (Object)ChatColor.GRAY + "Purchase your current Claim selection."}).build();
    public static final int MIN_SUBCLAIM_RADIUS = 2;
    public static final int MIN_CLAIM_RADIUS = 5;
    public static final int MAX_CHUNKS_PER_LIMIT = 16;
    public static final int CLAIM_BUFFER_RADIUS = 4;
    private static final int NEXT_PRICE_MULTIPLIER_AREA = 250;
    private static final int NEXT_PRICE_MULTIPLIER_CLAIM = 500;
    private static final double CLAIM_SELL_MULTIPLIER = 0.8;
    private static final double CLAIM_PRICE_PER_BLOCK = 0.25;
    public final ConcurrentMap<UUID, ClaimSelection> claimSelectionMap;
    private final SurgeCore plugin;

    public ClaimHandler(SurgeCore plugin) {
        this.plugin = plugin;
        CacheLoader<UUID, ClaimSelection> loader = new CacheLoader<UUID, ClaimSelection>(){

            public ClaimSelection load(UUID uuid) throws Exception {
                return ClaimHandler.this.claimSelectionMap.get(uuid);
            }
        };
        this.claimSelectionMap = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).build((CacheLoader)loader).asMap();
    }

    public int calculatePrice(Cuboid claim, int currentClaims, boolean selling) {
        if (currentClaims == -1 || !claim.hasBothPositionsSet()) {
            return 0;
        }
        int multiplier = 1;
        int remaining = claim.getArea();
        double price = 0.0;
        while (remaining > 0) {
            if (--remaining % 250 == 0) {
                ++multiplier;
            }
            price += 0.25 * (double)multiplier;
        }
        if (currentClaims != 0) {
            currentClaims = Math.max(currentClaims + (selling ? -1 : 0), 0);
            price += (double)(currentClaims * 500);
        }
        if (selling) {
            price *= 0.8;
        }
        return (int)price;
    }

    public boolean clearClaimSelection(Player player) {
        ClaimSelection claimSelection = this.plugin.getClaimHandler().claimSelectionMap.remove(player.getUniqueId());
        if (claimSelection != null) {
            this.plugin.getVisualiseHandler().clearVisualBlocks(player, VisualType.CREATE_CLAIM_SELECTION, null);
            return true;
        }
        return false;
    }

    public boolean canSubclaimHere(Player player, Location location) {
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            player.sendMessage((Object)ChatColor.RED + "You must be in a faction to subclaim land.");
            return false;
        }
        if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
            player.sendMessage((Object)ChatColor.RED + "You must be an officer to claim land.");
            return false;
        }
        if (!this.plugin.getFactionManager().getFactionAt(location).equals(playerFaction)) {
            player.sendMessage((Object)ChatColor.RED + "This location is not part of your factions' territory.");
            return false;
        }
        return true;
    }

    public boolean canClaimHere(Player player, Location location) {
        World world = location.getWorld();
        if(player.isOp()){
        	return true;
        }
        if (world.getEnvironment() != World.Environment.NORMAL) {
            player.sendMessage((Object)ChatColor.RED + "You can only claim land in the Overworld.");
            return false;
        }
        if (!(this.plugin.getFactionManager().getFactionAt(location) instanceof WildernessFaction)) {
            player.sendMessage(ChatColor.YELLOW + "You can only claim land in the " + (Object)CoreConfiguration.WILDERNESS_COLOUR + "Wilderness" + (Object)ChatColor.YELLOW + ". " + "Make sure you are past " + 1000 + " blocks from spawn..");
            return false;
        }
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            player.sendMessage((Object)ChatColor.RED + "You must be in a faction to claim land.");
            return false;
        }
        if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
            player.sendMessage((Object)ChatColor.RED + "You must be an officer to claim land.");
            return false;
        }
        if (playerFaction.getClaims().size() >= 8) {
            player.sendMessage((Object)ChatColor.RED + "Your faction has maximum claims - " + 8);
            return false;
        }
        int locX = location.getBlockX();
        int locZ = location.getBlockZ();
        FactionManager factionManager = this.plugin.getFactionManager();
        for (int x = locX - 5; x < locX + 5; ++x) {
            for (int z = locZ - 5; z < locZ + 5; ++z) {
                Faction factionAtNew = factionManager.getFactionAt(world, x, z);
                if (!(factionAtNew instanceof RoadFaction)) {
                    // empty if block
                }
                if (playerFaction.equals(factionAtNew) || !(factionAtNew instanceof ClaimableFaction)) continue;
                player.sendMessage((Object)ChatColor.RED + "This position contains enemy claims within a " + 5 + " block buffer radius.");
                return false;
            }
        }
        return true;
    }

    public boolean tryPurchasing(Player player, Claim claim) {
        int x;
        int z;
        Preconditions.checkNotNull((Object)claim, (Object)"Claim is null");
        World world = claim.getWorld();
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        int minimumX = claim.getMinimumX();
        int maximumX = claim.getMaximumX();
        int minimumZ = claim.getMinimumZ();
        int maximumZ = claim.getMaximumZ();
        FactionManager factionManager = this.plugin.getFactionManager();
        if(player.isOp()){
            claim.setY1(0);
            claim.setY2(256);
            if (!playerFaction.addClaim(claim, (CommandSender)player)) {
                return false;
            }
            Location center = claim.getCenter();
            int factionBalance = playerFaction.getBalance();
            int claimPrice = this.calculatePrice((Cuboid)claim, playerFaction.getClaims().size(), false);
            player.sendMessage((Object)ChatColor.YELLOW + "Claim has been purchased for " + (Object)ChatColor.GREEN + '$' + claimPrice + (Object)ChatColor.YELLOW + '.');
            playerFaction.setBalance(factionBalance - claimPrice);
            playerFaction.broadcast((Object)ChatColor.GOLD + player.getName() + (Object)ChatColor.GREEN + " claimed land for your faction at " + (Object)ChatColor.GOLD + '(' + center.getBlockX() + ", " + center.getBlockZ() + ')' + (Object)ChatColor.GREEN + '.', player.getUniqueId());
            return false;
        }
        if (world.getEnvironment() != World.Environment.NORMAL) {
            player.sendMessage((Object)ChatColor.RED + "You can only claim land in the Overworld.");
            return false;
        }
        if (playerFaction == null) {
            player.sendMessage((Object)ChatColor.RED + "You must be in a faction to claim land.");
            return false;
        }
        if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
            player.sendMessage((Object)ChatColor.RED + "You must be an officer to claim land.");
            return false;
        }
        if (playerFaction.getClaims().size() >= 8) {
            player.sendMessage((Object)ChatColor.RED + "Your faction has maximum claims - " + 8);
            return false;
        }
        int factionBalance = playerFaction.getBalance();
        int claimPrice = this.calculatePrice((Cuboid)claim, playerFaction.getClaims().size(), false);
        if (claimPrice > factionBalance) {
            player.sendMessage((Object)ChatColor.RED + "Your faction bank only has " + '$' + factionBalance + ", the price of this claim is " + '$' + claimPrice + '.');
            return false;
        }
        if (claim.getChunks().size() > 16) {
            player.sendMessage((Object)ChatColor.RED + "Claims cannot exceed " + 16 + " chunks.");
            return false;
        }
        if (claim.getWidth() < 5 || claim.getLength() < 5) {
            player.sendMessage((Object)ChatColor.RED + "Claims must be at least " + 5 + 'x' + 5 + " blocks.");
            return false;
        }
        for (x = minimumX; x < maximumX; ++x) {
            for (z = minimumZ; z < maximumZ; ++z) {
                Faction factionAt = factionManager.getFactionAt(world, x, z);
                if (factionAt == null || factionAt instanceof WildernessFaction) continue;
                player.sendMessage((Object)ChatColor.RED + "This claim contains a location not within the " + (Object)ChatColor.GRAY + "Wilderness" + (Object)ChatColor.RED + '.');
                return false;
            }
        }
        for (x = minimumX - 10; x < maximumX + 10; ++x) {
            for (z = minimumZ - 10; z < maximumZ + 10; ++z) {
                Faction factionAtNew = factionManager.getFactionAt(world, x, z);
                if (!(factionAtNew instanceof RoadFaction)) {
                    // empty if block
                }
                if (playerFaction.equals(factionAtNew) || !(factionAtNew instanceof ClaimableFaction)) continue;
                player.sendMessage((Object)ChatColor.RED + "This claim contains enemy claims within a " + 10 + " block buffer radius.");
                return false;
            }
        }
        Location minimum = claim.getMinimumPoint();
        Location maximum = claim.getMaximumPoint();
        Set<Claim> otherClaims = playerFaction.getClaims();
        boolean conjoined = otherClaims.isEmpty();
        if (!conjoined) {
            player.sendMessage((Object)ChatColor.RED + "Use /f unclaim to resize your faction claims.");
            return false;
        }
        claim.setY1(0);
        claim.setY2(256);
        if (!playerFaction.addClaim(claim, (CommandSender)player)) {
            return false;
        }
        Location center = claim.getCenter();
        player.sendMessage((Object)ChatColor.YELLOW + "Claim has been purchased for " + (Object)ChatColor.GREEN + '$' + claimPrice + (Object)ChatColor.YELLOW + '.');
        playerFaction.setBalance(factionBalance - claimPrice);
        playerFaction.broadcast((Object)ChatColor.GOLD + player.getName() + (Object)ChatColor.GREEN + " claimed land for your faction at " + (Object)ChatColor.GOLD + '(' + center.getBlockX() + ", " + center.getBlockZ() + ')' + (Object)ChatColor.GREEN + '.', player.getUniqueId());
        return true;
    }

}
