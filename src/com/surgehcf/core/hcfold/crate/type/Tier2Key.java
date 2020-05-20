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

public class Tier2Key
  extends EnderChestKey
{
  public Tier2Key()
  {
    super("Tier2", 3);
    setupRarity(new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.LOOT_BONUS_MOBS, 3).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.DAMAGE_ALL, ((Integer)CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.DAMAGE_ALL)).intValue() + 1).build(), 25);
    setupRarity(new ItemStack(Material.DIAMOND_BLOCK, 16), 15);
    setupRarity(new ItemStack(Material.GOLD_BLOCK, 16), 15);
    setupRarity(new ItemStack(Material.EMERALD_BLOCK, 6), 15);
    setupRarity(new ItemStack(Material.ENDER_PEARL, 16), 20);
    setupRarity(new ItemStack(Material.ENDER_PEARL, 16), 20);
    setupRarity(new ItemStack(Material.ENDER_PEARL, 16), 20);
    setupRarity(new ItemStack(Material.GLOWSTONE, 32), 25);
    setupRarity(new ItemBuilder(Material.DIAMOND_PICKAXE).enchant(Enchantment.LOOT_BONUS_BLOCKS, 3).enchant(Enchantment.DIG_SPEED, 5).enchant(Enchantment.DURABILITY, 3).build(), 25);
    setupRarity(new ItemStack(Material.SULPHUR, 32), 35);
    setupRarity(new ItemBuilder(Material.SKULL_ITEM, 2).data((short)1).build(), 35);
    setupRarity(new ItemBuilder(Material.GOLDEN_APPLE, 16).build(), 35);
    setupRarity(new ItemBuilder(Material.DIAMOND_HELMET).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, ((Integer)CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).intValue() + 1).build(), 28);
    setupRarity(new ItemBuilder(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, ((Integer)CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).intValue() + 1).build(), 28);
    setupRarity(new ItemBuilder(Material.DIAMOND_LEGGINGS).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, ((Integer)CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).intValue() + 1).build(), 28);
    setupRarity(new ItemBuilder(Material.DIAMOND_BOOTS).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, ((Integer)CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).intValue() + 1).enchant(Enchantment.PROTECTION_FALL, 4).build(), 28);
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
