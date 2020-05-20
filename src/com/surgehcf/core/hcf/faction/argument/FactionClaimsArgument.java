package com.surgehcf.core.hcf.faction.argument;

import me.milksales.util.command.CommandArgument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.claim.Claim;
import com.surgehcf.core.hcf.faction.type.ClaimableFaction;
import com.surgehcf.core.hcf.faction.type.Faction;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;

public class FactionClaimsArgument
extends CommandArgument {
    private final SurgeCore plugin;

    public FactionClaimsArgument(SurgeCore plugin) {
        super("claims", "View all claims for a faction.");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " [factionName]";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Faction targetFaction;
        PlayerFaction selfFaction;
        PlayerFaction playerFaction = selfFaction = sender instanceof Player ? this.plugin.getFactionManager().getPlayerFaction((Player)sender) : null;
        if (args.length < 2) {
            if (!(sender instanceof Player)) {
                sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
                return true;
            }
            if (selfFaction == null) {
                sender.sendMessage((Object)ChatColor.RED + "You are not in a faction.");
                return true;
            }
            targetFaction = selfFaction;
        } else {
            Faction faction = this.plugin.getFactionManager().getContainingFaction(args[1]);
            if (faction == null) {
                sender.sendMessage((Object)ChatColor.RED + "Faction named or containing member with IGN or UUID " + args[1] + " not found.");
                return true;
            }
            if (!(faction instanceof ClaimableFaction)) {
                sender.sendMessage((Object)ChatColor.RED + "You can only check the claims of factions that can have claims.");
                return true;
            }
            targetFaction = (ClaimableFaction)faction;
        }
        Set<Claim> claims = ((ClaimableFaction) targetFaction).getClaims();
        if (claims.isEmpty()) {
            sender.sendMessage((Object)ChatColor.RED + "Faction " + targetFaction.getDisplayName(sender) + (Object)ChatColor.RED + " has no claimed land.");
            return true;
        }
        if (sender instanceof Player && !sender.isOp() && targetFaction instanceof PlayerFaction && ((PlayerFaction)targetFaction).getHome() == null && (selfFaction == null || !selfFaction.equals(targetFaction))) {
            sender.sendMessage((Object)ChatColor.RED + "You cannot view the claims of " + targetFaction.getDisplayName(sender) + (Object)ChatColor.RED + " because their home is unset.");
            return true;
        }
        sender.sendMessage((Object)ChatColor.YELLOW + "Claims of " + targetFaction.getDisplayName(sender));
        for (Claim claim : claims) {
            sender.sendMessage((Object)ChatColor.GRAY + " " + claim.getFormattedName());
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2 || !(sender instanceof Player)) {
            return Collections.emptyList();
        }
        if (args[1].isEmpty()) {
            return null;
        }
        Player player = (Player)sender;
        ArrayList<String> results = new ArrayList<String>(this.plugin.getFactionManager().getFactionNameMap().keySet());
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (!player.canSee(target) || results.contains(target.getName())) continue;
            results.add(target.getName());
        }
        return results;
    }
}