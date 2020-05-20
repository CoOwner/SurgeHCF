package com.surgehcf.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class OtherCmds  implements CommandExecutor{



	//»
		@Override
		public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
			
			if(cmd.getName().equalsIgnoreCase("nv")){
				Player p = (Player)s;
				if(p.hasPermission("perk.nightvision")){
					if(!p.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
						p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
						p.sendMessage("§eSurgeHCF §6» §rToggled night vision perk!");
					}else{
						p.removePotionEffect(PotionEffectType.NIGHT_VISION);
						p.sendMessage("§eSurgeHCF §6» §rToggled night vision perk!");
					}
				}else{
					p.sendMessage("§eYou may purchase the §5Obsidian §erank or higher to have access to this command!");
					p.sendMessage("§eClick this link: §7http://store.surgehcf.com/");
				}
				}

				if(cmd.getName().equalsIgnoreCase("craft")){
				Player p = (Player)s;
				if(p.hasPermission("perk.craft")){
					p.openWorkbench(p.getLocation(), true);
					p.sendMessage("§eSurgeHCF §6» §rOpened your workbench.");
				}else{
					p.sendMessage("§eYou may purchase the §bDiamond §erank or higher to have access to this command!");
					p.sendMessage("§eClick this link: §7http://store.surgehcf.com/");
				}
				}


				if(cmd.getName().equalsIgnoreCase("near")){
				Player p = (Player)s;
				if(p.hasPermission("perk.near")){
					int count = 0;
					for(Entity i : p.getNearbyEntities(25, Math.max(25, 256), 25)){
						if(i.getType() == EntityType.PLAYER){
							count ++;
						}
					}
					p.sendMessage("§eSurgeHCF §6» §rPlayers near you §e(25 block radius)§r: §c" + count);
				}else{
					p.sendMessage("§eYou may purchase the §5Obsidian §erank or higher to have access to this command!");
					p.sendMessage("§eClick this link: §7http://store.surgehcf.com/");
				}
				}
			
			return true;
		}

}
/*
if(cmd.getName().equalsIgnoreCase("nv")){
Player p = (Player)s;
if(p.hasPermission("perk.nightvision")){
	if(!p.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
		p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
		p.sendMessage("§eToggled night vision perk!");
	}else{
		p.removePotionEffect(PotionEffectType.NIGHT_VISION);
		p.sendMessage("§eToggled night vision perk!");
	}
}else{
	p.sendMessage("§eYou may purchase the §5Juggernaut §erank or higher to have access to this command!");
	p.sendMessage("§eClick this link: §7http://roguehcf.buycraft.net/");
}
}

if(cmd.getName().equalsIgnoreCase("craft")){
Player p = (Player)s;
if(p.hasPermission("perk.craft")){
	p.openWorkbench(p.getLocation(), true);
	p.sendMessage("§eOpened your workbench.");
}else{
	p.sendMessage("§eYou may purchase the §6Gold §erank or higher to have access to this command!");
	p.sendMessage("§eClick this link: §7http://roguehcf.buycraft.net/");
}
}


if(cmd.getName().equalsIgnoreCase("near")){
Player p = (Player)s;
if(p.hasPermission("rank.gold")){
	int count = 0;
	for(Entity i : p.getNearbyEntities(25, Math.max(25, 256), 25)){
		if(i.getType() == EntityType.PLAYER){
			count ++;
		}
	}
	p.sendMessage("§ePlayers near you §7(25 block radius)§e: §r" + count);
}else{
	p.sendMessage("§eYou may purchase the §6Gold §erank or higher to have access to this command!");
	p.sendMessage("§eClick this link: §7http://roguehcf.buycraft.net/");
}
}*/