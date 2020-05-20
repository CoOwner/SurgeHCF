package com.surgehcf.essentials;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.surgehcf.SurgeCore;
import com.surgehcf.essentials.combat.KnockbackListener;
import com.surgehcf.essentials.commands.BlacklistCommand;
import com.surgehcf.essentials.commands.ClaimCommand;
import com.surgehcf.essentials.commands.GamemodeCommand;
import com.surgehcf.essentials.commands.GiveawayCommand;
import com.surgehcf.essentials.commands.JudgementDayCommand;
import com.surgehcf.essentials.commands.OtherCmds;
import com.surgehcf.essentials.commands.RankHandlers;
import com.surgehcf.essentials.commands.ReportCommand;
import com.surgehcf.essentials.commands.UnblacklistCommand;
import com.surgehcf.essentials.configuration.ConfigurationService;
import com.surgehcf.essentials.groupmanager.GroupManager;
import com.surgehcf.essentials.handlers.CoreHandler;
import com.surgehcf.essentials.security.SecurityCommand;
import com.surgehcf.essentials.security.SecurityHandler;
import com.surgehcf.essentials.security.SecurityListener;
import com.surgehcf.essentials.security.SecurityUtils;
import com.surgehcf.essentials.utilities.ModeratorMode;
import com.surgehcf.tab.SurgeTab;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class SurgeExtra implements Listener{

	
	public static SurgeExtra instance;
	private static String craftBukkitVersion;
	private static ArrayList<String> blockedCommands = new ArrayList<String>();
	
	public static void enable(){
		SurgeTab.enable();
		craftBukkitVersion = SurgeCore.getInstance().getServer().getClass().getPackage().getName().split("\\.")[3];
		log("&e[SurgeHCF] &bEnabling SurgeHCF version &ev1.0 &bfor SurgeHCF!");
		log("&e[SurgeHCF] &bDeveloped by &csubbotted&b!");
		log("");
		blockedCommands.add("/me");
		blockedCommands.add("/bukkit:me");
		log("&e[SurgeHCF] &bGenerating Instance...");
		instance = new SurgeExtra();
		log("&e[SurgeHCF] &bSaving default configuration files...");
		SurgeCore.getInstance().saveDefaultConfig();
		log("&e[SurgeHCF] &bRegistering Commands & Listeners...");
		SurgeCore.getInstance().getCommand("claim").setExecutor(new ClaimCommand());
		SurgeCore.getInstance().getCommand("gamemode").setExecutor(new GamemodeCommand());
		SurgeCore.getInstance().getCommand("gms").setExecutor(new GamemodeCommand());
		SurgeCore.getInstance().getCommand("gmc").setExecutor(new GamemodeCommand());
		SurgeCore.getInstance().getCommand("rank").setExecutor(new RankHandlers());
		SurgeCore.getInstance().getCommand("group").setExecutor(new RankHandlers());
		SurgeCore.getInstance().getCommand("blacklist").setExecutor(new BlacklistCommand());
		SurgeCore.getInstance().getCommand("report").setExecutor(new ReportCommand());
		SurgeCore.getInstance().getCommand("giveaway").setExecutor(new GiveawayCommand());
		SurgeCore.getInstance().getCommand("gma").setExecutor(new GamemodeCommand());
		SurgeCore.getInstance().getCommand("unblacklist").setExecutor(new UnblacklistCommand());
		SurgeCore.getInstance().getCommand("judgementday").setExecutor(new JudgementDayCommand());
		SurgeCore.getInstance().getCommand("pin").setExecutor(new SecurityCommand());
		SurgeCore.getInstance().getCommand("craft").setExecutor(new OtherCmds());
		SurgeCore.getInstance().getCommand("nv").setExecutor(new OtherCmds());
		SurgeCore.getInstance().getCommand("near").setExecutor(new OtherCmds());
		SurgeCore.getInstance().getCommand("changepin").setExecutor(new SecurityCommand());
		//HCF.getInstance().getCommand("mod").setExecutor(new ModeratorMode());
		SurgeCore.getInstance().getServer().getPluginManager().registerEvents(new CoreHandler(), SurgeCore.getInstance());
		SurgeCore.getInstance().getServer().getPluginManager().registerEvents(new GiveawayCommand(), SurgeCore.getInstance());
		SurgeCore.getInstance().getServer().getPluginManager().registerEvents(new KnockbackListener(), SurgeCore.getInstance());
		SurgeCore.getInstance().getServer().getPluginManager().registerEvents(new SecurityListener(), SurgeCore.getInstance());
		SurgeCore.getInstance().getServer().getPluginManager().registerEvents(new SurgeExtra(), SurgeCore.getInstance());
		//Bukkit.HCF.getInstance().getServer().getPluginManager().registerEvents(new ModeratorMode(),  this);
		SurgeCore.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(SurgeCore.getInstance(), new Runnable(){
			
			public void run(){
				ArrayList<String> names = new ArrayList<String>();
				for(PermissionUser p : PermissionsEx.getPermissionManager().getGroup("surge").getUsers()){
					names.add("§r" + p.getName() + "§r");
				}
				for(PermissionUser p : PermissionsEx.getPermissionManager().getGroup("vip").getUsers()){
					names.add("§r" + p.getName() + "§r");
				}
				SurgeCore.getInstance().getServer().broadcastMessage("§eSurge Users §6» §r" + names.toString().replace("[", " ").replace("]", " "));
				SurgeCore.getInstance().getServer().broadcastMessage("§7This can be purchased on our store at store.surgehcf.com");
			}
			
		}, 0L, 20 * 120);
		
		SurgeCore.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(SurgeCore.getInstance(), new Runnable(){
			// »§
			public void run(){
				
			}
			
		}, 0L, 20 * 360);
		
		SurgeCore.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(SurgeCore.getInstance(), new Runnable(){
			
			public void run(){
				for(String s : SecurityHandler.getLoggedOutPlayers()){
					if(SurgeCore.getInstance().getServer().getPlayer(s) == null)return;
					Player p = SurgeCore.getInstance().getServer().getPlayer(s);
					SecurityUtils.sendLoginMessage(p);
					p.playSound(p.getLocation(), Sound.NOTE_PLING, 4F, 4F);
				}
			}
			
		}, 0L, 20*10);
		ConfigurationService.createFile("help.txt");
		ConfigurationService.createFile("options.yml");
		ConfigurationService.getConfigFile("/options.yml").set("generated", true);
		log("&e[SurgeHCF] &bStartup Complete!");
	}
	
	public static void disable(){
		SurgeTab.disable();
		instance = null;
		SurgeCore.getInstance().saveConfig();
	}
	
	public static SurgeExtra getInstance(){
		return instance;
	}
	
	public ArrayList<String> getBlockedCommands(){
		return blockedCommands;
	}
	public ArrayList<String> getLogin(){
		return SecurityHandler.getLoggedOutPlayers();
	}
	public static void log(String s){
		SurgeCore.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', s));
	}
	
	@EventHandler
	public void motdSetter(ServerListPingEvent e){
		e.setMaxPlayers(1250);
		e.setMotd(ChatColor.translateAlternateColorCodes('&', SurgeCore.getInstance().getServer().getMotd()));
	}
	/**
	 * Getters
	 */
	

	GroupManager groupManager;
	
	public Server getServer(){
		return SurgeCore.getInstance().getServer();
	}
	
	public String getCraftBukkitVersion()
	{
	  return craftBukkitVersion;
	}
	
	public GroupManager getGroupManager(){
		return groupManager;
	}
	
	public FileConfiguration getConfig(){
		return SurgeCore.getInstance().getConfig();
	}
	
	public void saveConfig(){
		SurgeCore.getInstance().saveConfig();
	}
}
