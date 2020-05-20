package com.surgehcf.core.hcf.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OresCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;
        if((cmd.getName().equalsIgnoreCase("ores")) && ((sender instanceof Player)) &&
                (args.length > 1)) {
            sender.sendMessage("§cUsage: /ores <player>");
            return true;
        }
        if(args.length == 0) {
            sender.sendMessage("§cUsage: /ores <player>");
            return true;
        }
        Player target = Bukkit.getServer().getPlayer(args[0]);
        if((args.length == 1) &&
                (target == null)) {
            player.sendMessage("§cPlayer not found");
            return true;
        }
        sender.sendMessage("§6§m---*------------------------------------------*---");
        sender.sendMessage("                     §rOres mined by: §e" + target.getDisplayName());
        sender.sendMessage("§bDiamond Ore: §f" + target.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE));
        sender.sendMessage("§aEmerald Ore: §f" + target.getStatistic(Statistic.MINE_BLOCK, Material.EMERALD_ORE));
        sender.sendMessage("§7Iron Ore: §f" + target.getStatistic(Statistic.MINE_BLOCK, Material.IRON_ORE));
        sender.sendMessage("§6Gold Ore: §f" + target.getStatistic(Statistic.MINE_BLOCK, Material.GOLD_ORE));
        sender.sendMessage("§cRedstone Ore: §f" + target.getStatistic(Statistic.MINE_BLOCK, Material.REDSTONE_ORE));
        sender.sendMessage("§8Coal Ore: §f" + target.getStatistic(Statistic.MINE_BLOCK, Material.COAL_ORE));
        sender.sendMessage("§9Lapis Ore: §f" + target.getStatistic(Statistic.MINE_BLOCK, Material.LAPIS_ORE));
        sender.sendMessage("§6§m---*------------------------------------------*---");
        return false;
    }
}