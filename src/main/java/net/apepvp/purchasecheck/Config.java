package net.apepvp.purchasecheck;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {

	public static Double INTERVAL_TIME;
	public static Map<String, List<String>> COMMANDS = new HashMap<>();

	public static void load() {
		File file = new File(PurchaseCheck.getInstance().getDataFolder(), "config.yml");
		YamlConfiguration fc = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection cs = fc.getConfigurationSection("");
		if (cs == null) return;
		INTERVAL_TIME = cs.getDouble("interval_time");

		File file2 = new File(PurchaseCheck.getInstance().getDataFolder(), "commands.yml");
		YamlConfiguration fc2 = YamlConfiguration.loadConfiguration(file2);
		ConfigurationSection cs2 = fc2.getConfigurationSection("commands");
		if (cs2 == null) return;
		cs2.getKeys(false).forEach(key -> {
			COMMANDS.put(key, cs2.getStringList(key));
		});
	}

}
