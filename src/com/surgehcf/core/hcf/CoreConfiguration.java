package com.surgehcf.core.hcf;

import com.google.common.collect.ImmutableList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionType;

@Deprecated
public final class CoreConfiguration
{
  public static final TimeZone SERVER_TIME_ZONE;
  public static final int WARZONE_RADIUS = 750;
  public static final int SPAWN_BUFFER = 175;
  
  public static String C(String msg)
  {
    return ChatColor.translateAlternateColorCodes('&', msg);
  }
  
  public static String KIT_MAP_NAME = null;
  public static final boolean KIT_MAP = false;
  public static final List<String> DISALLOWED_FACTION_NAMES;
  public static final Map<Enchantment, Integer> ENCHANTMENT_LIMITS;
  public static final Map<PotionType, Integer> POTION_LIMITS;
  public static final Map<World.Environment, Integer> BORDER_SIZES;
  public static final Map<World.Environment, Double> SPAWN_RADIUS_MAP;
  public static final int FACTION_PLAYER_LIMIT = 10;
  public static final ChatColor TEAMMATE_COLOUR;
  public static final ChatColor ALLY_COLOUR;
  
  static
  {
    SERVER_TIME_ZONE = TimeZone.getTimeZone("EST");
    DISALLOWED_FACTION_NAMES = (List<String>) ImmutableList.of("kohieotw", "kohisotw", "hcteams", "hcteamseotw", "hcteamssotw", "exploitsquad", "staff", "mod", "owner", "cunt", "kys", "nigger", "putty", "fuck", "pussy", "dev", "admin", "ipvp", "para", "etb", "purgepots");
    ENCHANTMENT_LIMITS = new HashMap();
    POTION_LIMITS = new EnumMap(PotionType.class);
    BORDER_SIZES = new EnumMap(World.Environment.class);
    SPAWN_RADIUS_MAP = new EnumMap(World.Environment.class);
    
    POTION_LIMITS.put(PotionType.INSTANT_DAMAGE, Integer.valueOf(0));
    POTION_LIMITS.put(PotionType.REGEN, Integer.valueOf(0));
    POTION_LIMITS.put(PotionType.STRENGTH, Integer.valueOf(0));
    POTION_LIMITS.put(PotionType.WEAKNESS, Integer.valueOf(0));
    POTION_LIMITS.put(PotionType.SLOWNESS, Integer.valueOf(1));
    POTION_LIMITS.put(PotionType.INVISIBILITY, Integer.valueOf(1));
    POTION_LIMITS.put(PotionType.POISON, Integer.valueOf(1));
    ENCHANTMENT_LIMITS.put(Enchantment.PROTECTION_ENVIRONMENTAL, Integer.valueOf(1));
    ENCHANTMENT_LIMITS.put(Enchantment.DAMAGE_ALL, Integer.valueOf(1));
    ENCHANTMENT_LIMITS.put(Enchantment.ARROW_KNOCKBACK, Integer.valueOf(0));
    ENCHANTMENT_LIMITS.put(Enchantment.KNOCKBACK, Integer.valueOf(0));
    ENCHANTMENT_LIMITS.put(Enchantment.FIRE_ASPECT, Integer.valueOf(0));
    ENCHANTMENT_LIMITS.put(Enchantment.THORNS, Integer.valueOf(0));
    ENCHANTMENT_LIMITS.put(Enchantment.ARROW_FIRE, Integer.valueOf(1));
    ENCHANTMENT_LIMITS.put(Enchantment.ARROW_DAMAGE, Integer.valueOf(4));
    BORDER_SIZES.put(World.Environment.NORMAL, Integer.valueOf(3000));
    BORDER_SIZES.put(World.Environment.NETHER, Integer.valueOf(2500));
    BORDER_SIZES.put(World.Environment.THE_END, Integer.valueOf(2500));
    SPAWN_RADIUS_MAP.put(World.Environment.NORMAL, Double.valueOf(80.0D));
    SPAWN_RADIUS_MAP.put(World.Environment.NETHER, Double.valueOf(22.5D));
    SPAWN_RADIUS_MAP.put(World.Environment.THE_END, Double.valueOf(0.0D));
    DEFAULT_DEATHBAN_DURATION = TimeUnit.HOURS.toMillis(2L);
    TEAMMATE_COLOUR = ChatColor.GREEN;
    ALLY_COLOUR = ChatColor.AQUA;
    ARCHER_COLOUR = ChatColor.RED;
  }
  
  public static final ChatColor ENEMY_COLOUR = ChatColor.YELLOW;
  public static final ChatColor ARCHER_COLOUR;
  public static final ChatColor SAFEZONE_COLOUR = ChatColor.AQUA;
  public static final ChatColor ROAD_COLOUR = ChatColor.GOLD;
  public static final ChatColor WARZONE_COLOUR = ChatColor.DARK_RED;
  public static final ChatColor WILDERNESS_COLOUR = ChatColor.DARK_GREEN;
  public static final int MAX_ALLIES_PER_FACTION = 1;
  public static final long DTR_MILLIS_BETWEEN_UPDATES = TimeUnit.SECONDS.toMillis(45L);
  public static final String DTR_WORDS_BETWEEN_UPDATES = DurationFormatUtils.formatDurationWords(DTR_MILLIS_BETWEEN_UPDATES, true, true);
  public static long DEFAULT_DEATHBAN_DURATION;
  public static boolean CRATE_BROADCASTS = false;
}
