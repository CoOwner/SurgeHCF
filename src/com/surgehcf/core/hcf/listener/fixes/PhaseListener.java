package com.surgehcf.core.hcf.listener.fixes;

import java.util.concurrent.TimeUnit;

import me.milksales.util.chat.Text;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.surgehcf.SurgeCore;

public class PhaseListener
  implements Listener
{
  long gravityBlock;
  long utilityBlock;
  
  public PhaseListener()
  {
    this.gravityBlock = TimeUnit.MINUTES.toMillis(45L);
    this.utilityBlock = TimeUnit.MINUTES.toMillis(45L);
  }
  
  boolean sentAlert = false;
  @EventHandler
  public void onMove(PlayerInteractEvent e)
  {
    if ((e.getPlayer().getLocation().getBlock() != null) && (e.getPlayer().getLocation().getBlock().getType() == Material.TRAP_DOOR) && (!SurgeCore.getPlugin().getFactionManager().getFactionAt(e.getPlayer().getLocation()).equals(SurgeCore.getPlugin().getFactionManager().getPlayerFaction(e.getPlayer().getUniqueId()))))
    {
      e.getPlayer().teleport(e.getPlayer().getLocation().add(0.0D, 1.0D, 0.0D));
      Player p = e.getPlayer();
    }
  }
}
