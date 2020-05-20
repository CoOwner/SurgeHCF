package com.surgehcf.core.hcf.pvpclass.mage;

import me.milksales.util.BukkitUtils;
import me.milksales.util.chat.Lang;

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
import org.bukkit.event.Event;
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
import com.surgehcf.core.hcf.pvpclass.mage.MageClass;
import com.surgehcf.core.hcf.pvpclass.mage.MageData;
import com.surgehcf.core.hcf.pvpclass.mage.MageEffect;
import com.surgehcf.core.hcf.pvpclass.mage.MageRestorer;
import com.surgehcf.core.hcf.timer.TimerManager;
import com.surgehcf.core.hcf.timer.type.PvpProtectionTimer;

public class MageClass
  extends PvpClass
  implements Listener
{
  public static final int HELD_EFFECT_DURATION_TICKS = 100;
  
  public MageClass(SurgeCore plugin)
  {
    super("Drab", TimeUnit.SECONDS.toMillis(1L));
    this.mageDataMap = new HashMap();
    this.mageEffects = new EnumMap(Material.class);
    this.msgCooldowns = new TObjectLongHashMap();
    this.plugin = plugin;
    this.mageRestorer = new MageRestorer(plugin);
    this.passiveEffects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
    this.passiveEffects.add(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
    this.passiveEffects.add(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
    this.mageEffects.put(Material.SUGAR, new MageEffect(30, new PotionEffect(PotionEffectType.CONFUSION, 120, 2), new PotionEffect(PotionEffectType.CONFUSION, 40, 1)));
    this.mageEffects.put(Material.SPIDER_EYE, new MageEffect(30, new PotionEffect(PotionEffectType.WITHER, 100, 1), null));
    this.mageEffects.put(Material.MAGMA_CREAM, new MageEffect(30, new PotionEffect(PotionEffectType.BLINDNESS, 120, 0), new PotionEffect(PotionEffectType.BLINDNESS, 60, 0)));
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
    MageData mageData = new MageData();
    this.mageDataMap.put(player.getUniqueId(), mageData);
    mageData.startEnergyTracking();
    
    mageData.heldTask = new BukkitRunnable()
    {
      int lastEnergy;
      
      public void run()
      {
        ItemStack held = player.getItemInHand();
        PlayerFaction playerFaction;
        if (held != null)
        {
          MageEffect mageEffect = (MageEffect)MageClass.this.mageEffects.get(held.getType());
          if ((mageEffect != null) && (!MageClass.this.plugin.getFactionManager().getFactionAt(player.getLocation()).isSafezone()))
          {
            playerFaction = MageClass.this.plugin.getFactionManager().getPlayerFaction(player);
            if (playerFaction != null)
            {
              Collection<Entity> nearbyEntities = player.getNearbyEntities(25.0D, 25.0D, 25.0D);
              for (Entity nearby : nearbyEntities) {
                if (((nearby instanceof Player)) && (!player.equals(nearby)))
                {
                  Player target = (Player)nearby;
                  if (playerFaction.getMembers().containsKey(target.getUniqueId())) {}
                }
              }
            }
          }
        }
        int energy = (int)MageClass.this.getEnergy(player);
        if ((energy != 0) && (energy != this.lastEnergy) && ((energy % 10 == 0) || (this.lastEnergy - energy - 1 > 0) || (energy == 100.0D)))
        {
          this.lastEnergy = energy;
          player.sendMessage(ChatColor.AQUA + MageClass.this.name + " Energy: " + ChatColor.YELLOW + energy);
        }
      }
    }
    
      .runTaskTimer(this.plugin, 0L, 20L);
    return true;
  }
  
  public void onUnequip(Player player)
  {
    super.onUnequip(player);
    clearMageData(player.getUniqueId());
  }
  
  private void clearMageData(UUID uuid)
  {
    MageData mageData = (MageData)this.mageDataMap.remove(uuid);
    if ((mageData != null) && (mageData.heldTask != null)) {
      mageData.heldTask.cancel();
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onPlayerQuit(PlayerQuitEvent event)
  {
    clearMageData(event.getPlayer().getUniqueId());
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onPlayerKick(PlayerKickEvent event)
  {
    clearMageData(event.getPlayer().getUniqueId());
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
      MageEffect mageEffect = (MageEffect)this.mageEffects.get(newStack.getType());
      if (mageEffect != null) {
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
      MageEffect mageEffect = (MageEffect)this.mageEffects.get(stack.getType());
      if ((mageEffect == null) || (mageEffect.clickable == null)) {
        return;
      }
      event.setUseItemInHand(Event.Result.DENY);
      Player player = event.getPlayer();
      MageData mageData = (MageData)this.mageDataMap.get(player.getUniqueId());
      if (mageData != null)
      {
        if (!canUseMageEffect(player, mageData, mageEffect, true)) {
          return;
        }
        if (stack.getAmount() > 1) {
          stack.setAmount(stack.getAmount() - 1);
        } else {
          player.setItemInHand(new ItemStack(Material.AIR, 1));
        }
        PlayerFaction playerFaction;
        if ((mageEffect != null) && (!this.plugin.getFactionManager().getFactionAt(player.getLocation()).isSafezone()))
        {
          playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
          if ((playerFaction != null) && ((!mageEffect.clickable.getType().equals(PotionEffectType.WITHER)) || (!mageEffect.clickable.getType().equals(PotionEffectType.BLINDNESS)) || 
            (!mageEffect.clickable.getType().equals(PotionEffectType.CONFUSION))))
          {
            Collection<Entity> nearbyEntities = player.getNearbyEntities(25.0D, 25.0D, 25.0D);
            for (Entity nearby : nearbyEntities) {
              if (((nearby instanceof Player)) && (!player.equals(nearby)))
              {
                Player target = (Player)nearby;
                if (!playerFaction.getMembers().containsKey(target.getUniqueId())) {
                  this.mageRestorer.setRestoreEffect(target, mageEffect.clickable);
                }
              }
            }
          }
          else if ((playerFaction != null) && ((mageEffect.clickable.getType().equals(PotionEffectType.WITHER)) || (mageEffect.clickable.getType().equals(PotionEffectType.BLINDNESS)) || 
            (mageEffect.clickable.getType().equals(PotionEffectType.CONFUSION))))
          {
            Collection<Entity> nearbyEntities = player.getNearbyEntities(25.0D, 25.0D, 25.0D);
            for (Entity nearby : nearbyEntities) {
              if (((nearby instanceof Player)) && (!player.equals(nearby)))
              {
                Player target = (Player)nearby;
                if (!playerFaction.getMembers().containsKey(target.getUniqueId())) {
                  this.mageRestorer.setRestoreEffect(target, mageEffect.clickable);
                }
              }
            }
          }
          else if ((mageEffect.clickable.getType().equals(PotionEffectType.WITHER)) || (mageEffect.clickable.getType().equals(PotionEffectType.BLINDNESS)) || 
            (mageEffect.clickable.getType().equals(PotionEffectType.CONFUSION)))
          {
            Collection<Entity> nearbyEntities = player.getNearbyEntities(25.0D, 25.0D, 25.0D);
            for (Entity nearby : nearbyEntities) {
              if (((nearby instanceof Player)) && (!player.equals(nearby)))
              {
                Player target = (Player)nearby;
                if ((this.plugin.getFactionManager().getPlayerFaction(event.getPlayer()) == null) || (!playerFaction.getMembers().containsKey(target.getUniqueId()))) {
                  this.mageRestorer.setRestoreEffect(target, mageEffect.clickable);
                }
              }
            }
          }
        }
        this.mageRestorer.setRestoreEffect(player, mageEffect.clickable);
        double newEnergy = setEnergy(player, mageData.getEnergy() - mageEffect.energyCost);
        mageData.buffCooldown = (System.currentTimeMillis() + BUFF_COOLDOWN_MILLIS);
        player.sendMessage(ChatColor.YELLOW + "You have just used " + this.name + " buff " + ChatColor.AQUA + Lang.fromPotionEffectType(mageEffect.clickable.getType()) + ' ' + (mageEffect.clickable.getAmplifier() + 1) + ChatColor.YELLOW + " costing you " + ChatColor.BOLD + mageEffect.energyCost + ChatColor.YELLOW + " energy. " + "Your energy is now " + ChatColor.GREEN + newEnergy * 10.0D / 10.0D + ChatColor.YELLOW + '.');
      }
    }
  }
  
  private boolean canUseMageEffect(Player player, MageData mageData, MageEffect mageEffect, boolean sendFeedback)
  {
    String errorFeedback = null;
    double currentEnergy = mageData.getEnergy();
    if (mageEffect.energyCost > currentEnergy) {
      errorFeedback = ChatColor.RED + "You need at least " + ChatColor.BOLD + mageEffect.energyCost + ChatColor.RED + " energy to use this Mage buff, whilst you only have " + ChatColor.BOLD + currentEnergy + ChatColor.RED + '.';
    }
    long remaining = mageData.getRemainingBuffDelay();
    if (remaining > 0L) {
      errorFeedback = ChatColor.RED + "You still have a cooldown on this " + ChatColor.GREEN + ChatColor.BOLD + "Mage" + ChatColor.RED + " buff for another " + SurgeCore.getRemaining(remaining, true, false) + ChatColor.RED + '.';
    }
    Faction factionAt = this.plugin.getFactionManager().getFactionAt(player.getLocation());
    if (factionAt.isSafezone()) {
      errorFeedback = ChatColor.RED + "You may not use Mage buffs in safe-zones.";
    }
    if ((sendFeedback) && (errorFeedback != null)) {
      player.sendMessage(errorFeedback);
    }
    return errorFeedback == null;
  }
  
  public boolean isApplicableFor(Player player)
  {
    ItemStack helmet = player.getInventory().getHelmet();
    if ((helmet == null) || (helmet.getType() != Material.CHAINMAIL_HELMET)) {
      return false;
    }
    ItemStack chestplate = player.getInventory().getChestplate();
    if ((chestplate == null) || (chestplate.getType() != Material.CHAINMAIL_CHESTPLATE)) {
      return false;
    }
    ItemStack leggings = player.getInventory().getLeggings();
    if ((leggings == null) || (leggings.getType() != Material.CHAINMAIL_LEGGINGS)) {
      return false;
    }
    ItemStack boots = player.getInventory().getBoots();
    return (boots != null) && (boots.getType() == Material.CHAINMAIL_BOOTS);
  }
  
  public long getRemainingBuffDelay(Player player)
  {
    synchronized (this.mageDataMap)
    {
      MageData mageData = (MageData)this.mageDataMap.get(player.getUniqueId());
      return mageData == null ? 0L : mageData.getRemainingBuffDelay();
    }
  }
  
  public double getEnergy(Player player)
  {
    synchronized (this.mageDataMap)
    {
      MageData mageData = (MageData)this.mageDataMap.get(player.getUniqueId());
      return mageData == null ? 0.0D : mageData.getEnergy();
    }
  }
  
  public long getEnergyMillis(Player player)
  {
    synchronized (this.mageDataMap)
    {
      MageData mageData = (MageData)this.mageDataMap.get(player.getUniqueId());
      return mageData == null ? 0L : mageData.getEnergyMillis();
    }
  }
  
  public double setEnergy(Player player, double energy)
  {
    MageData mageData = (MageData)this.mageDataMap.get(player.getUniqueId());
    if (mageData == null) {
      return 0.0D;
    }
    mageData.setEnergy(energy);
    return mageData.getEnergy();
  }
  
  private static final long BUFF_COOLDOWN_MILLIS = TimeUnit.SECONDS.toMillis(6L);
  private static final int TEAMMATE_NEARBY_RADIUS = 25;
  private static final long HELD_REAPPLY_TICKS = 20L;
  private static final String MARK = BukkitUtils.STRAIGHT_LINE_DEFAULT.substring(0, 8);
  private final Map<UUID, MageData> mageDataMap;
  private final Map<Material, MageEffect> mageEffects;
  private final MageRestorer mageRestorer;
  private final SurgeCore plugin;
  private final TObjectLongMap<UUID> msgCooldowns;
}
