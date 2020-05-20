 package com.surgehcf.core.hcf.faction.argument;
 
  import me.milksales.util.command.CommandArgument;

import java.util.Iterator;
 import java.util.Set;

import org.apache.commons.lang.StringUtils;
 import org.bukkit.ChatColor;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandSender;
 import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.struct.ChatChannel;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;
 
 public class FactionMessageArgument
   extends CommandArgument
 {
   private final SurgeCore plugin;
   
   public FactionMessageArgument(SurgeCore plugin)
   {
     super("message", "Sends a message to your faction.");
     this.plugin = plugin;
     this.aliases = new String[] { "msg" };
   }
   
   public String getUsage(String label)
   {
     return '/' + label + ' ' + getName() + " <message>";
   }
   
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
   {
     if (!(sender instanceof Player))
     {
       sender.sendMessage(ChatColor.RED + "Only players can use faction chat.");
       return true;
     }
     if (args.length < 2)
     {
       sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
       return true;
     }
     Player player = (Player)sender;
     PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
     if (playerFaction == null)
     {
       sender.sendMessage(ChatColor.RED + "You are not in a faction.");
       return true;
     }
     String format = String.format(ChatChannel.FACTION.getRawFormat(player), new Object[] { "", StringUtils.join((Object[])args, ' ', 1, args.length) });
     Iterator<Player> iterator = playerFaction.getOnlinePlayers().iterator();
     while (iterator.hasNext())
     {
       Player target = (Player)iterator.next();
       target.sendMessage(format);
     }
     return true;
   }
 }

