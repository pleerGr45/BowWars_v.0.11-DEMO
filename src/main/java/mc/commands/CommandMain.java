package mc.commands;

import mc.bowwars.BowWars;
import mc.game.TeamBase;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * <p><b>Класс команды <i>/bwp</i></b>
 * <br> Исполнитель команды <b><i>/bwp</i></b>
 * @see CommandTeam
 * @author <b>pleer__gr45</b>
 * @version 0.6.5
 */
public class CommandMain implements CommandExecutor {

    /**
     * <p><b>Конструктор класса {@link CommandMain}</b>
     */
    public CommandMain() {}

    /**
     * <p><b>Метод выполнения команды</b>
     * <br>Использование <i><b>/bwp [ARGUMENTS]</b></i>
     * <p>Принимает аргументы:
     * <br><b>start</b> - запускает игру
     * <br><b>stop</b> - останавливает игру
     * <br><b>setbase</b> - устанавливает базу команды
     * @param sender <i>Отправитель команды</i>
     * @param command <i>Команда, которая была выполнена</i>
     * @param label <i>Сокращение выполненной команды</i>
     * @param args <i>Аргументы команды</i>
     * @return <b>true</b> - если команда выполнена правильно <br><b>false</b> - если команда выполнена неправильно
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //Если недостаточно прав
        if(!sender.hasPermission("bwp")) {
            sender.sendMessage(BowWars.getString("messages.commands.do_not_have_permissions"));
            return true;
        }
        //Если отправитель не является игроком
        if(!(sender instanceof Player)) return true;

        //Проверка аргументов
        if(args.length == 0) return false;
        if(args[0].equalsIgnoreCase("start")){
            //Команда: старт
            if(!BowWars.getGame().isGameStatus()) {
                BowWars.getGame().start(Bukkit.getPlayer(sender.getName()));
                sender.sendMessage(BowWars.getString("messages.commands.bwp_start"));
            } else sender.sendMessage(BowWars.getString("messages.commands.bwp_already_start"));
        } else if (args[0].equalsIgnoreCase("stop")) {
            if(BowWars.getGame().isGameStatus()) {
                BowWars.getGame().stop();
                sender.sendMessage(BowWars.getString("messages.commands.bwp_stop"));
            }
            else sender.sendMessage(BowWars.getString("messages.commands.bwp_already_stop"));
        } else if(args[0].equalsIgnoreCase("setbase")){
            //Команда: установить расположение базы
            if(args.length < 2) {sender.sendMessage(BowWars.getString("messages.commands.bwp_setbase.not_args")); return true;}
            if(args.length < 5) {sender.sendMessage(BowWars.getString("messages.commands.bwp_setbase.not_coord")); return true;}
                try {
                    parseTeam(args[1]).setCoords(tildaTransform(args[2], sender, 'x'), tildaTransform(args[3], sender, 'y'), tildaTransform(args[4], sender, 'z'), Bukkit.getWorld(Bukkit.getPlayer(sender.getName()).getWorld().getName()));
                   sender.sendMessage(BowWars.getString("messages.commands.bwp_setbase.team")+" на координатах: "+parseTeam(args[1]).getLocation().getBlockX()+" "+parseTeam(args[1]).getLocation().getBlockY()+" "+parseTeam(args[1]).getLocation().getBlockZ());
                } catch (NullPointerException e) {sender.sendMessage(BowWars.getString("messages.commands.bwp_setbase.team_null"));}
        } else if(args[0].equalsIgnoreCase("target")) {
            if(args.length < 2) {sender.sendMessage(BowWars.getString("messages.commands.bwp_target.null")); return true;}
            //Установить мишени команды
            if(args[1].equalsIgnoreCase("set")) {
                if(args.length < 6) {sender.sendMessage(BowWars.getString("messages.commands.bwp_target.set.usage")); return true;}
                try {
                    if(parseTeam(args[2]).addTarget(tildaTransform(args[3], sender, 'x'), tildaTransform(args[4], sender, 'y'), tildaTransform(args[5], sender, 'z'), Bukkit.getWorld(Bukkit.getPlayer(sender.getName()).getWorld().getName())))
                    sender.sendMessage(BowWars.getString("messages.commands.bwp_target.set.target")+" на координатах: "+parseTeam(args[2]).getTarget().getLocation().getBlockX()+" "+parseTeam(args[2]).getTarget().getLocation().getBlockY()+" "+parseTeam(args[2]).getTarget().getLocation().getBlockZ());
                    else sender.sendMessage(BowWars.getString("messages.commands.error"));
                } catch (NullPointerException e) {sender.sendMessage(BowWars.getString("messages.commands.bwp_target.remove.null_team"));}
            } else if(args[1].equalsIgnoreCase("remove")) {
                if(args.length < 3) {sender.sendMessage(BowWars.getString("messages.commands.bwp_target.remove.usage")); return true;}
                try {
                    try {
                        sender.sendMessage(BowWars.getString("messages.commands.bwp_target.remove.target") + " на координатах: " + parseTeam(args[2]).getTarget().getLocation().getBlockX() + " " + parseTeam(args[2]).getTarget().getLocation().getBlockY() + " " + parseTeam(args[2]).getTarget().getLocation().getBlockZ());
                    } catch (NullPointerException e) {sender.sendMessage(BowWars.getString("messages.commands.bwp_target.remove.max_remove"));}
                    if(!parseTeam(args[2]).removeTarget()) sender.sendMessage(BowWars.getString("messages.commands.error"));
                } catch (NullPointerException e) {sender.sendMessage(BowWars.getString("messages.commands.bwp_target.remove.null_team"));}
            }
        } else if(args[0].equalsIgnoreCase("center")) {
            if(args.length < 4) {sender.sendMessage(BowWars.getString("messages.commands.bwp_center.usage")); return true;}
            BowWars.getGame().setCenter(tildaTransform(args[1], sender, 'x'), tildaTransform(args[2], sender, 'y'), tildaTransform(args[3], sender, 'z'), Bukkit.getWorld(Bukkit.getPlayer(sender.getName()).getWorld().getName()));
            sender.sendMessage(BowWars.getString("messages.commands.bwp_center.success")+" на координатах: "+Bukkit.getPlayer(sender.getName()).getLocation().getBlockX()+" "+Bukkit.getPlayer(sender.getName()).getLocation().getBlockY()+" "+Bukkit.getPlayer(sender.getName()).getLocation().getBlockZ());
        } else if(args[0].equalsIgnoreCase("reserving")) {
            if(args.length < 7) return true;
            reservation(sender,
                    new Location(Bukkit.getPlayer(sender.getName()).getWorld(), tildaTransform(args[1], sender, 'x'), tildaTransform(args[2], sender, 'y'), tildaTransform(args[3], sender, 'z')),
                    new Location(Bukkit.getPlayer(sender.getName()).getWorld(), tildaTransform(args[4], sender, 'x'), tildaTransform(args[5], sender, 'y'), tildaTransform(args[6], sender, 'z')));
        } else {
            for(byte i = 0; i < 6; i++)
            sender.sendMessage(BowWars.getString("messages.commands.bwp_help_"+i));}

        return true;
 }

    /**
     * <p><b>Метод определения координаты</b>
     * <br>Метод определяет, если координата == ~, то возвращаем положение игрока по координате <b>axis</b>, если координата != ~, то возвращаем эту координату через <b>{@link Integer#parseInt(String)}</b>
     * <p>Используется в методе <b>{@link CommandMain#onCommand(CommandSender, Command, String, String[])}</b>. При возникновении ошибки возвращаем <b>0</b>
     * @param param <i>Параметр координаты</i>
     * @param sender <i>Отправитель команды</i>
     * @param axis <i>Возвращаемая величина</i>
     * @return <b>X, Y, Z</b> в зависимости от <b>axis</b>
     * @since 0.4.6
     */
    static int tildaTransform(String param, CommandSender sender, char axis){
        if(param.equals("~")) {
            if(axis == 'x') return Bukkit.getPlayer(sender.getName()).getLocation().getBlockX();
            if(axis == 'y') return Bukkit.getPlayer(sender.getName()).getLocation().getBlockY();
            if(axis == 'z') return Bukkit.getPlayer(sender.getName()).getLocation().getBlockZ();
        }

        try {Integer.parseInt(param);
        } catch(RuntimeException re) {sender.sendMessage(BowWars.getString("messages.commands.bwp_setbase.warn_coord")); return 0;}

        return Integer.parseInt(param);
    }

