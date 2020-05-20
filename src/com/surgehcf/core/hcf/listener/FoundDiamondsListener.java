package com.surgehcf.core.hcf.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.struct.Relation;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;
import com.surgehcf.core.hcf.user.FactionUser;
import com.surgehcf.core.hcf.user.UserManager;

public class FoundDiamondsListener
implements Listener {
    private static final String NOTIFICATION_PERMISSION = "hcf.founddiamonds.alert";
    public static final Material SEARCH_TYPE = Material.DIAMOND_ORE;
    private static final int SEARCH_RADIUS = 3;
    public final Set<String> foundLocations = new HashSet<String>();
    private final SurgeCore plugin;

    public FoundDiamondsListener(SurgeCore plugin) {
        this.plugin = plugin;
        this.foundLocations.addAll(plugin.getConfig().getStringList("registered-diamonds"));
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            if (block.getType() != SEARCH_TYPE) continue;
            this.foundLocations.add(block.getLocation().toString());
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block.getType() == SEARCH_TYPE) {
            this.foundLocations.add(block.getLocation().toString());
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event)
    {
      Player player = event.getPlayer();
      if (player.getGameMode() == GameMode.CREATIVE) {
        return;
      }
      if (player.getItemInHand().getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
        return;
      }
      Block block = event.getBlock();
      Location blockLocation = block.getLocation();
      int count;
      int y;
      if ((block.getType() == SEARCH_TYPE) && (this.foundLocations.add(blockLocation.toString())))
      {
        count = 1;
        for (int x = -5; x < 5; x++) {
          for (y = -5; y < 5; y++) {
            for (int z = -5; z < 5; z++)
            {
              Block otherBlock = blockLocation.clone().add(x, y, z).getBlock();
              if ((!otherBlock.equals(block)) && (otherBlock.getType() == SEARCH_TYPE) && (this.foundLocations.add(otherBlock.getLocation().toString()))) {
                count++;
              }
            }
          }
        }
        this.plugin.getUserManager().getUser(player.getUniqueId()).setDiamondsMined(this.plugin.getUserManager().getUser(player.getUniqueId()).getDiamondsMined() + count);
        for (Player on : Bukkit.getOnlinePlayers()) {
          if (this.plugin.getFactionManager().getPlayerFaction(player.getUniqueId()) != null)
          {
            String message = "§r[FD] " + this.plugin.getFactionManager().getPlayerFaction(player.getUniqueId()).getRelation(on).toChatColour() + player.getName() + ChatColor.AQUA+ " has found " + count + " diamond(s).";
            on.sendMessage(message);
          }
          else
          {
            String message = "§r[FD] §b" + player.getName() + " has found " + count + " diamond(s).";
            on.sendMessage(message);
          }
        }
      }
    }
    public void saveConfig() {
        this.plugin.getConfig().set("registered-diamonds", new ArrayList<String>(this.foundLocations));
        this.plugin.saveConfig();
    }
}