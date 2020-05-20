package com.surgehcf.core.hcf.command;

import me.milksales.util.BukkitUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand implements CommandExecutor, org.bukkit.command.TabCompleter
{
	ChatColor MAIN_COLOR;
	ChatColor SECONDARY_COLOR;
	ChatColor EXTRA_COLOR;
	ChatColor VALUE_COLOR;

	public HelpCommand()
	{
		this.VALUE_COLOR = ChatColor.GOLD;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player){
			Player p = (Player)sender;
			p.sendMessage(this.VALUE_COLOR.toString() + BukkitUtils.STRAIGHT_LINE_DEFAULT);
			p.sendMessage("§e§lSurgeHCF §6» §rHelp and Information §c[Map #1]");
			p.sendMessage(this.VALUE_COLOR.toString() + BukkitUtils.STRAIGHT_LINE_DEFAULT);
			p.sendMessage("§6Map Information:");
			p.sendMessage(" §eWarzone §6» §r1000");
			p.sendMessage(" §eEnchant Limits §6» §rProtection 1 §7/ §rSharpness 1");
			p.sendMessage(" §eMap Border §6» §rOverworld: 3000, Nether: 3000");
			p.sendMessage("§6Useful Commands:");
			p.sendMessage(" §e/report §6» §rReport a rule-breaker/cheater.");
			p.sendMessage(" §e/rules §6» §rView SurgeHCF's Official Rules & Punishments.");
			p.sendMessage(" §e/faction §6» §rFaction-Related Help");
			p.sendMessage("§6Server Information:");
			p.sendMessage(" §eTeamspeak §6» §rts.surgehcf.com");
			p.sendMessage(" §eForums §6» §rwww.surgehcf.com");
			p.sendMessage(" §eStore §6» §rstore.surgehcf.com");
			p.sendMessage(this.VALUE_COLOR.toString() + BukkitUtils.STRAIGHT_LINE_DEFAULT);
		}else{
			sender.sendMessage("§aYou must be a §lPLAYER §ato execute this command!");
		}

		return true;
	}

	   public java.util.List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		     return java.util.Collections.emptyList();
	   }
 }

