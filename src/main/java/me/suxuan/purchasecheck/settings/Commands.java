package me.suxuan.purchasecheck.settings;

import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.settings.YamlStaticConfig;

public final class Commands extends YamlStaticConfig {

	@Override
	protected void onLoad() throws Exception {
		this.loadConfiguration("commands.yml");
	}

	public static SerializedMap COMMANDS;

	private static void init() {
		setPathPrefix(null);
		COMMANDS = getMap("Commands");
	}
}
