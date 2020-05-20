package com.surgehcf.core.hcf.faction.argument.staff;

import org.bukkit.Bukkit;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.claim.ClaimSelection;
import com.surgehcf.core.hcf.faction.type.ClaimableFaction;
import com.surgehcf.core.hcf.faction.type.Faction;

import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.milksales.util.command.CommandArgument;

public class FactionClaimForArgument extends CommandArgument
{
    private final SurgeCore plugin;
    
    public FactionClaimForArgument(final SurgeCore plugin) {
        super("claimfor", "Claims land for another faction.");
        this.plugin = plugin;
        this.permission = "hcf.command.faction.argument." + this.getName();
    }
    
    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName() + " <factionName>";
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        final Faction targetFaction = this.plugin.getFactionManager().getFaction(args[1]);
        if (!(targetFaction instanceof ClaimableFaction)) {
            sender.sendMessage(ChatColor.RED + "Claimable faction named " + args[1] + " not found.");
            return true;
        }
        final Player player = (Player)sender;
        final UUID uuid = player.getUniqueId();
        final ClaimSelection claimSelection = this.plugin.getClaimHandler().claimSelectionMap.get(uuid);
        if (claimSelection == null || !claimSelection.hasBothPositionsSet()) {
            player.sendMessage(ChatColor.RED + "You have not set both positions of this claim selection.");
            return true;
        }
        if (this.plugin.getClaimHandler().tryPurchasing(player, claimSelection.toClaim(targetFaction))) {
            this.plugin.getClaimHandler().clearClaimSelection(player);
            player.setItemInHand(new ItemStack(Material.AIR, 1));
            sender.sendMessage(ChatColor.YELLOW + "Successfully claimed this land for " + ChatColor.RED + targetFaction.getName() + ChatColor.YELLOW + '.');
        }
        return true;
    }
    
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length != 2 || !(sender instanceof Player)) {
            return Collections.emptyList();
        }
        if (args[1].isEmpty()) {
            return null;
        }
        final Player player = (Player)sender;
        final List<String> results = new ArrayList<String>(this.plugin.getFactionManager().getFactionNameMap().keySet());
        for (final Player target : Bukkit.getOnlinePlayers()) {
            if (player.canSee(target) && !results.contains(target.getName())) {
                results.add(target.getName());
            }
        }
        return results;
    }
}