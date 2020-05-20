package com.surgehcf.core.hcfold.crate;

import java.util.LinkedHashMap;

import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

import com.google.common.collect.Sets;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.HashBasedTable;

import me.milksales.util.Config;

import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Table;
import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcfold.crate.Key;
import com.surgehcf.core.hcfold.crate.type.ConquestKey;
import com.surgehcf.core.hcfold.crate.type.KothKey;
import com.surgehcf.core.hcfold.crate.type.LootKey;
import com.surgehcf.core.hcfold.crate.type.Tier1Key;
import com.surgehcf.core.hcfold.crate.type.Tier2Key;

public class KeyManager
{
    private final KothKey kothKey;
    private final LootKey lootKey;
    private final Tier2Key tier2Key;
    private final Tier1Key tier1Key;
    private final ConquestKey conquestKey;
    private final Table<UUID, String, Integer> depositedCrateMap;
    private final Set<Key> keys;
    private final Config config;
    
    public KeyManager(final SurgeCore plugin) {
        this.depositedCrateMap = HashBasedTable.create();
        this.config = new Config((JavaPlugin)plugin, "key-data");
        this.keys = Sets.newHashSet(new Key[] { this.lootKey = new LootKey(), this.conquestKey = new ConquestKey(), this.kothKey = new KothKey(), this.tier2Key = new Tier2Key(), this.tier1Key = new Tier1Key() });
        this.reloadKeyData();
    }
    
    public Map<String, Integer> getDepositedCrateMap(final UUID uuid) {
        return (Map<String, Integer>)this.depositedCrateMap.row(uuid);
    }
    
    public Set<Key> getKeys() {
        return this.keys;
    }
    
    public ConquestKey getConquestKey() {
        return this.conquestKey;
    }
    
    public KothKey getEventKey() {
        return this.kothKey;
    }
    
    public Tier2Key getTier2Key() {
        return this.tier2Key;
    }
    
    public Tier1Key getTier1Key() {
        return this.tier1Key;
    }
    
    public LootKey getLootKey() {
        return this.lootKey;
    }
    
    public Key getKey(final String name) {
        for (final Key key : this.keys) {
            if (key.getName().equalsIgnoreCase(name)) {
                return key;
            }
        }
        return null;
    }
    
    @Deprecated
    public Key getKey(final Class<?> clazz) {
        for (final Key key : this.keys) {
            if (clazz.isAssignableFrom(key.getClass())) {
                return key;
            }
        }
        return null;
    }
    
    public Key getKey(final ItemStack stack) {
        if (stack != null && stack.hasItemMeta()) {
            for (final Key key : this.keys) {
                final ItemStack item = key.getItemStack();
                if (item.getItemMeta().getDisplayName().equals(stack.getItemMeta().getDisplayName())) {
                    return key;
                }
            }
            return null;
        }
        return null;
    }
    
    public void reloadKeyData() {
        for (final Key section : this.keys) {
            section.load(this.config);
        }
        Object object2 = this.config.get("deposited-key-map");
        if (object2 instanceof MemorySection) {
            MemorySection section2 = (MemorySection)object2;
            for (final String id : section2.getKeys(false)) {
                object2 = this.config.get(section2.getCurrentPath() + '.' + id);
                if (object2 instanceof MemorySection) {
                    section2 = (MemorySection)object2;
                    for (final String key2 : section2.getKeys(false)) {
                        this.depositedCrateMap.put(UUID.fromString(id), key2, this.config.getInt("deposited-key-map." + id + '.' + key2));
                    }
                }
            }
        }
    }
    
    public void saveKeyData() {
        for (final Key key : this.keys) {
            key.save(this.config);
        }
        final LinkedHashMap<String, Map<String, Integer>> saveMap2 = new LinkedHashMap<String, Map<String, Integer>>(this.depositedCrateMap.size());
        for (final Map.Entry<UUID, Map<String, Integer>> entry : this.depositedCrateMap.rowMap().entrySet()) {
            saveMap2.put(entry.getKey().toString(), entry.getValue());
        }
        this.config.set("deposited-key-map", (Object)saveMap2);
        this.config.save();
    }
}