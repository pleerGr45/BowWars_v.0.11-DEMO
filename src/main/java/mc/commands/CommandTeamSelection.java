package mc.commands;

import mc.bowwars.BowWars;
import mc.game.TeamBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * <p><b>Класс команды <i>/bwpts</i></b>
 * <br> Исполнитель команды <b><i>/bwpts</i></b>
 * @see CommandTeamSelection
 * @author <b>pleer__gr45</b>
 * @version 0.8
 */
public class CommandTeamSelection implements CommandExecutor {

    /**
     * <p><b>Метод выполнения команды</b>
     * <br>Использование <i><b>/bwpts</b></i>
     * <p>Принимает аргументы:
     * <br><b>give</b> - выдать предмет себе
     * <br><b>shove</b> - показать предмет игроку
     * @param sender <i>Отправитель команды</i>
     * @param command <i>Команда, которая была выполнена</i>
     * @param label <i>Сокращение выполненной команды</i>
     * @param args <i>Аргументы команды</i>
     * @return <b>true</b> - если команда выполнена правильно <br><b>false</b> - если команда выполнена неправильно
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //Если команда была отправлена не игроком
        if(!(sender instanceof Player)) return true;

        //Если недостаточно прав
        if(!sender.hasPermission("bwpts")) {
            return true;
        }

        if(!(args.length == 0)){
            ItemStack itemSelector = new ItemStack(Material.CLOCK, 1);
            ItemMeta meta = itemSelector.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Выбор команды"));
            itemSelector.setItemMeta(meta);

            if (args[0].equalsIgnoreCase("give")) {
                Bukkit.getPlayer(sender.getName()).getInventory().clear();
                Bukkit.getPlayer(sender.getName()).getInventory().setItem(0, itemSelector);
            } else if (args[0].equalsIgnoreCase("shove")) {
                if (args.length < 2) return true;
                Bukkit.getPlayer(args[1]).getInventory().clear();
                Bukkit.getPlayer(args[1]).getInventory().setItem(0, itemSelector);
            }
            return true;
        }

        Bukkit.getPlayer(sender.getName()).openInventory(createInventory(((Player) sender).getPlayer()));

        return true;
    }

    /**
     * <b>Метод создания обисания</b>
     * @param team <i>Команда</i>
     * @return Описание
     */
    public static List<String> setItemLore(TeamBase team) {
        List<String> lore = new ArrayList<>();
            lore.add(ChatColor.translateAlternateColorCodes('&', "&6"+team.getTeamStructure()+"/4"));
            for(byte i = 0; i < team.getTeamStructure(); i++){
                if(team.getPlayers()[i] != null) lore.add(ChatColor.translateAlternateColorCodes('&', "&7"+team.getPlayers()[i].getName()));
            }
        return lore;
    }

    /**
     * <b>Метод создания инвенторя</b>
     * @param player <i>Игрок</i>
     * @return Инвентарь
     */
    public static Inventory createInventory(Player player) {
        Inventory teamSelector;
        teamSelector = Bukkit.createInventory(player, 27, "Выбор команды");
        teamSelector.setItem(10, new ConstructTeamSelector(BowWars.getGame().getRed()).item);
        teamSelector.setItem(12, new ConstructTeamSelector(BowWars.getGame().getBlue()).item);
        teamSelector.setItem(14, new ConstructTeamSelector(BowWars.getGame().getYellow()).item);
        teamSelector.setItem(16, new ConstructTeamSelector(BowWars.getGame().getGreen()).item);
        teamSelector.setItem(8, new ConstructTeamSelector().item);
        return teamSelector;
    }

}



