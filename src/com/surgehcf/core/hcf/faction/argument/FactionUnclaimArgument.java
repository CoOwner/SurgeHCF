 package com.surgehcf.core.hcf.faction.argument;
 
  import me.milksales.util.chat.ClickAction;
 import me.milksales.util.chat.Text;
 import me.milksales.util.command.CommandArgument;

import java.util.Collection;
 import java.util.HashSet;

import net.minecraft.util.com.google.common.collect.ImmutableList;

 import org.bukkit.ChatColor;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandSender;
 import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.FactionMember;
import com.surgehcf.core.hcf.faction.claim.Claim;
import com.surgehcf.core.hcf.faction.struct.Role;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;
 
 
 
 public class FactionUnclaimArgument
   extends CommandArgument
 {
   private final SurgeCore plugin;
   
   public FactionUnclaimArgument(SurgeCore plugin)
   {
     super("unclaim", "Unclaims land from your faction.");
     this.plugin = plugin;
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + getName() + " ";
   }
   
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
     if (!(sender instanceof Player)) {
       sender.sendMessage(ChatColor.RED + "Only players can un-claim land from a faction.");
       return true;
     }
     Player player = (Player)sender;
     PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
     if (playerFaction == null) {
       sender.sendMessage(ChatColor.RED + "You are not in a faction.");
       return true;
     }
     FactionMember factionMember = playerFaction.getMember(player);
     if (factionMember.getRole() != Role.LEADER) {
       sender.sendMessage(ChatColor.RED + "You must be a faction leader to unclaim land.");
       return true;
     }
     Collection<Claim> factionClaims = playerFaction.getClaims();
     if (factionClaims.isEmpty()) {
       sender.sendMessage(ChatColor.RED + "Your faction does not own any claims.");
       return true;
     }
     if (args.length == 2) {
       if ((args[1].equalsIgnoreCase("yes")) && (stuff.contains(player.getName()))) {
         for (Claim claims : factionClaims) {
           playerFaction.removeClaim(claims, player);
         }
         factionClaims.clear();
         return true;
       }
       if ((args[1].equalsIgnoreCase("no")) && (stuff.contains(player.getName()))) {
         stuff.remove(player.getName());
         player.sendMessage(ChatColor.YELLOW + "You have been removed the unclaim-set.");
         return true;
       }
     }
     stuff.add(player.getName());
     new Text(ChatColor.YELLOW + "Do you want to unclaim " + ChatColor.BOLD + "all" + ChatColor.YELLOW + " of your land?").send(player);
     new Text(ChatColor.YELLOW + "If so, " + ChatColor.DARK_GREEN + "/f unclaim yes" + ChatColor.YELLOW + " otherwise do" + ChatColor.DARK_RED + " /f unclaim no" + ChatColor.GRAY + " (Click here to unclaim)").setHoverText(ChatColor.GOLD + "Click here to unclaim all").setClick(ClickAction.RUN_COMMAND, "/f unclaim yes").send(player);
     return true;
   }
   
 
   private static final HashSet<String> stuff = new HashSet();
   private static final ImmutableList<String> COMPLETIONS = ImmutableList.of("all");
 }

