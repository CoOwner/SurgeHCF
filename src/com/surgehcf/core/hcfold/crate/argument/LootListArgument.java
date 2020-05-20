package com.surgehcf.core.hcfold.crate.argument;

import me.milksales.util.command.CommandArgument;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcfold.crate.Key;
import com.surgehcf.core.hcfold.crate.KeyManager;

public class LootListArgument
extends CommandArgument {
    private final SurgeCore plugin;

    public LootListArgument(SurgeCore plugin) {
        super("list", "List all crate key types");
        this.plugin = plugin;
        this.permission = "hcf.command.loot.argument." + this.getName();
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List keyNames = this.plugin.getKeyManager().getKeys().stream().map(Key::getDisplayName).collect(Collectors.toList());
        sender.sendMessage((Object)ChatColor.GRAY + "List of key types: " + StringUtils.join(keyNames, (String)new StringBuilder().append((Object)ChatColor.GRAY).append(", ").toString()));
        return true;
    }
}