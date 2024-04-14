package me.suxuan.purchasecheck.commands;

import me.suxuan.purchasecheck.Main;
import me.suxuan.purchasecheck.utils.DatabaseUtils;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

@AutoRegister
public final class CheckCommand extends SimpleCommand {
	public CheckCommand() {
		super("purchasecheck|pc");
		setAutoHandleHelp(false);
	}

	@Override
	protected void onCommand() {
		if (args.length == 0) {
			DatabaseUtils.checkUpdate();
		} else if (args.length == 1 && "reload".equals(args[0])) {
			// Reload whole plugin
			Main.getInstance().reload();
		}
	}

	@Override
	protected List<String> tabComplete() {
		return List.of("reload");
	}
}
