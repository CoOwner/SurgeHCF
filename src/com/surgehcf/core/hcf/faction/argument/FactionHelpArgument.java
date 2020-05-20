package com.surgehcf.core.hcf.faction.argument;

import me.milksales.util.BukkitUtils;
import me.milksales.util.command.CommandArgument;

import java.util.Collection;
import java.util.List;

import net.minecraft.util.com.google.common.collect.ArrayListMultimap;
import net.minecraft.util.com.google.common.collect.ImmutableCollection;
import net.minecraft.util.com.google.common.collect.ImmutableMultimap;
import net.minecraft.util.com.google.common.collect.Multimap;
import net.minecraft.util.com.google.common.primitives.Ints;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.surgehcf.core.hcf.faction.FactionExecutor;

public class FactionHelpArgument
extends CommandArgument {
    private static final int HELP_PER_PAGE = 10;
    private final FactionExecutor executor;
    private ImmutableMultimap<Integer, String> pages;

    public FactionHelpArgument(FactionExecutor executor) {
        super("help", "View help on how to use factions.");
        this.executor = executor;
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            this.showPage(sender, label, 1);
            return true;
        }
        Integer page = Ints.tryParse((String)args[1]);
        if (page == null) {
            sender.sendMessage((Object)ChatColor.RED + "'" + args[1] + "' is not a valid number.");
            return true;
        }
        this.showPage(sender, label, page);
        return true;
    }

    private void showPage(CommandSender sender, String label, int pageNumber) {
        if (this.pages == null) {
            boolean isPlayer = sender instanceof Player;
            int val = 1;
            int count = 0;
            ArrayListMultimap pages = ArrayListMultimap.create();
            for (CommandArgument argument : this.executor.getArguments()) {
                String permission;
                if (argument.equals((Object)this) || (permission = argument.getPermission()) != null && !sender.hasPermission(permission) || argument.isPlayerOnly() && !isPlayer) continue;
                pages.get((Object)val).add((Object)ChatColor.YELLOW + "/" + label + ' ' + argument.getName() + (Object)ChatColor.GRAY + " §6» §r" + argument.getDescription());
                if (++count % 10 != 0) continue;
                ++val;
            }
            this.pages = ImmutableMultimap.copyOf((Multimap)pages);
        }
        int totalPageCount = this.pages.size() / 10 + 1;
        if (pageNumber < 1) {
            sender.sendMessage((Object)ChatColor.RED + "You cannot view a page less than 1.");
            return;
        }
        if (pageNumber > totalPageCount) {
            sender.sendMessage((Object)ChatColor.RED + "There are only " + totalPageCount + " pages.");
            return;
        }
        sender.sendMessage((Object)ChatColor.GOLD + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage("§eSURGEHCF §6» §rFaction Help §d(Page " + pageNumber + '/' + totalPageCount + ')');
        sender.sendMessage((Object)ChatColor.GOLD + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        for (String message : this.pages.get(pageNumber)) {
            sender.sendMessage("  " + message);
        }
        sender.sendMessage((Object)ChatColor.LIGHT_PURPLE + " §rTo view other pages, use " + (Object)ChatColor.YELLOW + '/' + label + ' ' + this.getName() + " <page#>" + (Object)ChatColor.GOLD + '.');
        sender.sendMessage((Object)ChatColor.GOLD + BukkitUtils.STRAIGHT_LINE_DEFAULT);
    }
}