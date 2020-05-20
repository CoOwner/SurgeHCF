package com.surgehcf.core.hcf.listener;

import me.milksales.base.kit.event.KitApplyEvent;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.type.Faction;

public class KitListener
  implements Listener
{
  private final SurgeCore plugin;
  
  public KitListener(SurgeCore plugin)
  {
    this.plugin = plugin;
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
  public void onKitApply(KitApplyEvent event)
  {
    Player player = event.getPlayer();
    Location location = player.getLocation();
    Faction factionAt = this.plugin.getFactionManager().getFactionAt(location);
    Faction playerFaction;
    if ((!factionAt.isSafezone()) && (((playerFaction = this.plugin.getFactionManager().getPlayerFaction(player)) == null) || (!playerFaction.equals(factionAt))))
    {
      player.sendMessage(ChatColor.YELLOW + "On SurgeHCF kits can only be applied in safe-zones or your own claims.");
      event.setCancelled(true);
    }
  }
}
