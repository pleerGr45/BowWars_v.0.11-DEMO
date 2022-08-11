package mc.commands;

import mc.bowwars.BowWars;
import mc.game.GeneratorsResource;
import mc.game.TeamBase;
import mc.objects.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

public class CommandGens implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //Если недостаточно прав
        if(!sender.hasPermission("bwpgens")) {
            sender.sendMessage(BowWars.getString("messages.commands.do_not_have_permissions"));
            return true;
        }
        //Если отправитель не является игроком
        if(!(sender instanceof Player)) return true;

        if(args.length == 0) return false;

        if(args[0].equalsIgnoreCase("add")){
            if(args.length < 6) return false;
            BowWars.getGame().getGens().add(
                    new GeneratorsResource(
                    new Location(((Player) sender).getWorld(),
                        CommandMain.tildaTransform(args[1], sender, 'x'),
                        CommandMain.tildaTransform(args[2], sender, 'y'),
                        CommandMain.tildaTransform(args[3], sender, 'z')),
                    Byte.parseByte(args[5]),
                    parseResource(args[4])));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aГенератор установлен под индексом &2&l"+(BowWars.getGame().getGens().size()-1)+" &r&aЗапомните его!"));
            return true;
        } else if(args[0].equalsIgnoreCase("remove")){
            if(BowWars.getGame().getGens().size() == 0) return true;
            BowWars.getGame().getGens().get(BowWars.getGame().getGens().size()-1).getStand().remove();
            BowWars.getGame().getGens().remove(BowWars.getGame().getGens().size()-1);
            return true;
        } else if(args[0].equalsIgnoreCase("get")){
            if(args.length < 2) return true;
            if(Byte.parseByte(args[1]) < 0 || Byte.parseByte(args[1]) >  BowWars.getGame().getGens().size()) return true;
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aГенератор под индексом &l&2"+Byte.parseByte(args[1])+" &r&aсодержит следущие значения: "));
            for (Field field : BowWars.getGame().getGens().get(Byte.parseByte(args[1])).getClass().getDeclaredFields()) {
                sender.sendMessage(field.getName()+" : "+field);
            }
            return true;
        } else if(args[0].equalsIgnoreCase("enable")){
            if(args.length < 2) return true;
            if(Byte.parseByte(args[1]) < 0 || Byte.parseByte(args[1]) > BowWars.getGame().getGens().size()) return true;
            BowWars.getGame().getGens().get(Byte.parseByte(args[1])).setEnable(true);
            return true;
        } else if(args[0].equalsIgnoreCase("disable")){
            if(args.length < 2) return true;
            if(Byte.parseByte(args[1]) < 0 || Byte.parseByte(args[1]) >  BowWars.getGame().getGens().size()) return true;
            BowWars.getGame().getGens().get(Byte.parseByte(args[1])).setEnable(false);
            return true;
        } else if(args[0].equalsIgnoreCase("list")){
            for(GeneratorsResource resource : BowWars.getGame().getGens()) {
                sender.sendMessage(resource+"");
            }
            return true;
        }

        return false;
    }

    /**
     * <p><b>Метод перевода аргумента команду (Объект класса {@link TeamBase})</b>
     * @param s <i>Аргумент</i>
     * @return Команда, которая был преобразованна из аргумента в объект класса <b>{@link TeamBase}</b>
     */
    @Nullable
    private static BWPResource parseResource(String s) {
        if(s.equalsIgnoreCase("stick")) return new BWPResStick((byte) 1, "Палка");
        else if(s.equalsIgnoreCase("string")) return new BWPResString((byte) 1, "Нить");
        else if(s.equalsIgnoreCase("flint")) return new BWPResFlint((byte) 1, "Кремний");
        else if(s.equalsIgnoreCase("lapis")) return new BWPResLapis((byte) 1, "Лазурит");
        else if(s.equalsIgnoreCase("amethyst")) return new BWPResAmethyst((byte) 1, "Аметист");
        return null;
    }
}
