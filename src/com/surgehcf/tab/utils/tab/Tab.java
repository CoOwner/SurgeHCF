package com.surgehcf.tab.utils.tab;


import lombok.Getter;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import com.surgehcf.tab.utils.TabList;
import com.surgehcf.tab.utils.tab.customevents.TabCreateEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class Tab {

    private static Set<Tab> playerTabs = new HashSet<>();
    private Player player;
    private Scoreboard scoreboard;
    private List<Entry> entries;

    public Tab(Player player) {
        this.player = player;

        entries = new ArrayList<>();

        clear();

        if (!player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
            scoreboard = player.getScoreboard();
            assemble();
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                    player.setScoreboard(scoreboard);
                    assemble();
                }
            }.runTask(TabList.getInstance().getPlugin());
        }

        playerTabs.add(this);
    }

    public void clear() {
        for (Entry entry : entries) {
            if (entry.nms() != null) {
                PacketPlayOutPlayerInfo packet = PacketPlayOutPlayerInfo.removePlayer(entry.nms());
                ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
            }
        }

        for (Player online : Bukkit.getOnlinePlayers()) {
            PacketPlayOutPlayerInfo packet = PacketPlayOutPlayerInfo.removePlayer(((CraftPlayer)online).getHandle());
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        }

        entries.clear();
    }

    private void assemble() {

        for (int i = 0; i < 60; i++) {
            int x = i % 3;
            int y = i / 3;
            new Entry(this, getNextBlank(), x, y).send();
        }

        Bukkit.getPluginManager().callEvent(new TabCreateEvent(this));
    }

    public Entry getByPosition(int x, int y) {
        for (Entry tabEntry : entries) {
            if (tabEntry.x() == x && tabEntry.y() == y) {
                return tabEntry;
            }
        }
        return null;
    }

    public String getNextBlank() {
        outer: for (String string : getAllBlanks()) {
            for (Entry tabEntry : entries) {
                if (tabEntry.text() != null && tabEntry.text().startsWith(string)) {
                    continue outer;
                }
            }
            return string;
        }
        return null;
    }

    private static List<String> getAllBlanks() {
        List<String> toReturn = new ArrayList<>();
        for (ChatColor chatColor : ChatColor.values()) {
            toReturn.add(chatColor + "" + ChatColor.RESET);
            for (ChatColor chatColor1 : ChatColor.values()) {

                if (toReturn.size() >= 60) {
                    return toReturn;
                }

                toReturn.add(chatColor + "" + chatColor1 + ChatColor.RESET);
            }
        }

        return toReturn;
    }

    public static Tab getByPlayer(Player player) {
        for (Tab playerTab : playerTabs) {
            if (playerTab.getPlayer().getName().equals(player.getName())) {
                return playerTab;
            }
        }
        return null;
    }

    public static Set<Tab> getPlayerTabs() {
        return playerTabs;
    }
}
