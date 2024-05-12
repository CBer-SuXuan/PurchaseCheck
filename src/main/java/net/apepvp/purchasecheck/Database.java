package net.apepvp.purchasecheck;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Database {

	private final static Database instance = new Database();

	public void findDataNotGiven(Consumer<Map<String, List<String>>> callThisWithDataFound) {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					int i = 0;
					ResultSet resultSet = PurchaseCheck.getApi().getSQL().query(
							"SELECT * FROM purchases WHERE isGiven = 'false'");
					Map<String, List<String>> dataFound = new HashMap<>();
					while (resultSet.next()) {
						List<String> tempList = new ArrayList<>();
						tempList.add(resultSet.getString("name"));
						tempList.add(String.valueOf(resultSet.getInt("productID")));
						tempList.add(String.valueOf(resultSet.getFloat("price")));
						tempList.add(resultSet.getString("customerEmail"));
						tempList.add(resultSet.getString("username"));
						tempList.add(resultSet.getString("paymentID"));
						tempList.add(String.valueOf(resultSet.getDate("date")));
						dataFound.put(String.valueOf(i), tempList);
						i++;
					}
					Bukkit.getScheduler().runTaskLater(PurchaseCheck.getInstance(), () -> callThisWithDataFound.accept(dataFound), 0);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}.runTaskAsynchronously(PurchaseCheck.getInstance());
	}

	public void setGiven(Integer productID) {
		new BukkitRunnable() {
			@Override
			public void run() {
				PurchaseCheck.getApi().getSQL().asyncUpdate(
						"UPDATE purchases SET isGiven = 'true' WHERE isGiven = 'false' and productID = " + productID);
			}
		}.runTaskAsynchronously(PurchaseCheck.getInstance());
	}

	public void checkUpdate() {
		Database.getInstance().findDataNotGiven(foundData -> {

			if (foundData.isEmpty()) {
				Bukkit.getLogger().info("[PurchaseCheck] [√]No uncheck purchase!");
				return;
			}

			// Iterate every data that is not execute.
			for (Map.Entry<String, List<String>> data : foundData.entrySet()) {
				// Cast into list
				List<String> dataValue = data.getValue();

				// Get product ID
				String productID = dataValue.get(1);

				// Run the command by the ID
				for (String command : Config.COMMANDS.get(productID)) {
					String finalCommand = command.replace("{name}", dataValue.get(0))
							.replace("{price}", dataValue.get(2))
							.replace("{customerEmail}", dataValue.get(3))
							.replace("{username}", dataValue.get(4))
							.replace("{paymentID}", dataValue.get(5))
							.replace("{date}", dataValue.get(6));
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand);
				}

				Database.getInstance().setGiven(Integer.valueOf(productID));
			}
		});
	}

	public static Database getInstance() {
		return instance;
	}
}
