package com.surgehcf.core.hcf.eventgame;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.eventgame.EventType;
import com.surgehcf.core.hcf.eventgame.tracker.ConquestTracker;
import com.surgehcf.core.hcf.eventgame.tracker.EventTracker;
import com.surgehcf.core.hcf.eventgame.tracker.KothTracker;

public enum EventType
{
    CONQUEST("Conquest", (EventTracker)new ConquestTracker(SurgeCore.getPlugin())), 
    KOTH("KOTH", (EventTracker)new KothTracker(SurgeCore.getPlugin()));
    
    private static final ImmutableMap<String, EventType> byDisplayName;
    private final EventTracker eventTracker;
    private final String displayName;
    
    private EventType(final String displayName, final EventTracker eventTracker) {
        this.displayName = displayName;
        this.eventTracker = eventTracker;
    }
    
    @Deprecated
    public static EventType getByDisplayName(final String name) {
        return (EventType)EventType.byDisplayName.get((Object)name.toLowerCase());
    }
    
    public EventTracker getEventTracker() {
        return this.eventTracker;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    static {
        final ImmutableMap.Builder<String, EventType> builder = (ImmutableMap.Builder<String, EventType>)new ImmutableBiMap.Builder();
        for (final EventType eventType : values()) {
            builder.put(eventType.displayName.toLowerCase(), eventType);
        }
        byDisplayName = builder.build();
    }
}