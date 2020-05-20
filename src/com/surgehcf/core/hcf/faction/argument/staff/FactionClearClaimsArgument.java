 package com.surgehcf.core.hcf.faction.argument.staff;
 
 import org.bukkit.ChatColor;
 import org.bukkit.command.CommandSender;
 import org.bukkit.conversations.Conversable;
 import org.bukkit.conversations.ConversationContext;
 import org.bukkit.conversations.ConversationFactory;
 import org.bukkit.conversations.Prompt;
 import org.bukkit.entity.Player;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.type.ClaimableFaction;
 
 public class FactionClearClaimsArgument extends me.milksales.util.command.CommandArgument
 {
   private final ConversationFactory factory;
   private final SurgeCore plugin;
   
   public FactionClearClaimsArgument(SurgeCore plugin)
   {
     super("clearclaims", "Clears the claims of a faction.");
     this.plugin = plugin;
     this.permission = ("hcf.command.faction.argument." + getName());
     this.factory = new ConversationFactory(plugin).withFirstPrompt(new ClaimClearAllPrompt(plugin)).withEscapeSequence("/no").withTimeout(10).withModality(false).withLocalEcho(true);
   }
   
   public String getUsage(String label) {
     return '/' + label + ' ' + getName() + " <playerName|factionName|all>";
   }
   
   public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
     if (args.length < 2) {
       sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
       return true;
     }
     if (args[1].equalsIgnoreCase("all")) {
       if (!(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
         sender.sendMessage(ChatColor.RED + "This command can be only executed from console.");
         return true;
       }
       Conversable conversable = (Conversable)sender;
       conversable.beginConversation(this.factory.buildConversation(conversable));
       return true;
     }
     
     com.surgehcf.core.hcf.faction.type.Faction faction = this.plugin.getFactionManager().getContainingFaction(args[1]);
     if (faction == null) {
       sender.sendMessage(ChatColor.RED + "Faction named or containing member with IGN or UUID " + args[1] + " not found.");
       return true;
     }
     if ((faction instanceof ClaimableFaction)) {
       ClaimableFaction claimableFaction = (ClaimableFaction)faction;
       claimableFaction.removeClaims(claimableFaction.getClaims(), sender);
       if ((claimableFaction instanceof com.surgehcf.core.hcf.faction.type.PlayerFaction)) {
         ((com.surgehcf.core.hcf.faction.type.PlayerFaction)claimableFaction).broadcast(ChatColor.GOLD.toString() + ChatColor.BOLD + "Your claims have been forcefully wiped by " + sender.getName() + '.');
       }
     }
     sender.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "Claims belonging to " + faction.getName() + " have been forcefully wiped.");
     return true;
   }
   
   public java.util.List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args)
   {
     if ((args.length != 2) || (!(sender instanceof Player))) {
       return java.util.Collections.emptyList();
     }
     if (args[1].isEmpty()) {
       return null;
     }
     Player player = (Player)sender;
     java.util.List<String> results = new java.util.ArrayList(this.plugin.getFactionManager().getFactionNameMap().keySet());
     for (Player target : org.bukkit.Bukkit.getOnlinePlayers()) {
       if ((player.canSee(target)) && (!results.contains(target.getName()))) {
         results.add(target.getName());
       }
     }
     return results;
   }
   
   private static class ClaimClearAllPrompt extends org.bukkit.conversations.StringPrompt
   {
     private final SurgeCore plugin;
     
     public ClaimClearAllPrompt(SurgeCore plugin) {
       this.plugin = plugin;
     }
     
     public String getPromptText(ConversationContext context) {
       return ChatColor.YELLOW + "Are you sure you want to do this? " + ChatColor.RED + ChatColor.BOLD + "All claims" + ChatColor.YELLOW + " will be cleared. " + "Type " + ChatColor.GREEN + "yes" + ChatColor.YELLOW + " to confirm or " + ChatColor.RED + "no" + ChatColor.YELLOW + " to deny.";
     }
     
     public Prompt acceptInput(ConversationContext context, String string) {
       String lowerCase2;
       String lowerCase = lowerCase2 = string.toLowerCase();
       switch (lowerCase2) {
       case "yes": 
         for (com.surgehcf.core.hcf.faction.type.Faction faction : this.plugin.getFactionManager().getFactions()) {
           if ((faction instanceof ClaimableFaction)) {
             ClaimableFaction claimableFaction = (ClaimableFaction)faction;
             claimableFaction.removeClaims(claimableFaction.getClaims(), org.bukkit.Bukkit.getConsoleSender());
           }
         }
         Conversable conversable = context.getForWhom();
         org.bukkit.Bukkit.broadcastMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "All claims have been cleared" + ((conversable instanceof CommandSender) ? " by " + ((CommandSender)conversable).getName() : "") + '.');
         return Prompt.END_OF_CONVERSATION;
       
       case "no": 
         context.getForWhom().sendRawMessage(ChatColor.BLUE + "Cancelled the process of clearing all faction claims.");
         return Prompt.END_OF_CONVERSATION;
       }
       
       context.getForWhom().sendRawMessage(ChatColor.RED + "Unrecognized response. Process of clearing all faction claims cancelled.");
       return Prompt.END_OF_CONVERSATION;
     }
   }
 }

