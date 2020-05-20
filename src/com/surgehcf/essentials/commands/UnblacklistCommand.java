package com.surgehcf.essentials.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.surgehcf.essentials.SurgeExtra;

public class UnblacklistCommand implements CommandExecutor{

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		
		if(s instanceof Player){
			s.sendMessage("§eStaff §6» §rError: §cYou can only unblacklist players via Console!");
		}else if(s instanceof ConsoleCommandSender){
			if(args.length == 1){
				SurgeExtra.getInstance().log("&cUnblacklisted player &4" + args[0]);
				ArrayList<String> blacklisted = new ArrayList<String>();
				blacklisted.addAll(SurgeExtra.getInstance().getConfig().getStringList("blacklisted_player_uuids"));
				blacklisted.remove(Bukkit.getServer().getOfflinePlayer(args[0]).getUniqueId().toString());
				SurgeExtra.getInstance().getConfig().set("blacklisted_player_uuids", blacklisted);
				SurgeExtra.getInstance().saveConfig();
			}else{
				SurgeExtra.getInstance().log("&cError: Not enough args (/unblacklist <player>)");
			}
		}
		return true;
	}

}

