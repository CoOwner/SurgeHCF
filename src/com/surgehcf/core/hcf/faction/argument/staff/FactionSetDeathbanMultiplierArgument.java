 package com.surgehcf.core.hcf.faction.argument.staff;
 
  import me.milksales.util.command.CommandArgument;
import net.minecraft.util.com.google.common.primitives.Doubles;

 import org.bukkit.ChatColor;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandSender;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.type.Faction;
 
 public class FactionSetDeathbanMultiplierArgument extends CommandArgument
 {
   private static final double MIN_MULTIPLIER = 0.0D;
   private static final double MAX_MULTIPLIER = 5.0D;
   private final SurgeCore plugin;
   
   public FactionSetDeathbanMultiplierArgument(SurgeCore plugin)
   {
     super("setdeathbanmultiplier", "Sets the deathban multiplier of a faction.");
     this.plugin = plugin;
     this.permission = ("hcf.command.faction.argument." + getName());
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + getName() + " <playerName|factionName> <newMultiplier>";
   }
   
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
     if (args.length < 3) {
       sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
       return true;
     }
     Faction faction = this.plugin.getFactionManager().getContainingFaction(args[1]);
     if (faction == null) {
       sender.sendMessage(ChatColor.RED + "Faction named or containing member with IGN or UUID " + args[1] + " not found.");
       return true;
     }
     Double multiplier = Doubles.tryParse(args[2]);
     if (multiplier == null) {
       sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a valid number.");
       return true;
     }
     if (multiplier.doubleValue() < 0.0D) {
       sender.sendMessage(ChatColor.RED + "Deathban multipliers may not be less than " + 0.0D + '.');
       return true;
     }
     if (multiplier.doubleValue() > 5.0D) {
       sender.sendMessage(ChatColor.RED + "Deathban multipliers may not be more than " + 5.0D + '.');
       return true;
     }
     double previousMultiplier = faction.getDeathbanMultiplier();
     faction.setDeathbanMultiplier(multiplier.doubleValue());
     Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Set deathban multiplier of " + faction.getName() + " from " + previousMultiplier + " to " + multiplier + '.');
     return true;
   }
 }

