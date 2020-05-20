 package com.surgehcf.core.hcf.eventgame;
 
 import com.surgehcf.SurgeCore;

import me.milksales.util.command.ArgumentExecutor;
 
 public class EventExecutor extends ArgumentExecutor
 {
   public EventExecutor(SurgeCore plugin)
   {
     super("event");
     addArgument(new com.surgehcf.core.hcf.eventgame.argument.EventCancelArgument(plugin));
     addArgument(new com.surgehcf.core.hcf.eventgame.argument.EventCreateArgument(plugin));
     addArgument(new com.surgehcf.core.hcf.eventgame.argument.EventDeleteArgument(plugin));
     addArgument(new com.surgehcf.core.hcf.eventgame.argument.EventRenameArgument(plugin));
     addArgument(new com.surgehcf.core.hcf.eventgame.argument.EventSetAreaArgument(plugin));
     addArgument(new com.surgehcf.core.hcf.eventgame.argument.EventSetCapzoneArgument(plugin));
     addArgument(new com.surgehcf.core.hcf.eventgame.argument.EventStartArgument(plugin));
     addArgument(new com.surgehcf.core.hcf.eventgame.argument.EventUptimeArgument(plugin));
   }
 }

