package com.surgehcf.core.hcf.faction.argument;

import me.milksales.util.command.CommandArgument;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.FactionMember;
import com.surgehcf.core.hcf.faction.struct.ChatChannel;
import com.surgehcf.core.hcf.faction.struct.Relation;
import com.surgehcf.core.hcf.faction.struct.Role;
import com.surgehcf.core.hcf.faction.type.Faction;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;

public class FactionAcceptArgument
extends CommandArgument {
    private final SurgeCore plugin;

    public FactionAcceptArgument(SurgeCore plugin) {
        super("accept", "Accept a join request from an existing faction.", new String[]{"join", "a"});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " <factionName>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Player player = (Player)sender;
        if (this.plugin.getFactionManager().getPlayerFaction(player) != null) {
            sender.sendMessage((Object)ChatColor.RED + "You are already in a faction.");
            return true;
        }
        Faction faction = this.plugin.getFactionManager().getContainingFaction(args[1]);
        if (faction == null) {
            sender.sendMessage((Object)ChatColor.RED + "Faction named or containing member with IGN or UUID " + args[1] + " not found.");
            return true;
        }
        if (!(faction instanceof PlayerFaction)) {
            sender.sendMessage((Object)ChatColor.RED + "You can only join player factions.");
            return true;
        }
        PlayerFaction targetFaction = (PlayerFaction)faction;
        if (targetFaction.getMembers().size() >= 10) {
            sender.sendMessage(faction.getDisplayName(sender) + (Object)ChatColor.RED + " is full. Faction limits are at " + 10 + '.');
            return true;
        }
        if (!targetFaction.isOpen() && !targetFaction.getInvitedPlayerNames().contains(player.getName())) {
            sender.sendMessage((Object)ChatColor.RED + faction.getDisplayName(sender) + (Object)ChatColor.RED + " has not invited you.");
            return true;
        }
        if (targetFaction.setMember(player, new FactionMember(player, ChatChannel.PUBLIC, Role.MEMBER))) {
            targetFaction.broadcast((Object)Relation.MEMBER.toChatColour() + sender.getName() + (Object)ChatColor.YELLOW + " has joined the faction.");
        }
        return true;
    }
}