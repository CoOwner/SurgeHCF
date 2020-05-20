package com.surgehcf.core.hcf.eventgame.argument;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;

import java.util.Collection;

import com.sk89q.worldedit.bukkit.selections.Selection;
import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.eventgame.CaptureZone;
import com.surgehcf.core.hcf.eventgame.faction.CapturableFaction;
import com.surgehcf.core.hcf.eventgame.faction.ConquestFaction;
import com.surgehcf.core.hcf.eventgame.faction.KothFaction;
import com.surgehcf.core.hcf.eventgame.tracker.ConquestTracker;
import com.surgehcf.core.hcf.eventgame.tracker.KothTracker;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.claim.Claim;
import com.surgehcf.core.hcf.faction.type.Faction;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import me.milksales.util.cuboid.Cuboid;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.milksales.util.command.CommandArgument;

public class EventSetCapzoneArgument extends CommandArgument
{
    private final SurgeCore plugin;
    
    public EventSetCapzoneArgument(final SurgeCore plugin) {
        super("setcapzone", "Sets the capture zone of an event");
        this.plugin = plugin;
        this.aliases = new String[] { "setcapturezone", "setcap", "setcappoint", "setcapturepoint", "setcappoint" };
        this.permission = "hcf.command.event.argument." + this.getName();
    }
    
    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName() + " <eventName>";
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can set KOTH arena capture points");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        final WorldEditPlugin worldEdit = this.plugin.getWorldEdit();
        if (worldEdit == null) {
            sender.sendMessage(ChatColor.RED + "WorldEdit must be installed to set KOTH capture points.");
            return true;
        }
        final Selection selection = worldEdit.getSelection((Player)sender);
        if (selection == null) {
            sender.sendMessage(ChatColor.RED + "You must make a WorldEdit selection to do this.");
            return true;
        }
        if (selection.getWidth() < 2 || selection.getLength() < 2) {
            sender.sendMessage(ChatColor.RED + "Capture zones must be at least " + 2 + 'x' + 2 + '.');
            return true;
        }
        final Faction faction = this.plugin.getFactionManager().getFaction(args[1]);
        if (!(faction instanceof CapturableFaction)) {
            sender.sendMessage(ChatColor.RED + "There is not a capturable faction named '" + args[1] + "'.");
            return true;
        }
        final CapturableFaction capturableFaction = (CapturableFaction)faction;
        final Collection<Claim> claims = capturableFaction.getClaims();
        if (claims.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Capture zones can only be inside the event claim.");
            return true;
        }
        final Claim claim = new Claim(faction, selection.getMinimumPoint(), selection.getMaximumPoint());
        final World world = claim.getWorld();
        final int minimumX = claim.getMinimumX();
        final int maximumX = claim.getMaximumX();
        final int minimumZ = claim.getMinimumZ();
        final int maximumZ = claim.getMaximumZ();
        final FactionManager factionManager = this.plugin.getFactionManager();
        for (int x = minimumX; x <= maximumX; ++x) {
            for (int z = minimumZ; z <= maximumZ; ++z) {
                final Faction factionAt = factionManager.getFactionAt(world, x, z);
                if (!factionAt.equals(capturableFaction)) {
                    sender.sendMessage(ChatColor.RED + "Capture zones can only be inside the event claim.");
                    return true;
                }
            }
        }
        CaptureZone captureZone;
        if (capturableFaction instanceof ConquestFaction) {
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /" + label + ' ' + this.getName() + ' ' + faction.getName() + " <red|blue|green|yellow>");
                return true;
            }
            final ConquestFaction conquestFaction = (ConquestFaction)capturableFaction;
            final ConquestFaction.ConquestZone conquestZone = ConquestFaction.ConquestZone.getByName(args[2]);
            if (conquestZone == null) {
                sender.sendMessage(ChatColor.RED + "There is no conquest zone named '" + args[2] + "'.");
                sender.sendMessage(ChatColor.RED + "Did you mean?: " + StringUtils.join((Iterable)ConquestFaction.ConquestZone.getNames(), ", "));
                return true;
            }
            captureZone = new CaptureZone(conquestZone.getName(), conquestZone.getColor().toString(), (Cuboid)claim, ConquestTracker.DEFAULT_CAP_MILLIS);
            conquestFaction.setZone(conquestZone, captureZone);
        }
        else {
            ((KothFaction)capturableFaction).setCaptureZone(captureZone = new CaptureZone(capturableFaction.getName(), (Cuboid)claim, KothTracker.DEFAULT_CAP_MILLIS));
        }
        sender.sendMessage(ChatColor.YELLOW + "Set capture zone " + captureZone.getDisplayName() + ChatColor.YELLOW + " for faction " + faction.getName() + ChatColor.YELLOW + '.');
        return true;
    }
    
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        switch (args.length) {
            case 2: {
                return (List<String>)this.plugin.getFactionManager();
            }
            case 3: {
                final Faction faction2 = this.plugin.getFactionManager().getFaction(args[1]);
                if (faction2 instanceof ConquestFaction) {
                    final ConquestFaction.ConquestZone[] zones = ConquestFaction.ConquestZone.values();
                    final List<String> results = new ArrayList<String>(zones.length);
                    for (final ConquestFaction.ConquestZone zone : zones) {
                        results.add(zone.name());
                    }
                    return results;
                }
                return Collections.emptyList();
            }
            default: {
                return Collections.emptyList();
            }
        }
    }
}