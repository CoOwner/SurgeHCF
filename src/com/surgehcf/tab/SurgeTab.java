package com.surgehcf.tab;

import java.io.File;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.eventgame.EventTimer;
import com.surgehcf.core.hcf.timer.Timer;
import com.surgehcf.tab.utils.TabList;
import com.surgehcf.tab.utils.tab.Tab;
import com.surgehcf.tab.utils.tab.customevents.TabCreateEvent;

public class SurgeTab implements Listener
{
	private static SurgeTab instance;

	
	private static SurgeCore inst;
	public static SurgeTab getInstance()
	{
		if (instance == null)
		{
			instance = new SurgeTab();
		}
		return instance;
	}


	public static void enable()
	{
		inst = SurgeCore.getInstance();
		instance = new SurgeTab();

		setupFiles();

		Bukkit.getServer().getPluginManager().registerEvents(getInstance(), inst);

		new TabList(SurgeCore.getInstance());

		SurgeCore.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(inst, new Runnable(){
			
			public void run(){
				for(Player all : Bukkit.getServer().getOnlinePlayers()){
					try{
						updateTabForPlayer(all, Tab.getByPlayer(all));
					}catch(NullPointerException e){
						Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "An error occurred when creating " + all.getName() + "'s tab list.");
					}
				}
			}
			
		}, 5L, 5L);
		
		getConsoleCommandSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[SurgeTab] &rSuccessfully loaded iTabAPI version" + inst.getDescription().getVersion() + "."));
	}

	public static void disable()
	{
		instance = null;

		getConsoleCommandSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[SurgeTab] &rPlugin disabled."));
	}

	private static String getCardinalDirection(Player player) 
	{
		double rotation = (player.getLocation().getYaw() - 90) % 360;
		if (rotation < 0) 
		{
			rotation += 360.0;
		}
		if (0 <= rotation && rotation < 22.5) 
		{
			return "N";
		}
		else if (22.5 <= rotation && rotation < 67.5) 
		{
			return "NE";
		}
		else if (67.5 <= rotation && rotation < 112.5) 
		{
			return "E";
		}
		else if (112.5 <= rotation && rotation < 157.5) 
		{
			return "SE";
		}
		else if (157.5 <= rotation && rotation < 202.5) 
		{
			return "S"; 
		}
		else if (202.5 <= rotation && rotation < 247.5) 
		{
			return "SW";
		}
		else if (247.5 <= rotation && rotation < 292.5) 
		{
			return "W";
		}
		else if (292.5 <= rotation && rotation < 337.5)
		{
			return "NW";
		}
		else if (337.5 <= rotation && rotation < 360.0)
		{
			return "N";
		}
		else 
		{
			return null;
		}
	}

	private static String translate(Player player, String path)
	{
		if (path.contains("%player_kills%"))
		{
			path = path.replace("%player_kills%", String.valueOf(player.getStatistic(Statistic.PLAYER_KILLS)));
		}

		if (path.contains("%player_deaths%"))
		{
			path = path.replace("%player_deaths%", String.valueOf(player.getStatistic(Statistic.DEATHS)));  
		}

		if (path.contains("%player_location%"))
		{
			path = path.replace("%player_location%", "(" + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockZ() + ") [" + getCardinalDirection(player) + "]");
		}

		if (path.contains("%online_players%"))
		{
			path = path.replace("%online_players%", String.valueOf(Bukkit.getServer().getOnlinePlayers().length));
		}

		if (path.contains("%player_ping%"))
		{
			path = path.replace("%player_ping%", String.valueOf(((CraftPlayer) player).getHandle().ping));
		}

		if(SurgeCore.getInstance().getFactionManager().getPlayerFaction(player) != null){
			
			if(path.contains("%ftag%")){

				path = path.replace("%ftag%", SurgeCore.getInstance().getFactionManager().getPlayerFaction(player).getName());
			}
			
			if(path.contains("%fdtr%")){
				path = path.replace("%fdtr%", "" + SurgeCore.getInstance().getFactionManager().getPlayerFaction(player).getDeathsUntilRaidable());
			}
			
			if(path.contains("%fhome%")){
				if(SurgeCore.getInstance().getFactionManager().getPlayerFaction(player).getHome() != null){
					path = path.replace("%fhome%", ChatColor.WHITE.toString() + SurgeCore.getInstance().getFactionManager().getPlayerFaction(player).getHome().getBlockX() + ", " + SurgeCore.getInstance().getFactionManager().getPlayerFaction(player).getHome().getBlockY()+ ", " + SurgeCore.getInstance().getFactionManager().getPlayerFaction(player).getHome().getBlockZ());
				}else{
					path = path.replace("%fhome%", ChatColor.WHITE + "None");
				}
				
			}

			if(path.contains("%fleader%")){
				path = path.replace("%fleader%", SurgeCore.getInstance().getFactionManager().getPlayerFaction(player).getLeader().getName());
			}
			
			if(path.contains("%fbal%")){
				path = path.replace("%fbal%", "$" + SurgeCore.getInstance().getFactionManager().getPlayerFaction(player).getBalance());
			}

		}else{
			if(path.contains("%ftag%")){

				path = path.replace("%ftag%", "N/A");
			}
			
			if(path.contains("%fdtr%")){
				path = path.replace("%fdtr%", "N/A");
			}
			
			if(path.contains("%fhome%")){
				path = path.replace("%fhome%", ChatColor.WHITE + "N/A");
			}

			if(path.contains("%fleader%")){
				path = path.replace("%fleader%", "N/A");
			}
			
			if(path.contains("%fbal%")){
				path = path.replace("%fbal%", "N/A");
			}
		}
		
		if(path.contains("%diamond%")){
			path = path.replace("%diamond%", "" + player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE));
		}
		
		if(path.contains("%lapis%")){
			path = path.replace("%lapis%", "" + player.getStatistic(Statistic.MINE_BLOCK, Material.LAPIS_ORE));
		}
		
		if(path.contains("%iron%")){
			path = path.replace("%iron%", "" + player.getStatistic(Statistic.MINE_BLOCK, Material.IRON_ORE));
		}
		
		if(path.contains("%gold%")){
			path = path.replace("%gold%", "" + player.getStatistic(Statistic.MINE_BLOCK, Material.GOLD_ORE));
		}
		
		if(path.contains("%coal%")){
			path = path.replace("%coal%", "" + player.getStatistic(Statistic.MINE_BLOCK, Material.COAL_ORE));
		}
		
		if(path.contains("%redstone%")){
			path = path.replace("%redstone%", "" + player.getStatistic(Statistic.MINE_BLOCK, Material.REDSTONE_ORE));
		}
		
		if(path.contains("%emerald%")){
			path = path.replace("%emerald%", "" + player.getStatistic(Statistic.MINE_BLOCK, Material.EMERALD_ORE));
		}
		return path;
		

	}

	
	public static void updateTabForPlayer(Player player, Tab playerTab){
		playerTab.getByPosition(0, 0).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("1")))).send();        
		playerTab.getByPosition(0, 1).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("2")))).send();
		playerTab.getByPosition(0, 2).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("3")))).send();
		playerTab.getByPosition(0, 3).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("4")))).send();
		playerTab.getByPosition(0, 4).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("5")))).send();  
		playerTab.getByPosition(0, 5).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("6")))).send();
		playerTab.getByPosition(0, 6).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("7")))).send();
		playerTab.getByPosition(0, 7).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("8")))).send();
		playerTab.getByPosition(0, 8).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("9")))).send();
		playerTab.getByPosition(0, 9).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("10")))).send();
		playerTab.getByPosition(0, 10).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("11")))).send();
		playerTab.getByPosition(0, 11).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("12")))).send();
		playerTab.getByPosition(0, 12).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("13")))).send();
		playerTab.getByPosition(0, 13).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("14")))).send();
		playerTab.getByPosition(0, 14).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("15")))).send();
		playerTab.getByPosition(0, 15).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("16")))).send();
		playerTab.getByPosition(0, 16).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("17")))).send();
		playerTab.getByPosition(0, 17).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("18")))).send();
		playerTab.getByPosition(0, 18).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("19")))).send();
		playerTab.getByPosition(0, 19).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("20")))).send();        
		playerTab.getByPosition(1, 0).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("21")))).send();        
		playerTab.getByPosition(1, 1).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("22")))).send();
		playerTab.getByPosition(1, 2).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("23")))).send();
		playerTab.getByPosition(1, 3).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("24")))).send();
		playerTab.getByPosition(1, 4).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("25")))).send();  
		playerTab.getByPosition(1, 5).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("26")))).send();
		playerTab.getByPosition(1, 6).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("27")))).send();
		playerTab.getByPosition(1, 7).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("28")))).send();
		playerTab.getByPosition(1, 8).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("29")))).send();
		playerTab.getByPosition(1, 9).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("30")))).send();
		playerTab.getByPosition(1, 10).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("31")))).send();
		playerTab.getByPosition(1, 11).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("32")))).send();
		playerTab.getByPosition(1, 12).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("33")))).send();
		playerTab.getByPosition(1, 13).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("34")))).send();
		playerTab.getByPosition(1, 14).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("35")))).send();
		playerTab.getByPosition(1, 15).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("36")))).send();
		playerTab.getByPosition(1, 16).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("37")))).send();
		playerTab.getByPosition(1, 17).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("38")))).send();
		playerTab.getByPosition(1, 18).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("39")))).send();
		playerTab.getByPosition(1, 19).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("40")))).send(); 
		playerTab.getByPosition(2, 0).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("41")))).send();        
		playerTab.getByPosition(2, 1).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("42")))).send();
		playerTab.getByPosition(2, 2).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("43")))).send();
		playerTab.getByPosition(2, 3).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("44")))).send();
		playerTab.getByPosition(2, 4).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("45")))).send();  
		playerTab.getByPosition(2, 5).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("46")))).send();
		playerTab.getByPosition(2, 6).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("47")))).send();
		playerTab.getByPosition(2, 7).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("48")))).send();
		playerTab.getByPosition(2, 8).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("49")))).send();
		playerTab.getByPosition(2, 9).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("50")))).send();
		playerTab.getByPosition(2, 10).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("51")))).send();
		playerTab.getByPosition(2, 11).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("52")))).send();
		playerTab.getByPosition(2, 12).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("53")))).send();
		playerTab.getByPosition(2, 13).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("54")))).send();
		playerTab.getByPosition(2, 14).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("55")))).send();
		playerTab.getByPosition(2, 15).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("56")))).send();
		playerTab.getByPosition(2, 16).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("57")))).send();
		playerTab.getByPosition(2, 17).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("58")))).send();
		playerTab.getByPosition(2, 18).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("59")))).send();
		playerTab.getByPosition(2, 19).text(translate(player, ChatColor.translateAlternateColorCodes('&', inst.getConfig().getString("60")))).send(); 
	}
	@EventHandler
	public void onPlayerTabCreateEvent(TabCreateEvent event) 
	{
		Player player = event.getPlayer();
		Tab playerTab = event.getPlayerTab();
		try{
			updateTabForPlayer(player, playerTab);
		}catch(NullPointerException e){
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "An error occurred when creating " + event.getPlayer().getName() + "'s tab list.");
		}
	}


	public static ConsoleCommandSender getConsoleCommandSender()
	{
		return Bukkit.getServer().getConsoleSender();
	}

	public static FileConfigurationOptions getFileConfigurationOptions()
	{
		return inst.getConfig().options();
	}

	private static void setupFiles()
	{
		try
		{
			if (!inst.getDataFolder().exists()) 
			{
				inst.getDataFolder().mkdirs();
			}

			File file = new File(inst.getDataFolder(), "config.yml");
			if (!file.exists())
			{
				getFileConfigurationOptions().copyDefaults(true);
				inst.saveConfig();
				getConsoleCommandSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[SurgeTab] &rCould not find file &dconfig.yml&e, the plugin will create the file automatically."));
			}
			else
			{
				getConsoleCommandSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[SurgeTab] &rThe file has been &ddetected, &eloading configuration."));
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
}