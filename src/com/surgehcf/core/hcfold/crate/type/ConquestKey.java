package com.surgehcf.core.hcfold.crate.type;

import me.milksales.util.ItemBuilder;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.surgehcf.core.hcf.CoreConfiguration;
import com.surgehcf.core.hcfold.crate.EnderChestKey;

public class ConquestKey
  extends EnderChestKey
{
  public ConquestKey()
  {
    super("Conquest", 7);
    setupRarity(new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.FIRE_ASPECT, 2).enchant(Enchantment.DAMAGE_ALL, ((Integer)CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.DAMAGE_ALL)).intValue() + 1).displayName(ChatColor.YELLOW + "Conquest Fire").build(), 3);
    setupRarity(new ItemStack(Material.DIAMOND_BLOCK, 64), 12);
    setupRarity(new ItemStack(Material.GOLD_BLOCK, 64), 15);
    setupRarity(new ItemStack(Material.IRON_BLOCK, 64), 15);
    setupRarity(new ItemBuilder(Material.GOLD_HELMET).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, ((Integer)CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).intValue() + 1).displayName(ChatColor.RED + "Bard Helmet").build(), 1);
    setupRarity(new ItemBuilder(Material.GOLD_CHESTPLATE).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, ((Integer)CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).intValue() + 1).displayName(ChatColor.RED + "Bard Chestplate").build(), 1);
    setupRarity(new ItemBuilder(Material.GOLD_LEGGINGS).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, ((Integer)CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).intValue() + 1).displayName(ChatColor.RED + "Bard Leggings").build(), 1);
    setupRarity(new ItemBuilder(Material.GOLD_BOOTS).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, ((Integer)CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).intValue() + 1).displayName(ChatColor.RED + "Bard Boots").build(), 1);
    setupRarity(new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.LOOT_BONUS_MOBS, 5).enchant(Enchantment.DAMAGE_ALL, ((Integer)CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.DAMAGE_ALL)).intValue() + 1).displayName(ChatColor.RED + ChatColor.ITALIC.toString() + "Conquest Looting").build(), 7);
    setupRarity(new ItemBuilder(Material.DIAMOND_PICKAXE).enchant(Enchantment.LOOT_BONUS_BLOCKS, 5).displayName(ChatColor.YELLOW + "CONQUEST Fortune").build(), 5);
    setupRarity(new ItemStack(Material.BEACON, 2), 2);
    setupRarity(new ItemBuilder(Material.GOLDEN_APPLE, 2).data((short)1).build(), 1);
    setupRarity(new ItemBuilder(Material.BOW).enchant(Enchantment.ARROW_DAMAGE, ((Integer)CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.ARROW_DAMAGE)).intValue()).enchant(Enchantment.ARROW_FIRE, 1).enchant(Enchantment.ARROW_INFINITE, 1).displayName(ChatColor.RED + ChatColor.ITALIC.toString() + "Conquest Bow").build(), 3);
    setupRarity(new ItemBuilder(Material.DIAMOND_HELMET).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, ((Integer)CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).intValue() + 1).displayName(ChatColor.RED + ChatColor.ITALIC.toString() + "Conquest Helmet").build(), 1);
    setupRarity(new ItemBuilder(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, ((Integer)CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).intValue() + 1).displayName(ChatColor.RED + ChatColor.ITALIC.toString() + "Conquest Chestplate").build(), 1);
    setupRarity(new ItemBuilder(Material.DIAMOND_LEGGINGS).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, ((Integer)CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).intValue() + 1).displayName(ChatColor.RED + ChatColor.ITALIC.toString() + "Conquest Leggings").build(), 1);
    setupRarity(new ItemBuilder(Material.DIAMOND_BOOTS).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, ((Integer)CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)).intValue() + 1).displayName(ChatColor.RED + ChatColor.ITALIC.toString() + "Conquest Boots").build(), 1);
    setupRarity(new ItemStack(Material.SULPHUR, 128), 7);
    setupRarity(new ItemStack(Material.SPECKLED_MELON, 128), 5);
    setupRarity(new ItemStack(Material.GLOWSTONE, 128), 5);
  }
  
  public ChatColor getColour()
  {
    return ChatColor.DARK_PURPLE;
  }
  
  public boolean getBroadcastItems()
  {
    return true;
  }
}
