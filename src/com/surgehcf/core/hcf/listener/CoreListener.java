package com.surgehcf.core.hcf.listener;

import me.milksales.base.BasePlugin;
import me.milksales.base.user.BaseUser;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.command.CobbleCommand;
import com.surgehcf.core.hcf.deathban.DeathbanManager;
import com.surgehcf.core.hcf.user.FactionUser;
import com.surgehcf.core.hcf.visualise.VisualiseHandler;

public class CoreListener
  implements Listener
{
  private final SurgeCore plugin;
  
  public CoreListener(SurgeCore plugin)
  {
    this.plugin = plugin;
  }
  
  @EventHandler
  public void onDamaging(EntityDamageByEntityEvent e)
  {
    if ((e.getDamager() instanceof Player))
    {
      Player p = (Player)e.getDamager();
      if (p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
        for (PotionEffect effect : p.getActivePotionEffects()) {
          if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE))
          {
            int level = effect.getAmplifier() + 1;
            double newDamage = e.getDamage(EntityDamageEvent.DamageModifier.BASE) / (level * 1.45D + 1.1D) * 1.5D;
            double damagePercent = newDamage / e.getDamage(EntityDamageEvent.DamageModifier.BASE);
            try
            {
              e.setDamage(EntityDamageEvent.DamageModifier.ARMOR, e.getDamage(EntityDamageEvent.DamageModifier.ARMOR) * damagePercent);
            }
            catch (Exception localException) {}
            try
            {
              e.setDamage(EntityDamageEvent.DamageModifier.MAGIC, e.getDamage(EntityDamageEvent.DamageModifier.MAGIC) * damagePercent);
            }
            catch (Exception localException1) {}
            try
            {
              e.setDamage(EntityDamageEvent.DamageModifier.RESISTANCE, e.getDamage(EntityDamageEvent.DamageModifier.RESISTANCE) * damagePercent);
            }
            catch (Exception localException2) {}
            try
            {
              e.setDamage(EntityDamageEvent.DamageModifier.BLOCKING, e.getDamage(EntityDamageEvent.DamageModifier.BLOCKING) * damagePercent);
            }
            catch (Exception localException3) {}
            e.setDamage(EntityDamageEvent.DamageModifier.BASE, newDamage);
            break;
          }
        }
      }
    }
  }
  
  @EventHandler
  public void onCommand(PlayerCommandPreprocessEvent e)
  {
    String[] args = e.getMessage().split(" ");
    if ((args[0].contains(":")) && (!e.getPlayer().hasPermission("staff.bypass")))
    {
      e.setCancelled(true);
      e.getPlayer().sendMessage(ChatColor.RED + "SurgeHCF does not allow that syntax!");
    }
  }
  
  @EventHandler
  public void onMobKiller(EntityDeathEvent event)
  {
    if (event.getEntityType() == EntityType.CREEPER)
    {
      Player p = event.getEntity().getKiller();
      FactionUser slain = this.plugin.getUserManager().getUser(p.getUniqueId());
      slain.setCreepersKilled(slain.getCreepersKilled() + 1);
    }
    else if (event.getEntityType() == EntityType.ENDERMAN)
    {
      Player p = event.getEntity().getKiller();
      FactionUser slain = this.plugin.getUserManager().getUser(p.getUniqueId());
      slain.setEnderKilled(slain.getEnderKilled() + 1);
    }
  }
  
  @EventHandler
  public void weatherChange(WeatherChangeEvent event)
  {
    if (event.toWeatherState())
    {
      World world = Bukkit.getWorld("world");
      if (event.getWorld() == world)
      {
        event.setCancelled(true);
        world.setStorm(false);
        world.setThundering(false);
        world.setWeatherDuration(0);
      }
    }
  }
  
  private static class LoginMessageRunnable
    extends BukkitRunnable
  {
    private final Player player;
    private final String message;
    
    public LoginMessageRunnable(Player player, String message)
    {
      this.player = player;
      this.message = message;
    }
    
    public void run()
    {
      this.player.sendMessage(this.message);
    }
  }
  
  public String C(String c)
  {
    return ChatColor.translateAlternateColorCodes('&', c);
  }
  
  @EventHandler(priority=EventPriority.LOWEST)
  public void onPlayerJoin(PlayerJoinEvent event)
  {
    event.setJoinMessage((String)null);
  }
  
  @EventHandler
  public void onLogin(PlayerLoginEvent e)
  {
    if ((!e.getPlayer().isWhitelisted()) && (Bukkit.hasWhitelist()) && (!e.getPlayer().hasPermission("rank.staff"))) {
      e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, ChatColor.translateAlternateColorCodes('&', "\n&cSurgeHCF is currently &4whitelisted&c!\n&ePlease check back later or go to our forums for the latest updates. &rsurgehcf.com"));
    } else {
      e.allow();
    }
  }
  
  @EventHandler
  public void onBlockBreak(BlockBreakEvent event)
  {
    if ((event.getBlock().getType() == Material.MOB_SPAWNER) && (event.getBlock().getWorld().getEnvironment() == World.Environment.NETHER) && (!event.getPlayer().isOp()))
    {
      event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot break mob spawners in the nether!");
      event.setCancelled(true);
    }
    if ((event.getBlock().getType() == Material.MOB_SPAWNER) && (event.getBlock().getWorld().getEnvironment() == World.Environment.THE_END) && (!event.getPlayer().isOp()))
    {
      event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot break mob spawners in the end!");
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void onCobble(PlayerPickupItemEvent e){
	  if(e.getItem().getItemStack().getType() == Material.COBBLESTONE && CobbleCommand.getCobblePlayers().contains(e.getPlayer().getName())){
		  e.setCancelled(true);
	  }
  }
  
  @EventHandler(priority=EventPriority.LOWEST)
  public void onPlayerQuit(PlayerKickEvent event)
  {
    event.setLeaveMessage((String)null);
  }
  
  @EventHandler(priority=EventPriority.LOWEST)
  public void onPlayerQuit(PlayerQuitEvent event)
  {
    event.setQuitMessage((String)null);
    Player player = event.getPlayer();
    this.plugin.getVisualiseHandler().clearVisualBlocks(player, null, null, false);
    this.plugin.getUserManager().getUser(player.getUniqueId()).setShowClaimMap(false);
  }
  
  @EventHandler(priority=EventPriority.LOWEST)
  public void onPlayerChangedWorld(PlayerChangedWorldEvent event)
  {
    Player player = event.getPlayer();
    this.plugin.getVisualiseHandler().clearVisualBlocks(player, null, null, false);
    this.plugin.getUserManager().getUser(player.getUniqueId()).setShowClaimMap(false);
  }
  

}
