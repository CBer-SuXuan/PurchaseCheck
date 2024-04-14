package me.suxuan.purchasecheck;

import lombok.Getter;
import me.suxuan.purchasecheck.settings.Config;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.database.SimpleDatabase;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Database extends SimpleDatabase {

	@Getter
	private final static Database instance = new Database();

	private Database() {
		this.addVariable("table", Config.TABLE);
	}

	@Override
	protected void onConnected() {
		System.out.println("[PurchaseCheck] [√]Database connected successfully!");
	}

	public void loadData(Player player) {
		Valid.checkSync("Please call loadData on the main thread!");
		Common.runAsync(() -> {
			try {
				ResultSet resultSet = this.query("SELECT * FROM {table} WHERE username = '" + player.getName() + "'");
				if (resultSet.next()) {
					int productID = resultSet.getInt("productID");
					String isGiven = resultSet.getString("isGiven");
					System.out.println("productID: " + productID + ", isGiven: " + isGiven);
				}
			} catch (Throwable t) {
				Common.error(t, "Error while loading " + player.getName() + "'s data in database!");
			}
		});
	}

	public void findDataNotGiven(Consumer<SerializedMap> callThisWithDataFound) {
		Valid.checkSync("Please call findDataNotGiven on the main thread!");
		Common.runAsync(() -> {
			try {
				int i = 0;
				ResultSet resultSet = this.query("SELECT * FROM {table} WHERE isGiven = 'false'");
				SerializedMap dataFound = new SerializedMap();
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
				Common.runLater(() -> callThisWithDataFound.accept(dataFound));
			} catch (Throwable t) {
				Common.error(t, "Error while finding data in database!");
			}
		});
	}

	public void setGiven() {
		Valid.checkSync("Please call setGiven on the main thread!");
		Common.runAsync(() -> {
			try {
				this.update("UPDATE {table} SET isGiven = 'true' WHERE isGiven = 'false'");
			} catch (Throwable t) {
				Common.error(t, "Error while setting isGiven to true in database!");
			}
		});
	}
}
