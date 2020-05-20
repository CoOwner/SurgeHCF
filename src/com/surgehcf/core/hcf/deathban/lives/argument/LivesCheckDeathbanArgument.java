 package com.surgehcf.core.hcf.deathban.lives.argument;
 
 import me.milksales.util.BukkitUtils;

import org.bukkit.ChatColor;
 import org.bukkit.Location;
 import org.bukkit.OfflinePlayer;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandSender;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.deathban.Deathban;
import com.surgehcf.core.hcf.user.FactionUser;
 
 public class LivesCheckDeathbanArgument extends me.milksales.util.command.CommandArgument
 {
	   private final SurgeCore plugin;
	   
	   public LivesCheckDeathbanArgument(SurgeCore plugin)
	   {
		     super("checkdeathban", "Check the deathban cause of player");
		     this.plugin = plugin;
		     this.permission = ("hcf.command.lives.argument." + getName());
	   }
	   // § »
	   public String getUsage(String label) {
		     return '/' + label + ' ' + getName() + " <playerName>";
	   }
	   
	   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		     if (args.length < 2) {
			       sender.sendMessage("§eUsage §6» §r" + getUsage(label));
			       return true;
		     }
		     OfflinePlayer target = org.bukkit.Bukkit.getOfflinePlayer(args[1]);
		     if ((!target.hasPlayedBefore()) && (!target.isOnline())) {
			       sender.sendMessage("§cError §6» §rThe player §e" + args[1] + " §rwas not found.");
			       return true;
		     }
		     Deathban deathban = this.plugin.getUserManager().getUser(target.getUniqueId()).getDeathban();
		     if ((deathban == null)) {
			       sender.sendMessage("§cError §6» §rThe player §e" + target.getName() + " §ris not deathbanned.");
			       return true;
		     }
		sender.sendMessage("§7" + BukkitUtils.STRAIGHT_LINE_DEFAULT);
		sender.sendMessage("§6Deathban Information of the player §c" + target.getName());
		sender.sendMessage("§7" + BukkitUtils.STRAIGHT_LINE_DEFAULT);
		sender.sendMessage("§eDeathban Time §6» §r" + com.surgehcf.core.hcf.DateFormatter.HR_MIN.format(deathban.getCreationMillis()));
		sender.sendMessage("§eRemaining Duration §6» §r" + org.apache.commons.lang.time.DurationFormatUtils.formatDurationWords(deathban.getExpiryMillis() - deathban.getCreationMillis(), true, true));
		Location location = deathban.getDeathPoint();
		if (location != null) {
			sender.sendMessage("§eLocation §6» §r" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + " (World: " + location.getWorld().getName() + ")");
		}
		sender.sendMessage("§eReason §6» §r" + com.google.common.base.Strings.nullToEmpty(deathban.getReason()));
		sender.sendMessage("§7" + BukkitUtils.STRAIGHT_LINE_DEFAULT);
		return true;
	}
	   
	   public java.util.List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		     if (args.length != 2) {
			       return java.util.Collections.emptyList();
		     }
		     java.util.List<String> results = new java.util.ArrayList();
		     for (FactionUser factionUser : this.plugin.getUserManager().getUsers().values()) {
			       Deathban deathban = factionUser.getDeathban();
			       if ((deathban != null) && (deathban.isActive())) {
				         OfflinePlayer offlinePlayer = org.bukkit.Bukkit.getOfflinePlayer(factionUser.getUserUUID());
				         String name = offlinePlayer.getName();
				         if (name != null)
				         {
					 
					           results.add(name); }
			       }
		     }
		     return results;
	   }
 }

