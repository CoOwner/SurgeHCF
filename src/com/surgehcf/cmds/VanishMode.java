package com.surgehcf.cmds;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.surgehcf.SurgeCore;

public class VanishMode implements CommandExecutor, Listener{

	private ArrayList<String> vanished = SurgeCore.getInstance().getVanishedPlayers();
	
	@EventHandler
	public void antiVanishGlitch(PlayerJoinEvent e){
		for(String s : vanished){
			if(Bukkit.getServer().getPlayer(s) != null){
				Player p = Bukkit.getServer().getPlayer(s);
				e.getPlayer().hidePlayer(p);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args){
		
		if(cmd.getName().equalsIgnoreCase("vanish")){
			
			if(s instanceof Player && s.hasPermission("rank.staff")){
				Player p = (Player)s;
				if(!vanished.contains(p.getName())){
					vanished.add(p.getName());
					p.sendMessage("§eSurge §6» §rToggled visibility to §evanished§r!");
					for(Player all : Bukkit.getServer().getOnlinePlayers()){
						if(!all.hasPermission("rank.staff")){
							all.hidePlayer(p);
						}
					}
				}else{
					vanished.remove(p.getName());
					p.sendMessage("§eSurge §6» §rToggled visibility to §avisible§r!");
					for(Player all : Bukkit.getServer().getOnlinePlayers()){
						if(!all.hasPermission("rank.staff")){
							all.showPlayer(p);
						}
					}
				}
			}else{
				s.sendMessage("§eSurge §6» §rYou do not have permission to use this command.");
			}
		}
		
		return true;
	}
}

