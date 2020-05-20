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
 
 public class CoordsCommand implements CommandExecutor, org.bukkit.command.TabCompleter
 {
	   ChatColor MAIN_COLOR;
	   ChatColor SECONDARY_COLOR;
	   ChatColor EXTRA_COLOR;
	   ChatColor VALUE_COLOR;
	   
	   public CoordsCommand()
	   {
		     this.VALUE_COLOR = ChatColor.GRAY;
	   }
	   
	public boolean onCommand(CommandSender p, Command command, String label, String[] args) {

		p.sendMessage("§6" + BukkitUtils.STRAIGHT_LINE_DEFAULT);
		p.sendMessage("§eSurgeHCF §6» §rImportant Locations §7(x, z)");
		p.sendMessage("§6" + BukkitUtils.STRAIGHT_LINE_DEFAULT);
		p.sendMessage("§eConquest §6» §r500, 500");
		p.sendMessage("§eHill (KoTH) §6» §r-500, 500");
		p.sendMessage("§eFantasy (KoTH) §6» §r500, -500");
		p.sendMessage("§eChurch (KoTH) §6» §r-500, -500");
		p.sendMessage("§6" + BukkitUtils.STRAIGHT_LINE_DEFAULT);
		p.sendMessage("§aGlowstone Mountain §6» §r128, 123 §7(Nether)");
		p.sendMessage("§aEnd Portals §6» §r1000, 1000 §7(Each Quadrant)");
		p.sendMessage("§aSpawn §6» §r0, 0 §7(Overworld & Nether)");
		p.sendMessage("§6" + BukkitUtils.STRAIGHT_LINE_DEFAULT);
		return true;
	}
	   
	   public java.util.List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		     return java.util.Collections.emptyList();
	   }
 }