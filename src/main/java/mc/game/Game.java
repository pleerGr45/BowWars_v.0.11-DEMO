package mc.game;

import mc.bowwars.BowWars;
import mc.commands.CommandTeam;
import mc.game.events.EventLoader;
import mc.objects.BWPArrow;
import mc.objects.BWPBow;
import org.bukkit.*;
import org.bukkit.block.data.type.Piston;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.*;

import java.awt.desktop.AppReopenedEvent;
import java.util.*;

/**
 * <p><b>Класс игры BowWars</b>
 * <br>Создаёт и запускает игру BowWars
 * @see BowWars
 * @author <b>pleer__gr45</b>
 * @version 0.1
 */
public class Game {

    //Команды
    private TeamBase red = new TeamBase(TeamBase.TeamColor.RED);
    private TeamBase blue = new TeamBase(TeamBase.TeamColor.BLUE);
    private TeamBase yellow = new TeamBase(TeamBase.TeamColor.YELLOW);
    private TeamBase green = new TeamBase(TeamBase.TeamColor.GREEN);

    /**
     * <p><b>Список зарезервированных блоков</b>
     * <br>Данная коллекция используется для защиты карты от разрушения
     * @see Game#gens
     */
    private Set<Material> blocks = new HashSet<Material>();
    /**
     * <p><b>Список генераторов ресурсов</b>
     * <br>Данная коллекция используется для реализации генераторов ресурсов.
     * @see Game#blocks
     */
    private List<GeneratorsResource> gens = new ArrayList();
    /**
     * <p><b>Статус игры</b>
     * <br>Показывает состояние игры в данный момент
     * <p><b>true</b> - если игра запущена
     * <br><b>false</b> - если игра не запущена
     */
    private volatile boolean gameStatus = false;
    /**
     * <p><b>Время игры</b>
     * <br>Показывает время игры
     * <p>Требуется для работы с главным потоком игры <b>({@link Game#idGameThread})</b>
     * <br>В различные промежутки времени происходят различные события
     */
    private volatile short time = 0;
    /**
     * <p><b>ID Потока игры</b>
     * <br>Используется для определения главного потока игры
     */
    private volatile int idGameThread;
    /**
     * <p><b>ID Потока появления ресурсов</b>
     * <br>Используется для определения потока ресурсов
     */
    private volatile int idResourceThread;
    /**
     * <p><b>Игроки</b>
     * <br>Массив, включающий в себя всех игроков
     * <p>Используется для обращения ко всем игрокам
     * @see TeamBase#players
     */
    private volatile Player[] players;

    /**
     * <p><b>Центр карты (Местонахождение по умолчанию)</b>
     */
    private Location gameCenter;

    //set & get методы
    public void setCenter(int x, int y, int z, World world) {gameCenter = new Location(world, x, y, z);}
    public void setBlocks(Set<Material> blocks) {this.blocks = blocks;}
    public void setTime(short time) {this.time = time;}
    public TeamBase getRed() {return red;}
    public TeamBase getBlue() {return blue;}
    public TeamBase getYellow() {return yellow;}
    public TeamBase getGreen() {return green;}
    public Player[] getPlayers() {return players; }
    public boolean isGameStatus() {return gameStatus;}
    public Set<Material> getBlocks() {return blocks;}
    public Location getGameCenter() {return gameCenter;}
    public List<GeneratorsResource> getGens() {return gens;}

    /**
     * <p><b>Конструктор класса {@link Game}</b>
     */
    public Game() {}

