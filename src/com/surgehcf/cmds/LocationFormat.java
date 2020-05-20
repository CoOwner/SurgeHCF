package com.surgehcf.cmds;

import org.bukkit.Location;

public class LocationFormat {

	public static String formatLocation(Location l){
		String formatted = l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ();
		return formatted;
	}
}
