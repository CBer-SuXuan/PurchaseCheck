package me.suxuan.purchasecheck.commands;

import me.suxuan.purchasecheck.Database;
import me.suxuan.purchasecheck.settings.Commands;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;

@AutoRegister
public final class TestCommand extends SimpleCommand {
	public TestCommand() {
		super("test");
	}

	@Override
	protected void onCommand() {
//		Database.getInstance().loadData(getPlayer());
		Database.getInstance().findDataNotGiven(foundData -> {
			System.out.println(foundData);
			tellNoPrefix("[PurchaseCheck] [√]Done!");
		});
		System.out.println(Commands.COMMANDS);
	}
}