    /**
     *  <p><b>Метод запуска игры</b>
     *  <br>Метод, запусткающий игру:
     *  <p>Происходит запуск главного <b>синхронного</b> потока игры BowWars
     *  <br><b>({@link BukkitScheduler#scheduleSyncRepeatingTask(Plugin, Runnable, long, long)})</b>, где происходят дольнейшие события игры
     */
    public void start(Player sender) {

        //Проверки
        if(red.getLocation() == null || blue.getLocation() == null || green.getLocation() == null || yellow.getLocation() == null || gameCenter == null) {sender.sendMessage(BowWars.getString("messages.commands.bwp_start_null_base_location")); return;}

        //Проверка на пустоту команд
        if (red.getTargetStructure() != 0) red.setBaseStatus(TeamBase.BaseStatus.ALIVE);
        if (blue.getTargetStructure() != 0) blue.setBaseStatus(TeamBase.BaseStatus.ALIVE);
        if (yellow.getTargetStructure() != 0) yellow.setBaseStatus(TeamBase.BaseStatus.ALIVE);
        if (green.getTargetStructure() != 0) green.setBaseStatus(TeamBase.BaseStatus.ALIVE);

        System.out.println(red.getBaseStatus());
        System.out.println(blue.getBaseStatus());
        System.out.println(green.getBaseStatus());
        System.out.println(yellow.getBaseStatus());

        if (red.getBaseStatus().equals(TeamBase.BaseStatus.ALIVE) && red.getTargetStructure() == 0) red.setBaseStatus(TeamBase.BaseStatus.BREAK);
        if (blue.getBaseStatus().equals(TeamBase.BaseStatus.ALIVE) && blue.getTargetStructure() == 0) blue.setBaseStatus(TeamBase.BaseStatus.BREAK);
        if (yellow.getBaseStatus().equals(TeamBase.BaseStatus.ALIVE) && yellow.getTargetStructure() == 0) yellow.setBaseStatus(TeamBase.BaseStatus.BREAK);
        if (green.getBaseStatus().equals(TeamBase.BaseStatus.ALIVE) && green.getTargetStructure() == 0) green.setBaseStatus(TeamBase.BaseStatus.BREAK);

        System.out.println(red.getBaseStatus());
        System.out.println(blue.getBaseStatus());
        System.out.println(green.getBaseStatus());
        System.out.println(yellow.getBaseStatus());

        //Настройка мира
        gameCenter.getWorld().setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        gameCenter.getWorld().setGameRule(GameRule.DO_MOB_SPAWNING, false);
        gameCenter.getWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);

        //Инициализировать игроков
        initializePlayers();

        for (Player player : players) {
            player.setGameMode(GameMode.ADVENTURE);

            player.setDisplayName("§"+CommandTeam.contentInTeam(player).getTc().colorSelector+CommandTeam.contentInTeam(player).getTc().name.charAt(0)+" "+player.getName()+"§r");

            Scoreboard s = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective o = s.registerNewObjective("tab", "dummy", "§6§lBowWars", RenderType.INTEGER);
            Objective kills = s.registerNewObjective("kills", "dummy", "Убийства", RenderType.INTEGER);
            Objective destroys = s.registerNewObjective("destroys", "dummy", "Разрушения", RenderType.INTEGER);
            Team t = s.registerNewTeam(CommandTeam.contentInTeam(player).getTc()+"");

            for (String i : s.getEntries()) {s.resetScores(i);}

            switch (CommandTeam.contentInTeam(player).getTc()) {
                case RED: t.setColor(ChatColor.RED); break;
                case BLUE: t.setColor(ChatColor.BLUE); break;
                case GREEN: t.setColor(ChatColor.YELLOW); break;
                case YELLOW: t.setColor(ChatColor.DARK_GREEN); break;
            }

            t.setDisplayName(CommandTeam.contentInTeam(player).getTc().name.charAt(0)+"");
            t.setPrefix(CommandTeam.contentInTeam(player).getTc().name.charAt(0)+"");
            t.setSuffix("§f");
            t.setAllowFriendlyFire(false);
            t.setCanSeeFriendlyInvisibles(true);

            o.setDisplayName("§6§lBowWars");

            o.getScore("").setScore(12);
            o.getScore("Команда: §"+CommandTeam.contentInTeam(player).getTc().colorSelector+CommandTeam.contentInTeam(player).getTc()).setScore(10);
            o.getScore("Состояние: §"+(CommandTeam.contentInTeam(player).getBaseStatus().equals(TeamBase.BaseStatus.ALIVE) ? "a" : "4")+CommandTeam.contentInTeam(player).getBaseStatus()).setScore(9);
            o.getScore("Мишеней: §2"+CommandTeam.contentInTeam(player).getTargetStructure()).setScore(8);
            o.getScore("").setScore(7);
            o.getScore("§cК §r"+tranformToScore(TeamBase.Target.getSizeTopTarget(red.getTargetStructure(), red.targets))).setScore(6);
            o.getScore("§9С §r"+tranformToScore(TeamBase.Target.getSizeTopTarget(blue.getTargetStructure(), blue.targets))).setScore(5);
            o.getScore("§aЗ §r"+tranformToScore(TeamBase.Target.getSizeTopTarget(green.getTargetStructure(), green.targets))).setScore(4);
            o.getScore("§eЖ §r"+tranformToScore(TeamBase.Target.getSizeTopTarget(yellow.getTargetStructure(), yellow.targets))).setScore(3);
            o.getScore("Убийств: §3").setScore(2);
            o.getScore("Разрушений: §3").setScore(1);
            o.getScore("§bcristalix.ru ").setScore(0);

            o.setDisplaySlot(DisplaySlot.SIDEBAR);

            player.setScoreboard(s);
        }

        //Статус игры = true
        gameStatus = true;

