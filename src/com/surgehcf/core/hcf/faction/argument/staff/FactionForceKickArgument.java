package com.surgehcf.core.hcf.faction.argument.staff;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.FactionMember;
import com.surgehcf.core.hcf.faction.struct.Role;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;

import me.milksales.util.command.CommandArgument;

public class FactionForceKickArgument extends CommandArgument
{
    private final SurgeCore plugin;
    
    public FactionForceKickArgument(final SurgeCore plugin) {
        super("forcekick", "Forcefully kick a player from their faction.");
        this.plugin = plugin;
        this.permission = "hcf.command.faction.argument." + this.getName();
    }
    
    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName() + " <playerName>";
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        final PlayerFaction playerFaction = this.plugin.getFactionManager().getContainingPlayerFaction(args[1]);
        if (playerFaction == null) {
            sender.sendMessage(ChatColor.RED + "Faction containing member with IGN or UUID " + args[1] + " not found.");
            return true;
        }
        final FactionMember factionMember = playerFaction.getMember(args[1]);
        if (factionMember == null) {
            sender.sendMessage(ChatColor.RED + "Faction containing member with IGN or UUID " + args[1] + " not found.");
            return true;
        }
        if (factionMember.getRole() == Role.LEADER) {
            sender.sendMessage(ChatColor.RED + "You cannot forcefully kick faction leaders.");
            return true;
        }
        if (playerFaction.setMember(factionMember.getUniqueId(), null, true)) {
            playerFaction.broadcast(ChatColor.GOLD.toString() + ChatColor.BOLD + factionMember.getName() + " has been forcefully kicked by " + sender.getName() + '.');
        }
        return true;
    }
    
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 2) ? null : Collections.emptyList();
    }
}