package net.apepvp.purchasecheck;

import net.apepvp.apecore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CheckCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (args.length == 0) {
			Database.getInstance().checkUpdate();
			if (sender instanceof Player) {
				MessageUtils.sendMessage((HumanEntity) sender, "&aCheck successfully!");
			}
		} else if (args.length == 1 && "reload".equals(args[0])) {
			Config.load();
			Bukkit.getScheduler().cancelTask(TaskManager.getTaskID());
			BukkitTask task = Bukkit.getScheduler().runTaskTimer(PurchaseCheck.getInstance(),
					() -> Database.getInstance().checkUpdate(), 0, (long) (20 * 60 * Config.INTERVAL_TIME));
			TaskManager.setTaskID(task.getTaskId());
			Bukkit.getLogger().info("[PurchaseCheck] [√]Plugin reload successfully!");
			if (sender instanceof Player) {
				MessageUtils.sendMessage((HumanEntity) sender, "&aPlugin reload successfully!");
			}
		}
		return true;
	}

	@Override
	public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
		if (strings.length == 1)
			return Collections.singletonList("reload");
		else
			return new ArrayList<>();
	}
}
