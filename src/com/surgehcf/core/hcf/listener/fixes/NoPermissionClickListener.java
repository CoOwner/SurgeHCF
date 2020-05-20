package com.surgehcf.core.hcf.listener.fixes;

import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.surgehcf.core.hcf.CoreConfiguration;

public class NoPermissionClickListener
  implements Listener
{
  @EventHandler
  public void onClick(PlayerInteractEvent e)
  {
    for (Enchantment enchantment : e.getItem().getEnchantments().keySet()) {
      if ((CoreConfiguration.ENCHANTMENT_LIMITS.containsKey(enchantment)) && (((Integer)e.getItem().getEnchantments().get(enchantment)).intValue() > ((Integer)CoreConfiguration.ENCHANTMENT_LIMITS.get(enchantment)).intValue()))
      {
        e.getItem().removeEnchantment(enchantment);
        e.getItem().addEnchantment(enchantment, ((Integer)CoreConfiguration.ENCHANTMENT_LIMITS.get(enchantment)).intValue());
      }
    }
  }
  
}
