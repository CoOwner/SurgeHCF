package com.surgehcf.core.hcf.listener.fixes;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PexCrashFix
  implements Listener
{
  @EventHandler
  public void onCommandSend(PlayerCommandPreprocessEvent e)
  {
    if ((!e.getPlayer().isOp()) || (!e.getPlayer().hasPermission("pex.bypass")))
    {
      String cmd = e.getMessage().toLowerCase().replaceFirst("/", "");
      if ((cmd.startsWith("pex")) || (cmd.startsWith("permission")) || (((cmd.contains("faction")) || (cmd.contains("f"))) && ((cmd.contains("top")) || (cmd.contains("t"))) && ((cmd.contains("balance")) || (cmd.contains("money")))))
      {
        e.setCancelled(true);
        e.getPlayer().sendMessage(ChatColor.RED + "You lack the correct permissions to run this command!");
      }
    }
  }
}
