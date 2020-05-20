package com.surgehcf.core.hcf.command;

import me.milksales.util.BukkitUtils;

import java.lang.invoke.LambdaMetafactory;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.timer.TimerManager;
import com.surgehcf.core.hcf.timer.type.TeleportTimer;

public class SpawnCommand
implements CommandExecutor,
TabCompleter {
	private static final long KIT_MAP_TELEPORT_DELAY = TimeUnit.SECONDS.toMillis(10);
	final SurgeCore plugin;

	public SpawnCommand(SurgeCore plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage((Object)ChatColor.RED + "This command is only executable by players.");
			return true;
		}
		Player player = (Player)sender;
		World world = player.getWorld();
		Location spawn = world.getSpawnLocation().clone().add(0.5, 0.5, 0.5);

		if(args.length == 0){
			player.teleport(world.getSpawnLocation());
			player.sendMessage(ChatColor.YELLOW + "Teleporting to spawn...");
		}else if (args.length > 0) {
			world = Bukkit.getWorld((String)args[0]);
			if (world == null) {
				sender.sendMessage((Object)ChatColor.RED + "There is not a world named " + args[0] + '.');
				return true;
			}
			spawn = world.getSpawnLocation().clone().add(0.5, 0.0, 0.5);
		}
		player.teleport(spawn, PlayerTeleportEvent.TeleportCause.COMMAND);
		player.sendMessage(ChatColor.YELLOW + "Teleporting to the spawn of " + world.getName() + ".");
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
	{
		return Collections.emptyList();
	}
}
