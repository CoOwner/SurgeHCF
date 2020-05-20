package com.surgehcf.core.hcf.faction.type;

import java.util.Map;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.CommandSender;

import com.surgehcf.core.hcf.CoreConfiguration;
import com.surgehcf.core.hcf.faction.type.Faction;

public class WarzoneFaction
  extends Faction
{
  public WarzoneFaction()
  {
    super("Warzone");
  }
  
  public WarzoneFaction(Map<String, Object> map)
  {
    super(map);
  }
  
  public String getDisplayName(CommandSender sender)
  {
    return ChatColor.DARK_RED + getName();
  }
}
