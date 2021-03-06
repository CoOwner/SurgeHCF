package com.surgehcf.core.hcf.faction.argument;

import me.milksales.util.command.CommandArgument;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.claim.ClaimHandler;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;

public class FactionClaimArgument
extends CommandArgument {
    private final SurgeCore plugin;

    public FactionClaimArgument(SurgeCore plugin) {
        super("claim", "Claim land in the Wilderness.", new String[]{"claimland"});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        Player player = (Player)sender;
        UUID uuid = player.getUniqueId();
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(uuid);
        if (playerFaction == null) {
            sender.sendMessage((Object)ChatColor.RED + "You are not in a faction.");
            return true;
        }
        if (playerFaction.isRaidable()) {
            sender.sendMessage((Object)ChatColor.RED + "You cannot claim land for your faction while raidable.");
            return true;
        }
        PlayerInventory inventory = player.getInventory();
        if (inventory.contains(ClaimHandler.CLAIM_WAND)) {
            sender.sendMessage((Object)ChatColor.RED + "You already have a claiming wand in your inventory.");
            return true;
        }
        if (!inventory.addItem(new ItemStack[]{ClaimHandler.CLAIM_WAND}).isEmpty()) {
            sender.sendMessage((Object)ChatColor.RED + "Your inventory is full.");
            return true;
        }
        sender.sendMessage((Object)ChatColor.YELLOW + "Claiming wand added to inventory, read the item to understand how to claim. You can also" + (Object)ChatColor.YELLOW + " use " + (Object)ChatColor.AQUA + '/' + label + " claimchunk" + (Object)ChatColor.YELLOW + '.');
        return true;
    }
}