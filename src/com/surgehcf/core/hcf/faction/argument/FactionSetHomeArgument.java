package com.surgehcf.core.hcf.faction.argument;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.CoreConfiguration;
import com.surgehcf.core.hcf.faction.FactionMember;
import com.surgehcf.core.hcf.faction.claim.Claim;
import com.surgehcf.core.hcf.faction.struct.Role;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.milksales.util.command.CommandArgument;

public class FactionSetHomeArgument extends CommandArgument
{
    private final SurgeCore plugin;
    
    public FactionSetHomeArgument(final SurgeCore plugin) {
        super("sethome", "Sets the faction home location.");
        this.plugin = plugin;
    }
    
    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName();
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        final Player player = (Player)sender;
        final PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            sender.sendMessage(ChatColor.RED + "You are not in a faction.");
            return true;
        }
        final FactionMember factionMember = playerFaction.getMember(player);
        if (factionMember.getRole() == Role.MEMBER) {
            sender.sendMessage(ChatColor.RED + "You must be a faction officer to set the home.");
            return true;
        }
        final Location location = player.getLocation();
        boolean insideTerritory = false;
        for (final Claim claim : playerFaction.getClaims()) {
            if (claim.contains(location)) {
                insideTerritory = true;
                break;
            }
        }
        if (!insideTerritory) {
            player.sendMessage(ChatColor.RED + "You may only set your home in your territory.");
            return true;
        }
        playerFaction.setHome(location);
        playerFaction.broadcast(CoreConfiguration.TEAMMATE_COLOUR + factionMember.getRole().getAstrix() + sender.getName() + ChatColor.YELLOW + " has set the faction home.");
        return true;
    }
}