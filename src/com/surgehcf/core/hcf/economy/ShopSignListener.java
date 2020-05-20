 package com.surgehcf.core.hcf.economy;
 
  import me.milksales.base.BasePlugin;
import me.milksales.util.InventoryUtils;

import java.util.Map;
 import java.util.Map.Entry;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;

import net.minecraft.util.com.google.common.primitives.Ints;

 import org.bukkit.ChatColor;
 import org.bukkit.block.Block;
 import org.bukkit.block.Sign;
 import org.bukkit.entity.Player;
 import org.bukkit.event.player.PlayerInteractEvent;
 import org.bukkit.inventory.ItemStack;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.api.Crowbar;
 
 public class ShopSignListener implements org.bukkit.event.Listener
 {
   private static final long SIGN_TEXT_REVERT_TICKS = 100L;
   
   public ShopSignListener(SurgeCore plugin)
   {
     this.plugin = plugin;
   }
   
   @org.bukkit.event.EventHandler(ignoreCancelled=false, priority=org.bukkit.event.EventPriority.HIGH)
   public void onPlayerInteract(PlayerInteractEvent event) {
     if (event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) {
       Block block = event.getClickedBlock();
       org.bukkit.block.BlockState state = block.getState();
       if ((state instanceof Sign)) {
         Sign sign = (Sign)state;
         String[] lines = sign.getLines();
         Integer quantity = Ints.tryParse(lines[2]);
         if (quantity == null) {
           return;
         }
         Integer price = Ints.tryParse(ALPHANUMERIC_REMOVER.matcher(lines[3]).replaceAll(""));
         if (price == null)
           return;
         ItemStack stack;
         if (lines[1].equalsIgnoreCase("Crowbar")) {
           stack = new Crowbar().getItemIfPresent();
         }
         else if ((stack = BasePlugin.getPlugin().getItemDb().getItem(ALPHANUMERIC_REMOVER.matcher(lines[1]).replaceAll(""), quantity.intValue())) == null) {
           return;
         }
         Player player = event.getPlayer();
         String[] fakeLines = (String[])java.util.Arrays.copyOf(sign.getLines(), 4);
         if (((lines[0].contains("Sell")) && (lines[0].contains(ChatColor.RED.toString()))) || (lines[0].contains(ChatColor.AQUA.toString()))) {
           int sellQuantity = Math.min(quantity.intValue(), InventoryUtils.countAmount(player.getInventory(), stack.getType(), stack.getDurability()));
           if (sellQuantity <= 0) {
             fakeLines[0] = (ChatColor.RED + "Not carrying any");
             fakeLines[2] = (ChatColor.RED + "on you.");
             fakeLines[3] = "";
           }
           else {
             int newPrice = price.intValue() / quantity.intValue() * sellQuantity;
             fakeLines[0] = (ChatColor.GREEN + "Sold " + sellQuantity);
             fakeLines[3] = (ChatColor.GREEN + "for " + '$' + newPrice);
             this.plugin.getEconomyManager().addBalance(player.getUniqueId(), newPrice);
             InventoryUtils.removeItem(player.getInventory(), stack.getType(), (short)stack.getData().getData(), sellQuantity);
             player.updateInventory();
           }
         }
         else {
           if ((!lines[0].contains("Buy")) || (!lines[0].contains(ChatColor.GREEN.toString()))) {
             return;
           }
           if (price.intValue() > this.plugin.getEconomyManager().getBalance(player.getUniqueId())) {
             fakeLines[0] = (ChatColor.RED + "Cannot afford");
           }
           else {
             fakeLines[0] = (ChatColor.GREEN + "Item bought");
             fakeLines[3] = (ChatColor.GREEN + "for " + '$' + price);
             this.plugin.getEconomyManager().subtractBalance(player.getUniqueId(), price.intValue());
             org.bukkit.World world = player.getWorld();
             org.bukkit.Location location = player.getLocation();
             Map<Integer, ItemStack> excess = player.getInventory().addItem(new ItemStack[] { stack });
             for (Map.Entry<Integer, ItemStack> excessItemStack : excess.entrySet()) {
               world.dropItemNaturally(location, (ItemStack)excessItemStack.getValue());
             }
             player.setItemInHand(player.getItemInHand());
           }
         }
         event.setCancelled(true);
         BasePlugin.getPlugin().getSignHandler().showLines(player, sign, fakeLines, 100L, true);
       }
     }
   }
   
 
   private static final Pattern ALPHANUMERIC_REMOVER = Pattern.compile("[^A-Za-z0-9]");
   private final SurgeCore plugin;
 }