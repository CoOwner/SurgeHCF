package com.surgehcf.core.hcf.user;

import com.google.common.collect.Maps;
import com.surgehcf.core.hcf.deathban.Deathban;

import me.milksales.util.GenericUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

public class FactionUser
implements ConfigurationSerializable {
    private final Set<UUID> factionChatSpying = new HashSet<UUID>();
    private final Set<String> shownScoreboardScores = new HashSet<String>();
    private final UUID userUUID;
    private boolean capzoneEntryAlerts;
    private boolean showClaimMap;
    private boolean showLightning = true;
    private Deathban deathban;
    private long lastFactionLeaveMillis;
    private int kills;
    private int diamondsMined;
    private int healthBrewed;
    private int deaths;
    private int creeperskilled;
    private int enderkilled;

    public FactionUser(UUID userUUID) {
        this.userUUID = userUUID;
    }

    public FactionUser(final Map<String, Object> map) {
        this.shownScoreboardScores.addAll(GenericUtils.createList(map.get("shownScoreboardScores"), (Class)String.class));
        this.factionChatSpying.addAll((Collection<? extends UUID>)GenericUtils.createList(map.get("faction-chat-spying"), String.class).stream().map(UUID::fromString).collect(Collectors.toList()));;
        this.userUUID = UUID.fromString((String)map.get("userUUID"));
        this.capzoneEntryAlerts = (Boolean)map.get("capzoneEntryAlerts");
        this.showLightning = (Boolean)map.get("showLightning");
        this.deathban = (Deathban)map.get("deathban");
        this.lastFactionLeaveMillis = Long.parseLong((String)map.get("lastFactionLeaveMillis"));
        this.diamondsMined = (Integer)map.get("diamonds");
        this.healthBrewed = (Integer)map.get("brewed");
        this.kills = (Integer)map.get("kills");
        this.deaths = (Integer)map.get("deaths");
        this.creeperskilled = (Integer)map.get("creeperskilled");
        this.enderkilled = (Integer)map.get("enderkilled");
    }

    public Map<String, Object> serialize() {
        LinkedHashMap map = Maps.newLinkedHashMap();
        map.put("shownScoreboardScores", new ArrayList<String>(this.shownScoreboardScores));
        map.put("faction-chat-spying", this.factionChatSpying.stream().map(UUID::toString).collect(Collectors.toList()));
        map.put("userUUID", this.userUUID.toString());
        map.put("diamonds", this.diamondsMined);
        map.put("brewed", this.healthBrewed);
        map.put("capzoneEntryAlerts", this.capzoneEntryAlerts);
        map.put("showClaimMap", this.showClaimMap);
        map.put("showLightning", this.showLightning);
        map.put("deathban", this.deathban);
        map.put("lastFactionLeaveMillis", Long.toString(this.lastFactionLeaveMillis));
        map.put("kills", this.kills);
        map.put("deaths", this.deaths);
        map.put("creeperskilled", this.creeperskilled);
        map.put("enderkilled", this.enderkilled);
        return map;
    }

    public boolean isCapzoneEntryAlerts() {
        return this.capzoneEntryAlerts;
    }

    public void setCapzoneEntryAlerts(boolean capzoneEntryAlerts) {
        this.capzoneEntryAlerts = capzoneEntryAlerts;
    }

    public boolean isShowClaimMap() {
        return this.showClaimMap;
    }

    public void setShowClaimMap(boolean showClaimMap) {
        this.showClaimMap = showClaimMap;
    }

    public int getKills() {
        return this.kills;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public int getCreepersKilled() {
        return this.creeperskilled;
    }

    public int getEnderKilled() {
        return this.enderkilled;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setCreepersKilled(int creeperskilled) {
        this.creeperskilled = creeperskilled;
    }

    public void setEnderKilled(int enderkilled) {
        this.enderkilled = enderkilled;
    }

    public int getDiamondsMined() {
        return this.diamondsMined;
    }

    public void setDiamondsMined(int diamondsMined) {
        this.diamondsMined = diamondsMined;
    }

    public int getHealthBrewed() {
        return this.healthBrewed;
    }

    public void setHealthBrewed(int healthBrewed) {
        this.healthBrewed = healthBrewed;
    }

    public Deathban getDeathban() {
        return this.deathban;
    }

    public void setDeathban(Deathban deathban) {
        this.deathban = deathban;
    }

    public void removeDeathban() {
        this.deathban = null;
    }

    public long getLastFactionLeaveMillis() {
        return this.lastFactionLeaveMillis;
    }

    public void setLastFactionLeaveMillis(long lastFactionLeaveMillis) {
        this.lastFactionLeaveMillis = lastFactionLeaveMillis;
    }

    public boolean isShowLightning() {
        return this.showLightning;
    }

    public void setShowLightning(boolean showLightning) {
        this.showLightning = showLightning;
    }

    public Set<UUID> getFactionChatSpying() {
        return this.factionChatSpying;
    }

    public Set<String> getShownScoreboardScores() {
        return this.shownScoreboardScores;
    }

    public UUID getUserUUID() {
        return this.userUUID;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer((UUID)this.userUUID);
    }
}