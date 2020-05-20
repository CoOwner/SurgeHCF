package com.surgehcf.tab.utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import com.surgehcf.tab.utils.tab.Tab;
import com.surgehcf.tab.utils.tab.customevents.TabDeleteEvent;

import java.util.HashSet;

@Getter
public class TabList implements Listener {

    private static TabList instance;
    private JavaPlugin plugin;
    private TabListOptions options;

    public TabList(JavaPlugin plugin) {
        this(plugin, TabListOptions.getDefaultOptions());
    }

    public TabList(JavaPlugin plugin, TabListOptions options) {

        if (Bukkit.getMaxPlayers() < 60) {
            throw new NumberFormatException("Player limit must be at least 60!");
        }

        instance = this;

        this.plugin = plugin;
        this.options = options;

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    checkPlayer(player);
                }
            }
        }.runTaskLaterAsynchronously(plugin, 4L);

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                checkPlayer(player);
            }
        }.runTaskLaterAsynchronously(plugin, 4L);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Tab playerTab = Tab.getByPlayer(player);
        if (playerTab != null) {

            for (Team team : new HashSet<>(playerTab.getScoreboard().getTeams())) {
                team.unregister();
            }

            Tab.getPlayerTabs().remove(playerTab);
            Bukkit.getPluginManager().callEvent(new TabDeleteEvent(playerTab));
        }
    }

    private void checkPlayer(Player player) {
        Tab playerTab = Tab.getByPlayer(player);
        if (playerTab == null) {
            long time = System.currentTimeMillis();
            new Tab(player);
            if (options.sendCreationMessage()) {
                player.sendMessage(ChatColor.GRAY + "We created your tab list in a time of " + (System.currentTimeMillis() - time) + " ms.");
            }
        } else {
            playerTab.clear();
        }
    }

    public static TabList getInstance() {
        return instance;
    }
}
