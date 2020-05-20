package com.surgehcf.core.hcf.command;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CobbleCommand implements CommandExecutor{

	private static final ArrayList<String> COBBLE_PLAYERS = new ArrayList<String>();
	
	public static ArrayList<String> getCobblePlayers(){
		return COBBLE_PLAYERS;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		if(getCobblePlayers().contains(sender.getName())){
			getCobblePlayers().remove(sender.getName());
			sender.sendMessage("§eSurgeHCF §6» §rCobblestone pickup §aenabled§r.");
		}else{
			getCobblePlayers().add(sender.getName());
			sender.sendMessage("§eSurgeHCF §6» §rCobblestone pickup §cdisabled§r.");
		}
		
		return true;
	}
}
