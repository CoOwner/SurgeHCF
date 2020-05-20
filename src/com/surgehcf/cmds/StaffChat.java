package com.surgehcf.cmds;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.surgehcf.SurgeCore;

public class StaffChat implements CommandExecutor, Listener{

	private ArrayList<String> staffChat = SurgeCore.getInstance().getStaffChatPlayers();
	
	@EventHandler
	public void chat(AsyncPlayerChatEvent e){
		if(staffChat.contains(e.getPlayer().getName())){
			e.setCancelled(true);
			if(!e.getPlayer().hasPermission("rank.staff")){
				staffChat.remove(e.getPlayer().getName());
				e.getPlayer().sendMessage("§eSurge §6» §rSeems like you was in staff mode without the required permissions? Not to worry, you have been removed from it!");
				return;
			}
			for(Player all : Bukkit.getServer().getOnlinePlayers()){
				if(all.hasPermission("rank.staff")){
					all.sendMessage("§8(§eStaff§8) §e" + e.getPlayer().getName() + ": §r" + ChatColor.translateAlternateColorCodes('&', e.getMessage()));
				}
			}
		}
	}
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args){
		
		if(cmd.getName().equalsIgnoreCase("sc")){
			
			if(s instanceof Player && s.hasPermission("rank.staff")){
				Player p = (Player)s;
				if(!staffChat.contains(p.getName())){
					staffChat.add(p.getName());
					p.sendMessage("§eSurge §6» §rToggled staff chat to §aenabled§r!");
				}else{
					staffChat.remove(p.getName());
					p.sendMessage("§eSurge §6» §rToggled staff chat to §cdisabled§r!");
				}
			}else{
				s.sendMessage("§eSurge §6» §rYou do not have permission to use this command.");
			}
		}
		
		return true;
	}
}
