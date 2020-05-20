 package com.surgehcf.core.hcf.eventgame.koth.argument;
 
  import me.milksales.util.JavaUtils;
 import me.milksales.util.command.CommandArgument;

import java.util.Collections;
 import java.util.List;

import org.apache.commons.lang.StringUtils;
 import org.apache.commons.lang.time.DurationFormatUtils;
 import org.bukkit.ChatColor;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandSender;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.eventgame.CaptureZone;
import com.surgehcf.core.hcf.eventgame.faction.KothFaction;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.type.Faction;
 
 
 
 public class KothSetCapDelayArgument
   extends CommandArgument
 {
   private final SurgeCore plugin;
   
   public KothSetCapDelayArgument(SurgeCore plugin)
   {
     super("setcapdelay", "Sets the cap delay of a KOTH");
     this.plugin = plugin;
     this.aliases = new String[] { "setcapturedelay" };
     this.permission = ("hcf.command.koth.argument." + getName());
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + getName() + " <kothName> <capDelay>";
   }
   
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
     if (args.length < 3) {
       sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
       return true;
     }
     Faction faction = this.plugin.getFactionManager().getFaction(args[1]);
     if ((faction == null) || (!(faction instanceof KothFaction))) {
       sender.sendMessage(ChatColor.RED + "There is not a KOTH arena named '" + args[1] + "'.");
       return true;
     }
     long duration = JavaUtils.parse(StringUtils.join((Object[])args, ' ', 2, args.length));
     if (duration == -1L) {
       sender.sendMessage(ChatColor.RED + "Invalid duration, use the correct format: 10m 1s");
       return true;
     }
     KothFaction kothFaction = (KothFaction)faction;
     CaptureZone captureZone = kothFaction.getCaptureZone();
     if (captureZone == null) {
       sender.sendMessage(ChatColor.RED + kothFaction.getDisplayName(sender) + ChatColor.RED + " does not have a capture zone.");
       return true;
     }
     if ((captureZone.isActive()) && (duration < captureZone.getRemainingCaptureMillis())) {
       captureZone.setRemainingCaptureMillis(duration);
     }
     captureZone.setDefaultCaptureMillis(duration);
     sender.sendMessage(ChatColor.YELLOW + "Set the capture delay of KOTH arena " + ChatColor.WHITE + kothFaction.getDisplayName(sender) + ChatColor.YELLOW + " to " + ChatColor.WHITE + DurationFormatUtils.formatDurationWords(duration, true, true) + ChatColor.WHITE + '.');
     return true;
   }
   
   public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
   {
     if (args.length != 2) {
       return Collections.emptyList();
     }
     return Collections.emptyList();
   }
 }

