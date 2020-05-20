package com.surgehcf.core.hcf.command;

import me.milksales.base.BasePlugin;
import me.milksales.base.PlayTimeManager;
import me.milksales.util.BukkitUtils;
import me.milksales.util.chat.ClickAction;
import me.milksales.util.chat.Text;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.deathban.Deathban;
import com.surgehcf.core.hcf.economy.EconomyManager;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.struct.Relation;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;
import com.surgehcf.core.hcf.user.FactionUser;
import com.surgehcf.core.hcf.user.UserManager;

public class PlayerStats
  implements CommandExecutor
{
  public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args)
  {
    if (!(cs instanceof Player))
    {
      cs.sendMessage(ChatColor.RED + "This command is only executable by players.");
      return true;
    }
    Player player = (Player)cs;
    if (args.length == 0)
    {
      sendInformation(player, Bukkit.getPlayer(cs.getName()));
      return true;
    }
    if (args.length != 1) {
      return false;
    }
    if (Bukkit.getPlayer(args[0]) != null)
    {
      sendInformation(player, Bukkit.getPlayer(args[0]));
      return true;
    }
    if (Bukkit.getOfflinePlayer(args[0]) == null)
    {
      player.sendMessage(ChatColor.GOLD + "Player named or with UUID '" + ChatColor.WHITE + args[0] + ChatColor.GOLD + "' not found");
      return true;
    }
    sendInformation(player, Bukkit.getOfflinePlayer(args[0]));
    return true;
  }
  
  public void sendInformation(Player player, OfflinePlayer target)
  {
    FactionUser hcf = SurgeCore.getPlugin().getUserManager().getUser(target.getUniqueId());
    player.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
    if (SurgeCore.getPlugin().getFactionManager().getPlayerFaction(target.getUniqueId()) != null)
    {
      player.sendMessage(SurgeCore.getPlugin().getFactionManager().getPlayerFaction(target.getUniqueId()).getRelation(player).toChatColour() + target.getName());
      new Text(ChatColor.YELLOW + "   Faction: " + SurgeCore.getPlugin().getFactionManager().getPlayerFaction(target.getUniqueId()).getDisplayName(player)).setHoverText(ChatColor.GRAY + "Click to view Faction").setClick(ClickAction.RUN_COMMAND, "/f who " + SurgeCore.getPlugin().getFactionManager().getPlayerFaction(target.getUniqueId()).getName()).send(player);
    }
    else
    {
      player.sendMessage(ChatColor.RED + target.getName());
    }
    player.sendMessage(ChatColor.YELLOW + "   PlayTime: " + ChatColor.GREEN + DurationFormatUtils.formatDurationWords(BasePlugin.getPlugin().getPlayTimeManager().getTotalPlayTime(target.getUniqueId()), true, true));
    if (hcf.getDiamondsMined() > 0) {
      player.sendMessage(ChatColor.YELLOW + "   Diamonds Mined: " + ChatColor.AQUA + hcf.getDiamondsMined());
    }
    if (hcf.getCreepersKilled() > 0) {
      player.sendMessage(ChatColor.YELLOW + "   Creepers Killed: " + ChatColor.AQUA + hcf.getCreepersKilled());
    }
    if (hcf.getEnderKilled() > 0) {
      player.sendMessage(ChatColor.YELLOW + "   Endermen Killed: " + ChatColor.AQUA + hcf.getEnderKilled());
    }
    if ((hcf.getDeathban() != null) && (hcf.getDeathban().getRemaining() > 0L)) {
      new Text(ChatColor.YELLOW + "   Deathbanned: " + (hcf.getDeathban().isActive() ? ChatColor.RED + DurationFormatUtils.formatDurationWords(hcf.getDeathban().getRemaining(), true, true) : new StringBuilder().append(ChatColor.RED).append("false").toString())).setHoverText(ChatColor.AQUA + "Un-Deathbanned in: " + DurationFormatUtils.formatDurationWords(hcf.getDeathban().getRemaining(), true, true)).send(player);
    }
    if (hcf.getKills() > 0) {
      player.sendMessage(ChatColor.YELLOW + "   Kills: " + ChatColor.GREEN + hcf.getKills());
    }
    if (hcf.getDeaths() > 0) {
      player.sendMessage(ChatColor.YELLOW + "   Deaths: " + ChatColor.RED + hcf.getDeaths());
    }
    player.sendMessage(ChatColor.YELLOW + "   Balance: " + ChatColor.GREEN + "$" + SurgeCore.getPlugin().getEconomyManager().getBalance(target.getUniqueId()));
    player.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
  }
}
