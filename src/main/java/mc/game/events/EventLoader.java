package mc.game.events;

import mc.bowwars.BowWars;
import mc.bowwars.ShowEnumConstant;
import mc.game.GeneratorsResource;
import mc.game.TeamBase;
import mc.objects.BWPResAmethyst;
import mc.objects.BWPResFlint;
import mc.objects.BWPResLapis;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * <b>Статичный загрузчик событий игры BowWars</b>
 *
 * @author <b>pleer__gr45</b>
 */
public class EventLoader {

    /**
     * <b>Перечисление событий</b>
     * <br>Создаёт и задаёт все неорбходимые параметры событиям
     * @see BowWarsEvent BowWarsEvent
     */
    public enum Events {

        //Улучшение генераторов
        LAPIS_L1((short)270, "&9Лазурит &1&lI &r&9уровня через - ", BarColor.BLUE, () -> {genUpgrade(BWPResLapis.class, (byte)25);}),
        LAPIS_L2((short)1200, "&9Лазурит &1&lII &r&9уровня через - ", BarColor.BLUE,() -> {genUpgrade(BWPResLapis.class, (byte)20);}),
        FLINT_L1((short)480, "&7Кремний &8&lI &r&7уровня через - ", BarColor.GREEN,() -> {genUpgrade(BWPResFlint.class, (byte)25);}),
        FLINT_L2((short)1410, "&7Кремний &8&lII &r&7уровня через - ", BarColor.GREEN,() -> {genUpgrade(BWPResFlint.class, (byte)20);}),
        AMETHYST_L1((short)800, "&dАметист &5&lI &r&dуровня через - ", BarColor.PURPLE,() -> {genUpgrade(BWPResAmethyst.class, (byte)70);}),
        AMETHYST_L2((short)1730, "&dАметист &5&lII &r&dуровня через - ", BarColor.PURPLE,() -> {genUpgrade(BWPResAmethyst.class, (byte)50);}),

        //События
        DESTROY_1((short)1800, "&eРазрушение &6&lI &r&eуровня через - ", BarColor.YELLOW, EventLoader::destroyTarget),
        GUARDIAN_1((short)1950, "&bХранитель &3&lI &r&bуровня через - ", BarColor.WHITE, () -> {guardianSpawn(BowWars.getGame().getGameCenter(), false);}),
        DESTROY_2((short)2100, "&eРазрушение &6&lII &r&eуровня через - ", BarColor.YELLOW, EventLoader::destroyTarget),
        GUARDIAN_2((short)2250, "&bХранитель &3&lII &r&bуровня через - ", BarColor.WHITE,() -> {guardianSpawn(BowWars.getGame().getGameCenter(), false);}),
        DESTROY_3((short)2400, "&eРазрушение &6&lIII &r&eуровня через - ", BarColor.YELLOW, EventLoader::destroyTarget),
        GUARDIAN_3((short)2550, "&bХранитель &3&lIII &r&bуровня через - ", BarColor.WHITE,() -> {guardianSpawn(BowWars.getGame().getGameCenter(), false);}),
        DESTROY_4((short)2700, "&eРазрушение &6&lIV &r&eуровня через - ", BarColor.YELLOW, EventLoader::destroyTarget),
        GUARDIAN_4((short)2850, "&bХранитель &3&lIV &r&bуровня через - ", BarColor.WHITE,() -> {guardianSpawn(BowWars.getGame().getGameCenter(), false);}),
        DESTROY_5((short)3000, "&eРазрушение &6&lV &r&eуровня через - ", BarColor.YELLOW, EventLoader::destroyTarget),
        SUPER_GUARDIAN((short)3150, "&3Супер хранитель &b через - ", BarColor.WHITE, () -> {guardianSpawn(BowWars.getGame().getGameCenter(), true);}),
        TOTAL_DESTRUCTION((short)3450, "&4Тотальное разрушение &c через - ", BarColor.RED, () -> {

        });

        /**
         * <b>Время срабатывания события</b>
         */
        @ShowEnumConstant(show = false)
        short time;
        /**
         * <b>Строка боссбара</b>
         */
        @ShowEnumConstant(show = false)
        String title;
        /**
         * <b>Цвет боссбара</b>
         */
        @ShowEnumConstant(show = false)
        BarColor color;
        /**
         * <b>Действие при срабатывании</b>
         */
        @ShowEnumConstant(show = false)
        BowWarsEvent event;

        Events(short time, String title, BarColor color, BowWarsEvent event) {
            this.time = time;
            this.title = title;
            this.color = color;
            this.event = event;
        }

        /**
         * <b>Метод получения всех констант данного перечисления</b>
         * <br>Метод берёт все константы, которые не были аннотированны {@link ShowEnumConstant @ShowEnumConstant<b>(<i>show = false</i>)</b>}
         * <p>Впоследствие метод помечен {@link Deprecated @Deprecated} <b>(Не используется)</b>
         * @see ShowEnumConstant @ShowEnumConstant
         * @return Колличество всех констант в перечислении
         */
        @Deprecated
        static byte getSizeElse() {
            byte size = (byte) Events.class.getDeclaredFields().length;
            for (Field field : Events.class.getDeclaredFields()) {
                for (Annotation annotation : field.getDeclaredAnnotations()) {
                    if (annotation.annotationType().equals(ShowEnumConstant.class)) {
                        if (((ShowEnumConstant) annotation).show() == false) size--;
                    }
                }
            }
            return size;
        }
    }

