package com.surgehcf.core.hcfold.crate;

import me.milksales.util.BukkitUtils;
import me.milksales.util.InventoryUtils;
import me.milksales.util.chat.Text;
import me.milksales.util.chat.TextUtils;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.type.Faction;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;
import com.surgehcf.core.hcfold.crate.EnderChestKey;
import com.surgehcf.core.hcfold.crate.Key;

public class KeyListener
  implements Listener
{
  private final SurgeCore plugin;
  
  public KeyListener(SurgeCore plugin)
  {
    this.plugin = plugin;
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
  public void onBlockPlace(BlockPlaceEvent event)
  {
    Key key = this.plugin.getKeyManager().getKey(event.getItemInHand());
    if (key != null) {
      event.setCancelled(true);
    }
  }
  
  public String color(String msg)
  {
    return ChatColor.translateAlternateColorCodes('&', msg);
  }
  
  @EventHandler
  public void onInteract1(PlayerInteractEvent e)
  {
    Player p = e.getPlayer();
    if (((e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) || (e.getAction().equals(Action.RIGHT_CLICK_AIR))) && 
      (e.getPlayer().getItemInHand() != null) && 
      (e.getPlayer().getItemInHand().hasItemMeta()) && 
      (e.getPlayer().getItemInHand().getItemMeta().hasDisplayName()) && 
      (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(color("&cDTR Book")))) {
      if (e.getPlayer().hasPermission("hcf.command.kit"))
      {
        Faction faction2 = this.plugin.getFactionManager().getContainingFaction(p.getName());
        if (faction2 == null)
        {
          p.sendMessage(ChatColor.RED + "You need a faction to use this.");
          return;
        }
        if (!(faction2 instanceof PlayerFaction))
        {
          p.sendMessage(ChatColor.RED + "You can only set DTR of player factions.");
          return;
        }
        if (((PlayerFaction)faction2).getDeathsUntilRaidable() <= 0.0D)
        {
          p.sendMessage(ChatColor.RED + "You're raidable! You cannot use this.");
          return;
        }
        if (((PlayerFaction)faction2).getDeathsUntilRaidable() + 1.0D <= ((PlayerFaction)faction2).getMaximumDeathsUntilRaidable())
        {
          p.sendMessage(ChatColor.RED + "You're on max DTR! You cannot use this.");
          return;
        }
        Double newDTR = Double.valueOf(1.0D);
        PlayerFaction playerFaction = (PlayerFaction)faction2;
        double previousDtr = playerFaction.getDeathsUntilRaidable();
        newDTR = Double.valueOf(playerFaction.setDeathsUntilRaidable(previousDtr + 1.0D));
        p.sendMessage(ChatColor.GREEN + "You used the " + ChatColor.AQUA + e.getPlayer().getItemInHand().getItemMeta().getDisplayName() + ChatColor.GREEN + " you're DTR is now " + ChatColor.YELLOW + newDTR);
        
        p.setItemInHand(new ItemStack(Material.AIR, 1));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7DTR Book &6�� &e" + playerFaction.getName() + " &aused the book&6."));
      }
      else
      {
        e.getPlayer().sendMessage("Report this to an Admin");
      }
    }
  }
  
  @EventHandler
  public void onFreeze(PlayerInteractEvent e)
  {
    Player p = e.getPlayer();
    if (((e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) || (e.getAction().equals(Action.RIGHT_CLICK_AIR))) && 
      (e.getPlayer().getItemInHand() != null) && 
      (e.getPlayer().getItemInHand().hasItemMeta()) && 
      (e.getPlayer().getItemInHand().getItemMeta().hasDisplayName()) && 
      (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(color("&bFreeze Book")))) {
      if (e.getPlayer().hasPermission("hcf.command.kit"))
      {
        Faction faction2 = this.plugin.getFactionManager().getContainingFaction(p.getName());
        if (faction2 == null)
        {
          p.sendMessage(ChatColor.RED + "You need a faction to use this.");
          return;
        }
        if (!(faction2 instanceof PlayerFaction))
        {
          p.sendMessage(ChatColor.RED + "You can only set DTR-Regen of player factions.");
          return;
        }
        if (((PlayerFaction)faction2).getDeathsUntilRaidable() <= 0.0D)
        {
          p.sendMessage(ChatColor.RED + "You're raidable! You cannot use this.");
          return;
        }
        if (((PlayerFaction)faction2).getRemainingRegenerationTime() <= 0L)
        {
          p.sendMessage(ChatColor.RED + "You're not on regen! You cannot use this.");
          return;
        }
        long newRegen = 0L;
        PlayerFaction playerFaction = (PlayerFaction)faction2;
        long previousRegenRemaining = playerFaction.getRemainingRegenerationTime();
        playerFaction.setRemainingRegenerationTime(newRegen);
        p.setItemInHand(new ItemStack(Material.AIR, 1));
        p.sendMessage(ChatColor.GREEN + "You used the " + ChatColor.AQUA + "Freeze Book" + ChatColor.GREEN + " you're now off DTR freeze");
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7Freeze Book &6�� &e" + playerFaction.getName() + " &aused the book&6."));
      }
      else
      {
        e.getPlayer().sendMessage("Report this to an Admin");
      }
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
  public void onInventoryClose(InventoryCloseEvent event)
  {
    Inventory inventory = event.getInventory();
    Inventory topInventory = event.getView().getTopInventory();
    if ((inventory != null) && (topInventory != null) && (topInventory.equals(inventory)) && (topInventory.getTitle().endsWith(" Key Reward")))
    {
      Player player = (Player)event.getPlayer();
      Location location = player.getLocation();
      World world = player.getWorld();
      boolean isEmpty = true;
      ItemStack[] var8 = topInventory.getContents();
      int var9 = var8.length;
      for (int var10 = 0; var10 < var9; var10++)
      {
        ItemStack stack = var8[var10];
        if ((stack != null) && (stack.getType() != Material.AIR))
        {
          world.dropItemNaturally(location, stack);
          isEmpty = false;
        }
      }
      if (!isEmpty) {
    	  player.sendMessage(ChatColor.GRAY.toString() + BukkitUtils.STRAIGHT_LINE_DEFAULT);
          player.sendMessage(ChatColor.YELLOW + "You closed your crate key inventory!");
          player.sendMessage(ChatColor.RESET + "Your items have been dropped to the ground.");
          player.sendMessage(ChatColor.GRAY.toString() + BukkitUtils.STRAIGHT_LINE_DEFAULT);
      }
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
  public void onInventoryDrag(InventoryDragEvent event)
  {
    Inventory inventory = event.getInventory();
    Inventory topInventory = event.getView().getTopInventory();
    if ((inventory != null) && (topInventory != null) && (topInventory.equals(inventory)) && (topInventory.getTitle().endsWith(" Key Reward"))) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
  public void onInventoryClick(InventoryClickEvent event)
  {
    Inventory clickedInventory = event.getClickedInventory();
    Inventory topInventory = event.getView().getTopInventory();
    if ((clickedInventory != null) && (topInventory != null) && (topInventory.getTitle().endsWith(" Key Reward")))
    {
      InventoryAction action = event.getAction();
      if ((!topInventory.equals(clickedInventory)) && (action == InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
        event.setCancelled(true);
      } else if ((topInventory.equals(clickedInventory)) && ((action == InventoryAction.PLACE_ALL) || (action == InventoryAction.PLACE_ONE) || (action == InventoryAction.PLACE_SOME))) {
        event.setCancelled(true);
      }
    }
  }
  
  private void decrementHand(Player player)
  {
    ItemStack stack = player.getItemInHand();
    if (stack.getAmount() <= 1) {
      player.setItemInHand(new ItemStack(Material.AIR, 1));
    } else {
      stack.setAmount(stack.getAmount() - 1);
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
  public void onPlayerInteract(PlayerInteractEvent event)
  {
    Player player = event.getPlayer();
    Action action = event.getAction();
    ItemStack stack = event.getItem();
    if (action == Action.RIGHT_CLICK_BLOCK)
    {
      Key key = this.plugin.getKeyManager().getKey(stack);
      if (key != null)
      {
        Block block = event.getClickedBlock();
        BlockState state = block.getState();
        if (((key instanceof EnderChestKey)) && (block.getType() == Material.ENDER_CHEST))
        {
          InventoryView openInventory = player.getOpenInventory();
          Inventory topInventory = openInventory.getTopInventory();
          if ((topInventory != null) && (topInventory.getTitle().endsWith(" Key Reward"))) {
            return;
          }
          EnderChestKey enderChestKey = (EnderChestKey)key;
          boolean broadcastLoot = enderChestKey.getBroadcastItems();
          int rolls = enderChestKey.getRolls();
          int size = InventoryUtils.getSafestInventorySize(rolls);
          Inventory inventory = Bukkit.createInventory(player, size, enderChestKey.getName() + " Key Reward");
          ItemStack[] loot = enderChestKey.getLoot();
          if (loot == null)
          {
            player.sendMessage(ChatColor.RED + "That key has no loot defined, please inform an admin.");
            return;
          }
          ArrayList finalLoot = new ArrayList();
          Random random = this.plugin.getRandom();
          for (int location = 0; location < rolls; location++)
          {
            ItemStack item = loot[random.nextInt(loot.length)];
            if ((item != null) && (item.getType() != Material.AIR))
            {
              finalLoot.add(item);
              inventory.setItem(location, item);
            }
          }
          if (broadcastLoot)
          {
            Text var20 = new Text();
            var20.append(new Text(player.getName()).setColor(ChatColor.AQUA));
            var20.append(new Text(" has obtained ").setColor(ChatColor.YELLOW));
            var20.append(TextUtils.joinItemList(finalLoot, ", ", true));
            var20.append(new Text(" from a ").setColor(ChatColor.YELLOW));
            var20.append(new Text(enderChestKey.getDisplayName()).setColor(enderChestKey.getColour()));
            var20.append(new Text(" key.").setColor(ChatColor.YELLOW));
            var20.broadcast();
          }
          Location var21 = block.getLocation();
          player.openInventory(inventory);
          player.playSound(var21, Sound.LEVEL_UP, 1.0F, 1.0F);
          decrementHand(player);
          event.setCancelled(true);
        }
      }
    }
  }
}