        //try {
            //Запуск асинхронного потока игры
            idGameThread = Bukkit.getScheduler().scheduleSyncRepeatingTask(BowWars.getPlugin(), () -> {
                //Начальное отображение
                if (time <= 20) for (Player player : players) {
                    player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&a"+(20 - time)), null, 1, 20, 1);
                    player.setLevel(20-time);
                    player.setExp(1-0.05f*time);
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 2);
                } //(Отсчёт) Отображение игрокам title @a[Только участникам игры] title [{"text":(20-time)}]

                //Телепорт всех игроков на свои базы
                if (time == 20) {

                    for(Player player : players) {
                        player.getInventory().clear();
                        player.getInventory().setItem(0, new BWPBow(BWPBow.BowType.COMMON_BOW, "Обычный лук"));
                        player.getInventory().setItem(9, new BWPArrow(BWPArrow.ArrowType.COMMON_ARROW, "Обычная стрела", (byte)8));
                        player.setGameMode(GameMode.SURVIVAL);
                        player.playSound(player, Sound.ENTITY_ENDER_DRAGON_GROWL, 5, 2);
                    }

                    commandSettings(red);
                    commandSettings(blue);
                    commandSettings(yellow);
                    commandSettings(green);

                    //Поток ресурсов
                    for(GeneratorsResource resource : gens) {
                        resource.setEnable(true);
                    }

                    idResourceThread = Bukkit.getScheduler().scheduleSyncRepeatingTask(BowWars.getPlugin(), () -> {
                        for(GeneratorsResource resource : gens) {
                            resource.addTime((byte)1);
                        }
                    }, 0, 20);

                    EventLoader.setVisible(true);
                    EventLoader.setPlayers(players);
                }

                if(time > 20) /*Конец если: */ if (getSizeAliveCommand() < 2) {end(); stop();}

                if (time >= 3600) {end(); stop();}

                for(Player player : players) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 1, 1, true, false));
                }

                //Загрузка событий
                EventLoader.update(time);
                //счёт
                updateScoreboard();
                //+секунда
                time++;
            }, 0, 20);
       // } catch (RuntimeException re) {
       //     for (Player player : Bukkit.getOnlinePlayers()) {
       //         player.sendMessage(BowWars.getString("messages.commands.bwp_start_game_thread_exception"));
       //     }
       // } finally {
       //     Bukkit.getScheduler().cancelTask(idGameThread);
       // }
    }

    private void updateScoreboard() {
        for(Player player : players) {
            player.getScoreboard().resetScores("Время: §6§l"+EventLoader.beautifulTimer(time, false));
            player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore("Время: §6§l"+EventLoader.beautifulTimer((short)(time+1), false)).setScore(11);
        }
    }

    public static String tranformToScore(byte targetStructure) {
        String score = "";

        for(byte i = 0; i < targetStructure; i++) {
            score += "§a▅";
        }

        for (byte i  = targetStructure; i < 5; i++) {
            score += "§c▅";
        }

        return score;
    }

    /**
     * <p><b>Метод остановки игры</b>
     * <br>Останавливает запущенную игру
     * <br>Устанавливает статус игры: <b>{@link Game#gameStatus} = false</b>
     * <br>Обнуляет все параметры предыдущей игры
     * <br>Выключает поток игры через {@link BukkitScheduler#cancelTask(int)}
     * @see Game#start(Player)
     */
    public void stop() {
        Bukkit.getScheduler().cancelTask(idResourceThread);
        Bukkit.getScheduler().cancelTask(idGameThread);

        for (Player player : players) {
            try {
            player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).unregister();
            } catch (RuntimeException runtimeException) {System.out.println("Exception in stop method (team): "+runtimeException.getCause());}
            try {
                player.getScoreboard().getTeam(CommandTeam.contentInTeam(player).getTc() + "").unregister();
            } catch (RuntimeException runtimeException) {System.out.println("Exception in stop method (team): "+runtimeException.getCause());}
        }

        gameStatus = false;
        time = 0;
        idResourceThread = 0;
        idGameThread = 0;
        for (byte i = 0; i < players.length; i++) players[i] = null;
        red.teamNulling();
        blue.teamNulling();
        yellow.teamNulling();
        green.teamNulling();
        EventLoader.setVisible(false);
        EventLoader.update((short)0);

    }

    /**
     * <p><b>Метод инициализации игроков</b>
     * <p>Метод используется для составления обшего массива {@link Game#players} из всех команд игры:
     * <br>{@link Game#red}, {@link Game#blue}, {@link Game#yellow}, {@link Game#green}
     * <p>В методе {@link Game#enumPlayers(TeamBase, byte)} из каждой это переменной вызывается метод {@link TeamBase#getPlayers()}, и просходит присваивание
     * @see Game#enumPlayers(TeamBase, byte)
     */
    private void initializePlayers() {
        players = new Player[(red.getTeamStructure()+blue.getTeamStructure())+(yellow.getTeamStructure()+green.getTeamStructure())];
        byte element = 0;
        element = enumPlayers(red, element);
        element = enumPlayers(blue, element);
        element = enumPlayers(yellow, element);
        enumPlayers(green, element);
    }

    /**
     * <p><b>Метод пересчёта игроков</b>
     * <br>используется в методе {@link Game#initializePlayers()}
     * @param team <i>Команда, в которой ведётся пересчёт игроков и присваивание:</i>
     * <br><b>{@link Game#players}[element] = team.{@link TeamBase#getPlayers()}[i]</b>
     * @param element - <i>Количество активных игроков <b>(Используется для работы с массивом)</b></i>
     * @return element
     */
    private byte enumPlayers(TeamBase team, byte element) {
        for(byte i = 0; i < team.getTeamStructure(); i++){
            players[element] = team.getPlayers()[i];
            element++;
        }
        return element;
    }

    /**
     * <p><b>Метод изначальной настройки команды</b>
     * <br> Телепортирует, устанавливает точку возрождения, выдаёт изначальные вещи игрокам в зависимости от их команды
     * @param team <i>Команда</i>
     */
    private void commandSettings(TeamBase team) {
        for(byte i = 0; i < team.getTeamStructure(); i++) {
            team.getPlayers()[i].setBedSpawnLocation(team.getLocation());
            team.getPlayers()[i].teleport(team.getLocation());

            team.getPlayers()[i].getInventory().setBoots(createItem(Material.LEATHER_BOOTS, team, "Ботинки"));
            team.getPlayers()[i].getInventory().setLeggings(createItem(Material.LEATHER_LEGGINGS, team, "Поножи"));
            team.getPlayers()[i].getInventory().setChestplate(createItem(Material.LEATHER_CHESTPLATE, team, "Куртка"));
        }
    }

    /**
     * <p><b>Метод настройки пердмета</b>
     * @param material <i>материал предмета предмета</i>
     * @param team <i>Команда</i>
     * @param name <i>Имя предмета</i>
     * @return Мета
     */
    public static ItemStack createItem(Material material, TeamBase team, String name){
        ItemStack item = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta)item.getItemMeta();
        meta.setUnbreakable(true);
        switch (team.getTc()) {
            case RED: meta.setColor(Color.RED); break;
            case BLUE: meta.setColor(Color.BLUE); break;
            case YELLOW: meta.setColor(Color.YELLOW); break;
            case GREEN: meta.setColor(Color.GREEN); break;
        }
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', name)));
        item.setItemMeta(meta);
        return item;
    }

    private byte getSizeAliveCommand() {
        byte size = 4;

        if (red.getBaseStatus().equals(TeamBase.BaseStatus.DEATH)) size--;
        if (blue.getBaseStatus().equals(TeamBase.BaseStatus.DEATH)) size--;
        if (yellow.getBaseStatus().equals(TeamBase.BaseStatus.DEATH)) size--;
        if (green.getBaseStatus().equals(TeamBase.BaseStatus.DEATH)) size--;

        return size;
    }

    private void end() {

        TeamBase team;
        String name;

        if(red.getBaseStatus().equals(TeamBase.BaseStatus.ALIVE) || red.getBaseStatus().equals(TeamBase.BaseStatus.BREAK)) {
            team = red;
            name = "Победила команда &c&lКрасных";
        } else if(blue.getBaseStatus().equals(TeamBase.BaseStatus.ALIVE)|| blue.getBaseStatus().equals(TeamBase.BaseStatus.BREAK)) {
            team = blue;
            name = "Победила команда &9&lСиних";
        } else if(yellow.getBaseStatus().equals(TeamBase.BaseStatus.ALIVE)|| yellow.getBaseStatus().equals(TeamBase.BaseStatus.BREAK)) {
            team = yellow;
            name = "Победила команда &e&lЖёлтых";
        } else if(green.getBaseStatus().equals(TeamBase.BaseStatus.ALIVE)|| green.getBaseStatus().equals(TeamBase.BaseStatus.BREAK)) {
            team = green;
            name = "Победила команда &a&lЗелёных";
        } else {return;}

        if(getSizeAliveCommand() > 1) {
            name = "&6&lНичья!";
        }

        for(Player player : players) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7================================="));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', name));
            String players = "";
            if (getSizeAliveCommand() == 1) {
                System.out.println("y");
                for (byte i = 0; i < team.getTeamStructure(); i++) players += team.getPlayers()[i].getName() + ", ";
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', players));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&lТоп по убийствам: "));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c1.&r "));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e2.&r "));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&63.&r "));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7================================="));
            }
        }
    }

}