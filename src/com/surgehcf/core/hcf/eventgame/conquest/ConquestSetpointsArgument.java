package com.surgehcf.core.hcf.eventgame.conquest;

import net.minecraft.util.com.google.common.primitives.Ints;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.eventgame.EventType;
import com.surgehcf.core.hcf.eventgame.tracker.ConquestTracker;
import com.surgehcf.core.hcf.faction.type.Faction;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;

import me.milksales.util.command.CommandArgument;

public class ConquestSetpointsArgument extends CommandArgument
{
    private final SurgeCore plugin;
    
    public ConquestSetpointsArgument(final SurgeCore plugin) {
        super("setpoints", "Sets the points of a faction in the Conquest event", "hcf.command.conquest.argument.setpoints");
        this.plugin = plugin;
    }
    
    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName() + " <factionName> <amount>";
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        final Faction faction = this.plugin.getFactionManager().getFaction(args[1]);
        if (!(faction instanceof PlayerFaction)) {
            sender.sendMessage(ChatColor.RED + "Faction " + args[1] + " is either not found or is not a player faction.");
            return true;
        }
        final Integer amount = Ints.tryParse(args[2]);
        if (amount == null) {
            sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a number.");
            return true;
        }
        if (amount > 200) {
            sender.sendMessage(ChatColor.RED + "Maximum points for Conquest is " + 200 + '.');
            return true;
        }
        final PlayerFaction playerFaction = (PlayerFaction)faction;
        ((ConquestTracker)EventType.CONQUEST.getEventTracker()).setPoints(playerFaction, amount);
        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Set the points of faction " + playerFaction.getName() + " to " + amount + '.');
        return true;
    }
}