package com.surgehcf.core.hcf.faction.struct;

import java.util.Locale;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.surgehcf.core.hcf.CoreConfiguration;
import com.surgehcf.core.hcf.faction.struct.ChatChannel;

public enum ChatChannel
{
  FACTION("Faction"),  ALLIANCE("Alliance"),  PUBLIC("Public");
  
  private final String name;
  
  private ChatChannel(String name)
  {
    this.name = name;
  }
  
  public static ChatChannel parse(String id)
  {
    return parse(id, PUBLIC);
  }
  
  public static ChatChannel parse(String id, ChatChannel def)
  {
    String s;
    String lowerCase = s = id = id.toLowerCase(Locale.ENGLISH);
    switch (s)
    {
    case "f": 
    case "faction": 
    case "fc": 
    case "fac": 
    case "fact": 
      return FACTION;
    case "a": 
    case "alliance": 
    case "ally": 
    case "ac": 
      return ALLIANCE;
    case "p": 
    case "pc": 
    case "g": 
    case "gc": 
    case "global": 
    case "pub": 
    case "publi": 
    case "public": 
      return PUBLIC;
    }
    return def == null ? null : def.getRotation();
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getDisplayName()
  {
    String prefix = null;
    switch (this)
    {
    case FACTION: 
      prefix = CoreConfiguration.TEAMMATE_COLOUR.toString();
      break;
    case ALLIANCE: 
      prefix = CoreConfiguration.ALLY_COLOUR.toString();
      break;
    default: 
      prefix = CoreConfiguration.ENEMY_COLOUR.toString();
    }
    return prefix + this.name;
  }
  
  public String getShortName()
  {
    switch (this)
    {
    case FACTION: 
      return "FC";
    case ALLIANCE: 
      return "AC";
    }
    return "PC";
  }
  
  public ChatChannel getRotation()
  {
    switch (this)
    {
    case FACTION: 
      return PUBLIC;
    case PUBLIC: 
      return ALLIANCE;
    case ALLIANCE: 
      return FACTION;
    }
    return PUBLIC;
  }
  
  public String getRawFormat(Player player)
  {
    switch (this)
    {
    case FACTION: 
      return CoreConfiguration.TEAMMATE_COLOUR + "(" + getDisplayName() + CoreConfiguration.TEAMMATE_COLOUR + ") " + player.getName() + ChatColor.GRAY + ": " + ChatColor.YELLOW + "%2$s";
    case ALLIANCE: 
      return CoreConfiguration.ALLY_COLOUR + "(" + getDisplayName() + CoreConfiguration.ALLY_COLOUR + ") " + player.getName() + ChatColor.GRAY + ": " + ChatColor.YELLOW + "%2$s";
    }
    throw new IllegalArgumentException("Cannot get the raw format for public chat channel");
  }
}
