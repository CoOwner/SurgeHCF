package com.surgehcf.tab.utils.tab.customevents;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.surgehcf.tab.utils.tab.Tab;

@Getter
public class TabCreateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Tab playerTab;
    private Player player;

    public TabCreateEvent(Tab playerTab) {
        this.player = playerTab.getPlayer();
        this.playerTab = playerTab;
    }
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
