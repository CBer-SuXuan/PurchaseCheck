package me.suxuan.purchasecheck;

import me.suxuan.purchasecheck.settings.Config;
import me.suxuan.purchasecheck.utils.DatabaseUtils;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;

public final class Main extends SimplePlugin {

	private static Main instance;

	@Override
	protected void onPluginStart() {
		if ("127.0.0.1".equals(Config.HOST) && Config.PORT == 3306 && "root".equals(Config.DATABASE) && "root".equals(Config.PASSWORD)) {
			Common.runLater(60, () -> {
				System.out.println("[PurchaseCheck] [!]Go to config.yml file and config MySQL database connection!");
				System.out.println("[PurchaseCheck] [!]After config type '/pc reload' in console to connect MySQL");
			});
		} else
			try {
				Database.getInstance().connect(Config.HOST, Config.PORT, Config.DATABASE, Config.USER, Config.PASSWORD);
				Common.runTimer(20 * 60 * Config.INTERVAL_TIME, DatabaseUtils::checkUpdate);
			} catch (Exception e) {
				Common.warning(e.getMessage());
			}
	}

	@Override
	protected void onPluginReload() {
		try {
			Database.getInstance().connect(Config.HOST, Config.PORT, Config.DATABASE, Config.USER, Config.PASSWORD);
			Common.runTimer(20 * 60 * Config.INTERVAL_TIME, DatabaseUtils::checkUpdate);
		} catch (Exception e) {
			Common.warning(e.getMessage());
		}
	}
}
