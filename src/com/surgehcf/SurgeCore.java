package com.surgehcf;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.surgehcf.SurgeCore;
import com.surgehcf.cmds.FlightCommand;
import com.surgehcf.cmds.ModeratorMode;
import com.surgehcf.cmds.StaffChat;
import com.surgehcf.cmds.TeamspeakCommand;
import com.surgehcf.cmds.VanishMode;
import com.surgehcf.core.hcf.CooldownTimers;
import com.surgehcf.core.hcf.DateFormatter;
import com.surgehcf.core.hcf.command.BardCommand;
import com.surgehcf.core.hcf.command.CobbleCommand;
import com.surgehcf.core.hcf.command.CoordsCommand;
import com.surgehcf.core.hcf.command.CrowbarCommand;
import com.surgehcf.core.hcf.command.ForeheadCommand;
import com.surgehcf.core.hcf.command.HelpCommand;
import com.surgehcf.core.hcf.command.LogoutCommand;
import com.surgehcf.core.hcf.command.OresCommand;
import com.surgehcf.core.hcf.command.PlayerStats;
import com.surgehcf.core.hcf.command.PvpTimerCommand;
import com.surgehcf.core.hcf.command.RefundCommand;
import com.surgehcf.core.hcf.command.SurgeCommand;
import com.surgehcf.core.hcf.command.RulesCommand;
import com.surgehcf.core.hcf.command.SOTWCommand;
import com.surgehcf.core.hcf.command.SpawnCommand;
import com.surgehcf.core.hcf.command.SpawnerCommand;
import com.surgehcf.core.hcf.deathban.Deathban;
import com.surgehcf.core.hcf.deathban.DeathbanListener;
import com.surgehcf.core.hcf.deathban.DeathbanManager;
import com.surgehcf.core.hcf.deathban.FlatFileDeathbanManager;
import com.surgehcf.core.hcf.deathban.lives.LivesExecutor;
import com.surgehcf.core.hcf.economy.EconomyCommand;
import com.surgehcf.core.hcf.economy.EconomyManager;
import com.surgehcf.core.hcf.economy.FlatFileEconomyManager;
import com.surgehcf.core.hcf.economy.PayCommand;
import com.surgehcf.core.hcf.economy.ShopSignListener;
import com.surgehcf.core.hcf.eventgame.CaptureZone;
import com.surgehcf.core.hcf.eventgame.EventExecutor;
import com.surgehcf.core.hcf.eventgame.EventScheduler;
import com.surgehcf.core.hcf.eventgame.conquest.ConquestExecutor;
import com.surgehcf.core.hcf.eventgame.eotw.EotwCommand;
import com.surgehcf.core.hcf.eventgame.eotw.EotwHandler;
import com.surgehcf.core.hcf.eventgame.eotw.EotwListener;
import com.surgehcf.core.hcf.eventgame.faction.CapturableFaction;
import com.surgehcf.core.hcf.eventgame.faction.ConquestFaction;
import com.surgehcf.core.hcf.eventgame.faction.KothFaction;
import com.surgehcf.core.hcf.eventgame.koth.KothExecutor;
import com.surgehcf.core.hcf.faction.FactionExecutor;
import com.surgehcf.core.hcf.faction.FactionManager;
import com.surgehcf.core.hcf.faction.FactionMember;
import com.surgehcf.core.hcf.faction.FlatFileFactionManager;
import com.surgehcf.core.hcf.faction.claim.Claim;
import com.surgehcf.core.hcf.faction.claim.ClaimHandler;
import com.surgehcf.core.hcf.faction.claim.ClaimWandListener;
import com.surgehcf.core.hcf.faction.claim.Subclaim;
import com.surgehcf.core.hcf.faction.type.ClaimableFaction;
import com.surgehcf.core.hcf.faction.type.EndPortalFaction;
import com.surgehcf.core.hcf.faction.type.Faction;
import com.surgehcf.core.hcf.faction.type.PlayerFaction;
import com.surgehcf.core.hcf.faction.type.RoadFaction;
import com.surgehcf.core.hcf.faction.type.SpawnFaction;
import com.surgehcf.core.hcf.listener.AutoSmeltOreListener;
import com.surgehcf.core.hcf.listener.BookDeenchantListener;
import com.surgehcf.core.hcf.listener.BorderListener;
import com.surgehcf.core.hcf.listener.ChatListener;
import com.surgehcf.core.hcf.listener.CoreListener;
import com.surgehcf.core.hcf.listener.CrowbarListener;
import com.surgehcf.core.hcf.listener.DeathListener;
import com.surgehcf.core.hcf.listener.DeathMessageListener;
import com.surgehcf.core.hcf.listener.DeathSignListener;
import com.surgehcf.core.hcf.listener.ElevatorListener;
import com.surgehcf.core.hcf.listener.EntityLimitListener;
import com.surgehcf.core.hcf.listener.ExpListener;
import com.surgehcf.core.hcf.listener.ExpMultiplierListener;
import com.surgehcf.core.hcf.listener.FactionListener;
import com.surgehcf.core.hcf.listener.FoundDiamondsListener;
import com.surgehcf.core.hcf.listener.FurnaceSmeltSpeederListener;
import com.surgehcf.core.hcf.listener.ItemStatTrackingListener;
import com.surgehcf.core.hcf.listener.KitListener;
import com.surgehcf.core.hcf.listener.PortalListener;
import com.surgehcf.core.hcf.listener.ProtectionListener;
import com.surgehcf.core.hcf.listener.SignSubclaimListener;
import com.surgehcf.core.hcf.listener.SkullListener;
import com.surgehcf.core.hcf.listener.UnRepairableListener;
import com.surgehcf.core.hcf.listener.WorldListener;
import com.surgehcf.core.hcf.listener.fixes.BeaconStreanthFixListener;
import com.surgehcf.core.hcf.listener.fixes.BlockJumpGlitchFixListener;
import com.surgehcf.core.hcf.listener.fixes.BoatGlitchFixListener;
import com.surgehcf.core.hcf.listener.fixes.EnchantLimitListener;
import com.surgehcf.core.hcf.listener.fixes.EnderChestRemovalListener;
import com.surgehcf.core.hcf.listener.fixes.HungerFixListener;
import com.surgehcf.core.hcf.listener.fixes.InfinityArrowFixListener;
import com.surgehcf.core.hcf.listener.fixes.KeyDupeGlitchListener;
import com.surgehcf.core.hcf.listener.fixes.PearlGlitchListener;
import com.surgehcf.core.hcf.listener.fixes.PexCrashFix;
import com.surgehcf.core.hcf.listener.fixes.PhaseListener;
import com.surgehcf.core.hcf.listener.fixes.PotionLimitListener;
import com.surgehcf.core.hcf.listener.fixes.VoidGlitchFixListener;
import com.surgehcf.core.hcf.pvpclass.PvpClassManager;
import com.surgehcf.core.hcf.pvpclass.archer.ArcherClass;
import com.surgehcf.core.hcf.scoreboard.ScoreboardHandler;
import com.surgehcf.core.hcf.timer.TimerExecutor;
import com.surgehcf.core.hcf.timer.TimerManager;
import com.surgehcf.core.hcf.timer.type.SOTWTimer;
import com.surgehcf.core.hcf.user.FactionUser;
import com.surgehcf.core.hcf.user.UserManager;
import com.surgehcf.core.hcf.visualise.ProtocolLibHook;
import com.surgehcf.core.hcf.visualise.VisualiseHandler;
import com.surgehcf.core.hcf.visualise.WallBorderListener;
import com.surgehcf.core.hcfold.EndListener;
import com.surgehcf.core.hcfold.EventSignListener;
import com.surgehcf.core.hcfold.MapKitCommand;
import com.surgehcf.core.hcfold.combatlog.CombatLogListener;
import com.surgehcf.core.hcfold.combatlog.CustomEntityRegistration;
import com.surgehcf.core.hcfold.crate.KeyListener;
import com.surgehcf.core.hcfold.crate.KeyManager;
import com.surgehcf.core.hcfold.crate.LootExecutor;
import com.surgehcf.essentials.SurgeExtra;
import com.surgehcf.tab.SurgeTab;

