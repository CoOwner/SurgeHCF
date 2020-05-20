 package com.surgehcf.core.hcf.eventgame.koth.argument;
 
  import me.milksales.util.command.CommandArgument;

import org.bukkit.ChatColor;
 import org.bukkit.command.CommandSender;

import com.surgehcf.core.hcf.eventgame.koth.KothExecutor;
 
 public class KothHelpArgument extends CommandArgument
 {
   private final KothExecutor kothExecutor;
   
   public KothHelpArgument(KothExecutor kothExecutor)
   {
     super("help", "View help about how KOTH's work");
     this.kothExecutor = kothExecutor;
     this.permission = ("hcf.command.koth.argument." + getName());
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + getName();
   }
   
   public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
     sender.sendMessage(ChatColor.AQUA + "*** KotH Help ***");
     for (CommandArgument argument : this.kothExecutor.getArguments()) {
       if (!argument.equals(this)) {
         String permission = argument.getPermission();
         if ((permission == null) || (sender.hasPermission(permission)))
         {
 
           sender.sendMessage(ChatColor.GRAY + argument.getUsage(label) + " - " + argument.getDescription() + '.'); }
       }
     }
     sender.sendMessage(ChatColor.GRAY + "/fac show <kothName> - View information about a KOTH.");
     return true;
   }
 }

