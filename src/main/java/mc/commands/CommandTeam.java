package mc.commands;

import mc.bowwars.BowWars;
import mc.game.TeamBase;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import javax.annotation.Nullable;

/**
 * <p><b>Класс команды <i>/bwpt</i></b>
 * <br> Исполнитель команды <b><i>/bwpt</i></b>
 * @see CommandMain
 * @author <b>pleer__gr45</b>
 * @version 0.8
 */
public class CommandTeam implements CommandExecutor {

    /**
     * <p><b>Конструктор класса {@link CommandTeam}</b>
     */
    public CommandTeam() {}

    /**
     * <p><b>Метод выполнения команды</b>
     * <br>Использование <i><b>/bwpt [ARGUMENTS]</b></i>
     * <p>Принимает аргументы:
     * <br><b>join</b> - войти в команду
     * <br><b>leave</b> - выйти из команды
     * <br><b>list</b> - список игроков в команде
     * <br><b>throw</b> - забросить игрока в комадну
     * <br><b>kick</b> - выгнять игрока из команды
     * @param sender <i>Отправитель команды</i>
     * @param command <i>Команда, которая была выполнена</i>
     * @param label <i>Сокращение выполненной команды</i>
     * @param args <i>Аргументы команды</i>
     * @return <b>true</b> - если команда выполнена правильно <br><b>false</b> - если команда выполнена неправильно
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //Если команда была отправлена не игроком
        if(!(sender instanceof Player)) return true;

        //Если недостаточно прав
        if(!sender.hasPermission("bwpt")) {
            sender.sendMessage(BowWars.getString("messages.commands.do_not_have_permissions"));
            return true;
        }

        if(args.length == 0) {sender.sendMessage(BowWars.getString("messages.commands.bwp_team.not_args")); return true;}

        //Команды для которых не нужны права
        if(args[0].equalsIgnoreCase("join")) {
            if(args.length < 2) {sender.sendMessage(BowWars.getString("messages.commands.bwp_team.null_join")); return true;}

            if(!(contentInTeam(Bukkit.getPlayer(sender.getName())) == null)) return true;
            if(CommandMain.parseTeam(args[1]).getTeamStructure() >= 4 ) return true;

            return joinInTeam(Bukkit.getPlayer(sender.getName()), args);
        } else if(args[0].equalsIgnoreCase("leave")) {
            if(contentInTeam(Bukkit.getPlayer(sender.getName())) == null) {sender.sendMessage(BowWars.getString("messages.commands.bwp_team.not_in_the_team")); return true;}
            contentInTeam(Bukkit.getPlayer(sender.getName())).removePlayer(Bukkit.getPlayer(sender.getName()));
            return true;
        } else if(args[0].equalsIgnoreCase("throw")){
            if(args.length < 3) {sender.sendMessage(BowWars.getString("messages.commands.bwp_team.null_throw")); return true;}
            for(Player player : Bukkit.getOnlinePlayers()) if(player.getUniqueId().equals(Bukkit.getPlayer(args[2]).getUniqueId())) {
                if(!(contentInTeam(Bukkit.getPlayer(args[2])) == null)) {sender.sendMessage(BowWars.getString("messages.commands.bwp_team.throw_already_in_command")); return true;}
                return joinInTeam(Bukkit.getPlayer(args[2]), args);}
            sender.sendMessage(BowWars.getString("messages.commands.throw_not_online_player"));
            return true;
        } else if(args[0].equalsIgnoreCase("kick")){
            if(args.length < 2) {sender.sendMessage(BowWars.getString("messages.commands.bwp_team.null_kick")); return true;}
            if(contentInTeam(Bukkit.getPlayer(args[1])) == null) {sender.sendMessage(BowWars.getString("messages.commands.bwp_team.throw_not_in_the_team")); return true;}
            contentInTeam(Bukkit.getPlayer(args[1])).removePlayer(Bukkit.getPlayer(args[1]));
            return true;
        } else if(args[0].equalsIgnoreCase("list")){
            if(args.length < 2) {sender.sendMessage(BowWars.getString("messages.commands.bwp_team.null_list")); return true;}
            if (args[1].equalsIgnoreCase("red")) {
                printList(BowWars.getGame().getRed(), Bukkit.getPlayer(sender.getName()));
                return true;
            } else if (args[1].equalsIgnoreCase("blue")) {
                printList(BowWars.getGame().getBlue(), Bukkit.getPlayer(sender.getName()));
                return true;
            } else if (args[1].equalsIgnoreCase("green")) {
                printList(BowWars.getGame().getGreen(), Bukkit.getPlayer(sender.getName()));
                return true;
            } else if (args[1].equalsIgnoreCase("yellow")) {
                printList(BowWars.getGame().getYellow(), Bukkit.getPlayer(sender.getName()));
                return true;
            }
        }
        return false;
    }

    /**
     * <p><b>Метод проверки игрока в команде</b>
     * <br>Проверяет есть ли данный игрок в данной команде <b>(в методе {@link CommandTeam#checkTeam(TeamBase, Player)})</b>,
     * <br>и возвращает команду, в которой состоит игрок
     * @param player <i>Игрок</i>
     * @return Возвращает команду, в которой состоит игрок, или же <b>null</b>, если не состоит нигде
     * @since 0.6.3
     */
    @Nullable
    public static TeamBase contentInTeam(Player player) {
        if(checkTeam(BowWars.getGame().getRed(), player)) {return BowWars.getGame().getRed();}
        if(checkTeam(BowWars.getGame().getBlue(), player)) {return BowWars.getGame().getBlue();}
        if(checkTeam(BowWars.getGame().getYellow(), player)) {return BowWars.getGame().getYellow();}
        if(checkTeam(BowWars.getGame().getGreen(), player)) {return BowWars.getGame().getGreen();}
        return null;
    }

