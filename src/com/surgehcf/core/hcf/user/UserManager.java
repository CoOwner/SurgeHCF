package com.surgehcf.core.hcf.user;

import me.milksales.util.Config;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Server;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.eventgame.tracker.MoreObjects;
import com.surgehcf.core.hcf.user.FactionUser;

public class UserManager
  implements Listener
{
  private final SurgeCore plugin;
  private final Map<UUID, FactionUser> users;
  private Config userConfig;
  
  public UserManager(SurgeCore plugin)
  {
    this.users = new HashMap();
    this.plugin = plugin;
    reloadUserData();
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }
  
  @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
  public void onPlayerJoin(PlayerJoinEvent event)
  {
    UUID uuid = event.getPlayer().getUniqueId();
    this.users.putIfAbsent(uuid, new FactionUser(uuid));
  }
  
  public Map<UUID, FactionUser> getUsers()
  {
    return this.users;
  }
  
  public FactionUser getUserAsync(UUID uuid)
  {
    synchronized (this.users)
    {
      FactionUser revert;
      FactionUser user = (FactionUser)this.users.putIfAbsent(uuid, revert = new FactionUser(uuid));
      return (FactionUser)MoreObjects.firstNonNull(user, revert);
    }
  }
  
  public FactionUser getUser(UUID uuid)
  {
    FactionUser revert;
    FactionUser user = (FactionUser)this.users.putIfAbsent(uuid, revert = new FactionUser(uuid));
    return (FactionUser)MoreObjects.firstNonNull(user, revert);
  }
  
  public void reloadUserData()
  {
    this.userConfig = new Config(this.plugin, "faction-users");
    Object object = this.userConfig.get("users");
    MemorySection section;
    if ((object instanceof MemorySection))
    {
      section = (MemorySection)object;
      Collection<String> keys = section.getKeys(false);
      for (String id : keys) {
        this.users.put(UUID.fromString(id), (FactionUser)this.userConfig.get(section.getCurrentPath() + '.' + id));
      }
    }
  }
  
  public void saveUserData()
  {
    Set<Map.Entry<UUID, FactionUser>> entrySet = this.users.entrySet();
    Map<String, FactionUser> saveMap = new LinkedHashMap(entrySet.size());
    for (Map.Entry<UUID, FactionUser> entry : entrySet) {
      saveMap.put(((UUID)entry.getKey()).toString(), entry.getValue());
    }
    this.userConfig.set("users", saveMap);
    this.userConfig.save();
  }
}
