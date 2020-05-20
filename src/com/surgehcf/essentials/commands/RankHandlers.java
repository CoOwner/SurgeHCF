package com.surgehcf.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.essentials.SurgeExtra;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class RankHandlers implements CommandExecutor{

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("group")){
			final Player p = (Player)s;
			if(p.isOp()){
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex reload");
				if(args.length == 0){
					p.sendMessage("§r§m---------------§7[ §6Groups Help §7]§r§m---------------");
					p.sendMessage("§7§oThis is a simple addon for SurgeHCF to make PermissionsEx a lot simpler to use.");
					p.sendMessage("§b/group list: §fList all groups.");
					p.sendMessage("§b/group create <group>: §fCreate a group.");
					p.sendMessage("§b/group delete <group>: §fDelete a group.");
					p.sendMessage("§b/group addpermission <group> <permission>: §fAdd a permission to a group.");
					p.sendMessage("§b/group deletepermission <group> <permission>: §fDelete a permission from a group.");
					p.sendMessage("§b/group listusers <group>: §fList the users in a group.");
				}else{
					if(args.length == 1){
						
						p.sendMessage("§cLoading list of groups...");
						p.sendMessage("§r§m------------------------------------------------------------");
						p.sendMessage("§6§lSurgeHCF §6» §rCurrent Groups §e(PermissionsEx)");
						p.sendMessage("§r§m------------------------------------------------------------");
						int number = 0;
						for(PermissionGroup i : PermissionsEx.getPermissionManager().getGroupList()){
							number = number + 1;
							p.sendMessage("§7[§c#" + number + "§7] §f" + i.getName());
						}
						p.sendMessage("§r§m------------------------------------------------------------");
						p.sendMessage("§rUse §e/rank <player> <group> §rto add a player to a group.");
						p.sendMessage("§r§m------------------------------------------------------------");
					}
					if(args.length == 2){
						if(args[0].equalsIgnoreCase("create")){
							final String newGroup = args[1].toLowerCase();
							p.sendMessage("§c(Progress...) §eAttempting to create group §d" + newGroup + "§e.");
							if(PermissionsEx.getPermissionManager().getGroupNames().contains(newGroup)){
								p.sendMessage("§c(Error) §eThe group §d" + newGroup + " §ealready exists."); 
							}else{
								p.sendMessage("§c(Progress...) §eCreating the group §d" + newGroup + "§e.");
								Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex group " + newGroup + " create");
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SurgeCore.getInstance(), new Runnable(){
									
									public void run(){
										if(PermissionsEx.getPermissionManager().getGroup(newGroup) != null){
											p.sendMessage("§a(Success) §eCreated the group §d" + newGroup + "§e! Do §b/group addpermission " + newGroup + " <permission> §eto get started!");
										}else{
											p.sendMessage("§c(Error) §eCould not create the group §d" + newGroup + "§e.");
											}
									}
									
								}, 10L);
							}
							
						}else
						if(args[0].equalsIgnoreCase("delete")){
							final String newGroup = args[1].toLowerCase();
							p.sendMessage("§c(Progress...) §eAttempting to delete group §d" + newGroup + "§e.");
							if(!PermissionsEx.getPermissionManager().getGroupNames().contains(newGroup)){
								p.sendMessage("§c(Error) §eThe group §d" + newGroup + " §edoes not exist."); 
							}else{
								p.sendMessage("§c(Progress...) §eDeleting the group §d" + newGroup + "§e.");
								Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex group " + newGroup + " delete");
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SurgeCore.getInstance(), new Runnable(){
									
									public void run(){
										if(PermissionsEx.getPermissionManager().getGroupList().contains(newGroup) == false){
											p.sendMessage("§a(Success) §eDeleted the group §d" + newGroup + "§e successfully!");
										}else{
											p.sendMessage("§c(Error) §eCould not delete the group §d" + newGroup + "§e.");
										}
									}
									
								}, 10L);
							}
						}else{
							p.sendMessage("§cInvalid Syntax. Do /group for help on this command!");
						}
					}
					if(args.length == 3){
						if(args[0].equalsIgnoreCase("addpermission")){
							final String group = args[1].toLowerCase();
							final String toAdd = args[2].toLowerCase();
							if(PermissionsEx.getPermissionManager().getGroupList().contains(toAdd)){
								p.sendMessage("§c(Error) §eThe group §d" + group + "§ecould not be found!");
							}else
							if(PermissionsEx.getPermissionManager().getGroup(group).getPermissions(p.getWorld().getName()).contains(args[2])){
								p.sendMessage("§c(Error) §eThe group §d" + group + " §ealready has the permission §b" + toAdd + "§e!");
							}else{
								Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex group " + group + " add " + toAdd);
								p.sendMessage("§a(Success) §eAdded permission §d" + toAdd + " §eto the group §b" + group + "§e!"); 
							}
							Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex reload");
						}
						else
						if(args[0].equalsIgnoreCase("deletepermission")){
							final String group = args[1].toLowerCase();
							final String toAdd = args[2].toLowerCase();
							if(PermissionsEx.getPermissionManager().getGroupList().contains(toAdd)){
								p.sendMessage("§c(Error) §eThe group §d" + group + "§ecould not be found!");
							}else
							if(!PermissionsEx.getPermissionManager().getGroup(group).getPermissions(p.getWorld().getName()).contains(args[2])){
								p.sendMessage("§c(Error) §eThe group §d" + group + " §edoes not have the permission §b" + toAdd + "§e!");
							}else{
								Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex group " + group + " remove " + toAdd);
								p.sendMessage("§a(Success) §eRemoved permission §d" + toAdd + " §efrom the group §b" + group + "§e!"); 
							}
							Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex reload");
						}else{
							p.sendMessage("§cInvalid Syntax. Do /group for help on this command!");
						}
					}else if(args.length > 3){
						p.sendMessage("§cInvalid Syntax. Do /group for help on this command!");
					}
				}
			}else{
				p.sendMessage("§eSurgeHCF §6» §rYou do not have the required rank to use this command, babe!");
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("rank")){
			Player p = (Player)s;
			if(p.isOp()){
				if(args.length != 2){
					p.sendMessage("§eSurgeHCF §6» §cError: §rCorrect Usage: §e/rank <player> <rank>");
				}else{
					p.sendMessage("§aAttempting to add " + args[0] + " §ato the group §c" + args[1]);
					if(PermissionsEx.getPermissionManager().getGroupNames().contains(args[1])){
						if(Bukkit.getServer().getPlayer(args[0]) != null){
							p.sendMessage("§c(Progress...) §eRemoving player from current groups...");
							Player t = Bukkit.getServer().getPlayer(args[0]);
							for(PermissionGroup i : PermissionsEx.getPermissionManager().getUser(t).getGroups()){
								PermissionsEx.getUser(t).removeGroup(i);
								p.sendMessage("§c(Progress...) §eRemoved §c" + t.getName() + " §efrom the group §9" + i.getName() + " §esuccessfully.");
							}
							p.sendMessage("§c(Progress...) §eAdding player to specified group...");
							PermissionsEx.getPermissionManager().getUser(t).addGroup(PermissionsEx.getPermissionManager().getGroup(args[1]));
							p.sendMessage("§a(Finished) §eAdded §c" + t.getName() + " §eto the group §9" + args[1] + " §esuccessfully.");
							t.sendMessage("§eSurgeHCF §6» §e" + p.getName() + " §rset your rank to §d" + args[1] + " §rand removed you from your other groups.");
							for(Player all : Bukkit.getServer().getOnlinePlayers()){
								if(all.isOp()){
									if(all.getName() != p.getName()){
										all.sendMessage("§7[§6!§7] §c" + p.getName() + " §7set §e" + args[0] + "§7's rank to §9" + args[1]);
									}
								}
							}
						}else{
							p.sendMessage("§cError: §7The player §e" + args[0] + " §7was not found!");
						}
					}else{
						p.sendMessage("§cError: §7The group §e" + args[1] + " §7was not found!");
						p.sendMessage("§cLoading list of groups...");
						p.sendMessage("§r§m------------------------------------------------------------");
						p.sendMessage("§eSurgeHCF §6» §rCurrent Groups §7(PermissionsEx)");
						p.sendMessage("§r§m------------------------------------------------------------");
						int number = 0;
						for(PermissionGroup i : PermissionsEx.getPermissionManager().getGroupList()){
							number = number + 1;
							p.sendMessage("§7[§c#" + number + "§7] §f" + i.getName());
						}
						p.sendMessage("§r§m------------------------------------------------------------");
						p.sendMessage("§eWARNING: §rThe rank is CaSe SeNsItIvE!");
						p.sendMessage("§r§m------------------------------------------------------------");
					}
				}
			}else{
				p.sendMessage("§eSurgeHCF §6» §rOops! Seems like you don't have a high enough rank!");
			}
		}
		
		
		return true;
	}

}

