 package com.surgehcf.core.hcf.eventgame.conquest;
 
 import com.surgehcf.core.hcf.eventgame.conquest.ConquestSetpointsArgument;

import me.milksales.util.command.ArgumentExecutor;
 
 public class ConquestExecutor extends ArgumentExecutor
 {
   public ConquestExecutor(com.surgehcf.SurgeCore plugin)
   {
     super("conquest");
     addArgument(new ConquestSetpointsArgument(plugin));
   }
 }