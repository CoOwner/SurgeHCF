package com.surgehcf.essentials.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.essentials.SurgeExtra;

@SuppressWarnings("unused")
public class JudgementDayCommand  implements CommandExecutor{



	//»
		@Override
		public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
			if(cmd.getName().equalsIgnoreCase("judgementday")){
				if(s.isOp()){
					if(s instanceof Player){
						Player p = (Player)s;
						if(args.length == 0){
							p.sendMessage("§r§m-------------------------------------------------------------");
							p.sendMessage("§eSurgeHCF §6» §rJudgement Day §7(Command Help)");
							p.sendMessage("§r§m-------------------------------------------------------------");
							p.sendMessage("§e/" + label + " <confirm/start>: §fStart Judgement Day.");
							p.sendMessage("§e/" + label + " list: §fList players to be banned on Judgement Day.");
							p.sendMessage("§e/" + label + " <add/remove>: §fAdd/Remove players from the ban wave.");
							p.sendMessage("§r§m-------------------------------------------------------------");
						}else if(args.length == 1){
							if(args[0].equalsIgnoreCase("begin") || args[0].equalsIgnoreCase("start")){
								Bukkit.getServer().broadcastMessage("§7§m-------------------------------------------------------------");
								Bukkit.getServer().broadcastMessage("§eSurgeHCF §6» §rJudgement Day is about to begin!");
								Bukkit.getServer().broadcastMessage("");
								Bukkit.getServer().broadcastMessage("§eThis is picked for a specific day where a huge list of players that are known cheaters, hackers, griefers or just scumbags get blacklisted from the network.");
								Bukkit.getServer().broadcastMessage("§cJudgement Day is starting in §l10 seconds.");
								Bukkit.getServer().broadcastMessage("§7§m-------------------------------------------------------------");
								SurgeExtra.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(SurgeCore.getInstance(), new Runnable(){
									public void run(){
										Bukkit.getServer().broadcastMessage("§4§m-------------------------------------------------------------");
										Bukkit.getServer().broadcastMessage("§eSurgeHCF §rJudgement Day has begun");
										Bukkit.getServer().broadcastMessage("");
										Bukkit.getServer().broadcastMessage("§eGood Luck!");
										Bukkit.getServer().broadcastMessage("§cJudgement Day has §lBEGUN§c!");
										Bukkit.getServer().broadcastMessage("§4§m-------------------------------------------------------------");
										ArrayList<String> ban = new ArrayList<String>();
										ban.addAll(SurgeExtra.getInstance().getConfig().getStringList("judgement_day_bans"));
										for(String name : ban){
											Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "ipban " + name + " &e[Judgement Day] &rBlacklisted from the SurgeHCF Network.");
										}
									}
								}, 20 * 10);
							}
						}else if(args.length == 2){
							
						}
					}else{
						s.sendMessage("§4You can only start judgement day as a player.");
					}
				}else{
					
				}
			}
			return true;
		}


}
