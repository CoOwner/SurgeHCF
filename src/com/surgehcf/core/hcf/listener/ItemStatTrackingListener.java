package com.surgehcf.core.hcf.listener;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStatTrackingListener
  implements Listener
{
  @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
  public void onPlayerDeath(PlayerDeathEvent event)
  {
    Player player = event.getEntity();
    Player killer = player.getKiller();
    if (killer != null)
    {
      ItemStack stack = killer.getItemInHand();
      if ((stack != null) && (EnchantmentTarget.WEAPON.includes(stack))) {
        addDeathLore(stack, player, killer);
      }
    }
  }
  
  private void addDeathLore(ItemStack stack, Player player, Player killer)
  {
    ItemMeta meta = stack.getItemMeta();
    List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList(2);
    if ((lore.isEmpty()) || (!((String)lore.get(0)).startsWith(ChatColor.GOLD + ChatColor.BOLD.toString() + "Kills ")))
    {
      lore.add(0, ChatColor.GOLD + ChatColor.BOLD.toString() + "Kills " + ChatColor.RED + 1);
    }
    else
    {
      String killsString = ((String)lore.get(0)).replace(ChatColor.GOLD + ChatColor.BOLD.toString() + "Kills ", "").replace(ChatColor.YELLOW + "]", "");
      Integer kills = Integer.valueOf(1);
      try
      {
        Integer.parseInt(killsString);
        kills = Integer.valueOf(Integer.parseInt(killsString));
      }
      catch (NumberFormatException e)
      {
        e.printStackTrace();
      }
      Integer killafteradd = Integer.valueOf(kills.intValue() + 1);
      lore.set(0, ChatColor.GOLD + ChatColor.BOLD.toString() + "Kills " + ChatColor.RED + killafteradd);
    }
    meta.setLore(lore.subList(0, Math.min(6, lore.size())));
    stack.setItemMeta(meta);
  }
}