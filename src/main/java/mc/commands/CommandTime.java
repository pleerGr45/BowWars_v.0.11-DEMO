package mc.commands;

import mc.bowwars.BowWars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandTime implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        try {
            Short.parseShort(args[0]);
        } catch (NumberFormatException e) {return false;}

        if(Short.parseShort(args[0]) < 0 || Short.parseShort(args[0]) > 3600) return true;
        BowWars.getGame().setTime(Short.parseShort(args[0]));

        return true;
    }
}