    /**
     * <b>Текущее событие</b>
     */
    private static Events currentUpdate;
    private static short oldTime = 0;
    private static BossBar bar = Bukkit.createBossBar(ChatColor.translateAlternateColorCodes('&', "&fВы играете в &6&lBowWars на &b&lCristalix.ru&f!"), BarColor.PINK, BarStyle.SOLID);

    //get & set Методы
    public static void setVisible(boolean visible) {bar.setVisible(visible);}
    public static void setPlayers(Player[] players) {for (Player player : players) {bar.addPlayer(player);}}

    /**
     * <b>Главный метод загрузчика {@link EventLoader}</b>
     * <br>Определяет какое событие проиходит сейчас, или сколько осталоь времени до ближайшего события
     * @param time <i>Время, прошедшее от начала (от 0)</i>
     */
    public static void update(short time) {

        if (time > 30 && time <= 270) {
            oldTime = 30;
            currentUpdate = Events.LAPIS_L1;
        } else if(time > 270 && time <= 480) {
            oldTime = 270;
            currentUpdate = Events.FLINT_L1;
        } else if(time > 480 && time <= 800) {
            oldTime = 480;
            currentUpdate = Events.AMETHYST_L1;
        } else if(time > 800 && time <= 1200) {
            oldTime = 800;
            currentUpdate = Events.LAPIS_L2;
        } else if(time > 1200 && time <= 1410) {
            oldTime = 1200;
            currentUpdate = Events.FLINT_L2;
        } else if(time > 1410 && time <= 1730) {
            oldTime = 1410;
            currentUpdate = Events.AMETHYST_L2;
        } else if(time > 1730 && time <= 1800) {
            oldTime = 1730;
            currentUpdate = Events.DESTROY_1;
        } else if(time > 1800 && time <= 1950) {
            oldTime = 1800;
            currentUpdate = Events.GUARDIAN_1;
        } else if(time > 1950 && time <= 2100) {
            oldTime = 1950;
            currentUpdate = Events.DESTROY_2;
        } else if(time > 2100 && time <= 2250) {
            oldTime = 2100;
            currentUpdate = Events.GUARDIAN_2;
        } else if(time > 2250 && time <= 2400) {
            oldTime = 2250;
            currentUpdate = Events.DESTROY_3;
        } else if(time > 2400 && time <= 2550) {
            oldTime = 2400;
            currentUpdate = Events.GUARDIAN_3;
        } else if(time > 2550 && time <= 2700) {
            oldTime = 2550;
            currentUpdate = Events.DESTROY_4;
        } else if(time > 2700 && time <= 2850) {
            oldTime = 2700;
            currentUpdate = Events.GUARDIAN_4;
        } else if(time > 2850 && time <= 3000) {
            oldTime = 2850;
            currentUpdate = Events.DESTROY_5;
        } else if(time > 3000 && time <= 3150) {
            oldTime = 3000;
            currentUpdate = Events.SUPER_GUARDIAN;
        } else if(time > 3150 && time <= 3450) {
            oldTime = 3150;
            currentUpdate = Events.TOTAL_DESTRUCTION;
        } else return;

        bar.setTitle(ChatColor.translateAlternateColorCodes('&', currentUpdate.title+beautifulTimer((short) (currentUpdate.time-time), true)));
        bar.setColor(currentUpdate.color);
        bar.setProgress((double)(time-oldTime)/(currentUpdate.time-oldTime));

        if(currentUpdate.time == time) currentUpdate.event.whenTriggered();

        if(time == 3450) setVisible(false);
    }

    public static String beautifulTimer(short sec, boolean color) {
        return (color ?"§f§l" : "")+(sec/60 < 10 ? "0"+sec/60 : sec/60)+":"+(sec%60 < 10 ? "0"+sec%60 : sec%60);
    }

    private static void genUpgrade(Class c, byte newSpawnTime) {
        for (GeneratorsResource resource : BowWars.getGame().getGens()) {
            if(resource.getType().getClass().equals(c)) resource.setSpawnTime(newSpawnTime);
        }
    }

    private static void destroyTarget() {
        BowWars.getGame().getRed().setTargets(destroy(BowWars.getGame().getRed()));
        BowWars.getGame().getRed().setTargets(destroy(BowWars.getGame().getBlue()));
        BowWars.getGame().getRed().setTargets(destroy(BowWars.getGame().getGreen()));
        BowWars.getGame().getRed().setTargets(destroy(BowWars.getGame().getYellow()));
    }

    private static TeamBase.Target[] destroy(TeamBase team) {
        if (team.getBaseStatus().equals(TeamBase.BaseStatus.ALIVE))
            for (byte i = 0; i < team.getTargetStructure(); i++) {
                if (!team.getTargets()[i].isDestroyed()) team.getTargets()[i].destroy("");
                break;
            }
        return team.getTargets();
    }

    private static void guardianSpawn(Location location, boolean super_guardian) {

        Guardian safer = (Guardian)(!super_guardian
            ? location.getWorld().spawnEntity(location, EntityType.GUARDIAN)
            : location.getWorld().spawnEntity(location, EntityType.ELDER_GUARDIAN)
        );

        safer.setCustomNameVisible(true);
        safer.setCustomName(!super_guardian ? ChatColor.translateAlternateColorCodes('&', "&dХранитель") : ChatColor.translateAlternateColorCodes('&', "&5Супер хранитель"));
        safer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(!super_guardian ? 400 : 600);
        safer.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(!super_guardian ? 16 : 32);

        safer.setHealth(!super_guardian ? 400 : 600);
    }

    /**
     * <b>Приватный конструктор загрузчика {@link EventLoader}</b>
     * <br><i>Невозможно создать экземляр данного класса, потому что он является статичным классом-загрузчиком</i>
     */
    private EventLoader() {}
}
