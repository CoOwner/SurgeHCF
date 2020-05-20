package com.surgehcf.essentials.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.surgehcf.essentials.SurgeExtra;

public class ClaimCommand implements CommandExecutor{


	public void exec(String s){
		SurgeExtra.getInstance().getServer().dispatchCommand(SurgeExtra.getInstance().getServer().getConsoleSender(), s);
	}
//»
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if(cmd.getName().equalsIgnoreCase("claim")){
			if(args.length == 0){
				Player p = (Player)s;
				String playerIdentifier = p.getUniqueId().toString();
				ArrayList<String> claimed = new ArrayList<String>();
				claimed.addAll(SurgeExtra.getInstance().getConfig().getStringList("lists.claimed_rewards"));
				if(!claimed.contains(playerIdentifier)){
					p.sendMessage("§7Grabbing your group and giving you rewards...");
					if(!p.hasPermission("rank.donator")){
						p.sendMessage("§eSurgeHCF §6» §rTo receive crate keys and lives each map purchase a rank @ §ehttp://store.surgehcf.com/");
					}else if(p.hasPermission("rank.iron")){
						Bukkit.getServer().broadcastMessage("» §7" + p.getName() + " §eclaimed their rewards for this map using §b\"/claim\"");
						p.sendMessage("§aReceived rewards for the §7Iron §arank!");
						exec("lives give " + p.getName() + " 4");
						exec("crate key " + p.getName() + " Legend 3");
						exec("crate key " + p.getName() + " Surge 3");
						claimed.add(p.getUniqueId().toString());
						SurgeExtra.getInstance().getConfig().set("lists.claimed_rewards", claimed);
					}else if(p.hasPermission("rank.gold")){
						Bukkit.getServer().broadcastMessage("» §6" + p.getName() + " §eclaimed their rewards for this map using §b\"/claim\"");
						p.sendMessage("§aReceived rewards for the §6Gold §arank!");
						exec("lives give " + p.getName() + " 6");
						exec("crate key " + p.getName() + " Legend 5");
						exec("crate key " + p.getName() + " Surge 5");
						claimed.add(p.getUniqueId().toString());
						SurgeExtra.getInstance().getConfig().set("lists.claimed_rewards", claimed);
					}else if(p.hasPermission("rank.diamond")){
						Bukkit.getServer().broadcastMessage("» §7" + p.getName() + " §bclaimed their rewards for this map using §b\"/claim\"");
						p.sendMessage("§aReceived rewards for the §bDiamond §arank!");
						exec("lives give " + p.getName() + " 10");
						exec("crate key " + p.getName() + " Legend 8");
						exec("crate key " + p.getName() + " Surge 8");
						claimed.add(p.getUniqueId().toString());
						SurgeExtra.getInstance().getConfig().set("lists.claimed_rewards", claimed);
					}else if(p.hasPermission("rank.obsidian")){
						Bukkit.getServer().broadcastMessage("» §5" + p.getName() + " §eclaimed their rewards for this map using §b\"/claim\"");
						p.sendMessage("§aReceived rewards for the §5Obsidian §arank!");
						exec("lives give " + p.getName() + " 16");
						exec("crate key " + p.getName() + " Legend 10");
						exec("crate key " + p.getName() + " Surge 10");
						claimed.add(p.getUniqueId().toString());
						SurgeExtra.getInstance().getConfig().set("lists.claimed_rewards", claimed);
					}else if(p.hasPermission("rank.surge")){
						Bukkit.getServer().broadcastMessage("» §8[§eSurge§8] §e" + p.getName() + " §eclaimed their rewards for this map using §b\"/claim\"");
						p.sendMessage("§aReceived rewards for the §eSurge §arank!");
						exec("lives give " + p.getName() + " 18");
						exec("crate key " + p.getName() + " Legend 14");
						exec("crate key " + p.getName() + " Surge 14");
						claimed.add(p.getUniqueId().toString());
						SurgeExtra.getInstance().getConfig().set("lists.claimed_rewards", claimed);
					}
				}else{
					p.sendMessage("§eSurgeHCF §6» §rYou have already claimed your rewards for this map!");
				}
			}
		}
		
		
		return true;
	}

}
