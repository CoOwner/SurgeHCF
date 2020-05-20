 package com.surgehcf.core.hcf.timer;
 
 import me.milksales.util.Config;
 
 public abstract class Timer
 {
   protected final String name;
   protected final long defaultCooldown;
   
   public Timer(String name, long defaultCooldown) {
     this.name = name;
     this.defaultCooldown = defaultCooldown;
   }
   
   public abstract String getScoreboardPrefix();
   
   public String getName() {
     return this.name;
   }
   
   public final String getDisplayName() {
     return getScoreboardPrefix() + this.name;
   }
   
   public void load(Config config) {}
   
   public void onDisable(Config config) {}
 }


/* Location:              E:\Users\PUTTYDOTEXE\Desktop\Gabe\OLD\ZOlus\HCF (Zolus).jar!\com\exodon\hcf\timer\Timer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */