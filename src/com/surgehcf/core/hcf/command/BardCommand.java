package com.surgehcf.core.hcf.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.pvpclass.bard.BardClass;
import com.surgehcf.core.hcf.pvpclass.bard.BardData;

import net.md_5.bungee.api.ChatColor;

public class BardCommand implements CommandExecutor{


	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

		if(!(sender instanceof Player))return true;

		Player p = (Player) sender;

		if(args.length == 0){
			sender.sendMessage(ChatColor.RED + "Usage: /bard <setenergy / checkenergy> [player]");
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("checkenergy")){
				if(SurgeCore.getInstance().getPvpClassManager().getEquippedClass(p) != null){
					if(!(SurgeCore.getInstance().getPvpClassManager().getEquippedClass(p) instanceof BardClass)){
						p.sendMessage(ChatColor.RED + "You are not a bard.");
						return true;
					}
					BardClass bard = (BardClass)SurgeCore.getInstance().getPvpClassManager().getEquippedClass(p);
					p.sendMessage(ChatColor.AQUA + "Energy: " + ChatColor.YELLOW + bard.getEnergy(p));
				}else{
					p.sendMessage(ChatColor.RED + "You are not a bard.");
				}
			}else{
				sender.sendMessage(ChatColor.RED + "Usage: /bard <setenergy / checkenergy> [player]");
			}
		}else if(args.length == 2){
			if(args[0].equalsIgnoreCase("checkenergy")){
				Player t = Bukkit.getServer().getPlayer(args[1]);
				if(t != null){
					if(SurgeCore.getInstance().getPvpClassManager().getEquippedClass(t) != null){
						if(!(SurgeCore.getInstance().getPvpClassManager().getEquippedClass(t) instanceof BardClass)){
							p.sendMessage(ChatColor.RED + t.getName() + " is not a bard.");
							return true;
						}
						BardClass bard = (BardClass)SurgeCore.getInstance().getPvpClassManager().getEquippedClass(t);
						p.sendMessage(ChatColor.AQUA + t.getName() + "'s Energy: " + ChatColor.YELLOW + bard.getEnergy(t));
					}else{
						p.sendMessage(ChatColor.RED + t.getName() + " is not a bard.");
					}
				}else{
					p.sendMessage(ChatColor.RED + args[1] + " is not online.");
				}
			}else if(args[0].equalsIgnoreCase("setenergy")){
				Double energy;
				
				try{
					energy = Double.parseDouble(args[1]);
				}catch(Exception e){
					p.sendMessage(ChatColor.RED + "Please enter a valid number eg 1.5!");
					return true;
				}
				
				if(SurgeCore.getInstance().getPvpClassManager().getEquippedClass(p) != null){
					if(!(SurgeCore.getInstance().getPvpClassManager().getEquippedClass(p) instanceof BardClass)){
						p.sendMessage(ChatColor.RED + "You are not a bard.");
						return true;
					}
					BardClass bard = (BardClass)SurgeCore.getInstance().getPvpClassManager().getEquippedClass(p);
					bard.setEnergy(p, energy);
					p.sendMessage(ChatColor.GREEN + "Set your own energy to " + energy + "!");
				}else{
					p.sendMessage(ChatColor.RED + "You are not a bard.");
				}
			}else{
				sender.sendMessage(ChatColor.RED + "Usage: /bard <setenergy / checkenergy> [player] [energy]");
			}
		}else if(args.length == 3){
			if(args[0].equalsIgnoreCase("setenergy")){
				Double energy;
				
				try{
					energy = Double.parseDouble(args[2]);
				}catch(Exception e){
					p.sendMessage(ChatColor.RED + "Please enter a valid number eg 1.5!");
					return true;
				}
				
				Player t = Bukkit.getServer().getPlayer(args[1]);
				if(t != null){
					if(SurgeCore.getInstance().getPvpClassManager().getEquippedClass(t) != null){
						if(!(SurgeCore.getInstance().getPvpClassManager().getEquippedClass(t) instanceof BardClass)){
							p.sendMessage(ChatColor.RED + t.getName() + " is not a bard.");
							return true;
						}
						BardClass bard = (BardClass)SurgeCore.getInstance().getPvpClassManager().getEquippedClass(t);
						bard.setEnergy(t, energy);
						p.sendMessage(ChatColor.GREEN + "Set " + t.getName() + "'s energy to " + energy + "!");
					}else{
						p.sendMessage(ChatColor.RED + t.getName() + " is not a bard.");
					}
				}else{
					p.sendMessage(ChatColor.RED + args[1] + " is not online.");
				}

			}else{
				sender.sendMessage(ChatColor.RED + "Usage: /bard <setenergy / checkenergy> [player] [energy]");
			}
		}

		return true;
	}
}
