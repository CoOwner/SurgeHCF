 package com.surgehcf.core.hcf.deathban.lives.argument;
 
  import me.milksales.base.BaseConstants;
import me.milksales.util.command.CommandArgument;

import org.bukkit.ChatColor;
 import org.bukkit.OfflinePlayer;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandSender;

import com.surgehcf.SurgeCore;
 
 public class LivesSetArgument extends CommandArgument
 {
   private final SurgeCore plugin;
   
   public LivesSetArgument(SurgeCore plugin)
   {
     super("set", "Set how much lives a player has");
     this.plugin = plugin;
     this.permission = ("hcf.command.lives.argument." + getName());
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + getName() + " <playerName> <amount>";
   }
   
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
     if (args.length < 3) {
       sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
       return true;
     }
     Integer amount = net.minecraft.util.com.google.common.primitives.Ints.tryParse(args[2]);
     if (amount == null) {
       sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a number.");
       return true;
     }
     OfflinePlayer target = me.milksales.util.BukkitUtils.offlinePlayerWithNameOrUUID(args[1]);
     if ((!target.hasPlayedBefore()) && (!target.isOnline())) {
       sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, new Object[] { args[1] }));
       return true;
     }
     this.plugin.getDeathbanManager().setLives(target.getUniqueId(), amount.intValue());
     sender.sendMessage(ChatColor.YELLOW + target.getName() + " now has " + ChatColor.GOLD + amount + ChatColor.YELLOW + " lives.");
     return true;
   }
   
   public java.util.List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
     return args.length == 2 ? null : java.util.Collections.emptyList();
   }
 }
