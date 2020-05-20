 package com.surgehcf.core.hcf.command;
 
  import java.util.HashMap;

import org.bukkit.Bukkit;
 import org.bukkit.ChatColor;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandSender;
 import org.bukkit.entity.Player;
 import org.bukkit.inventory.ItemStack;
 import org.bukkit.inventory.PlayerInventory;

import com.surgehcf.core.hcf.listener.DeathListener;
 
 public class RefundCommand implements org.bukkit.command.CommandExecutor
 {
   public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args)
   {
     String Usage = ChatColor.RED + "/" + s + " <playerName> <reason>";
     if (!(cs instanceof Player)) {
       cs.sendMessage(ChatColor.RED + "You must be a player");
       return true;
     }
     Player p = (Player)cs;
     if (args.length < 2) {
       cs.sendMessage(Usage);
       return true;
     }
     if (Bukkit.getPlayer(args[0]) == null) {
       p.sendMessage(ChatColor.RED + "Player must be online");
       return true;
     }
     Player target = Bukkit.getPlayer(args[0]);
     if (DeathListener.PlayerInventoryContents.containsKey(target.getUniqueId())) {
       target.getInventory().setContents((ItemStack[])DeathListener.PlayerInventoryContents.get(target.getUniqueId()));
       target.getInventory().setArmorContents((ItemStack[])DeathListener.PlayerArmorContents.get(target.getUniqueId()));
       String reason = org.apache.commons.lang.StringUtils.join((Object[])args, ' ', 2, args.length);
       Command.broadcastCommandMessage(p, ChatColor.YELLOW + "Refunded " + target.getName() + "'s items for: " + reason);
       DeathListener.PlayerArmorContents.remove(target.getUniqueId());
       DeathListener.PlayerInventoryContents.remove(target.getUniqueId());
       return true;
     }
     p.sendMessage(ChatColor.RED + "Player was already refunded items");
     return false;
   }
 }

