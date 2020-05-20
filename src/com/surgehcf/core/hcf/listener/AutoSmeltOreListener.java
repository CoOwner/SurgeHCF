package com.surgehcf.core.hcf.listener;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AutoSmeltOreListener
implements Listener
{
	private String AUTO_SMELT_ORE_PERMISSION = "surge.autosmelt";

	@EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		if ((player.getGameMode() != GameMode.CREATIVE) && (player.hasPermission(this.AUTO_SMELT_ORE_PERMISSION)))
		{
			ItemStack stack = player.getItemInHand();
			if ((stack != null) && (stack.getType() != Material.AIR) && (!stack.containsEnchantment(Enchantment.SILK_TOUCH)))
			{
				Block block = event.getBlock();
				Material dropType = null;
				switch (block.getType())
				{
				case IRON_ORE: 
					dropType = Material.IRON_INGOT;
					break;
				case GOLD_ORE: 
					dropType = Material.GOLD_INGOT;
					break;
				default: 
					return;
				}
				Location location = block.getLocation();
				World world = location.getWorld();
				ItemStack drop = new ItemStack(dropType, 2);
				world.dropItemNaturally(location, drop);
				block.setType(Material.AIR);
				block.getState().update();
			}
		}

	}
}
