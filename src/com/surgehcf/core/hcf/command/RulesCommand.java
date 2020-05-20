package com.surgehcf.core.hcf.command;

import me.milksales.util.BukkitUtils;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.surgehcf.core.hcf.CoreConfiguration;

public class RulesCommand implements CommandExecutor, org.bukkit.command.TabCompleter {
	ChatColor MAIN_COLOR;
	ChatColor SECONDARY_COLOR;
	ChatColor EXTRA_COLOR;
	ChatColor VALUE_COLOR;

	public RulesCommand() {
		this.VALUE_COLOR = ChatColor.GRAY;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage(this.VALUE_COLOR + BukkitUtils.STRAIGHT_LINE_DEFAULT);
		sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "  SurgeHCF" + ChatColor.GRAY + " - "
				+ ChatColor.YELLOW + "Rules");
		sender.sendMessage(this.VALUE_COLOR + BukkitUtils.STRAIGHT_LINE_DEFAULT);
		sender.sendMessage(ChatColor.YELLOW + " 1. " + ChatColor.GREEN
				+ "No DDoS/Dox/Related Threats. §7(IP banned, forum ban, blacklist from Buycraft, no unban)");
		sender.sendMessage(ChatColor.YELLOW + " 2. " + ChatColor.GREEN
				+ "No illegal modifications to the minecraft client. §7(Dependant on illegal modifications used)");
		sender.sendMessage(ChatColor.YELLOW + " 3. " + ChatColor.GREEN
				+ "No personal attacks/harrassment. §7(Dependant on severity)");
		sender.sendMessage(
				ChatColor.YELLOW + " 4. " + ChatColor.GREEN + "No spamming/flooding chat. §7(5 minute mute)");
		sender.sendMessage(ChatColor.YELLOW + " 5. " + ChatColor.GREEN
				+ "No exploiting bugs/glitches. §7(Dependant on the glitch exploited)");
		sender.sendMessage(
				ChatColor.YELLOW + " 6. " + ChatColor.GREEN + "Staff Disrespect is not allowed. §7(5 minute mute)");
		sender.sendMessage(ChatColor.YELLOW + " 7. " + ChatColor.GREEN + "Griefing is not allowed. §7(2 day ban)");
		sender.sendMessage(
				ChatColor.YELLOW + " 8. " + ChatColor.GREEN + "Insiding is not allowed. §7(2 day ban + insider rank)");
		sender.sendMessage(
				ChatColor.YELLOW + " 9. " + ChatColor.GREEN + "Kick and Killing is not allowed. §7(1 day ban)");
		sender.sendMessage(ChatColor.YELLOW + " 10. " + ChatColor.GREEN + "Block Glitching is not allowed. §7(Warn)");
		sender.sendMessage("§e11. §aBan evasion is not allowed. §7(IP-Ban)");
		sender.sendMessage(
				"§e12. §aServer/Plugins/Related disrespect/flaming is not allowed. §7(Mute, mute length dependant on severity)");
		sender.sendMessage(this.VALUE_COLOR + BukkitUtils.STRAIGHT_LINE_DEFAULT);
		return true;
	}

	public java.util.List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return java.util.Collections.emptyList();
	}
}