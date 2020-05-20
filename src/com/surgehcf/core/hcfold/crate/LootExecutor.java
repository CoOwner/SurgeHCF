package com.surgehcf.core.hcfold.crate;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcfold.crate.argument.LootBankArgument;
import com.surgehcf.core.hcfold.crate.argument.LootBroadcastsArgument;
import com.surgehcf.core.hcfold.crate.argument.LootDepositArgument;
import com.surgehcf.core.hcfold.crate.argument.LootGiveArgument;
import com.surgehcf.core.hcfold.crate.argument.LootListArgument;
import com.surgehcf.core.hcfold.crate.argument.LootWithdrawArgument;

import me.milksales.util.command.ArgumentExecutor;

public class LootExecutor
  extends ArgumentExecutor
{
  public LootExecutor(SurgeCore plugin)
  {
    super("loot");
    addArgument(new LootBankArgument(plugin));
    addArgument(new LootBroadcastsArgument());
    addArgument(new LootDepositArgument(plugin));
    addArgument(new LootGiveArgument(plugin));
    addArgument(new LootListArgument(plugin));
    addArgument(new LootWithdrawArgument(plugin));
  }
}
