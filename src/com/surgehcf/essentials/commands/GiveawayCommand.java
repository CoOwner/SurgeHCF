package com.surgehcf.essentials.commands;

import java.util.HashMap;
import java.util.IllegalFormatException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;

import com.surgehcf.essentials.utilities.RandomUtils;

public class GiveawayCommand implements CommandExecutor, Listener{

	//»
	
	public static boolean giveawayActive = false;
	public static int giveawayNumber = 0;
	
	public static void endGiveaway(){
		giveawayActive = false;
		giveawayNumber = 0;
	}
	
	public static void endGiveaway(Player winner){
		Bukkit.broadcastMessage("§eSurgeHCF §6» §rThe giveaway has been §cended§r. The winner is §e" + winner.getName() + "§r who guessed the correct number, §e" + giveawayNumber + "§r.");
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "mutechat");
		giveawayActive = false;
		giveawayNumber = 0;
	}
	
	@EventHandler
	public void playerWinGiveaway(AsyncPlayerChatEvent e){
		if(giveawayActive){
			try{
				int no = Integer.parseInt(e.getMessage());
				if(giveawayNumber == no){
					endGiveaway(e.getPlayer());
				}
			}catch(IllegalFormatException ee){}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		
		if(s.isOp()){
			if(args.length == 0){
				s.sendMessage("§cTo start the giveaway use /giveaway start (maxNumber).");
				s.sendMessage("§cTo end the giveaway use /giveaway end.");
			}else if(args.length == 1){
				if(args[0].equalsIgnoreCase("end") || args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("cancel")){
					if(giveawayActive){
						giveawayActive = false;
						Bukkit.broadcastMessage("§eSurgeHCF §6» §rThe giveaway has been §ccancelled§r. The correct number was §e" + giveawayNumber + "§r.");
					}else{
						s.sendMessage("§cThere is no active giveaway.");
					}
				}else{
					s.sendMessage("§cTo start the giveaway use /giveaway start (maxNumber).");
					s.sendMessage("§cTo end the giveaway use /giveaway end.");
				}
			}else if(args.length == 2){
				if(args[0].equalsIgnoreCase("start")){
					try{
						int no = Integer.parseInt(args[1]);
						giveawayActive = true;
						RandomUtils utils = new RandomUtils();
						giveawayNumber = utils.getRandomNumber(1, no);
						s.sendMessage("§aThe winning number is " + giveawayNumber + "!");
						Bukkit.broadcastMessage("§eSurgeHCF §6» §rA giveaway has started. Type a number in chat between 1 and " + no + " to enter!");
					}catch(IllegalFormatException e){
						s.sendMessage("§cYou did not enter a valid integer. (0-" + Integer.MAX_VALUE + ")");
					}
				}
			}
		}else{
			s.sendMessage("§cYou do not have permission.");
		}
		
		return true;
	}

}
