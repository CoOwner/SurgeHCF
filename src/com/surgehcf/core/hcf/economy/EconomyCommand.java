package com.surgehcf.core.hcf.economy;

import me.milksales.base.BaseConstants;
import me.milksales.util.BukkitUtils;
import me.milksales.util.JavaUtils;

import java.util.UUID;

import net.minecraft.util.com.google.common.collect.ImmutableList;
import net.minecraft.util.com.google.common.primitives.Ints;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.economy.EconomyManager;

public class EconomyCommand
implements CommandExecutor {
    private static final int MAX_ENTRIES = 10;
    private static final ImmutableList<String> COMPLETIONS_SECOND = ImmutableList.of("add", "set", "take");
    private final SurgeCore plugin;

    public EconomyCommand(SurgeCore plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        OfflinePlayer target;
        boolean hasStaffPermission = sender.hasPermission(command.getPermission() + ".staff");
        if (args.length > 0 && hasStaffPermission) {
            target = BukkitUtils.offlinePlayerWithNameOrUUID((String)args[0]);
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <playerName>");
                return true;
            }
            target = (OfflinePlayer)sender;
        }
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }
        UUID uuid = target.getUniqueId();
        int balance = this.plugin.getEconomyManager().getBalance(uuid);
        if (args.length < 2 || !hasStaffPermission) {
            sender.sendMessage(ChatColor.YELLOW + (sender.equals(target) ? "Your balance" : new StringBuilder().append("Balance of ").append(target.getName()).toString()) + " is " + ChatColor.GREEN + '$' + balance + ChatColor.GOLD + '.');
            return true;
        }
        if (args[1].equalsIgnoreCase("give") || args[1].equalsIgnoreCase("add")) {
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /" + label + ' ' + target.getName() + ' ' + args[1] + " <amount>");
                return true;
            }
            Integer amount = Ints.tryParse((String)args[2]);
            if (amount == null) {
                sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a valid number.");
                return true;
            }
            int newBalance = this.plugin.getEconomyManager().addBalance(uuid, amount);
            sender.sendMessage(new String[]{ChatColor.YELLOW + "Added " + '$' + JavaUtils.format((Number)amount) + " to balance of " + target.getName() + '.', ChatColor.YELLOW + "Balance of " + target.getName() + " is now " + '$' + newBalance + '.'});
            return true;
        }
        if (args[1].equalsIgnoreCase("take") || args[1].equalsIgnoreCase("negate") || args[1].equalsIgnoreCase("minus") || args[1].equalsIgnoreCase("subtract")) {
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /" + label + ' ' + target.getName() + ' ' + args[1] + " <amount>");
                return true;
            }
            Integer amount = Ints.tryParse((String)args[2]);
            if (amount == null) {
                sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a valid number.");
                return true;
            }
            int newBalance = this.plugin.getEconomyManager().subtractBalance(uuid, amount);
            sender.sendMessage(new String[]{ChatColor.YELLOW + "Taken " + '$' + JavaUtils.format((Number)amount) + " from balance of " + target.getName() + '.', ChatColor.YELLOW + "Balance of " + target.getName() + " is now " + '$' + newBalance + '.'});
            return true;
        }
        if (!args[1].equalsIgnoreCase("set")) {
            sender.sendMessage(ChatColor.GOLD + (sender.equals(target) ? "Your balance" : new StringBuilder().append("Balance of ").append(target.getName()).toString()) + " is " + ChatColor.WHITE + '$' + balance + ChatColor.GOLD + '.');
            return true;
        }
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + ' ' + target.getName() + ' ' + args[1] + " <amount>");
            return true;
        }
        Integer amount = Ints.tryParse((String)args[2]);
        if (amount == null) {
            sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a valid number.");
            return true;
        }
        int newBalance = this.plugin.getEconomyManager().setBalance(uuid, amount);
        sender.sendMessage(ChatColor.YELLOW + "Set balance of " + target.getName() + " to " + '$' + JavaUtils.format((Number)newBalance) + '.');
        return true;
    }
}