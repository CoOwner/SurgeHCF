package com.surgehcf.core.hcfold.crate.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.surgehcf.core.hcf.CoreConfiguration;

import me.milksales.util.command.CommandArgument;

public class LootBroadcastsArgument extends CommandArgument
{
    public LootBroadcastsArgument() {
        super("broadcasts", "Toggle broadcasts for key announcements", new String[] { "togglealerts", "togglebroadcasts" });
        this.permission = "hcf.command.loot.argument." + this.getName();
    }
    
    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName();
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final boolean newBroadcasts = CoreConfiguration.CRATE_BROADCASTS = !CoreConfiguration.CRATE_BROADCASTS;
        sender.sendMessage(ChatColor.GOLD + "Crate keys " + (newBroadcasts ? "now" : "no longer") + " broadcasts reward messages.");
        return true;
    }
}