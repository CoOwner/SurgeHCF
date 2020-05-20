package com.surgehcf.core.hcf.faction.type;

import me.milksales.util.BukkitUtils;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.surgehcf.core.hcf.CoreConfiguration;
import com.surgehcf.core.hcf.faction.claim.Claim;
import com.surgehcf.core.hcf.faction.type.ClaimableFaction;
import com.surgehcf.core.hcf.faction.type.Faction;
import com.surgehcf.core.hcf.faction.type.RoadFaction;

public class RoadFaction
extends ClaimableFaction
implements ConfigurationSerializable {
    public static final int ROAD_EDGE_DIFF = 1000;
    public static final int ROAD_WIDTH_LEFT = 6;
    public static final int ROAD_WIDTH_RIGHT = 6;
    public static final int ROAD_MIN_HEIGHT = 0;
    public static final int ROAD_MAX_HEIGHT = 256;

    public RoadFaction(String name) {
        super(name);
    }

    public RoadFaction(Map<String, Object> map) {
        super(map);
    }

    @Override
    public String getDisplayName(CommandSender sender) {
        return (Object)CoreConfiguration.ENEMY_COLOUR + this.getName().replace("st", "st ").replace("th", "th ");
    }

    @Override
    public void printDetails(CommandSender sender) {
    	sender.sendMessage(ChatColor.GOLD + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage("" + ' ' + this.getDisplayName(sender));
        sender.sendMessage((Object)ChatColor.YELLOW + "  Location: " + (Object)ChatColor.GRAY + "None");
        sender.sendMessage(ChatColor.GOLD + BukkitUtils.STRAIGHT_LINE_DEFAULT);
    }

    public static class WestRoadFaction
    extends RoadFaction
    implements ConfigurationSerializable {
        public WestRoadFaction() {
            super("WestRoad");
            for (World world : Bukkit.getWorlds()) {
                World.Environment environment = world.getEnvironment();
                if (environment == World.Environment.THE_END) continue;
                int borderSize = CoreConfiguration.BORDER_SIZES.get((Object)environment);
                double offset = CoreConfiguration.SPAWN_RADIUS_MAP.get((Object)environment) + 1.0;
            }
        }

        public WestRoadFaction(Map<String, Object> map) {
            super(map);
        }
    }

    public static class SouthRoadFaction
    extends RoadFaction
    implements ConfigurationSerializable {
        public SouthRoadFaction() {
            super("SouthRoad");
            for (World world : Bukkit.getWorlds()) {
                World.Environment environment = world.getEnvironment();
                if (environment == World.Environment.THE_END) continue;
                int borderSize = CoreConfiguration.BORDER_SIZES.get((Object)environment);
                double offset = CoreConfiguration.SPAWN_RADIUS_MAP.get((Object)environment) + 1.0;
            }
        }

        public SouthRoadFaction(Map<String, Object> map) {
            super(map);
        }
    }

    public static class EastRoadFaction
    extends RoadFaction
    implements ConfigurationSerializable {
        public EastRoadFaction() {
            super("EastRoad");
            for (World world : Bukkit.getWorlds()) {
                World.Environment environment = world.getEnvironment();
                if (environment == World.Environment.THE_END) continue;
                int borderSize = CoreConfiguration.BORDER_SIZES.get((Object)environment);
                double offset = CoreConfiguration.SPAWN_RADIUS_MAP.get((Object)environment) + 1.0;
            }
        }

        public EastRoadFaction(Map<String, Object> map) {
            super(map);
        }
    }

    public static class NorthRoadFaction
    extends RoadFaction
    implements ConfigurationSerializable {
        public NorthRoadFaction() {
            super("NorthRoad");
            for (World world : Bukkit.getWorlds()) {
                World.Environment environment = world.getEnvironment();
                if (environment == World.Environment.THE_END) continue;
                int borderSize = CoreConfiguration.BORDER_SIZES.get((Object)environment);
                double offset = CoreConfiguration.SPAWN_RADIUS_MAP.get((Object)environment) + 1.0;
            }
        }

        public NorthRoadFaction(Map<String, Object> map) {
            super(map);
        }
    }

}