package com.surgehcf.essentials.security;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.surgehcf.essentials.SurgeExtra;
import com.surgehcf.essentials.security.SecurityHandler;
import com.surgehcf.essentials.security.SecurityUtils;

public class SecurityListener implements Listener{

	@SuppressWarnings("deprecation")
	@EventHandler
	public void login(PlayerJoinEvent e){
		Player p = e.getPlayer();
		if(!p.hasPermission("rank.staff"))return;
		if(!SecurityHandler.getLoggedOutPlayers().contains(p.getName())){
			SecurityHandler.getLoggedOutPlayers().add(p.getName());
		}
		SecurityUtils.sendLoginMessage(p);
		String correctIp = SurgeExtra.getInstance().getConfig().getString("security.ipaddresses." + p.getName());
		String ip = p.getAddress().getAddress().getHostAddress().toString();
		if(!ip.equals(correctIp)){
			for(Player all : Bukkit.getServer().getOnlinePlayers()){
				if(!all.isOp())return;
				if(all.getName().equalsIgnoreCase(p.getName()))return;
				all.sendMessage("§eSecurity §6» §e" + p.getName() + "§r logged in with the ip address " + ip + " whereas the registered IP is " + correctIp);
			}
		}
	}
	
	@EventHandler
	public void commands(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		if(SecurityHandler.getLoggedOutPlayers().contains(p.getName())){
			if(e.getMessage().toLowerCase().startsWith("/pin") || e.getMessage().toLowerCase().startsWith("/login"))return;
			e.setCancelled(true);
			p.sendMessage("§eSecurity §6» §rYou cannot use §ecommands §runtil you login.");
		}
	}
	
	@EventHandler
	public void move(PlayerMoveEvent e){
		Player p = e.getPlayer();
		if(SecurityHandler.getLoggedOutPlayers().contains(p.getName())){
			e.setTo(e.getFrom());
		}
	}
	
	@EventHandler
	public void damage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			if(SecurityHandler.getLoggedOutPlayers().contains(p.getName())){
				e.setCancelled(true);
			}
		}
	}
}
