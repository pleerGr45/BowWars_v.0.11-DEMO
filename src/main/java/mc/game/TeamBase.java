package mc.game;

import mc.bowwars.BowWars;
import mc.commands.CommandTeam;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;

import javax.annotation.Nullable;

/**
 * <p><b>Класс описания команды</b>
 * <br>Описывает майнкрафт команду игры BowWars</b>
 * @see Game
 * @author <b>pleer__gr45</b>
 * @version 0.7.4
 */
public class TeamBase {

    /**
     * <p><b>Перечисление цвета команды</b>
     * <br>Используется при создании игры, а также в классе {@link TeamBase} для создания переменной <b>{@link TeamBase#tc}</b>
     * <p>Параметры:
     * <br><b>{@link TeamColor#RED}</b> - Команда красных
     * <br><b>{@link TeamColor#BLUE}</b> - Команда синих
     * <br><b>{@link TeamColor#YELLOW}</b> - Команда жёлтых
     * <br><b>{@link TeamColor#GREEN}</b> - Команда зелёных
     * @see BaseStatus
     */
    public enum TeamColor {
        RED(Material.RED_WOOL, 'c', "Красные"),
        BLUE(Material.BLUE_WOOL, '9', "Синие"),
        YELLOW(Material.YELLOW_WOOL, 'e', "Жёлтые"),
        GREEN(Material.GREEN_WOOL, 'a', "Зелёные");

        public char colorSelector;
        String name;
        public Material material;

        TeamColor(Material material, char colorSelector, String name) {
            this.material = material;
            this.colorSelector = colorSelector;
            this.name = name;
        }}

    /**
     * <p><b>Перечисление статуса команды</b>
     * <br>Используется при создании игры, а также в классе {@link TeamBase} для создания переменной {@link TeamBase#baseStatus}
     * <p>Параметры:
     * <br><b>{@link BaseStatus#BREAK}</b> - Сломанная команда
     * <br><b>{@link BaseStatus#DEATH}</b> - Погибшая команда
     * <br><b>{@link BaseStatus#ALIVE}</b> - ЖИвая команда
     * @see TeamColor
     */
    public enum BaseStatus {BREAK, DEATH, ALIVE}

    /**
     * <p><b>Класс описывающий мишень команды команды</b>
     * @since 0.7.4
     */
    public class Target implements Listener {
        /**
         * <p><b>Местоположение мишени</b>
         * @see Location
         */
        Location location;
        /**
         * <p><b>Данные блока</b>
         * @see BlockData
         */
        BlockData blockData = Bukkit.createBlockData(Material.TARGET);
        /**
         * <p><b>Статус мишени</b>
         * <br><b>true</b> - сломана <br><b>false</b> - цела
         */
        boolean destroyed = false;

        //get методы
        public boolean isDestroyed() {return destroyed;}
        public Location getLocation() {return location;}

        /**
         * <p><b>Конструктор класса {@link Target Target}</b>
         * @param x <i>Коордната <b>x</b></i>
         * @param y <i>Коордната <b>y</b></i>
         * @param z <i>Коордната <b>z</b></i>
         * @param world <i>Майнкрафт мир</i>
         */
        Target(int x, int y, int z, World world) {
            location = new Location(world, x, y, z);
            location.getWorld().setBlockData(location, blockData);
        }

