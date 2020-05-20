package com.surgehcf.core.hcfold.crate.argument;

import me.milksales.util.command.CommandArgument;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcfold.crate.KeyManager;

public class LootBankArgument
  extends CommandArgument
{
  private final SurgeCore plugin;
  
  public LootBankArgument(SurgeCore plugin)
  {
    super("bank", "Check the loot keys in your bank account");
    this.plugin = plugin;
    this.permission = ("hcf.command.loot.argument." + getName());
  }
  
  public String getUsage(String label)
  {
    return '/' + label + ' ' + getName();
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    if (!(sender instanceof Player))
    {
      sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
      return true;
    }
    Player player = (Player)sender;
    UUID uuid = player.getUniqueId();
    Map<String, Integer> crateKeyMap = this.plugin.getKeyManager().getDepositedCrateMap(uuid);
    if (crateKeyMap.isEmpty())
    {
      sender.sendMessage(ChatColor.RED + "There are no keys in your bank account.");
      return true;
    }
    for (Map.Entry<String, Integer> entry : crateKeyMap.entrySet()) {
      sender.sendMessage(ChatColor.YELLOW + (String)entry.getKey() + ": " + ChatColor.GOLD + entry.getValue());
    }
    return true;
  }
}
