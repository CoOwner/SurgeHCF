package com.surgehcf.core.hcfold.crate.argument;

import me.milksales.util.command.CommandArgument;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.util.com.google.common.primitives.Ints;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcfold.crate.Key;
import com.surgehcf.core.hcfold.crate.KeyManager;

public class LootWithdrawArgument
extends CommandArgument {
    private final SurgeCore plugin;

    public LootWithdrawArgument(SurgeCore plugin) {
        super("withdraw", "Withdraws keys from your bank account");
        this.plugin = plugin;
        this.aliases = new String[]{"retrieve"};
        this.permission = "hcf.command.loot.argument." + this.getName();
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " <keyName> <amount>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        if (args.length < 3) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Key key = this.plugin.getKeyManager().getKey(args[1]);
        if (key == null) {
            sender.sendMessage((Object)ChatColor.RED + "There is no key type named '" + args[1] + "'.");
            return true;
        }
        Integer quantity = Ints.tryParse((String)args[2]);
        if (quantity == null) {
            sender.sendMessage((Object)ChatColor.RED + "'" + args[3] + "' is not a number.");
            return true;
        }
        if (quantity <= 0) {
            sender.sendMessage((Object)ChatColor.RED + "You can only withdraw crate keys in positive amounts.");
            return true;
        }
        Player player = (Player)sender;
        UUID uuid = player.getUniqueId();
        Map<String, Integer> crateKeyMap = this.plugin.getKeyManager().getDepositedCrateMap(uuid);
        String keyName = key.getName();
        int keyBalance = crateKeyMap.getOrDefault(keyName, 0);
        if (quantity > keyBalance) {
            sender.sendMessage((Object)ChatColor.RED + "You tried to withdraw " + quantity + ' ' + keyName + " keys, but you only have " + keyBalance + " in your bank account.");
            return true;
        }
        int newBalance = keyBalance - quantity;
        crateKeyMap.put(keyName, newBalance);
        ItemStack stack = key.getItemStack();
        stack.setAmount(quantity.intValue());
        Location location = player.getLocation();
        World world = player.getWorld();
        for (Map.Entry entry : player.getInventory().addItem(new ItemStack[]{stack}).entrySet()) {
            world.dropItemNaturally(location, (ItemStack)entry.getValue());
        }
        sender.sendMessage((Object)ChatColor.YELLOW + "Successfully withdraw " + quantity + ' ' + keyName + " keys from bank account. You now " + newBalance + " of these keys.");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return Collections.emptyList();
        }
        return this.plugin.getKeyManager().getKeys().stream().map(Key::getName).collect(Collectors.toList());
    }
}