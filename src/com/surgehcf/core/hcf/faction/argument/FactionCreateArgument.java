 package com.surgehcf.core.hcf.faction.argument;
 
  import me.milksales.util.command.CommandArgument;

import org.bukkit.ChatColor;
 import org.bukkit.command.CommandSender;
 import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.CooldownTimers;
import com.surgehcf.core.hcf.faction.FactionManager;
 
 public class FactionCreateArgument extends CommandArgument
 {
   private final SurgeCore plugin;
   
   public FactionCreateArgument(SurgeCore plugin)
   {
     super("create", "Create a faction.", new String[] { "make", "define" });
     this.plugin = plugin;
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + getName() + " <factionName>";
   }
   
   public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
     if (!(sender instanceof Player)) {
       sender.sendMessage(ChatColor.RED + "This command may only be executed by players.");
       return true;
     }
     if (args.length < 2) {
       sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
       return true;
     }
     Player p = (Player)sender;
     String name = args[1];
     if (com.surgehcf.core.hcf.CoreConfiguration.DISALLOWED_FACTION_NAMES.contains(name.toLowerCase())) {
       sender.sendMessage("§cYou cannot create a faction with that name!");
       return true;
     }
     if (name.length() < 3) {
       sender.sendMessage(ChatColor.RED + "Faction names must have at least " + 3 + " characters.");
       return true;
     }
     if (name.length() > 16) {
       sender.sendMessage(ChatColor.RED + "Faction names cannot be longer than " + 16 + " characters.");
       return true;
     }
     if (!me.milksales.util.JavaUtils.isAlphanumeric(name)) {
       sender.sendMessage(ChatColor.RED + "Faction names may only be alphanumeric.");
       return true;
     }
     if (this.plugin.getFactionManager().getFaction(name) != null) {
       sender.sendMessage(ChatColor.RED + "Faction '" + name + "' already exists.");
       return true;
     }
     if (this.plugin.getFactionManager().getPlayerFaction((Player)sender) != null) {
       sender.sendMessage(ChatColor.RED + "You are already in a faction.");
       return true;
     }
     if (CooldownTimers.isOnCooldown("Faction_cooldown", p)) {
       p.sendMessage(ChatColor.RED + "You cannot create a faction for another: " + ChatColor.YELLOW + org.apache.commons.lang.time.DurationFormatUtils.formatDurationWords(CooldownTimers.getCooldownForPlayerLong("Faction_cooldown", p), true, true) + ChatColor.RED + ".");
       return true;
     }
     CooldownTimers.addCooldown("Faction_cooldown", p, 120);
     this.plugin.getFactionManager().createFaction(new com.surgehcf.core.hcf.faction.type.PlayerFaction(name), sender);
     return true;
   }
 }
