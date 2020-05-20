package com.surgehcf.core.hcf.faction.type;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.surgehcf.core.hcf.faction.claim.Claim;
import com.surgehcf.core.hcf.faction.type.ClaimableFaction;

public class EndPortalFaction
  extends ClaimableFaction
  implements ConfigurationSerializable
{
  public EndPortalFaction()
  {
    super("EndPortal");
    World overworld = Bukkit.getWorld("world");
    int maxHeight = overworld.getMaxHeight();
    int min = 985;
    int max = 1015;
    addClaim(new Claim(this, new Location(overworld, 985.0D, 0.0D, 985.0D), new Location(overworld, 1015.0D, maxHeight, 1015.0D)), null);
    addClaim(new Claim(this, new Location(overworld, -1015.0D, maxHeight, -1015.0D), new Location(overworld, -985.0D, 0.0D, -985.0D)), null);
    addClaim(new Claim(this, new Location(overworld, -1015.0D, 0.0D, 985.0D), new Location(overworld, -985.0D, maxHeight, 1015.0D)), null);
    addClaim(new Claim(this, new Location(overworld, 985.0D, 0.0D, -1015.0D), new Location(overworld, 1015.0D, maxHeight, -985.0D)), null);
    this.safezone = false;
  }
  
  public EndPortalFaction(Map<String, Object> map)
  {
    super(map);
  }
  
  public String getDisplayName(CommandSender sender)
  {
    return ChatColor.DARK_AQUA + getName().replace("EndPortal", "End Portal");
  }
  
  public boolean isDeathban()
  {
    return true;
  }
}
