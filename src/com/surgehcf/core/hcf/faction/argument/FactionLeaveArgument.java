package com.surgehcf.core.hcf.faction.argument;

import me.milksales.util.command.CommandArgument;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.FactionMember;
import com.surgehcf.core.hcf.faction.struct.Relation;
import com.surgehcf.core.hcf.faction.struct.Role;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;

public class FactionLeaveArgument
extends CommandArgument {
    private final SurgeCore plugin;

    public FactionLeaveArgument(SurgeCore plugin) {
        super("leave", "Leave your current faction.");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "Only players can leave faction.");
            return true;
        }
        Player player = (Player)sender;
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            sender.sendMessage((Object)ChatColor.RED + "You are not in a faction.");
            return true;
        }
        UUID uuid = player.getUniqueId();
        if (playerFaction.getMember(uuid).getRole() == Role.LEADER) {
            sender.sendMessage((Object)ChatColor.RED + "You cannot leave factions as a leader. Either use " + (Object)ChatColor.GOLD + '/' + label + " disband" + (Object)ChatColor.RED + " or " + (Object)ChatColor.GOLD + '/' + label + " leader" + (Object)ChatColor.RED + '.');
            return true;
        }
        if (playerFaction.setMember(player, null)) {
            sender.sendMessage((Object)ChatColor.YELLOW + "Successfully left the faction.");
            playerFaction.broadcast((Object)Relation.ENEMY.toChatColour() + sender.getName() + (Object)ChatColor.YELLOW + " has left the faction.");
        }
        return true;
    }
}