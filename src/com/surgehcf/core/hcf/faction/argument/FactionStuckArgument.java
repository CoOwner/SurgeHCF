 package com.surgehcf.core.hcf.faction.argument;
 
 import org.bukkit.ChatColor;
 import org.bukkit.command.CommandSender;
 import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.timer.type.StuckTimer;
 
 public class FactionStuckArgument extends me.milksales.util.command.CommandArgument
 {
   private final SurgeCore plugin;
   
   public FactionStuckArgument(SurgeCore plugin)
   {
     super("stuck", "Teleport to a safe position.", new String[] { "trap", "trapped" });
     this.plugin = plugin;
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + getName();
   }
   
   public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
     if (!(sender instanceof Player)) {
       sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
       return true;
     }
     Player player = (Player)sender;
     if (player.getWorld().getEnvironment() != org.bukkit.World.Environment.NORMAL) {
       sender.sendMessage(ChatColor.RED + "You can only use this command from the overworld.");
       return true;
     }
     StuckTimer stuckTimer = this.plugin.getTimerManager().stuckTimer;
     if (!stuckTimer.setCooldown(player, player.getUniqueId())) {
       sender.sendMessage(ChatColor.RED + "Your " + stuckTimer.getDisplayName() + ChatColor.RED + " timer is already active.");
       return true;
     }
     sender.sendMessage(ChatColor.YELLOW + stuckTimer.getDisplayName() + ChatColor.YELLOW + " timer has started. " + "Teleportation will commence in " + ChatColor.LIGHT_PURPLE + SurgeCore.getRemaining(stuckTimer.getRemaining(player), true, false) + ChatColor.YELLOW + ". " + "This will cancel if you move more than " + 5 + " blocks.");
     return true;
   }
 }

