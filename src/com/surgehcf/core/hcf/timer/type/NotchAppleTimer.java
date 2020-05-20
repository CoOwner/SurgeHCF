 package com.surgehcf.core.hcf.timer.type;
 
  import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
 import org.bukkit.entity.Player;
 import org.bukkit.event.EventPriority;
 import org.bukkit.event.player.PlayerItemConsumeEvent;
 import org.bukkit.inventory.ItemStack;
 import org.bukkit.plugin.java.JavaPlugin;

import com.surgehcf.core.hcf.timer.PlayerTimer;
 
 public class NotchAppleTimer extends PlayerTimer implements org.bukkit.event.Listener
 {
   public NotchAppleTimer(JavaPlugin plugin)
   {
     super("Gapple", TimeUnit.HOURS.toMillis(6L));
   }
   
   public String getScoreboardPrefix() {
     return ChatColor.GOLD.toString();
   }
   
   @org.bukkit.event.EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
   public void onPlayerConsume(PlayerItemConsumeEvent event) {
     ItemStack stack = event.getItem();
     if ((stack != null) && (stack.getType() == org.bukkit.Material.GOLDEN_APPLE) && (stack.getDurability() == 1)) {
       Player player = event.getPlayer();
       if (!setCooldown(player, player.getUniqueId(), this.defaultCooldown, false)) {
         event.setCancelled(true);
         player.sendMessage(ChatColor.RED + "You still have a " + getDisplayName() + ChatColor.RED + " cooldown for another " + ChatColor.BOLD + com.surgehcf.SurgeCore.getRemaining(getRemaining(player), true, false) + ChatColor.RED + '.');
       }
     }
   }
 }