    /**
     * <p><b>Метод перевода аргумента команду (Объект класса {@link TeamBase})</b>
     * @param s <i>Аргумент</i>
     * @return Команда, которая был преобразованна из аргумента в объект класса <b>{@link TeamBase}</b>
     * @throws NullPointerException Если аргумент не может быть преобразован в команду
     */
    @Nullable
    public static TeamBase parseTeam(String s) throws NullPointerException {
        if(s.equalsIgnoreCase("red")) return BowWars.getGame().getRed();
        else if(s.equalsIgnoreCase("blue")) return BowWars.getGame().getBlue();
        else if(s.equalsIgnoreCase("yellow")) return BowWars.getGame().getYellow();
        else if(s.equalsIgnoreCase("green")) return BowWars.getGame().getGreen();
        return null;
    }

    private static void reservation(CommandSender sender, Location p1, Location p2) {
        System.out.println("method:reservation(01, p2) loaded");
        String xl_op;
        String yl_op;
        String zl_op;

        if(p1.getBlockX() > p2.getBlockX()) xl_op = ">=";
        else if(p1.getBlockX() < p2.getBlockX()) xl_op = "<=";
        else return;

        if(p1.getBlockY() > p2.getBlockY()) yl_op = ">=";
        else if(p1.getBlockY() < p2.getBlockY()) yl_op = "<=";
        else return;

        if(p1.getBlockZ() > p2.getBlockZ()) zl_op = ">=";
        else if(p1.getBlockZ() < p2.getBlockZ()) zl_op = "<=";
        else return;

        System.out.println("start map reserving");
        mapReservation(sender, p1, p2, xl_op, yl_op, zl_op);
    }

