package com.surgehcf.core.hcfold;

import me.milksales.util.ItemBuilder;
import me.milksales.util.chat.Lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.util.org.apache.commons.lang3.text.WordUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.CoreConfiguration;

public class MapKitCommand
  implements CommandExecutor, TabCompleter, Listener
{
  private final Set<Inventory> tracking = new HashSet();
  
  public MapKitCommand(SurgeCore plugin)
  {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
	  sender.sendMessage("§eMap Kit §6» §rProtection " + CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)+ ", Sharpness " + CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.DAMAGE_ALL)+ ", Power "  + CoreConfiguration.ENCHANTMENT_LIMITS.get(Enchantment.ARROW_DAMAGE) + ".");
    return true;
  }
  
  public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
  {
    return Collections.emptyList();
  }

}
