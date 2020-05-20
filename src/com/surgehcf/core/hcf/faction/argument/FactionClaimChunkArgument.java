package com.surgehcf.core.hcf.faction.argument;

import me.milksales.util.command.CommandArgument;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.FactionMember;
import com.surgehcf.core.hcf.faction.claim.Claim;
import com.surgehcf.core.hcf.faction.claim.ClaimHandler;
import com.surgehcf.core.hcf.faction.struct.Role;
import com.surgehcf.core.hcf.faction.type.Faction;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;

public class FactionClaimChunkArgument
extends CommandArgument {
    private static final int CHUNK_RADIUS = 7;
    private final SurgeCore plugin;

    public FactionClaimChunkArgument(SurgeCore plugin) {
        super("claimchunk", "Claim a chunk of land in the Wilderness.", new String[]{"chunkclaim"});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        Player player = (Player)sender;
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            sender.sendMessage((Object)ChatColor.RED + "You are not in a faction.");
            return true;
        }
        if (playerFaction.isRaidable()) {
            sender.sendMessage((Object)ChatColor.RED + "You cannot claim land for your faction while raidable.");
            return true;
        }
        if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
            sender.sendMessage((Object)ChatColor.RED + "You must be an officer to claim land.");
            return true;
        }
        Location location = player.getLocation();
        this.plugin.getClaimHandler().tryPurchasing(player, new Claim(playerFaction, location.clone().add(7.0, 0.0, 7.0), location.clone().add(-7.0, 256.0, -7.0)));
        return true;
    }
}