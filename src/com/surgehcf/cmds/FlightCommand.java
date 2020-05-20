package com.surgehcf.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlightCommand implements CommandExecutor{
	
	
	  @Override
	  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	  {
		  
		  if(cmd.getName().equalsIgnoreCase("fly")){
			  if(sender instanceof Player){
				  Player p = (Player)sender;
				  
				  if(p.hasPermission("rank.staff")){
					  if(p.getAllowFlight()){
						  p.setAllowFlight(false);
						  p.setFlying(false);
						  p.sendMessage("§eSurge §6» §rToggled flight mode to §cdisabled§r.");
					  }else{
						  p.setAllowFlight(true);
						  p.setFlying(true);
						  p.sendMessage("§eSurge §6» §rToggled flight mode to §aenabled§r.");
					  }
				  }else{
					  p.sendMessage("§eSurge §6» §rRequired Rank: §5Trial-Mod");
				  }
			  }else{
				  sender.sendMessage("§eSurge §6» §rYou ain't a player my dude.");
			  }
		  }
		  return true;
	  }

}
