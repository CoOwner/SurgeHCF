package com.surgehcf.core.hcf.pvpclass.bard;

import me.milksales.util.BukkitUtils;
import me.milksales.util.chat.Lang;

import org.bukkit.event.*;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.minecraft.util.gnu.trove.map.TObjectLongMap;
import net.minecraft.util.gnu.trove.map.hash.TObjectLongHashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.type.Faction;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;
import com.surgehcf.core.hcf.pvpclass.PvpClass;
import com.surgehcf.core.hcf.pvpclass.PvpClassManager;
import com.surgehcf.core.hcf.pvpclass.bard.BardClass;
import com.surgehcf.core.hcf.pvpclass.bard.BardData;
import com.surgehcf.core.hcf.pvpclass.bard.BardEffect;
import com.surgehcf.core.hcf.pvpclass.bard.BardRestorer;
import com.surgehcf.core.hcf.timer.TimerManager;
import com.surgehcf.core.hcf.timer.type.PvpProtectionTimer;

public class BardClass
  extends PvpClass
  implements Listener
{
  public static final int HELD_EFFECT_DURATION_TICKS = 100;
  
  public BardClass(SurgeCore plugin)
  {
    super("Bard", TimeUnit.SECONDS.toMillis(1L));
    this.bardDataMap = new HashMap();
    this.bardEffects = new EnumMap(Material.class);
    this.msgCooldowns = new TObjectLongHashMap();
    this.plugin = plugin;
    this.bardRestorer = new BardRestorer(plugin);
    this.passiveEffects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
    this.passiveEffects.add(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
    this.passiveEffects.add(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
    this.bardEffects.put(Material.SUGAR, new BardEffect(35, new PotionEffect(PotionEffectType.SPEED, 120, 2), new PotionEffect(PotionEffectType.SPEED, 120, 1)));
    this.bardEffects.put(Material.BLAZE_POWDER, new BardEffect(40, new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 1), new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 0)));
    this.bardEffects.put(Material.IRON_INGOT, new BardEffect(30, new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 80, 2), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 0)));
    this.bardEffects.put(Material.GHAST_TEAR, new BardEffect(25, new PotionEffect(PotionEffectType.REGENERATION, 60, 2), new PotionEffect(PotionEffectType.REGENERATION, 120, 0)));
    this.bardEffects.put(Material.FEATHER, new BardEffect(30, new PotionEffect(PotionEffectType.JUMP, 120, 6), new PotionEffect(PotionEffectType.JUMP, 120, 1)));
    this.bardEffects.put(Material.SPIDER_EYE, new BardEffect(50, new PotionEffect(PotionEffectType.WITHER, 120, 1), null));
    this.bardEffects.put(Material.MAGMA_CREAM, new BardEffect(5, new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 900, 0), new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 120, 0)));
    this.bardEffects.put(Material.WHEAT, new BardEffect(5, new PotionEffect(PotionEffectType.SATURATION, 900, 0), new PotionEffect(PotionEffectType.SATURATION, 120, 0)));
    this.bardEffects.put(Material.FERMENTED_SPIDER_EYE, new BardEffect(75, new PotionEffect(PotionEffectType.INVISIBILITY, 3000, 0), new PotionEffect(PotionEffectType.NIGHT_VISION, 120, 0)));
  }
  
  public boolean onEquip(final Player player)
  {
    if ((this.plugin.getTimerManager().pvpProtectionTimer.legible.contains(player.getUniqueId())) || (this.plugin.getTimerManager().pvpProtectionTimer.getRemaining(player) > 0L))
    {
      player.sendMessage(ChatColor.RED + "You cannot equip classes that effect PvP while you are protected from pvp" + ChatColor.GRAY + " (" + getName() + ")");
      return false;
    }
    if (!super.onEquip(player)) {
      return false;
    }
    BardData bardData = new BardData();
    this.bardDataMap.put(player.getUniqueId(), bardData);
    bardData.startEnergyTracking();
    
    bardData.heldTask = new BukkitRunnable()
    {
      int lastEnergy;
      
      public void run()
      {
        ItemStack held = player.getItemInHand();
        BardEffect bardEffect;
        PlayerFaction playerFaction;
        if (held != null)
        {
          bardEffect = (BardEffect)BardClass.this.bardEffects.get(held.getType());
          if ((bardEffect != null) && (!BardClass.this.plugin.getFactionManager().getFactionAt(player.getLocation()).isSafezone()))
          {
            playerFaction = BardClass.this.plugin.getFactionManager().getPlayerFaction(player);
            if (playerFaction != null)
            {
              Collection<Entity> nearbyEntities = player.getNearbyEntities(25.0D, 25.0D, 25.0D);
              for (Entity nearby : nearbyEntities) {
                if (((nearby instanceof Player)) && (!player.equals(nearby)))
                {
                  Player target = (Player)nearby;
                  Player p = player;
                  if (playerFaction.getMembers().containsKey(target.getUniqueId())) {
                    BardClass.this.bardRestorer.setRestoreEffect(target, bardEffect.heldable);
                  }
                }
                else if (bardEffect.heldable.getType() == PotionEffectType.JUMP)
                {
                  player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 120, 1));
                }
              }
            }
          }
        }
        int energy = (int)BardClass.this.getEnergy(player);
        if ((energy != 0) && (energy != this.lastEnergy) && ((energy % 10 == 0) || (this.lastEnergy - energy - 1 > 0) || (energy == 100.0D)))
        {
          this.lastEnergy = energy;
          player.sendMessage(ChatColor.AQUA + BardClass.this.name + " Energy: " + ChatColor.YELLOW + energy);
        }
      }
    }
    
      .runTaskTimer(this.plugin, 0L, 20L);
    return true;
  }
  
  public void onUnequip(Player player)
  {
    super.onUnequip(player);
    clearBardData(player.getUniqueId());
  }
  
  private void clearBardData(UUID uuid)
  {
    BardData bardData = (BardData)this.bardDataMap.remove(uuid);
    if ((bardData != null) && (bardData.heldTask != null)) {
      bardData.heldTask.cancel();
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onPlayerQuit(PlayerQuitEvent event)
  {
    clearBardData(event.getPlayer().getUniqueId());
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onPlayerKick(PlayerKickEvent event)
  {
    clearBardData(event.getPlayer().getUniqueId());
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onItemHeld(PlayerItemHeldEvent event)
  {
    Player player = event.getPlayer();
    PvpClass equipped = this.plugin.getPvpClassManager().getEquippedClass(player);
    if ((equipped == null) || (!equipped.equals(this))) {
      return;
    }
    UUID uuid = player.getUniqueId();
    long lastMessage = this.msgCooldowns.get(uuid);
    long millis = System.currentTimeMillis();
    if ((lastMessage != this.msgCooldowns.getNoEntryValue()) && (lastMessage - millis > 0L)) {
      return;
    }
    ItemStack newStack = player.getInventory().getItem(event.getNewSlot());
    if (newStack != null)
    {
      BardEffect bardEffect = (BardEffect)this.bardEffects.get(newStack.getType());
      if (bardEffect != null) {
        this.msgCooldowns.put(uuid, millis + 1500L);
      }
    }
  }
  
  @EventHandler(ignoreCancelled=false, priority=EventPriority.MONITOR)
  public void onPlayerInteract(PlayerInteractEvent event)
  {
    if (!event.hasItem()) {
      return;
    }
    Action action = event.getAction();
    if ((action == Action.RIGHT_CLICK_AIR) || ((!event.isCancelled()) && (action == Action.RIGHT_CLICK_BLOCK)))
    {
      ItemStack stack = event.getItem();
      BardEffect bardEffect = (BardEffect)this.bardEffects.get(stack.getType());
      if ((bardEffect == null) || (bardEffect.clickable == null)) {
        return;
      }
      event.setUseItemInHand(Event.Result.DENY);
      Player player = event.getPlayer();
      BardData bardData = (BardData)this.bardDataMap.get(player.getUniqueId());
      if (bardData != null)
      {
        if (!canUseBardEffect(player, bardData, bardEffect, true)) {
          return;
        }
        if (stack.getAmount() > 1) {
          stack.setAmount(stack.getAmount() - 1);
        } else {
          player.setItemInHand(new ItemStack(Material.AIR, 1));
        }
        if ((bardEffect != null) && (!this.plugin.getFactionManager().getFactionAt(player.getLocation()).isSafezone()))
        {
          PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
          if ((playerFaction != null) && (!bardEffect.clickable.getType().equals(PotionEffectType.WITHER)))
          {
            Collection<Entity> nearbyEntities = player.getNearbyEntities(25.0D, 25.0D, 25.0D);
            for (Entity nearby : nearbyEntities) {
              if (((nearby instanceof Player)) && (!player.equals(nearby)))
              {
                Player target = (Player)nearby;
                if (playerFaction.getMembers().containsKey(target.getUniqueId())) {
                  this.bardRestorer.setRestoreEffect(target, bardEffect.clickable);
                }
              }
            }
          }
          else if ((playerFaction != null) && (bardEffect.clickable.getType().equals(PotionEffectType.WITHER)))
          {
            Collection<Entity> nearbyEntities = player.getNearbyEntities(25.0D, 25.0D, 25.0D);
            for (Entity nearby : nearbyEntities) {
              if (((nearby instanceof Player)) && (!player.equals(nearby)))
              {
                Player target = (Player)nearby;
                if (!playerFaction.getMembers().containsKey(target.getUniqueId())) {
                  this.bardRestorer.setRestoreEffect(target, bardEffect.clickable);
                }
              }
            }
          }
          else if (bardEffect.clickable.getType().equals(PotionEffectType.WITHER))
          {
            Collection<Entity> nearbyEntities = player.getNearbyEntities(25.0D, 25.0D, 25.0D);
            for (Entity nearby : nearbyEntities) {
              if (((nearby instanceof Player)) && (!player.equals(nearby)))
              {
                Player target = (Player)nearby;
                this.bardRestorer.setRestoreEffect(target, bardEffect.clickable);
              }
            }
          }
        }
        this.bardRestorer.setRestoreEffect(player, bardEffect.clickable);
        double newEnergy = setEnergy(player, bardData.getEnergy() - bardEffect.energyCost);
        bardData.buffCooldown = (System.currentTimeMillis() + BUFF_COOLDOWN_MILLIS);
        player.sendMessage(ChatColor.YELLOW + "You have just used " + this.name + " buff " + ChatColor.AQUA + Lang.fromPotionEffectType(bardEffect.clickable.getType()) + ' ' + (bardEffect.clickable.getAmplifier() + 1) + ChatColor.YELLOW + " costing you " + ChatColor.BOLD + bardEffect.energyCost + ChatColor.YELLOW + " energy. " + "Your energy is now " + ChatColor.GREEN + newEnergy * 10.0D / 10.0D + ChatColor.YELLOW + '.');
      }
    }
  }
  
  private boolean canUseBardEffect(Player player, BardData bardData, BardEffect bardEffect, boolean sendFeedback)
  {
    String errorFeedback = null;
    double currentEnergy = bardData.getEnergy();
    if (bardEffect.energyCost > currentEnergy) {
      errorFeedback = ChatColor.RED + "You need at least " + ChatColor.BOLD + bardEffect.energyCost + ChatColor.RED + " energy to use this Bard buff, whilst you only have " + ChatColor.BOLD + currentEnergy + ChatColor.RED + '.';
    }
    long remaining = bardData.getRemainingBuffDelay();
    if (remaining > 0L) {
      errorFeedback = ChatColor.RED + "You still have a cooldown on this " + ChatColor.GREEN + ChatColor.BOLD + "Bard" + ChatColor.RED + " buff for another " + SurgeCore.getRemaining(remaining, true, false) + ChatColor.RED + '.';
    }
    Faction factionAt = this.plugin.getFactionManager().getFactionAt(player.getLocation());
    if (factionAt.isSafezone()) {
      errorFeedback = ChatColor.RED + "You may not use Bard buffs in safe-zones.";
    }
    if ((sendFeedback) && (errorFeedback != null)) {
      player.sendMessage(errorFeedback);
    }
    return errorFeedback == null;
  }
  
  public boolean isApplicableFor(Player player)
  {
    ItemStack helmet = player.getInventory().getHelmet();
    if ((helmet == null) || (helmet.getType() != Material.GOLD_HELMET)) {
      return false;
    }
    ItemStack chestplate = player.getInventory().getChestplate();
    if ((chestplate == null) || (chestplate.getType() != Material.GOLD_CHESTPLATE)) {
      return false;
    }
    ItemStack leggings = player.getInventory().getLeggings();
    if ((leggings == null) || (leggings.getType() != Material.GOLD_LEGGINGS)) {
      return false;
    }
    ItemStack boots = player.getInventory().getBoots();
    return (boots != null) && (boots.getType() == Material.GOLD_BOOTS);
  }
  
  public long getRemainingBuffDelay(Player player)
  {
    synchronized (this.bardDataMap)
    {
      BardData bardData = (BardData)this.bardDataMap.get(player.getUniqueId());
      return bardData == null ? 0L : bardData.getRemainingBuffDelay();
    }
  }
  
  public double getEnergy(Player player)
  {
    synchronized (this.bardDataMap)
    {
      BardData bardData = (BardData)this.bardDataMap.get(player.getUniqueId());
      return bardData == null ? 0.0D : bardData.getEnergy();
    }
  }
  
  public long getEnergyMillis(Player player)
  {
    synchronized (this.bardDataMap)
    {
      BardData bardData = (BardData)this.bardDataMap.get(player.getUniqueId());
      return bardData == null ? 0L : bardData.getEnergyMillis();
    }
  }
  
  public double setEnergy(Player player, double energy)
  {
    BardData bardData = (BardData)this.bardDataMap.get(player.getUniqueId());
    if (bardData == null) {
      return 0.0D;
    }
    bardData.setEnergy(energy);
    return bardData.getEnergy();
  }
  
  private static final long BUFF_COOLDOWN_MILLIS = TimeUnit.SECONDS.toMillis(6L);
  private static final int TEAMMATE_NEARBY_RADIUS = 35;
  private static final long HELD_REAPPLY_TICKS = 20L;
  private static final String MARK = BukkitUtils.STRAIGHT_LINE_DEFAULT.substring(0, 8);
  private final Map<UUID, BardData> bardDataMap;
  private final Map<Material, BardEffect> bardEffects;
  private final BardRestorer bardRestorer;
  private final SurgeCore plugin;
  private final TObjectLongMap<UUID> msgCooldowns;
}
