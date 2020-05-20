package com.surgehcf.core.hcfold.crate.type;

import me.milksales.util.ItemBuilder;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.surgehcf.core.hcf.CoreConfiguration;
import com.surgehcf.core.hcf.api.Crowbar;
import com.surgehcf.core.hcfold.crate.EnderChestKey;

public class Tier1Key
  extends EnderChestKey
{
  public Tier1Key()
  {
    super("Tier1", 3);
    setupRarity(new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 1).enchant(Enchantment.LOOT_BONUS_MOBS, 3).build(), 25);
    setupRarity(new ItemStack(Material.DIAMOND_BLOCK, 8), 25);
    setupRarity(new ItemStack(Material.GOLD_BLOCK, 8), 25);
    setupRarity(new ItemStack(Material.EMERALD_BLOCK, 4), 25);
    setupRarity(new ItemStack(Material.ENDER_PEARL, 16), 25);
    setupRarity(new ItemStack(Material.ENDER_PEARL, 16), 25);
    setupRarity(new ItemStack(Material.GLOWSTONE, 16), 28);
    setupRarity(new ItemBuilder(Material.DIAMOND_PICKAXE).enchant(Enchantment.DIG_SPEED, 5).enchant(Enchantment.DURABILITY, 3).build(), 25);
    setupRarity(new ItemStack(Material.SULPHUR, 16), 35);
    setupRarity(new ItemBuilder(Material.SKULL_ITEM, 2).data((short)1).build(), 35);
    setupRarity(new ItemBuilder(Material.GOLDEN_APPLE, 16).build(), 29);
    setupRarity(new ItemBuilder(Material.DIAMOND_HELMET).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, ((Integer)CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).intValue()).build(), 27);
    setupRarity(new ItemBuilder(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, ((Integer)CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).intValue()).build(), 28);
    setupRarity(new ItemBuilder(Material.DIAMOND_LEGGINGS).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, ((Integer)CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).intValue()).build(), 29);
    setupRarity(new ItemBuilder(Material.DIAMOND_BOOTS).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, ((Integer)CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).intValue()).build(), 30);
  }
  
  public ChatColor getColour()
  {
    return ChatColor.YELLOW;
  }
  
  public boolean getBroadcastItems()
  {
    return false;
  }
}
