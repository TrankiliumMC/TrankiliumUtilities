package fr.trankilium.trankiliumutilities.utilities.ranks.gui;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RanksGUICommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender.isOp() || commandSender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null && target.isOnline()) {
                    RanksGUI gui = new RanksGUI(target);
                    target.openInventory(gui.getInventory());
                    return true;
                }
            }
        }
        return false;
    }
}