    /**
     * <p><b>Метод проверки игрока в команде</b>
     * <br>Проверяет есть ли данный игрок в данной команде
     * <p>Используется в методе <b>{@link CommandTeam#contentInTeam(Player)}</b>
     * @param team <i>Команда</i>
     * @param player <i>Игрок</i>
     * @return <b>true</b> - если есть игрок в команде <br><b>false</b> - если нет игрока в команде
     * @since 0.6.3
     */
    private static boolean checkTeam(TeamBase team, Player player) {
        for(byte i = 0; i < team.getTeamStructure(); i++) {
            if(team.getPlayers()[i].getUniqueId().equals(player.getUniqueId())) {return true;}
        }
        return false;
    }

    /**
     * <p><b>Метод вывода игроков в указанной команде</b>
     * <br> Выводит всех игроков в данной команде данному игроку
     * @param team <i>Команда</i>
     * @param player <i>Игрок (отправитель команды)</i>
     * @since 0.7.1
     */
    public static void printList(TeamBase team, Player player) {
        if(team.getTeamStructure() == 0) {player.sendMessage(BowWars.getString("messages.commands.bwp_team.void_team"));}
        else for(byte i = 0; i < team.getTeamStructure(); i++){
            player.sendMessage(team.getPlayers()[i].getName());
        }
    }

    /**
     * <p><b>Методы входа в команду</b>
     * <br>Забрасывает игрока в выбранную команду
     * @since 0.8
     * @param player <i>Игрок</i>
     * @param args <i>Аргументы команды</i>
     * @return <b>true</b> - если метод выполнился верно <br><b>false</b> - если метод выполнился неправильно
     */
    private static boolean joinInTeam(Player player, String[] args) {
        if (args[1].equalsIgnoreCase("red")) {
            return BowWars.getGame().getRed().addPlayer(player);
        } else if (args[1].equalsIgnoreCase("blue")) {
           return BowWars.getGame().getBlue().addPlayer(player);
        } else if (args[1].equalsIgnoreCase("green")) {
            return BowWars.getGame().getGreen().addPlayer(player);
        } else if (args[1].equalsIgnoreCase("yellow")) {
            return BowWars.getGame().getYellow().addPlayer(player);
        }
        return false;
    }

}
