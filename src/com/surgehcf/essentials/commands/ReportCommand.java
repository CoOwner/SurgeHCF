package com.surgehcf.essentials.commands;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class ReportCommand implements CommandExecutor{

	public HashMap<String, Long> timerExpiry = new HashMap<String, Long>();
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		
		if(args.length <= 1){
			s.sendMessage(ChatColor.RED + "Usage: /report <player> <reason>");
		}else{
			if(timerExpiry.containsKey(s.getName())){
				if(timerExpiry.get(s.getName()) > System.currentTimeMillis()){
					s.sendMessage(ChatColor.RED + "You are still on a helpop cooldown!");
					return true;
				}
			}
			Player t = Bukkit.getServer().getPlayer(args[0]);
			StringBuilder sb = new StringBuilder("");
			for(int i=1; i<args.length; i++){ // change 1
				sb.append(args[i]).append(" ");
			}
			String msg = sb.toString();
			Bukkit.broadcast("§c[Report] §e" + s.getName() + " §rreported player §4" + t.getName() + " §rfor: §d" + msg, "rank.staff");
			s.sendMessage(ChatColor.GREEN + "Your message has been received and you are now on a report cooldown for 30 seconds.");
			timerExpiry.put(s.getName(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30));
		}
		
		return true;
	}
}
