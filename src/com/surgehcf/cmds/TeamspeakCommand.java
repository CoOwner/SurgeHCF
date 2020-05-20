package com.surgehcf.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TeamspeakCommand implements CommandExecutor{
	
	
	  @Override
	  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	  {
		  
		  if(cmd.getName().equalsIgnoreCase("ts")){
			  sender.sendMessage("§eOffical Teamspeak §6» §rts.surgehcf.com:10060");
		  }
		  return true;
	  }

}
