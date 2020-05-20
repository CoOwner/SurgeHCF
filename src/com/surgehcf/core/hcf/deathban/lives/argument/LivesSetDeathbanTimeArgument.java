 package com.surgehcf.core.hcf.deathban.lives.argument;
 
 import me.milksales.util.JavaUtils;
 import me.milksales.util.command.CommandArgument;
 import java.util.Collections;
 import org.bukkit.ChatColor;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandSender;
 
 public class LivesSetDeathbanTimeArgument extends CommandArgument
 {
   public LivesSetDeathbanTimeArgument()
   {
     super("setdeathbantime", "Sets the base deathban time");
     this.permission = ("hcf.command.lives.argument." + getName());
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + getName() + " <time>";
   }
   
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
     if (args.length < 2) {
       sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
       return true;
     }
     long duration = JavaUtils.parse(args[1]);
     if (duration == -1L) {
       sender.sendMessage(ChatColor.RED + "Invalid duration, use the correct format: 10m 1s");
       return true;
     }
     com.surgehcf.core.hcf.CoreConfiguration.DEFAULT_DEATHBAN_DURATION = duration;
     Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Base death-ban time set to " + org.apache.commons.lang.time.DurationFormatUtils.formatDurationWords(duration, true, true) + " (not including multipliers, etc).");
     return true;
   }
   
   public java.util.List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
     return Collections.emptyList();
   }
 }
