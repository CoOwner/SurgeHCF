package com.surgehcf.core.hcf.faction;

import me.milksales.util.command.ArgumentExecutor;
import me.milksales.util.command.CommandArgument;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.faction.argument.FactionAcceptArgument;
import com.surgehcf.core.hcf.faction.argument.FactionAllyArgument;
import com.surgehcf.core.hcf.faction.argument.FactionChatArgument;
import com.surgehcf.core.hcf.faction.argument.FactionClaimArgument;
import com.surgehcf.core.hcf.faction.argument.FactionClaimChunkArgument;
import com.surgehcf.core.hcf.faction.argument.FactionClaimsArgument;
import com.surgehcf.core.hcf.faction.argument.FactionCreateArgument;
import com.surgehcf.core.hcf.faction.argument.FactionDemoteArgument;
import com.surgehcf.core.hcf.faction.argument.FactionDepositArgument;
import com.surgehcf.core.hcf.faction.argument.FactionDisbandArgument;
import com.surgehcf.core.hcf.faction.argument.FactionHelpArgument;
import com.surgehcf.core.hcf.faction.argument.FactionHomeArgument;
import com.surgehcf.core.hcf.faction.argument.FactionInviteArgument;
import com.surgehcf.core.hcf.faction.argument.FactionInvitesArgument;
import com.surgehcf.core.hcf.faction.argument.FactionKickArgument;
import com.surgehcf.core.hcf.faction.argument.FactionLeaderArgument;
import com.surgehcf.core.hcf.faction.argument.FactionLeaveArgument;
import com.surgehcf.core.hcf.faction.argument.FactionListArgument;
import com.surgehcf.core.hcf.faction.argument.FactionMapArgument;
import com.surgehcf.core.hcf.faction.argument.FactionMessageArgument;
import com.surgehcf.core.hcf.faction.argument.FactionOpenArgument;
import com.surgehcf.core.hcf.faction.argument.FactionPromoteArgument;
import com.surgehcf.core.hcf.faction.argument.FactionRenameArgument;
import com.surgehcf.core.hcf.faction.argument.FactionSetHomeArgument;
import com.surgehcf.core.hcf.faction.argument.FactionShowArgument;
import com.surgehcf.core.hcf.faction.argument.FactionStatsArgument;
import com.surgehcf.core.hcf.faction.argument.FactionStuckArgument;
import com.surgehcf.core.hcf.faction.argument.FactionUnallyArgument;
import com.surgehcf.core.hcf.faction.argument.FactionUnclaimArgument;
import com.surgehcf.core.hcf.faction.argument.FactionUninviteArgument;
import com.surgehcf.core.hcf.faction.argument.FactionWithdrawArgument;
import com.surgehcf.core.hcf.faction.argument.staff.FactionChatSpyArgument;
import com.surgehcf.core.hcf.faction.argument.staff.FactionClaimForArgument;
import com.surgehcf.core.hcf.faction.argument.staff.FactionClearClaimsArgument;
import com.surgehcf.core.hcf.faction.argument.staff.FactionForceJoinArgument;
import com.surgehcf.core.hcf.faction.argument.staff.FactionForceKickArgument;
import com.surgehcf.core.hcf.faction.argument.staff.FactionForceLeaderArgument;
import com.surgehcf.core.hcf.faction.argument.staff.FactionForcePromoteArgument;
import com.surgehcf.core.hcf.faction.argument.staff.FactionRemoveArgument;
import com.surgehcf.core.hcf.faction.argument.staff.FactionSetDeathbanMultiplierArgument;
import com.surgehcf.core.hcf.faction.argument.staff.FactionSetDtrArgument;
import com.surgehcf.core.hcf.faction.argument.staff.FactionSetDtrRegenArgument;

public class FactionExecutor
  extends ArgumentExecutor
{
  private final CommandArgument helpArgument;
  
  public FactionExecutor(SurgeCore plugin)
  {
    super("faction");
    addArgument(new FactionAcceptArgument(plugin));
    addArgument(new FactionAllyArgument(plugin));
    addArgument(new FactionChatArgument(plugin));
    addArgument(new FactionChatSpyArgument(plugin));
    addArgument(new FactionClaimArgument(plugin));
    addArgument(new FactionClaimChunkArgument(plugin));
    addArgument(new FactionClaimForArgument(plugin));
    addArgument(new FactionClaimsArgument(plugin));
    addArgument(new FactionClearClaimsArgument(plugin));
    addArgument(new FactionCreateArgument(plugin));
    addArgument(new FactionDemoteArgument(plugin));
    addArgument(new FactionDepositArgument(plugin));
    addArgument(new FactionDisbandArgument(plugin));
    addArgument(new FactionSetDtrRegenArgument(plugin));
    addArgument(new FactionForceJoinArgument(plugin));
    addArgument(new FactionForceKickArgument(plugin));
    addArgument(new FactionForceLeaderArgument(plugin));
    addArgument(new FactionForcePromoteArgument(plugin));
    addArgument(this.helpArgument = new FactionHelpArgument(this));
    addArgument(new FactionHomeArgument(this, plugin));
    addArgument(new FactionInviteArgument(plugin));
    addArgument(new FactionInvitesArgument(plugin));
    addArgument(new FactionKickArgument(plugin));
    addArgument(new FactionLeaderArgument(plugin));
    addArgument(new FactionLeaveArgument(plugin));
    addArgument(new FactionListArgument(plugin));
    addArgument(new FactionMapArgument(plugin));
    addArgument(new FactionMessageArgument(plugin));
    addArgument(new FactionOpenArgument(plugin));
    addArgument(new FactionRemoveArgument(plugin));
    addArgument(new FactionRenameArgument(plugin));
    addArgument(new FactionPromoteArgument(plugin));
    addArgument(new FactionSetDtrArgument(plugin));
    addArgument(new FactionSetDeathbanMultiplierArgument(plugin));
    addArgument(new FactionSetHomeArgument(plugin));
    addArgument(new FactionShowArgument(plugin));
    addArgument(new FactionStuckArgument(plugin));
    addArgument(new FactionStatsArgument(plugin));
    addArgument(new FactionUnclaimArgument(plugin));
    addArgument(new FactionUnallyArgument(plugin));
    addArgument(new FactionUninviteArgument(plugin));
    addArgument(new FactionWithdrawArgument(plugin));
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    if (args.length < 1)
    {
      this.helpArgument.onCommand(sender, command, label, args);
      return true;
    }
    CommandArgument argument = getArgument(args[0]);
    if (argument != null)
    {
      String permission = argument.getPermission();
      if ((permission == null) || (sender.hasPermission(permission)))
      {
        argument.onCommand(sender, command, label, args);
        return true;
      }
    }
    this.helpArgument.onCommand(sender, command, label, args);
    return true;
  }
}