    //bwp reserving 72 64 219 68 68 224
    private static void mapReservation(CommandSender sender, Location p1, Location p2, String xl_op, String yl_op, String zl_op) {
        System.out.println("p1x: "+p1.getBlockX()+"\np1y: "+p1.getBlockY()+"\np1z: "+p1.getBlockZ()+"\np2x: "+p2.getBlockX()+"\np2y: "+p2.getBlockY()+"\np2z: "+p2.getBlockZ()+"\nxl_op: "+xl_op+"\nyl_op: "+yl_op+"\nzl_op: "+zl_op);
        Set<Material> blockList = BowWars.getGame().getBlocks();
        World world = p1.getWorld();
        Bukkit.getScheduler().scheduleSyncDelayedTask(BowWars.getPlugin(), () -> {
            for(int x = p1.getBlockX(); logic(x, xl_op, p2.getBlockX()); x = nextLogic(x, xl_op)) {
                if(x < p2.getBlockX()) Bukkit.getPlayer(sender.getName()).sendTitle(ChatColor.translateAlternateColorCodes('&', "&e&l"+((int)(((double)(x)/p2.getBlockX())*100)+"%")), null, 1, 20, 1);
                else Bukkit.getPlayer(sender.getName()).sendTitle(ChatColor.translateAlternateColorCodes('&', "&e&l"+((int)(((double)(p2.getBlockX())/x)*100)+"%")), null, 1, 20, 1);
                for(int y = p1.getBlockY(); logic(y, yl_op, p2.getBlockY()); y = nextLogic(y, yl_op)) {
                    for(int z = p1.getBlockZ(); logic(z, zl_op, p2.getBlockZ()); z = nextLogic(z, zl_op)) {
                        blockList.add(world.getBlockAt(x, y, z).getType());
                    }
                }
            }
            System.out.println("Map loaded");
            BowWars.getGame().setBlocks(blockList);
            for (Material material: BowWars.getGame().getBlocks()) {
                sender.sendMessage(material+"");
            }
        }, 0);

    }

    /**
     * <p><b>Метод увеличения проверки неравенства</b>
     * @param param <i>Измменяемое значение в цикле for</i>
     * @param op <i>Операция</i>
     * @param param2 <i>Неизменяемое значение в цикле for</i>
     * @return <b>true</b> если неравенство верно <br><b>false</b> если неравенство неверно
     */
    public static boolean logic(int param, String op, int param2) {
        if(op.equals(">=")) {
            if(param >= param2) return true;
        } else {
            if(param <= param2) return true;
        }
        return false;
    }

    /**
     * <p><b>Метод увеличения или уменьшения числа в зависимости от операции</b>
     * @param param1 <i>параметр, кторой нужно увеличить/уменьшить</i>
     * @param op <i>Операция</i>
     * @return изменённый параметр x
     */
    public static int nextLogic(int param1, String op) {
        return op.equals(">=") ? param1-1 : param1+1;
    }

}
