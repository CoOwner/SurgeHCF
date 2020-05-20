package com.surgehcf.core.hcf.faction.type;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.surgehcf.core.hcf.CoreConfiguration;
import com.surgehcf.core.hcf.faction.claim.Claim;
import com.surgehcf.core.hcf.faction.type.ClaimableFaction;

public class SpawnFaction
  extends ClaimableFaction
  implements ConfigurationSerializable
{
  public SpawnFaction()
  {
    super("Spawn");
    this.safezone = true;
  }
  
  public SpawnFaction(Map<String, Object> map)
  {
    super(map);
  }
  
  public boolean isDeathban()
  {
    return false;
  }
}
