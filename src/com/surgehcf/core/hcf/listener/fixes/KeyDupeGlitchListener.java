package com.surgehcf.core.hcf.listener.fixes;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.surgehcf.SurgeCore;

import net.md_5.bungee.api.ChatColor;

public class KeyDupeGlitchListener implements Listener{

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if(!p.getItemInHand().hasItemMeta()){
        	return;
        }
        ItemMeta meta = p.getItemInHand().getItemMeta();
        if (meta.getDisplayName().contains(" Key Reward")){
        	p.sendMessage(ChatColor.RED + "[Warning] " + ChatColor.GRAY + "SurgeHCF has patched this duping glitch. Please go on a different server if you wish to dupe! :)");
            e.setBuild(false);
        }
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onCraft(CraftItemEvent e){
    	ArrayList<Material> items = new ArrayList<Material>();
    	for(ItemStack item : e.getInventory().getContents()){
    		if(item.getType() == Material.MINECART || item.getType() == Material.CHEST || item.getType() == Material.TRAPPED_CHEST || item.getType() == Material.HOPPER){
    			items.add(item.getType());
    		}
    	}
    	
    	if((items.contains(Material.MINECART) && items.contains(Material.CHEST)) || (items.contains(Material.MINECART) && items.contains(Material.TRAPPED_CHEST))  ||  (items.contains(Material.MINECART) && items.contains(Material.HOPPER))){
    		((Player) e.getWhoClicked()).sendMessage(ChatColor.RED + "[Warning] " + ChatColor.GRAY + "SurgeHCF has patched this duping glitch. Please go on a different server if you wish to dupe! :)");
    		e.setCancelled(true);
    	}
    }
}
