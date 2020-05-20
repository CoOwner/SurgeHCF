package com.surgehcf.essentials.security;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.surgehcf.essentials.SurgeExtra;
import com.surgehcf.essentials.security.SecurityHandler;
import com.surgehcf.essentials.security.SecurityUtils;


public class SecurityCommand implements CommandExecutor{

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args){
		
		if(s instanceof Player){
			Player p = (Player)s;
			if(cmd.getName().equalsIgnoreCase("pin")){
				if(p.hasPermission("rank.staff")){
					if(!SecurityHandler.getLoggedOutPlayers().contains(p.getName())){
						p.sendMessage("§eSecurity §6» §eYou have already logged in!");
						p.playSound(p.getLocation(), Sound.NOTE_BASS, 4F, 4F);
						return true;
					}
					if(args.length == 0){
						String correctpass1 = SurgeExtra.getInstance().getConfig().getString("security.passcodes." + p.getUniqueId().toString());
						if(correctpass1 == null){
							SecurityUtils.sendLoginMessage(p);
							p.sendMessage("§eSecurity §6» §rLogged in without a password. (No password set, please use /changepin)");
							for(Player all : Bukkit.getServer().getOnlinePlayers()){
								if(all.isOp()){
									all.sendMessage("§eSecurity §6» §e" + p.getName() + " §rlogged in without a password due to §lno password set§r. Please get this player to set a password as soon as possible!");
								}
							}
							SecurityHandler.getLoggedOutPlayers().remove(p.getName());
							SecurityUtils.sendOnLoginMessage(p);
							return true;
						}
						p.sendMessage("§eSecurity §6» §eCorrect Usage: §r/pin <number/password>");
					}else if(args.length == 1){
						String pass = args[0];
						String correctpass = SurgeExtra.getInstance().getConfig().getString("security.passcodes." + p.getUniqueId().toString());
						if(correctpass == null){
							SecurityUtils.sendOnLoginMessage(p);
							p.sendMessage("§eSecurity §6» §rLogged in without a password. (No password set, please use /changepin)");
							for(Player all : Bukkit.getServer().getOnlinePlayers()){
								if(all.isOp()){
									all.sendMessage("§eSecurity §6» §e" + p.getName() + " §rlogged in without a password due to §lno password set§r. Please get this player to set a password as soon as possible!");
								}
							}
							SecurityHandler.getLoggedOutPlayers().remove(p.getName());
							return true;
						}
						if(!pass.equals(correctpass)){
							p.sendMessage("§eSecurity §6» §cIncorrect Passcode.");	
							p.sendMessage("§eSecurity §6» §cIf you  forgotten your passcode comehave into Teamspeak and contact an administrator @ §lts.SurgeExtra.com");
						}else{
							SecurityHandler.getLoggedOutPlayers().remove(p.getName());
							SecurityUtils.sendOnLoginMessage(p);
							String correctIp = SurgeExtra.getInstance().getConfig().getString("security.ipaddresses." + p.getUniqueId().toString());
							String ip = p.getAddress().getAddress().getHostAddress().toString();
							if(!ip.equals(correctIp)){
								for(Player all : Bukkit.getServer().getOnlinePlayers()){
									if(!all.isOp())return true;
									if(!all.getName().equalsIgnoreCase(p.getName()))return true;
									all.sendMessage("§eSecurity §6» §c(Severe) §e" + p.getName() + " §rsigned in on a different ip address to their registered ip. This could be a hacker, it is recommended to bring them into TS!");
								}
							}
						}
					}else{
						p.sendMessage("§eSecurity §6» §eWarning: §rPin Codes only support one string or number. Attempted to log in with §l" + args[0]);
					}
				}else{
					p.sendMessage("§eSecurity §6» §rYou must be a staff member to access this command!");
				}
			}
			
			if(cmd.getName().equalsIgnoreCase("changepin")){
				if(p.hasPermission("rank.staff")){
					if(args.length == 0){
						p.sendMessage("§eSecurity §6» §eCorrect Usage: §r/changepin <number/password>");
					}else if(args.length == 1){
						String pass = args[0];
						SurgeExtra.getInstance().getConfig().set("security.passcodes." + p.getUniqueId().toString(), pass);
						SurgeExtra.getInstance().getConfig().set("security.ipaddresses." + p.getUniqueId().toString(), p.getAddress().getAddress().getHostAddress().toString());
						p.sendMessage("§eSecurity §6» §r§aSet password to §l" + pass);
						p.sendMessage("§eSecurity §6» §r§aRegistered IP address to §l" + p.getAddress().getAddress().getHostAddress().toString());
						SurgeExtra.getInstance().saveConfig();
					}else{
						p.sendMessage("§eSecurity §6» §r§cWarning: §rPin Codes only support one string or number pattern (e.g: 1111 / abc1).");
					}
				}else{
					p.sendMessage("§eSecurity §6» §rYou must be a staff member to access this command!");
				}
			}
			}else{
			s.sendMessage("§cYou must be a §b§lPLAYER §cto use this command.");
			}
		
		
		return true;
	}
}
