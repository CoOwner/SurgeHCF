package com.surgehcf.core.hcf.scoreboard;

import java.util.List;
import org.bukkit.entity.Player;

import com.surgehcf.core.hcf.scoreboard.SidebarEntry;

public abstract interface SidebarProvider
{
  public abstract String getTitle();
  
  public abstract List<SidebarEntry> getLines(Player paramPlayer);
}
