package com.surgehcf.core.hcf.listener;

import me.milksales.util.JavaUtils;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.minecraft.server.v1_7_R4.EntityLightning;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityWeather;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import net.minecraft.server.v1_7_R4.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.CoreConfiguration;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.FactionMember;
import com.surgehcf.core.hcf.faction.struct.Role;
import com.surgehcf.core.hcf.faction.type.Faction;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;
import com.surgehcf.core.hcf.user.FactionUser;
import com.surgehcf.core.hcf.user.UserManager;

public class DeathListener
  implements Listener
{
  public static HashMap<UUID, ItemStack[]> PlayerInventoryContents = new HashMap();
  public static HashMap<UUID, ItemStack[]> PlayerArmorContents = new HashMap();
  private static final long BASE_REGEN_DELAY = TimeUnit.MINUTES.toMillis(40L);
  private final SurgeCore plugin;
  
  public DeathListener(SurgeCore plugin)
  {
    this.plugin = plugin;
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.LOWEST)
  public void onPlayerDeathKillIncrement(PlayerDeathEvent event)
  {
    Player killer = event.getEntity().getKiller();
    Player p = event.getEntity().getPlayer();
    if (killer != null)
    {
      FactionUser user = this.plugin.getUserManager().getUser(killer.getUniqueId());
      FactionUser slain = this.plugin.getUserManager().getUser(p.getUniqueId());
      user.setKills(user.getKills() + 1);
      slain.setDeaths(slain.getDeaths() + 1);
    }
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onPlayerDeath(PlayerDeathEvent event)
  {
    Player player = event.getEntity();
    PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player.getUniqueId());
    if (playerFaction != null)
    {
      Faction factionAt = this.plugin.getFactionManager().getFactionAt(player.getLocation());
      Role role = playerFaction.getMember(player.getUniqueId()).getRole();
      if (playerFaction.getDeathsUntilRaidable() >= -5.0D)
      {
        playerFaction.setDeathsUntilRaidable(playerFaction.getDeathsUntilRaidable() - factionAt.getDtrLossMultiplier());
        playerFaction.setRemainingRegenerationTime(BASE_REGEN_DELAY + playerFaction.getOnlinePlayers().size() * TimeUnit.MINUTES.toMillis(2L));
        playerFaction.broadcast(ChatColor.YELLOW + "Member Death: " + CoreConfiguration.TEAMMATE_COLOUR + role.getAstrix() + player.getName() + ChatColor.YELLOW + ". DTR:" + ChatColor.GRAY + " [" + playerFaction.getDtrColour() + JavaUtils.format(Double.valueOf(playerFaction.getDeathsUntilRaidable())) + ChatColor.WHITE + '/' + ChatColor.WHITE + playerFaction.getMaximumDeathsUntilRaidable() + ChatColor.GRAY + "].");
      }
      else
      {
        playerFaction.setRemainingRegenerationTime(BASE_REGEN_DELAY + playerFaction.getOnlinePlayers().size() * TimeUnit.MINUTES.toMillis(2L));
        playerFaction.broadcast(ChatColor.YELLOW + "Member Death: " + CoreConfiguration.TEAMMATE_COLOUR + role.getAstrix() + player.getName() + ChatColor.YELLOW + ". DTR:" + ChatColor.GRAY + " [" + playerFaction.getDtrColour() + JavaUtils.format(Double.valueOf(playerFaction.getDeathsUntilRaidable())) + ChatColor.WHITE + '/' + ChatColor.WHITE + playerFaction.getMaximumDeathsUntilRaidable() + ChatColor.GRAY + "].");
      }
    }
    if (Bukkit.spigot().getTPS()[0] > 12.0D)
    {
      PlayerInventoryContents.put(player.getUniqueId(), player.getInventory().getContents());
      PlayerArmorContents.put(player.getUniqueId(), player.getInventory().getArmorContents());
      Location location = player.getLocation();
      WorldServer worldServer = ((CraftWorld)location.getWorld()).getHandle();
      EntityLightning entityLightning = new EntityLightning(worldServer, location.getX(), location.getY(), location.getZ(), false);
      PacketPlayOutSpawnEntityWeather packet = new PacketPlayOutSpawnEntityWeather(entityLightning);
      for (Player target : Bukkit.getOnlinePlayers()) {
        if (this.plugin.getUserManager().getUser(target.getUniqueId()).isShowLightning())
        {
          ((CraftPlayer)target).getHandle().playerConnection.sendPacket(packet);
          target.playSound(target.getLocation(), Sound.AMBIENCE_THUNDER, 1.0F, 1.0F);
        }
      }
    }
  }
}
