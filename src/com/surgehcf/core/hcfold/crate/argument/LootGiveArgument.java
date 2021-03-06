package com.surgehcf.core.hcfold.crate.argument;

import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.List;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.inventory.PlayerInventory;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcfold.crate.Key;

import org.bukkit.inventory.ItemStack;

import net.minecraft.util.com.google.common.primitives.Ints;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.milksales.util.command.CommandArgument;

public class LootGiveArgument extends CommandArgument
{
    private final SurgeCore plugin;
    
    public LootGiveArgument(final SurgeCore plugin) {
        super("give", "Gives a crate key to a player");
        this.plugin = plugin;
        this.aliases = new String[] { "send" };
        this.permission = "hcf.command.loot.argument." + this.getName();
    }
    
    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName() + " <playerName> <type> <amount>";
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        final Player target = Bukkit.getPlayer(args[1]);
        if (target == null || (sender instanceof Player && !((Player)sender).canSee(target))) {
            sender.sendMessage(ChatColor.GOLD + "Player '" + ChatColor.WHITE + args[1] + ChatColor.GOLD + "' not found.");
            return true;
        }
        final Key key = this.plugin.getKeyManager().getKey(args[2]);
        if (key == null) {
            sender.sendMessage(ChatColor.RED + "There is no key type named '" + args[2] + "'.");
            return true;
        }
        Integer quantity;
        if (args.length >= 4) {
            quantity = Ints.tryParse(args[3]);
            if (quantity == null) {
                sender.sendMessage(ChatColor.RED + "'" + args[3] + "' is not a number.");
                return true;
            }
        }
        else {
            quantity = 1;
        }
        if (quantity <= 0) {
            sender.sendMessage(ChatColor.RED + "You can only give keys in positive quantities.");
            return true;
        }
        final ItemStack stack = key.getItemStack().clone();
        final int maxAmount = 16;
        if (quantity > 16) {
            sender.sendMessage(ChatColor.RED + "You cannot give keys in quantities more than " + 16 + '.');
            return true;
        }
        stack.setAmount((int)quantity);
        final PlayerInventory inventory = target.getInventory();
        final Location location = target.getLocation();
        final World world = target.getWorld();
        final Map<Integer, ItemStack> excess = (Map<Integer, ItemStack>)inventory.addItem(new ItemStack[] { stack });
        for (final ItemStack entry : excess.values()) {
            world.dropItemNaturally(location, entry);
        }
        sender.sendMessage(ChatColor.GREEN + "Given " + quantity + "x " + key.getDisplayName() + ChatColor.GREEN + " key to " + target.getName() + '.');
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            return null;
        }
        if (args.length == 3) {
            return this.plugin.getKeyManager().getKeys().stream().map(Key::getName).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}