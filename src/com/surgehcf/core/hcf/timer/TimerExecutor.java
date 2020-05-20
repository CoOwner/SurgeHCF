 package com.surgehcf.core.hcf.timer;
 
 import com.surgehcf.SurgeCore;

import me.milksales.util.command.ArgumentExecutor;
 
 public class TimerExecutor extends ArgumentExecutor
 {
   public TimerExecutor(SurgeCore plugin)
   {
     super("timer");
     addArgument(new com.surgehcf.core.hcf.timer.argument.TimerCheckArgument(plugin));
     addArgument(new com.surgehcf.core.hcf.timer.argument.TimerSetArgument(plugin));
   }
 }


/* Location:              E:\Users\PUTTYDOTEXE\Desktop\Gabe\OLD\ZOlus\HCF (Zolus).jar!\com\exodon\hcf\timer\TimerExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */