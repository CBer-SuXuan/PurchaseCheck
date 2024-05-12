package net.apepvp.purchasecheck;

import net.apepvp.apecore.ApeCore;
import net.apepvp.apecore.api.ApeCoreAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.logging.Level;

public final class PurchaseCheck extends JavaPlugin {

	private static PurchaseCheck instance;
	private static ApeCore core;
	private static ApeCoreAPI api;

	@Override
	public void onEnable() {
		instance = this;
		core = (ApeCore) Bukkit.getPluginManager().getPlugin("ApeCore");
		if (core == null) {
			instance.getLogger().info("Could not connect to the core disabling plugin");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		api = core.getApi();

		createYmlFiles();

		Config.load();

		BukkitTask task = Bukkit.getScheduler().runTaskTimer(this, () -> Database.getInstance().checkUpdate(), 0, (long) (20L * 60 * Config.INTERVAL_TIME));
		TaskManager.setTaskID(task.getTaskId());

		getCommand("check").setExecutor(new CheckCommand());

	}

	public void createYmlFiles() {
		saveDefaultResource("config");
		saveDefaultResource("commands");
	}

	void saveDefaultResource(String resource) {
		String path = resource + ".yml";
		File file = new File(getDataFolder(), path);
		if (!file.exists()) {
			this.saveResource(path, false);
			this.getLogger().log(Level.INFO, "Saved default resource " + path);
		}
	}

	public static PurchaseCheck getInstance() {
		return instance;
	}

	public static ApeCoreAPI getApi() {
		return core.getApi();
	}
}
