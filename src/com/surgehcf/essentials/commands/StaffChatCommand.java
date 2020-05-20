package com.surgehcf.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.surgehcf.essentials.SurgeExtra;

public class StaffChatCommand implements CommandExecutor{



	//»
		@Override
		public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
			if(cmd.getName().equalsIgnoreCase("staffchat")){
				if(!SurgeExtra.getInstance().getGroupManager().getStaffChatMembers().contains(s.getName())){
					SurgeExtra.getInstance().getGroupManager().getStaffChatMembers().add(s.getName());
					s.sendMessage("§eStaff §6» §rEnabled staff chat.");
				}else{
					SurgeExtra.getInstance().getGroupManager().getStaffChatMembers().remove(s.getName());
					s.sendMessage("§eStaff §6» §rDisabled staff chat");
				}
			}
			return true;
		}

}


