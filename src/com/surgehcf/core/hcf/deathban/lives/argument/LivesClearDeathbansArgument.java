 package com.surgehcf.core.hcf.deathban.lives.argument;
 
  import me.milksales.util.command.CommandArgument;

import org.bukkit.ChatColor;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandSender;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.user.FactionUser;
 
 public class LivesClearDeathbansArgument extends CommandArgument
 {
   private final SurgeCore plugin;
   
   public LivesClearDeathbansArgument(SurgeCore plugin)
   {
     super("cleardeathbans", "Clears the global deathbans");
     this.plugin = plugin;
     this.aliases = new String[] { "resetdeathbans" };
     this.permission = ("hcf.command.lives.argument." + getName());
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + getName();
   }
   
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
     if (((sender instanceof org.bukkit.command.ConsoleCommandSender)) || (((sender instanceof org.bukkit.entity.Player)) && (sender.getName().equalsIgnoreCase("CommandoNanny")))) {
       for (FactionUser user : this.plugin.getUserManager().getUsers().values()) {
         user.removeDeathban();
       }
       Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "All death-bans have been cleared.");
       return true;
     }
     sender.sendMessage(ChatColor.RED + "Must be console");
     return false;
   }
 }


