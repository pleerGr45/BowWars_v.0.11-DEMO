package mc.bowwars;

import mc.commands.*;
import mc.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.logging.Logger;

/**
 * <p><b>Класс Плагина игры BowWars</b>
 * <br>Запускает работу плагина
 * @author <b>pleer__gr45</b>
 * @version 0.3
 */
public final class BowWars extends JavaPlugin {

    /**
     * <p><b>Логгер</b>
     * <br>Выводит в консоль сообщения и предупреждения
     */
    Logger log = Logger.getLogger("Minecraft");
    /**
     * <p><b>Экземпляр класс {@link Game}</b>
     * @see Game#start(org.bukkit.entity.Player player)
     */
    private static Game game;
    /**
     * <p><b>Экземпляр класс {@link BowWars}</b>
     * <br>объект данного класса (плагин)
     */
    private static BowWars plugin;

    //get методы
    public static BowWars getPlugin() {return plugin;}
    public static Game getGame() {return game;}

    /**
     * <p><b>Метод включения прагина</b>
     * <br>Срабатывает при включении плагина
     * <p><b>●</b> Регистрация команд
     * <br><b>●</b> Регистрация обработчиков
     * <br><b>●</b> Загруска файлов
     */
    @Override
    public void onEnable() {
        plugin = this;
        game = new Game();

        //Обработччик команд
            Bukkit.getPluginManager().registerEvents(new BowWarsEventHandler(), this);
            log.info("[BWP] [25%]: main event handler load: BowWarsHandlerEvent");

        //Регистрация команд
            getCommand("bwp").setExecutor(new CommandMain());
            log.info("[BWP] [30%]: main command load: bwp");

            getCommand("bwpt").setExecutor(new CommandTeam());
            log.info("[BWP] [45%]: team command load: bwpt");

            getCommand("bwpts").setExecutor(new CommandTeamSelection());
            log.info("[BWP] [60%]: team selection command load: bwpts");

            getCommand("bwpgens").setExecutor(new CommandGens());
            log.info("[BWP] [75%]: generator resource command load: bwpgens");

            getCommand("bwptrader").setExecutor(new CommandTrader());
            log.info("[BWP] [90%]: trader command load: bwptrader");

            getCommand("bwptime").setExecutor(new CommandTime());

        //Успешный запуск
        log.info("[BWP] [100%]: Bow Wars Plugin enabled!");
    }

    /**
     * <p><b>Метод выключения прагина</b>
     * <br>Срабатывает при выключении плагина
     */
    @Override
    public void onDisable() {
        //Выключение
        log.info("[BWP] [100%]: Bow Wars Plugin disabled!");
    }

    /**
     * <p><b>Метод получения строк из конфига</b>
     * @param path <i>Путь к сообщению в config.yml</i>
     * @return Строка из config.yml, обработанная методом <br>{@link ChatColor#translateAlternateColorCodes(char, String)}
     */
    @Nullable
    public static String getString(String path){
        try {return ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(path));}
        catch(RuntimeException re) {return null;}
    }
}