        public void destroy(String name) {
            location.getWorld().setBlockData(location, Bukkit.createBlockData(Material.BEDROCK));
            location.getWorld().spawnParticle(Particle.SPELL_WITCH, location, 100);
            destroyed = true;

            if(getSizeTopTarget(targetStructure, targets) == 0) {
                setBaseStatus(BaseStatus.BREAK);
            }

            for(Player player : players) {
                if(player == null) continue;
                player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&cМИШЕНЬ УНИЧТОЖЕНА!"), null, 10, 20, 10);
                player.playSound(player, Sound.ENTITY_WITHER_SPAWN, 10, 2);
                if(baseStatus.equals(BaseStatus.BREAK)) {
                    player.getScoreboard().resetScores("Состояние: §a" + BaseStatus.ALIVE);
                    player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore("Состояние: §4" + CommandTeam.contentInTeam(player).getBaseStatus()).setScore(9);
                }

                player.getScoreboard().resetScores("Мишеней: §2"+(getSizeTopTarget(CommandTeam.contentInTeam(player).targetStructure, CommandTeam.contentInTeam(player).targets)+1));
                player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore("Мишеней: §2"+(getSizeTopTarget(CommandTeam.contentInTeam(player).targetStructure, CommandTeam.contentInTeam(player).targets))).setScore(8);
            }

            for (Player player : BowWars.getGame().getPlayers()) {

                player.getScoreboard().resetScores("§cК §r"+Game.tranformToScore((byte) (getSizeTopTarget(BowWars.getGame().getRed().targetStructure, BowWars.getGame().getRed().targets)+1)));
                player.getScoreboard().resetScores("§9С §r"+Game.tranformToScore((byte) (getSizeTopTarget(BowWars.getGame().getBlue().targetStructure, BowWars.getGame().getBlue().targets)+1)));
                player.getScoreboard().resetScores("§aЗ §r"+Game.tranformToScore((byte) (getSizeTopTarget(BowWars.getGame().getGreen().targetStructure, BowWars.getGame().getGreen().targets)+1)));
                player.getScoreboard().resetScores("§eЖ §r"+Game.tranformToScore((byte) (getSizeTopTarget(BowWars.getGame().getYellow().targetStructure, BowWars.getGame().getYellow().targets)+1)));

                player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore("§cК §r"+Game.tranformToScore(getSizeTopTarget(BowWars.getGame().getRed().targetStructure, BowWars.getGame().getRed().targets))).setScore(6);
                player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore("§9С §r"+Game.tranformToScore(getSizeTopTarget(BowWars.getGame().getBlue().targetStructure, BowWars.getGame().getBlue().targets))).setScore(5);
                player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore("§aЗ §r"+Game.tranformToScore(getSizeTopTarget(BowWars.getGame().getGreen().targetStructure, BowWars.getGame().getGreen().targets))).setScore(4);
                player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore("§eЖ §r"+Game.tranformToScore(getSizeTopTarget(BowWars.getGame().getYellow().targetStructure, BowWars.getGame().getYellow().targets))).setScore(3);
                }

            for(Player player : BowWars.getGame().getPlayers()) {

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7------------------------------------"));
                if (!baseStatus.equals(BaseStatus.BREAK)) player.sendMessage(ChatColor.translateAlternateColorCodes('&', "Мишень команды &" + tc.colorSelector + tc.name + "&r была уничтожена "+name+"&r! Осталось мишеней: &6&l" + getSizeTopTarget(targetStructure, targets)));
                else {player.sendMessage(ChatColor.translateAlternateColorCodes('&', "Мишень команды &" + tc.colorSelector + tc.name + "&r была уничтожена " + name + "! &3&lФИНАЛЬНОЕ УНИЧТОЖЕНИЕ!")); }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7------------------------------------"));
            }
        }

        public static byte getSizeTopTarget(byte targetStructure, Target[] targets) {
            byte size = 0;
            for(byte i = 0; i < targetStructure; i++) {
                if(!targets[i].isDestroyed()) size++;
            }
            return size;
        }
    }

    /**
     * <p><b>Цвет команды</b>
     */
    protected TeamColor tc;
    /**
     * <p><b>Координаты базы команды</b>
     * <br>Положение точки возрождения
     */
    protected Location location;
    /**
     * <p><b>Мишени</b>
     * <br>Массив хранить информацию о мишениях данной команды
     */
    protected Target[] targets = new Target[5];
    /**
     * <p><b>Игроки</b>
     * <br>Массив хранит информацию об игроках данной команды
     */
    protected Player[] players = new Player[4];
    /**
     * <p><b>Структура мишеней</b>
     * <br>Показывает количесво установленных мишеней
     * <p><b><i>5 - нужное значение для старта игры, используется в версии игры 4x4x4x4</i></b>
     */
    protected byte targetStructure = 0;
    /**
     * <p><b>Структура команды</b>
     * <br>Показывает количесво игроков в команде
     */
    protected byte teamStructure = 0;
    /**<p><b>Статус команды</b>
     * @see BaseStatus
     */
    protected BaseStatus baseStatus = BaseStatus.DEATH;
    /**
     * <p><b>Конструктор класса {@link TeamBase}</b>
     */
    public TeamBase(TeamColor tc) {this.tc = tc;}

