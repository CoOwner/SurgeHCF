 package com.surgehcf.core.hcf.deathban.lives;
 
 import com.surgehcf.SurgeCore;

import me.milksales.util.command.ArgumentExecutor;
 
 public class LivesExecutor extends ArgumentExecutor
 {
   public LivesExecutor(SurgeCore plugin)
   {
     super("lives");
     addArgument(new com.surgehcf.core.hcf.deathban.lives.argument.LivesCheckArgument(plugin));
     addArgument(new com.surgehcf.core.hcf.deathban.lives.argument.LivesCheckDeathbanArgument(plugin));
     addArgument(new com.surgehcf.core.hcf.deathban.lives.argument.LivesClearDeathbansArgument(plugin));
     addArgument(new com.surgehcf.core.hcf.deathban.lives.argument.LivesGiveArgument(plugin));
     addArgument(new com.surgehcf.core.hcf.deathban.lives.argument.LivesReviveArgument(plugin));
     addArgument(new com.surgehcf.core.hcf.deathban.lives.argument.LivesSetArgument(plugin));
     addArgument(new com.surgehcf.core.hcf.deathban.lives.argument.LivesSetDeathbanTimeArgument());
   }
 }