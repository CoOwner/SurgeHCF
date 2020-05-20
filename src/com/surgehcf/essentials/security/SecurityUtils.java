package com.surgehcf.essentials.security;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SecurityUtils {

	public static void sendLoginMessage(Player p){
		p.sendMessage("§eSurgeSecurity §6» §cPlease login using /pin <pin/password>");
		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100000, 0));
	}
	
	public static void sendOnLoginMessage(Player p){
		p.sendMessage("§eSurgeSecurity §6» §aYou have logged in!");
		p.playSound(p.getLocation(), Sound.LEVEL_UP, 4F, 4F);
		p.removePotionEffect(PotionEffectType.BLINDNESS);
	}
}
