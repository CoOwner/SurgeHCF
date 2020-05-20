package com.surgehcf.essentials.handlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.surgehcf.SurgeCore;
import com.surgehcf.essentials.SurgeExtra;
import com.surgehcf.essentials.configuration.ConfigurationService;

import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("deprecation")
public class CoreHandler implements Listener{

	ArrayList<String> tpCooldown = new ArrayList<String>();
	ArrayList<String> banCooldown = new ArrayList<String>();
	HashMap<Player, Integer> banCount = new HashMap<Player, Integer>();
	
	public void exec(String s){
		SurgeExtra.getInstance().getServer().dispatchCommand(SurgeExtra.getInstance().getServer().getConsoleSender(), s);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void join(PlayerJoinEvent e){
		Player p = e.getPlayer();
		p.sendMessage("§6§m--*--------------------------------------------------*--" );
		p.sendMessage("§eWelcome back to §6SurgeHCF §eMap I, §d" + p.getName() + "§e!");
		p.sendMessage("");
		p.sendMessage("§eWebsite §6» §rwww.surgehcf.com");
		p.sendMessage("§eStore §6» §rstore.surgehcf.com");
		p.sendMessage("§eTeamspeak §6» §rts.surgehcf.com");
		p.sendMessage("");
		p.sendMessage("§eSurgeHCF is on Map #1 which started on the §611th January 2017");
		p.sendMessage("§7Use the command \"§e/help§7\" §7for more information!");
		p.sendMessage("§6§m--*--------------------------------------------------*--" );
		e.setJoinMessage(null);
		if(!p.hasPlayedBefore()){
			exec("crate key " + p.getName() + " Starter 1");
			p.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 16));
			p.getInventory().addItem(new ItemStack(Material.FISHING_ROD, 1));
			p.sendMessage(ChatColor.GREEN + "Seems like you haven't played before. You have received your starter items!");
		}
	}
	@EventHandler
	public void antiAbuse(PlayerCommandPreprocessEvent e){
		String message = e.getMessage().toLowerCase();
		final Player p = e.getPlayer();
		if(message.startsWith("/f setdtr all")){
			if(!p.hasPermission("rank.staff"))return;
			e.setCancelled(true);
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "ipban " + p.getName() + " Security Ban: Attempted to set dtr of all factions.");
		}else if(message.startsWith("/tphere")){
			if(!p.hasPermission("rank.staff"))return;
			if(!tpCooldown.contains(p.getName())){
				tpCooldown.add(p.getName());
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SurgeCore.getInstance(), new Runnable(){
					public void run(){
						tpCooldown.remove(p.getName());
					}
				}, 20 * 10);
			}else{
				p.sendMessage("§eSurgeHCF §7» §rFor security reasons, you cannot teleport more than 1 player to yourself within 10 seconds.");
				p.sendMessage("§eSurgeHCF §7» §rSorry for any inconvenience caused!");
				e.setCancelled(true);
			}
		}else if(message.startsWith("/ban") || message.startsWith("/ipban")){
			if(!p.hasPermission("rank.staff"))return;
			if(!banCooldown.contains(p.getName())){
				banCooldown.add(p.getName());
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SurgeCore.getInstance(), new Runnable(){
					public void run(){
						banCooldown.remove(p.getName());
						banCount.put(p, 0);
					}
				}, 20 * 3);
			}else{
				if(banCount.get(p) == null){
					banCount.put(p, 1);
					e.setCancelled(true);
					p.sendMessage("§eSurgeHCF §7» §rFor security reasons, you cannot ban more than 1 player within 3 seconds.");
					p.sendMessage("§eSurgeHCF §7» §rSorry for any inconvenience caused!");
					return;
				}
				if(banCount.get(p) > 4){
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "ipban " + p.getName() + " Security Ban: Attempted to ban more than 5 players in 3 seconds.");
				}
				e.setCancelled(true);
				banCount.put(p, banCount.get(p) + 1);
				p.sendMessage("§eSurgeHCF §7» §rFor security reasons, you cannot ban more than 1 player within 3 seconds.");
				p.sendMessage("§eSurgeHCF §7» §rSorry for any inconvenience caused!");
			}
		}else{
			for(String s : SurgeExtra.getInstance().getBlockedCommands()){
				if(message.startsWith(s)){
					e.setCancelled(true);
					p.sendMessage("§eSurgeHCF §7» §rThat command is §e§nblocked§r due to an exploit or related issue.");
				}
			}
		}
	}


	@EventHandler
	public void br(BlockBreakEvent e){
		Player player = e.getPlayer();
		ItemStack stack = player.getItemInHand();
		if(stack.getType() == Material.DIAMOND_PICKAXE || stack.getType() == Material.IRON_PICKAXE || stack.getType() == Material.GOLD_PICKAXE || stack.getType() == Material.WOOD_PICKAXE || stack.getType() == Material.HOPPER_MINECART){
			ItemMeta sm = stack.getItemMeta();
			if(sm.hasLore()){
				sm.getLore().clear();
			}
			ArrayList<String> lore = new ArrayList<String>();
			Player target = player;
			lore.add("§6» §bDiamond Ore: §7" + target.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE));
			lore.add("§6» §aEmerald Ore: §7" + target.getStatistic(Statistic.MINE_BLOCK, Material.EMERALD_ORE));
			lore.add("§6» §7Iron Ore: §7" + target.getStatistic(Statistic.MINE_BLOCK, Material.IRON_ORE));
			lore.add("§6» §eGold Ore: §7" + target.getStatistic(Statistic.MINE_BLOCK, Material.GOLD_ORE));
			lore.add("§6» §cRedstone Ore: §7" + target.getStatistic(Statistic.MINE_BLOCK, Material.REDSTONE_ORE));
			lore.add("§6» §8Coal Ore: §7" + target.getStatistic(Statistic.MINE_BLOCK, Material.COAL_ORE));
			lore.add("§6» §9Lapis Ore: §7" + target.getStatistic(Statistic.MINE_BLOCK, Material.LAPIS_ORE));
			sm.setLore(lore);
			stack.setItemMeta(sm);
		}
	}
	@EventHandler
	public void join(PlayerPreLoginEvent e){
		ArrayList<String> blacklist = new ArrayList<String>();
		blacklist.addAll(SurgeExtra.getInstance().getConfig().getStringList("blacklisted_player_uuids"));
		Bukkit.getServer().getConsoleSender().sendMessage(blacklist.toString());
		for(String uuid : blacklist){
			String reason = "§cYou have been BLACKLISTED from the SurgeHCF network.";
			if(uuid.equalsIgnoreCase(e.getUniqueId().toString())){
				Bukkit.getServer().getConsoleSender().sendMessage("BLACKLISTED: " + uuid);
				e.disallow(PlayerPreLoginEvent.Result.KICK_OTHER, "§e§lSURGE §6» §rYou have been §cBLACKLISTED\n\n§eReason §6» §r" + reason + "\n\n§cThis ban cannot be appealed and there is little chance of being unbanned.\n\n\n\n");
			}
		}
	}
}
