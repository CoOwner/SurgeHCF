package com.surgehcf.core.hcf.faction.argument;

import me.milksales.util.JavaUtils;
import me.milksales.util.command.CommandArgument;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import net.minecraft.util.com.google.common.collect.ImmutableList;
import net.minecraft.util.com.google.common.primitives.Ints;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.economy.EconomyManager;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.FactionMember;
import com.surgehcf.core.hcf.faction.struct.Relation;
import com.surgehcf.core.hcf.faction.struct.Role;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;

public class FactionDepositArgument
  extends CommandArgument
{
  private static final ImmutableList<String> COMPLETIONS = ImmutableList.of("all");
  private final SurgeCore plugin;
  
  public FactionDepositArgument(SurgeCore plugin)
  {
    super("deposit", "Deposits money to the faction balance.", new String[] { "d" });
    this.plugin = plugin;
  }
  
  public String getUsage(String label)
  {
    return '/' + label + ' ' + getName() + " <all|amount>";
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    if (!(sender instanceof Player))
    {
      sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
      return true;
    }
    if (args.length < 2)
    {
      sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
      return true;
    }
    Player player = (Player)sender;
    PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
    if (playerFaction == null)
    {
      sender.sendMessage(ChatColor.RED + "You are not in a faction.");
      return true;
    }
    UUID uuid = player.getUniqueId();
    int playerBalance = this.plugin.getEconomyManager().getBalance(uuid);
    Integer amount;
    if (args[1].equalsIgnoreCase("all"))
    {
      amount = Integer.valueOf(playerBalance);
    }
    else if ((amount = Ints.tryParse(args[1])) == null)
    {
      sender.sendMessage(ChatColor.RED + "'" + args[1] + "' is not a valid number.");
      return true;
    }
    if (amount.intValue() <= 0)
    {
      sender.sendMessage(ChatColor.RED + "Amount must be positive.");
      return true;
    }
    if (playerBalance < amount.intValue())
    {
      sender.sendMessage(ChatColor.RED + "You need at least " + '$' + JavaUtils.format(amount) + " to do this, you only have " + '$' + JavaUtils.format(Integer.valueOf(playerBalance)) + '.');
      return true;
    }
    this.plugin.getEconomyManager().subtractBalance(uuid, amount.intValue());
    playerFaction.setBalance(playerFaction.getBalance() + amount.intValue());
    playerFaction.broadcast(Relation.MEMBER.toChatColour() + playerFaction.getMember(player).getRole().getAstrix() + sender.getName() + ChatColor.YELLOW + " has deposited " + ChatColor.GREEN + '$' + JavaUtils.format(amount) + ChatColor.YELLOW + " into the faction balance.");
    return true;
  }
  
  public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
  {
    return args.length == 2 ? COMPLETIONS : Collections.emptyList();
  }
}
