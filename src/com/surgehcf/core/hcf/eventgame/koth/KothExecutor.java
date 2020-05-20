 package com.surgehcf.core.hcf.eventgame.koth;
 
  import me.milksales.util.command.ArgumentExecutor;

import org.bukkit.command.Command;

import com.surgehcf.core.hcf.eventgame.koth.argument.KothScheduleArgument;
 
 public class KothExecutor extends ArgumentExecutor
 {
   private final KothScheduleArgument kothScheduleArgument;
   
   public KothExecutor(com.surgehcf.SurgeCore plugin)
   {
     super("koth");
     addArgument(new com.surgehcf.core.hcf.eventgame.koth.argument.KothHelpArgument(this));
     addArgument(new com.surgehcf.core.hcf.eventgame.koth.argument.KothNextArgument(plugin));
     addArgument(this.kothScheduleArgument = new KothScheduleArgument(plugin));
     addArgument(new com.surgehcf.core.hcf.eventgame.koth.argument.KothSetCapDelayArgument(plugin));
   }
   
   public boolean onCommand(org.bukkit.command.CommandSender sender, Command command, String label, String[] args) {
     if (args.length < 1) {
       this.kothScheduleArgument.onCommand(sender, command, label, args);
       return true;
     }
     return super.onCommand(sender, command, label, args);
   }
 }