    //get & set методы
    public void setBaseStatus(BaseStatus baseStatus) {this.baseStatus = baseStatus;}
    public void setCoords(int x, int y, int z, World world) {location = new Location(world, x, y, z);}
    public void setTargets(Target[] targets) {this.targets = targets;}
    public byte getTeamStructure() {return teamStructure;}
    public byte getTargetStructure() {return targetStructure;}
    public Player[] getPlayers() {return players;}
    public Target[] getTargets() {return targets;}
    public TeamColor getTc() {return tc;}
    @Nullable
    public Target getTarget() {
        if(targetStructure == 0) return null;
        return targets[targetStructure-1];
    }
    public BaseStatus getBaseStatus() {return baseStatus;}
    public Location getLocation() {return location;}

    public TeamBase() {}

    /**
     * <p><b>Метод добавления игрока в команду</b>
     * <br>Добавляет игрока в данную команду
     * @param player <i>Игрок, которого нужно добавить в команду</i>
     * @return <br><b>true</b> - успех <br><b>false</b> - неудача
     */
    public boolean addPlayer(Player player) {
        for(byte i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = player;
                teamStructure++;
                return true;
            }
        }
        return false;
    }

    /**
     * <p><b>Метод добавления мишени</b>
     * <br>Добавляет мишень в команду
     * @param x <i>Положение по оси <b>x</b></i>
     * @param y <i>Положение по оси <b>y</b></i>
     * @param z <i>Положение по оси <b>z</b></i>
     * @param world <i>Майнкрафт мир</i>
     * @return <br><b>true</b> - успех <br><b>false</b> - неудача
     */
    public boolean addTarget(int x, int y, int z, World world) {
        for(byte i = 0; i < targets.length; i++) {
            if(targets[i] == null) {
                targets[i] = new Target(x, y, z, world);
                targetStructure++;
                return true;
            }
        }
        return false;
    }

    /**
     * <p><b>Метод удаления игрока из команды</b>
     * <br>Удаляет нужного игрока из данной команды
     * @param player <i>Игрок, которого нужно удалить из команды</i>
     */
    public void removePlayer(Player player) {
        for(byte i = 0; i < teamStructure; i++){
            if(players[i].getUniqueId().equals(player.getUniqueId())){
                players[i] = null;
                teamStructure--;
                return;
            }
        }
    }

    /**
     * <p><b>Метод удаления мишени</b>
     * <br>Удаляет последнюю мишень данной команды
     * @return <br><b>true</b> - успех <br><b>false</b> - неудача
     */
    public boolean removeTarget() {
        for(byte i = (byte)(targetStructure-1); i >= 0; i--) {
            if(targets[i] != null) {
                targets[i].getLocation().getWorld().setBlockData(targets[i].getLocation(), Bukkit.createBlockData(Material.AIR));
                targets[i] = null;
                targetStructure--;
                return true;
            }
        }
        return false;
    }

    /**
     * <p><b>Метод команды</b>
     * <br>Все параметры команды = <b>null/0/VOID</b>
     * <p>Используется в методе {@link Game#stop()}
     */
    public void teamNulling() {
        for (byte i = 0; i < players.length; i++) {players[i] = null;}
        for (byte i = 0; i < targets.length; i++) {targets[i] = null;}
        setBaseStatus(TeamBase.BaseStatus.DEATH);
        location.setX(0); location.setY(0); location.setZ(0);
        teamStructure = 0;
        targetStructure = 0;
    }
}
