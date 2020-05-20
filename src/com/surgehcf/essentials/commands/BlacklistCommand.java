package com.surgehcf.essentials.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.surgehcf.essentials.SurgeExtra;

public class BlacklistCommand implements CommandExecutor{



	// » §
		@SuppressWarnings("deprecation")
		@Override
		public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
			
			if(cmd.getName().equalsIgnoreCase("blacklist")){
				if(s.isOp()){
					if(args.length < 2){
						s.sendMessage("§eSurgeHCF §7» §rUsage: §e/blacklist <username> <reason>");
					}else{
						OfflinePlayer toBan = Bukkit.getServer().getOfflinePlayer(args[0]);
						if(toBan == null){
							s.sendMessage("§cThat player has never joined Surge or is not a player!");
							return true;
						}
						StringBuilder sb = new StringBuilder("");
						for(int i=1; i<args.length; i++){ // change 1
							sb.append(args[i]).append(" ");
						}
						String msg = sb.toString();
						for(Player online : Bukkit.getServer().getOnlinePlayers()){
							online.playSound(online.getLocation(), Sound.ENDERDRAGON_GROWL, 10L, 10L);
							online.getWorld().playEffect(online.getLocation(), Effect.EXPLOSION_HUGE, 20);
							online.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 0));
						}
						Bukkit.getServer().broadcastMessage("§e§m-----§6§m-----§e§m-----§6§m-----§e§m-----§6§m-----§e§m-----§e§m-----§6§m-----§e§m-----§6§m-----");
						Bukkit.getServer().broadcastMessage("§e§lSURGE §6» §eImportant Announcement §7- §cType: §rBlacklist");
						Bukkit.getServer().broadcastMessage("§e§m-----§6§m-----§e§m-----§6§m-----§e§m-----§6§m-----§e§m-----§e§m-----§6§m-----§e§m-----§6§m-----");
						Bukkit.getServer().broadcastMessage("§e§lSURGE §6» §e" + toBan.getName() + " §rhas been §cBLACKLISTED§r!");
						Bukkit.getServer().broadcastMessage("§e§lSURGE §6» §rThis player will not be able to: §eaccess the forums§r, §epurchase an unban§r, §eor join ever again!");
						Bukkit.getServer().broadcastMessage("§e§lSURGE §6» §eReason: §r" + msg);
						Bukkit.getServer().broadcastMessage("§e§m-----§6§m-----§e§m-----§6§m-----§e§m-----§6§m-----§e§m-----§e§m-----§6§m-----§e§m-----§6§m-----");
						Command.broadcastCommandMessage(s, "§eStaff §6» §rBlacklisted player §c" + toBan.getName() + " §rfor §d" + msg);
						ArrayList<String> blacklisted = new ArrayList<String>();
						blacklisted.addAll(SurgeExtra.getInstance().getConfig().getStringList("blacklisted_player_uuids"));
						blacklisted.add(toBan.getUniqueId().toString());
						if(toBan.isOnline()){
							Player p = Bukkit.getServer().getPlayer(args[0]);
							p.kickPlayer("§rYou have been §eblacklisted §rfrom the SurgeHCF Network!\n\n§eReason §6» §r" + msg + "\n§eExpires §6» §rNever (Blacklist)\n§eBlacklisted by §6» §r" + s.getName());
						}
						SurgeExtra.getInstance().getConfig().set("blacklisted_player_uuids", blacklisted);
						SurgeExtra.getInstance().saveConfig();
						SurgeExtra.getInstance().log("&4" + toBan.getName() + " &7was blacklisted for &c" + msg + ".");
						
					}
				}else{
					s.sendMessage("§eStaff §6» §rYou must be an opped player to use this command.");
					s.sendMessage("§eStaff §6» §rTry and contact one of the Owners or Managers if this player needs to be blacklisted urgently.");
				}
			}
			return true;
		}

}
