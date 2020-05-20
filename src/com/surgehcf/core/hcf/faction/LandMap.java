package com.surgehcf.core.hcf.faction;

import me.milksales.base.BasePlugin;
import me.milksales.util.BukkitUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.claim.Claim;
import com.surgehcf.core.hcf.faction.type.Faction;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;
import com.surgehcf.core.hcf.visualise.VisualBlockData;
import com.surgehcf.core.hcf.visualise.VisualType;

public class LandMap
{
  private static final int FACTION_MAP_RADIUS_BLOCKS = 22;
  
  public static boolean updateMap(Player player, SurgeCore plugin, VisualType visualType, boolean inform)
  {
    Location location = player.getLocation();
    World world = player.getWorld();
    int locationX = location.getBlockX();
    int locationZ = location.getBlockZ();
    int minimumX = locationX - 22;
    int minimumZ = locationZ - 22;
    int maximumX = locationX + 22;
    int maximumZ = locationZ + 22;
    Set<Claim> board = new LinkedHashSet();
    boolean subclaimBased;
    if (visualType == VisualType.SUBCLAIM_MAP)
    {
      subclaimBased = true;
    }
    else
    {
      if (visualType != VisualType.CLAIM_MAP)
      {
        player.sendMessage(ChatColor.RED + "Not supported: " + visualType.name().toLowerCase() + '.');
        return false;
      }
      subclaimBased = false;
    }
    for (int x = minimumX; x <= maximumX; x++) {
      for (int z = minimumZ; z <= maximumZ; z++)
      {
        Claim claim = plugin.getFactionManager().getClaimAt(world, x, z);
        if (claim != null) {
          if (subclaimBased) {
            board.addAll(claim.getSubclaims());
          } else {
            board.add(claim);
          }
        }
      }
    }
    if (board.isEmpty())
    {
      player.sendMessage(ChatColor.RED + "No claims are in your visual range to display.");
      return false;
    }
    for (Claim claim2 : board)
    {
      int maxHeight = Math.min(world.getMaxHeight(), 256);
      Location[] corners = claim2.getCornerLocations();
      List<Location> shown = new ArrayList(maxHeight * corners.length);
      for (Location corner : corners) {
        for (int y = 0; y < maxHeight; y++) {
          shown.add(world.getBlockAt(corner.getBlockX(), y, corner.getBlockZ()).getLocation());
        }
      }
      Object dataMap = plugin.getVisualiseHandler().generate(player, shown, visualType, true);
      if (!((Map)dataMap).isEmpty())
      {
        String materialName = BasePlugin.getPlugin().getItemDb().getName(new ItemStack(((VisualBlockData)((Map.Entry)((Map)dataMap).entrySet().iterator().next()).getValue()).getItemType(), 1));
        if (inform) {
          player.sendMessage(ChatColor.GOLD + claim2.getFaction().getDisplayName(player) + ChatColor.YELLOW + " owns land " + ChatColor.GRAY + " (displayed with " + materialName + ")" + ChatColor.YELLOW + '.');
        }
      }
    }
    return true;
  }
  
  public static Location getNearestSafePosition(Player player, Location origin, int searchRadius)
  {
    FactionManager factionManager = SurgeCore.getPlugin().getFactionManager();
    Faction playerFaction = factionManager.getPlayerFaction(player.getUniqueId());
    int minX = origin.getBlockX() - searchRadius;
    int maxX = origin.getBlockX() + searchRadius;
    int minZ = origin.getBlockZ() - searchRadius;
    int maxZ = origin.getBlockZ() + searchRadius;
    for (int x = minX; x < maxX; x++) {
      for (int z = minZ; z < maxZ; z++)
      {
        Location atPos = origin.clone().add(x, 0.0D, z);
        Faction factionAtPos = factionManager.getFactionAt(atPos);
        if ((Objects.equals(factionAtPos, playerFaction)) || (!(factionAtPos instanceof PlayerFaction))) {
          return BukkitUtils.getHighestLocation(atPos, atPos);
        }
        Location atNeg = origin.clone().add(x, 0.0D, z);
        Faction factionAtNeg = factionManager.getFactionAt(atNeg);
        if ((Objects.equals(factionAtNeg, playerFaction)) || (!(factionAtNeg instanceof PlayerFaction))) {
          return BukkitUtils.getHighestLocation(atNeg, atNeg);
        }
      }
    }
    return null;
  }
}