import me.milksales.base.BasePlugin;
import me.milksales.base.ServerHandler;
import me.milksales.util.BukkitUtils;
import me.milksales.util.chat.ClickAction;
import me.milksales.util.chat.Text;

public class SurgeCore extends JavaPlugin implements Listener{
	public SurgeCore() {
		this.random = new Random();
	}

	public static SurgeCore getPlugin() {
		return plugin;
	}

	public static String c(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	public static String getRemaining(long millis, boolean milliseconds) {
		return getRemaining(millis, milliseconds, true);
	}

	public static String getRemaining(long duration, boolean milliseconds, boolean trail) {
		if ((milliseconds) && (duration < MINUTE)) {
			return ((DecimalFormat) (trail ? DateFormatter.REMAINING_SECONDS_TRAILING
					: DateFormatter.REMAINING_SECONDS).get()).format(duration * 0.001D) + 's';
		}
		return DurationFormatUtils.formatDuration(duration, (duration >= HOUR ? "HH:" : "") + "mm:ss");
	}

	public void onEnable() {
		instance = this;
		plugin = this;
		CustomEntityRegistration.registerCustomEntities();
		ProtocolLibHook.hook(this);
		Plugin wep = Bukkit.getPluginManager().getPlugin("WorldEdit");
		this.worldEdit = (((wep instanceof WorldEditPlugin)) && (wep.isEnabled()) ? (WorldEditPlugin) wep : null);
		registerConfiguration();
		registerCommands();
		registerManagers();
		registerListeners();
		registerCooldowns();
		SurgeExtra.enable();
		new BukkitRunnable() {
			public void run() {
				Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&',
						"&eAll factions have been saved to the &d&ldatabase&e."), "staff.mod");
				SurgeCore.this.saveData();
			}
		}

		.runTaskTimerAsynchronously(plugin, TimeUnit.MINUTES.toMillis(1L), TimeUnit.MINUTES.toMillis(1L));
	}

	private void registerCooldowns() {
		CooldownTimers.createCooldown("Faction_cooldown");
		CooldownTimers.createCooldown("Assassin_item_cooldown");
		CooldownTimers.createCooldown("Archer_item_cooldown");
		CooldownTimers.createCooldown("Rogue_cooldown");
		CooldownTimers.createCooldown("Flip_cooldown");
	}

	private void saveData() {
		this.deathbanManager.saveDeathbanData();
		this.economyManager.saveEconomyData();
		this.factionManager.saveFactionData();
		this.keyManager.saveKeyData();
		this.timerManager.saveTimerData();
		this.userManager.saveUserData();
	}

	public void onDisable() {
		CustomEntityRegistration.unregisterCustomEntities();
		CombatLogListener.removeCombatLoggers();
		this.pvpClassManager.onDisable();
		this.scoreboardHandler.clearBoards();
		SurgeExtra.disable();
		this.foundDiamondsListener.saveConfig();
		saveData();
		plugin = null;
	}

	private void registerConfiguration() {
		ConfigurationSerialization.registerClass(CaptureZone.class);
		ConfigurationSerialization.registerClass(Deathban.class);
		ConfigurationSerialization.registerClass(Claim.class);
		ConfigurationSerialization.registerClass(Subclaim.class);
		ConfigurationSerialization.registerClass(Deathban.class);
		ConfigurationSerialization.registerClass(FactionUser.class);
		ConfigurationSerialization.registerClass(ClaimableFaction.class);
		ConfigurationSerialization.registerClass(ConquestFaction.class);
		ConfigurationSerialization.registerClass(CapturableFaction.class);
		ConfigurationSerialization.registerClass(KothFaction.class);
		ConfigurationSerialization.registerClass(EndPortalFaction.class);
		ConfigurationSerialization.registerClass(Faction.class);
		ConfigurationSerialization.registerClass(FactionMember.class);
		ConfigurationSerialization.registerClass(PlayerFaction.class);
		ConfigurationSerialization.registerClass(RoadFaction.class);
		ConfigurationSerialization.registerClass(SpawnFaction.class);
		ConfigurationSerialization.registerClass(RoadFaction.NorthRoadFaction.class);
		ConfigurationSerialization.registerClass(RoadFaction.EastRoadFaction.class);
		ConfigurationSerialization.registerClass(RoadFaction.SouthRoadFaction.class);
		ConfigurationSerialization.registerClass(RoadFaction.WestRoadFaction.class);
	}

	private void registerListeners() {
		PluginManager manager = getServer().getPluginManager();
		manager.registerEvents(new ElevatorListener(), this);
		manager.registerEvents(new ArcherClass(this), this);
		manager.registerEvents(new AutoSmeltOreListener(), this);
		manager.registerEvents(new BlockJumpGlitchFixListener(), this);
		manager.registerEvents(new KeyDupeGlitchListener(), this);
		manager.registerEvents(new BoatGlitchFixListener(), this);
		manager.registerEvents(new BookDeenchantListener(), this);
		manager.registerEvents(new BorderListener(), this);
		manager.registerEvents(new SOTWTimer(), this);
		manager.registerEvents(new ChatListener(this), this);
		manager.registerEvents(new ClaimWandListener(this), this);
		manager.registerEvents(new CombatLogListener(this), this);
		manager.registerEvents(new CoreListener(this), this);
		manager.registerEvents(new CrowbarListener(this), this);
		manager.registerEvents(new DeathListener(this), this);
		manager.registerEvents(new DeathMessageListener(this), this);
		manager.registerEvents(new DeathSignListener(this), this);
		manager.registerEvents(new DeathbanListener(this), this);
		manager.registerEvents(new EnchantLimitListener(), this);
		manager.registerEvents(new EnderChestRemovalListener(), this);
		manager.registerEvents(new EntityLimitListener(), this);
		manager.registerEvents(new FlatFileFactionManager(this), this);
		manager.registerEvents(new EndListener(), this);
		manager.registerEvents(new EotwListener(this), this);
		manager.registerEvents(new EventSignListener(), this);
		manager.registerEvents(new ExpMultiplierListener(), this);
		manager.registerEvents(new FactionListener(this), this);
		manager.registerEvents(this.foundDiamondsListener = new FoundDiamondsListener(this), this);
		manager.registerEvents(new FurnaceSmeltSpeederListener(), this);
		manager.registerEvents(new InfinityArrowFixListener(), this);
		manager.registerEvents(new KitListener(this), this);
		manager.registerEvents(new ItemStatTrackingListener(), this);
		manager.registerEvents(new PhaseListener(), this);
		manager.registerEvents(new HungerFixListener(), this);
		manager.registerEvents(new PearlGlitchListener(this), this);
		manager.registerEvents(new PortalListener(this), this);
		manager.registerEvents(new PotionLimitListener(), this);
		manager.registerEvents(new ProtectionListener(this), this);
		manager.registerEvents(new SignSubclaimListener(this), this);
		manager.registerEvents(new ShopSignListener(this), this);
		manager.registerEvents(new SkullListener(), this);
		manager.registerEvents(new BeaconStreanthFixListener(), this);
		manager.registerEvents(new VoidGlitchFixListener(), this);
		manager.registerEvents(new WallBorderListener(this), this);
		manager.registerEvents(new WorldListener(this), this);
		manager.registerEvents(new ExpListener(), this);
		manager.registerEvents(new PexCrashFix(), this);
		manager.registerEvents(new UnRepairableListener(), this);
		manager.registerEvents(new ModeratorMode(), this);
		manager.registerEvents(this, this);
		manager.registerEvents(new StaffChat(), this);
	}

	private void registerCommands() {
		getCommand("ores").setExecutor(new OresCommand());
		getCommand("sotw").setExecutor(new SOTWCommand());
		getCommand("conquest").setExecutor(new ConquestExecutor(this));
		getCommand("crowbar").setExecutor(new CrowbarCommand());
		getCommand("economy").setExecutor(new EconomyCommand(this));
		getCommand("eotw").setExecutor(new EotwCommand(this));
		getCommand("game").setExecutor(new EventExecutor(this));
		getCommand("rules").setExecutor(new RulesCommand());
		getCommand("help").setExecutor(new HelpCommand());
		getCommand("coords").setExecutor(new CoordsCommand());
		getCommand("spawner").setExecutor(new SpawnerCommand(this));
		getCommand("faction").setExecutor(new FactionExecutor(this));
		getCommand("lives").setExecutor(new LivesExecutor(this));
		getCommand("logout").setExecutor(new LogoutCommand(this));
		getCommand("mapkit").setExecutor(new MapKitCommand(this));
		getCommand("pay").setExecutor(new PayCommand(this));
		getCommand("pvptimer").setExecutor(new PvpTimerCommand(this));
		getCommand("refund").setExecutor(new RefundCommand());
		getCommand("spawn").setExecutor(new SpawnCommand(this));
		getCommand("surge").setExecutor(new SurgeCommand(this));
		getCommand("stats").setExecutor(new PlayerStats());
		getCommand("vanish").setExecutor(new VanishMode());
		getCommand("sc").setExecutor(new StaffChat());
		getCommand("ts").setExecutor(new TeamspeakCommand());
		getCommand("koth").setExecutor(new KothExecutor(this));
		getCommand("mod").setExecutor(new ModeratorMode());
		getCommand("fly").setExecutor(new FlightCommand());
		getCommand("cobble").setExecutor(new CobbleCommand());
		getCommand("bard").setExecutor(new BardCommand());
		Map<String, Map<String, Object>> map = getDescription().getCommands();
		for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
			PluginCommand command = getCommand((String) entry.getKey());
			command.setPermission("command." + (String) entry.getKey());
			command.setPermissionMessage(ChatColor.GOLD + "No permission.");
		}
	}

	private void registerManagers() {
		this.claimHandler = new ClaimHandler(this);
		this.deathbanManager = new FlatFileDeathbanManager(this);
		this.economyManager = new FlatFileEconomyManager(this);
		this.eotwHandler = new EotwHandler(this);
		this.eventScheduler = new EventScheduler(this);
		this.factionManager = new FlatFileFactionManager(this);
		this.pvpClassManager = new PvpClassManager(this);
		this.timerManager = new TimerManager(this);
		this.scoreboardHandler = new ScoreboardHandler(this);
		this.userManager = new UserManager(this);
		this.visualiseHandler = new VisualiseHandler();
		this.keyManager = new KeyManager(this);
		getServer().getPluginManager().registerEvents(new KeyListener(this), this);
		getCommand("eventloot").setExecutor(new LootExecutor(this));
	}

	public Random getRandom() {
		return this.random;
	}

	public WorldEditPlugin getWorldEdit() {
		return this.worldEdit;
	}

	public KeyManager getKeyManager() {
		return this.keyManager;
	}

	public ClaimHandler getClaimHandler() {
		return this.claimHandler;
	}

	public DeathbanManager getDeathbanManager() {
		return this.deathbanManager;
	}

	public EconomyManager getEconomyManager() {
		return this.economyManager;
	}

	public EotwHandler getEotwHandler() {
		return this.eotwHandler;
	}

	public FactionManager getFactionManager() {
		return this.factionManager;
	}

	public PvpClassManager getPvpClassManager() {
		return this.pvpClassManager;
	}

	public static SurgeCore getInstance() {
		return instance;
	}

	public ScoreboardHandler getScoreboardHandler() {
		return this.scoreboardHandler;
	}

	public TimerManager getTimerManager() {
		return this.timerManager;
	}

	public ServerHandler getServerHandler() {
		return BasePlugin.getPlugin().getServerHandler();
	}

	public UserManager getUserManager() {
		return this.userManager;
	}

	public VisualiseHandler getVisualiseHandler() {
		return this.visualiseHandler;
	}

	public ArrayList getStaffModePlayers(){
		return this.sataffModePlayers;
	}

	public ArrayList getStaffChatPlayers(){
		return this.staffChat;
	}

	public ArrayList getToggledStaffBoardPlayers(){
		return this.staffboard;
	}

	public ArrayList getVanishedPlayers(){
		return this.vanish;
	}


	private static final long MINUTE = TimeUnit.MINUTES.toMillis(1L);
	private static final long HOUR = TimeUnit.HOURS.toMillis(1L);
	private static SurgeCore plugin;
	private static SurgeCore instance;
	public EventScheduler eventScheduler;
	private Random random;
	private WorldEditPlugin worldEdit;
	private FoundDiamondsListener foundDiamondsListener;
	private ClaimHandler claimHandler;
	private KeyManager keyManager;
	private DeathbanManager deathbanManager;
	private EconomyManager economyManager;
	private EotwHandler eotwHandler;
	private FactionManager factionManager;
	private PvpClassManager pvpClassManager;
	private ScoreboardHandler scoreboardHandler;
	private TimerManager timerManager;
	private UserManager userManager;
	private VisualiseHandler visualiseHandler;
	private ArrayList<String> sataffModePlayers = new ArrayList<String>();
	private ArrayList<String> staffChat = new ArrayList<String>();
	private ArrayList<String> staffboard = new ArrayList<String>();
	private ArrayList<String> vanish = new ArrayList<String>();

	@EventHandler
	public void antiPl(PlayerCommandPreprocessEvent e){
		String[] message = e.getMessage().split(" ");
		for(String word : message){
			if(word.equalsIgnoreCase("/pl") || word.equalsIgnoreCase("/plugins") || word.equalsIgnoreCase("/ver") || word.equalsIgnoreCase("/version") || word.equalsIgnoreCase("/about") || word.equalsIgnoreCase("/icanhasbukkit")){
				e.getPlayer().sendMessage("Unknown command. Type \"/help\" for help.");
				e.setCancelled(true);
			}
		}
	}

}
