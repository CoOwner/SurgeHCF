
package com.surgehcf.core.hcf.faction.argument;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import me.milksales.util.JavaUtils;
import net.minecraft.util.com.google.common.primitives.Ints;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.google.common.collect.ImmutableList;
import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.CoreConfiguration;
import com.surgehcf.core.hcf.faction.FactionMember;
import com.surgehcf.core.hcf.faction.argument.FactionWithdrawArgument;
import com.surgehcf.core.hcf.faction.struct.Role;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;

import me.milksales.util.command.CommandArgument;

public class FactionWithdrawArgument extends CommandArgument
{
    private static final ImmutableList<String> COMPLETIONS;
    private final SurgeCore plugin;
    
    public FactionWithdrawArgument(final SurgeCore plugin) {
        super("withdraw", "Withdraws money from the faction balance.", new String[] { "w" });
        this.plugin = plugin;
    }
    
    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName() + " <all|amount>";
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can update the faction balance.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        final Player player = (Player)sender;
        final PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            sender.sendMessage(ChatColor.RED + "You are not in a faction.");
            return true;
        }
        final UUID uuid = player.getUniqueId();
        final FactionMember factionMember = playerFaction.getMember(uuid);
        if (factionMember.getRole() == Role.MEMBER) {
            sender.sendMessage(ChatColor.RED + "You must be a faction officer to withdraw money.");
            return true;
        }
        final int factionBalance = playerFaction.getBalance();
        Integer amount;
        if (args[1].equalsIgnoreCase("all")) {
            amount = factionBalance;
        }
        else if ((amount = Ints.tryParse(args[1])) == null) {
            sender.sendMessage(ChatColor.RED + "Error: '" + args[1] + "' is not a valid number.");
            return true;
        }
        if (amount <= 0) {
            sender.sendMessage(ChatColor.RED + "Amount must be positive.");
            return true;
        }
        if (amount > factionBalance) {
            sender.sendMessage(ChatColor.RED + "Your faction need at least " + '$' + JavaUtils.format((Number)amount) + " to do this, whilst it only has " + '$' + JavaUtils.format((Number)factionBalance) + '.');
            return true;
        }
        this.plugin.getEconomyManager().addBalance(uuid, amount);
        playerFaction.setBalance(factionBalance - amount);
        playerFaction.broadcast(CoreConfiguration.TEAMMATE_COLOUR + factionMember.getRole().getAstrix() + sender.getName() + ChatColor.YELLOW + " has withdrew " + ChatColor.BOLD + '$' + JavaUtils.format((Number)amount) + ChatColor.YELLOW + " from the faction balance.");
        return true;
    }
    
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (List<String>)((args.length == 2) ? FactionWithdrawArgument.COMPLETIONS : Collections.emptyList());
    }
    
    static {
        COMPLETIONS = ImmutableList.of("all");
    }
}