 package com.surgehcf.core.hcf.faction.argument;
 
  import me.milksales.util.chat.Text;

import java.util.List;
 import java.util.regex.Pattern;

import org.bukkit.ChatColor;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandSender;
 import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.FactionMember;
import com.surgehcf.core.hcf.faction.struct.Relation;
import com.surgehcf.core.hcf.faction.type.Faction;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;
 
 public class FactionInviteArgument extends me.milksales.util.command.CommandArgument
 {
   public FactionInviteArgument(SurgeCore plugin)
   {
     super("invite", "Invite a player to the faction.");
     this.plugin = plugin;
     this.aliases = new String[] { "inv", "invitemember", "inviteplayer" };
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + getName() + " <playerName>";
   }
   
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
     if (!(sender instanceof Player)) {
       sender.sendMessage(ChatColor.RED + "Only players can invite to a faction.");
       return true;
     }
     if (args.length < 2) {
       sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
       return true;
     }
     if (!USERNAME_REGEX.matcher(args[1]).matches()) {
       sender.sendMessage(ChatColor.RED + "'" + args[1] + "' is an invalid username.");
       return true;
     }
     Player player = (Player)sender;
     PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
     if (playerFaction == null) {
       sender.sendMessage(ChatColor.RED + "You are not in a faction.");
       return true;
     }
     if (playerFaction.getMember(player.getUniqueId()).getRole() == com.surgehcf.core.hcf.faction.struct.Role.MEMBER) {
       sender.sendMessage(ChatColor.RED + "You must a faction officer to invite members.");
       return true;
     }
     java.util.Set<String> invitedPlayerNames = playerFaction.getInvitedPlayerNames();
     String name = args[1];
     if (playerFaction.getMember(name) != null) {
       sender.sendMessage(ChatColor.RED + "'" + name + "' is already in your faction.");
       return true;
     }
     if ((!this.plugin.getEotwHandler().isEndOfTheWorld()) && (playerFaction.isRaidable())) {
       sender.sendMessage(ChatColor.RED + "You may not invite players whilst your faction is raidable.");
       return true;
     }
     if (!invitedPlayerNames.add(name)) {
       sender.sendMessage(ChatColor.RED + name + " has already been invited.");
       return true;
     }
     Player target = org.bukkit.Bukkit.getPlayer(name);
     if (target != null) {
       name = target.getName();
       Text text = new Text(sender.getName()).setColor(Relation.ENEMY.toChatColour()).append(new Text(" has invited you to join ").setColor(ChatColor.YELLOW));
       text.append(new Text(playerFaction.getName()).setColor(Relation.ENEMY.toChatColour())).append(new Text(". ").setColor(ChatColor.YELLOW));
       text.append(new Text("Click here").setColor(ChatColor.GREEN).setClick(me.milksales.util.chat.ClickAction.RUN_COMMAND, '/' + label + " accept " + playerFaction.getName()).setHoverText(ChatColor.AQUA + "Click to join " + playerFaction.getDisplayName(target) + ChatColor.AQUA + '.')).append(new Text(" to accept this invitation.").setColor(ChatColor.YELLOW));
       text.send(target);
     }
     playerFaction.broadcast(Relation.MEMBER.toChatColour() + sender.getName() + ChatColor.YELLOW + " has invited " + Relation.ENEMY.toChatColour() + name + ChatColor.YELLOW + " to the faction.");
     return true;
   }
   
   public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
     if ((args.length != 2) || (!(sender instanceof Player))) {
       return java.util.Collections.emptyList();
     }
     Player player = (Player)sender;
     PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
     if ((playerFaction == null) || (playerFaction.getMember(player.getUniqueId()).getRole() == com.surgehcf.core.hcf.faction.struct.Role.MEMBER)) {
       return java.util.Collections.emptyList();
     }
     List<String> results = new java.util.ArrayList();
     for (Player target : org.bukkit.Bukkit.getOnlinePlayers()) {
       if ((player.canSee(target)) && (!results.contains(target.getName()))) {
         Faction targetFaction = this.plugin.getFactionManager().getPlayerFaction(target.getUniqueId());
         if ((targetFaction == null) || (!targetFaction.equals(playerFaction)))
         {
 
           results.add(target.getName()); }
       }
     }
     return results;
   }
   
 
   private static final Pattern USERNAME_REGEX = Pattern.compile("^[a-zA-Z0-9_]{2,16}$");
   private final SurgeCore plugin;
 }


