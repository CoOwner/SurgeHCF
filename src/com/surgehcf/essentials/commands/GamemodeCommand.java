package com.surgehcf.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.surgehcf.essentials.SurgeExtra;

public class GamemodeCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		
		if(s instanceof ConsoleCommandSender){
			SurgeExtra.getInstance().log("&cYou cannot use this command as console.");
			return true;
		}
		Player p = (Player)s;
		if(cmd.getName().equalsIgnoreCase("gamemode") && p.hasPermission("surgehcf.gamemode")){
			if(args.length == 0){
				p.sendMessage("§eSurge §6» §eYour gamemode is currently §6" + p.getGameMode().toString());
			}else if(args.length == 1){
				if(args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c")){
					p.setGameMode(GameMode.CREATIVE);
					Command.broadcastCommandMessage(s, "§eSurge §6» §eSet gamemode to §dCREATIVE§e!");
				}
				if(args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s")){
					p.setGameMode(GameMode.SURVIVAL);
					Command.broadcastCommandMessage(s, "§eSurge §6» §eSet gamemode to §dSURVIVAL§e!");
				}
				if(args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a")){
					p.setGameMode(GameMode.ADVENTURE);
					Command.broadcastCommandMessage(s, "§eSurge §6» §eSet gamemode to §dADVENTURE§e!");
				}
			}else if(args.length == 1 && p.hasPermission("surgehcf.gamemode.others")){
				if(args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c")){
					Player target = Bukkit.getServer().getPlayer(args[1]);
					target.setGameMode(GameMode.CREATIVE);
					Command.broadcastCommandMessage(s, "§eSurge §6» §eSet " + target.getName() + "'s gamemode to §dCREATIVE§e!");
				}
				if(args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s")){
					Player target = Bukkit.getServer().getPlayer(args[1]);
					target.setGameMode(GameMode.SURVIVAL);
					Command.broadcastCommandMessage(s, "§eSurge §6» §eSet " + target.getName() + "'s gamemode to §dSURVIVAL§e!");
				}
				if(args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a")){
					Player target = Bukkit.getServer().getPlayer(args[1]);
					target.setGameMode(GameMode.ADVENTURE);
					Command.broadcastCommandMessage(s, "§eSurge §6» §eSet " + target.getName() + "'s gamemode to §dADVENTURE§e!");
				}
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("gmc") && p.hasPermission("surgehcf.gamemode")){
			if(args.length == 0){
				p.setGameMode(GameMode.CREATIVE);
				Command.broadcastCommandMessage(s, "§eSurge §6» §eSet gamemode to §dCREATIVE§e!");
			}
			if(args.length == 1 && p.hasPermission("surgehcf.gamemode.others")){
				Player target = Bukkit.getServer().getPlayer(args[0]);
				target.setGameMode(GameMode.CREATIVE);
				Command.broadcastCommandMessage(s, "§eSurge §6» §eSet " + target.getName() + "'s gamemode to §dCREATIVE§e!");
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("gms") && p.hasPermission("surgehcf.gamemode")){
			if(args.length == 0){
				p.setGameMode(GameMode.SURVIVAL);
				Command.broadcastCommandMessage(s, "§eSurge §6» §eSet gamemode to §dSURVIVAL§e!");
			}
			if(args.length == 1 && p.hasPermission("surgehcf.gamemode.others")){
				Player target = Bukkit.getServer().getPlayer(args[0]);
				target.setGameMode(GameMode.SURVIVAL);
				Command.broadcastCommandMessage(s, "§eSurge §6» §eSet " + target.getName() + "'s gamemode to §dSURVIVAL§e!");
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("gma") && p.hasPermission("surgehcf.gamemode")){
			if(args.length == 0){
				p.setGameMode(GameMode.ADVENTURE);
				Command.broadcastCommandMessage(s, "§eSurge §6» §eSet gamemode to §dADVENTURE§e!");
			}
			if(args.length == 1 && p.hasPermission("surgehcf.gamemode.others")){
				Player target = Bukkit.getServer().getPlayer(args[0]);
				target.setGameMode(GameMode.ADVENTURE);
				Command.broadcastCommandMessage(s, "§eSurge §6» §eSet " + target.getName() + "'s gamemode to §dADVENTURE§e!");
			}
		}
		
		return true;
	}

}
