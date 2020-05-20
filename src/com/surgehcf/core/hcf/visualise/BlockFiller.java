package com.surgehcf.core.hcf.visualise;

import com.google.common.collect.Iterables;
import com.surgehcf.core.hcf.visualise.VisualBlockData;

import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

abstract class BlockFiller {
    abstract VisualBlockData generate(final Player p0, final Location p1);

    ArrayList<VisualBlockData> bulkGenerate(final Player player, final Iterable<Location> locations) {
        final ArrayList<VisualBlockData> data = new ArrayList<VisualBlockData>(Iterables.size((Iterable) locations));
        for (final Location location : locations) {
            data.add(this.generate(player, location));
        }
        return data;
    }
}
