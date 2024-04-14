package me.suxuan.purchasecheck.settings;

import org.mineacademy.fo.settings.YamlStaticConfig;

public final class Config extends YamlStaticConfig {

	@Override
	protected void onLoad() {
		this.loadConfiguration("config.yml");
	}

	public static String HOST;
	public static Integer PORT;
	public static String DATABASE;
	public static String USER;
	public static String PASSWORD;
	public static String TABLE;
	public static Integer INTERVAL_TIME;

	private static void init() {
		setPathPrefix(null);
		HOST = getString("Host");
		PORT = getInteger("Port");
		DATABASE = getString("Database");
		USER = getString("User");
		PASSWORD = getString("Password");
		TABLE = getString("Table");
		INTERVAL_TIME = getInteger("Interval_Time");
	}

}
