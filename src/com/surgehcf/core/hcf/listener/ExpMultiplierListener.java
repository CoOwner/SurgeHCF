package com.surgehcf.core.hcf.listener;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.projectiles.ProjectileSource;

import com.surgehcf.core.hcf.user.UserManager;

public class ExpMultiplierListener
  implements Listener
{
  @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
  public void onEntityDeath(EntityDeathEvent event)
  {
    double amount = event.getDroppedExp();
    Player killer = event.getEntity().getKiller();
    if ((killer != null) && (amount > 0.0D))
    {
      ItemStack stack = killer.getItemInHand();
      if ((stack != null) && (stack.getType() != Material.AIR))
      {
        int enchantmentLevel = stack.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
        if (enchantmentLevel > 0L)
        {
          double multiplier = enchantmentLevel * 3.5D;
          int result = (int)Math.ceil(amount * multiplier);
          event.setDroppedExp(result);
        }
      }
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
  public void onBlockBreak(BlockBreakEvent event)
  {
    double amount = event.getExpToDrop();
    Player player = event.getPlayer();
    ItemStack stack = player.getItemInHand();
    if ((stack != null) && (stack.getType() != Material.AIR) && (amount > 0.0D))
    {
      int enchantmentLevel = stack.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
      if (enchantmentLevel > 0)
      {
        double multiplier = enchantmentLevel * 5.5D;
        int result = (int)Math.ceil(amount * multiplier);
        event.setExpToDrop(result);
      }
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
  public void onPlayerPickupExp(PlayerExpChangeEvent event)
  {
    double amount = event.getAmount();
    if (amount > 0.0D)
    {
      int result = (int)Math.ceil(amount * 5.0D);
      event.setAmount(result);
    }
  }
  
	public int getRandomInteger(int lowest, int highest){
		
		Random x = new Random();
		int Low = lowest;
		int High = highest;
		int r = x.nextInt(High-Low) + Low;
		
		return r;
	}
	
  @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
  public void onPlayerFish(PlayerFishEvent event)
  {
    double amount = event.getExpToDrop();
    if (amount > 0.0D)
    {
      amount = Math.ceil(amount * 2.0D);
      ProjectileSource projectileSource = event.getHook().getShooter();
      if ((projectileSource instanceof Player))
      {
        ItemStack stack = ((Player)projectileSource).getItemInHand();
        int enchantmentLevel = stack.getEnchantmentLevel(Enchantment.LUCK);
        if (enchantmentLevel > 0L) {
          amount = Math.ceil(amount * (enchantmentLevel * 1.5D));
        }
      }
      event.setExpToDrop((int)amount);
    }
    Player p = event.getPlayer();
    int lucky = getRandomInteger(1, 50);
    if(lucky == 10 && event.getCaught() != null){
    	p.sendMessage("");
    	p.sendMessage("§6You just caught a lucky fish! (1/50 chance)");
    	p.sendMessage("§eYour reward is being chosen by the server...");
    	p.sendMessage("");
    	p.sendMessage("§7Possible Rewards: §o(500$, 16 Enderpearls, Prot 1 Unbreaking 3 Diamond Chestplate, 2 Gold Blocks, 3 Diamond Blocks)");
    	p.sendMessage("");
    	int type = getRandomInteger(1, 5);
    	if(type == 1){
    		p.sendMessage("§eWoah! You caught a lucky fish and earnt §6$500§e!");
    		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "eco " + p.getName() + " add 500");
    	}else if(type == 2){
    		p.sendMessage("§eSome say you can find rare pearls in the pond...");
    		p.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 16));
    	}else if(type == 3){
    		p.sendMessage("§eDiamond Chestplate? No problem!");
    		ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE);
    		ItemMeta meta = chest.getItemMeta();
    		meta.setDisplayName("§eThe lucky chestplate of the severn seas!");
    		meta.setLore(Arrays.asList("§bCaught by fishing in the seas of SurgeHCF!"));
    		chest.setItemMeta(meta);
    		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
    		chest.addEnchantment(Enchantment.DURABILITY, 3);
    		p.getInventory().addItem(chest);
    	}else if(type == 4){
    		p.sendMessage("§eApparently, if you fish on SurgeHCF you can find a pot of gold!");
    		ItemStack chest = new ItemStack(Material.GOLD_BLOCK);
    		ItemMeta meta = chest.getItemMeta();
    		meta.setDisplayName("§ePot of Gold!");
    		meta.setLore(Arrays.asList("§bCaught by fishing in the seas of SurgeHCF!"));
    		chest.setItemMeta(meta);
    		chest.setAmount(2);
    		p.getInventory().addItem(chest);
    	}else if(type == 5){
    		p.sendMessage("§eSome say, if you fish on SurgeHCF you can find valuable minerals like diamonds!");
    		ItemStack chest = new ItemStack(Material.DIAMOND_BLOCK);
    		ItemMeta meta = chest.getItemMeta();
    		meta.setDisplayName("§eDiamonds!");
    		meta.setLore(Arrays.asList("§bCaught by fishing in the seas of SurgeHCF!"));
    		chest.setItemMeta(meta);
    		chest.setAmount(3);
    		p.getInventory().addItem(chest);
    	}else if(type == 6){
    		p.sendMessage("§eYou didn't get a reward this time, try again next time!");
    	}
    }
    
  }
}
