package me.suxuan.purchasecheck.utils;

import me.suxuan.purchasecheck.Database;
import me.suxuan.purchasecheck.settings.Commands;
import org.bukkit.Bukkit;
import org.mineacademy.fo.Common;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DatabaseUtils {

	public static void checkUpdate() {
		Database.getInstance().findDataNotGiven(foundData -> {

			if (foundData.isEmpty()) {
				System.out.println("[PurchaseCheck] [√]No uncheck purchase!");
				return;
			}

			// Iterate every data that is not execute.
			for (Map.Entry<String, Object> data : foundData) {
				// Cast into list
				List<String> dataValue = (List<String>) data.getValue();

				// Get product ID
				String productID = dataValue.get(1);

				// Check if ID is valid
				if (!Arrays.asList("1", "2", "3").contains(productID))
					System.out.println("[PurchaseCheck] [!]Wrong product ID: " + productID);

				// Run the command by the ID
				for (String command : Commands.COMMANDS.getStringList(productID)) {
					String finalCommand = command.replace("{name}", dataValue.get(0))
							.replace("{price}", dataValue.get(2))
							.replace("{customerEmail}", dataValue.get(3))
							.replace("{username}", dataValue.get(4))
							.replace("{paymentID}", dataValue.get(5))
							.replace("{date}", dataValue.get(6));
					Common.dispatchCommand(Bukkit.getConsoleSender(), finalCommand);
				}
			}
			Database.getInstance().setGiven();
		});
	}

}
