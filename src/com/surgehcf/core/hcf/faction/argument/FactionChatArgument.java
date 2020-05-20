package com.surgehcf.core.hcf.faction.argument;

import me.milksales.util.command.CommandArgument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.FactionMember;
import com.surgehcf.core.hcf.faction.struct.ChatChannel;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;

public class FactionChatArgument
extends CommandArgument {
    private final SurgeCore plugin;

    public FactionChatArgument(SurgeCore plugin) {
        super("chat", "Toggle faction chat only mode on or off.", new String[]{"c"});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " [fac|public|ally] [message]";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ChatChannel parsed;
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
        FactionMember member = playerFaction.getMember(player.getUniqueId());
        ChatChannel currentChannel = member.getChatChannel();
        ChatChannel chatChannel = parsed = args.length >= 2 ? ChatChannel.parse(args[1], null) : currentChannel.getRotation();
        if (parsed == null && currentChannel != ChatChannel.PUBLIC) {
            Set<Player> recipients = playerFaction.getOnlinePlayers();
            if (currentChannel == ChatChannel.ALLIANCE) {
                for (PlayerFaction ally : playerFaction.getAlliedFactions()) {
                    recipients.addAll(ally.getOnlinePlayers());
                }
            }
            String format = String.format(currentChannel.getRawFormat(player), "", StringUtils.join((Object[])args, (char)' ', (int)1, (int)args.length));
            for (Player recipient : recipients) {
                recipient.sendMessage(format);
            }
            return true;
        }
        ChatChannel newChannel = parsed == null ? currentChannel.getRotation() : parsed;
        member.setChatChannel(newChannel);
        sender.sendMessage((Object)ChatColor.YELLOW + "You are now in " + (Object)ChatColor.AQUA + newChannel.getDisplayName().toLowerCase() + (Object)ChatColor.YELLOW + " chat mode.");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2 || !(sender instanceof Player)) {
            return Collections.emptyList();
        }
        ChatChannel[] values = ChatChannel.values();
        ArrayList<String> results = new ArrayList<String>(values.length);
        for (ChatChannel type : values) {
            results.add(type.getName());
        }
        return results;
    }
}