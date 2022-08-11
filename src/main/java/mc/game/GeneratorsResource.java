package mc.game;

import mc.objects.BWPResource;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;

/**
 * <b>Класс описывающий генератор ресурсов</b>
 * @see Listener
 */
public class GeneratorsResource implements Listener {

    /**
     * <b>Активность</b>
     */
    protected boolean enable = false;
    /**
     * <b>Время до появления ресурса</b>
     */
    protected byte time = 0;
    /**
     * <b>Частота появления ресурсов (от 0)</b>
     */
    protected byte spawnTime;
    /**
     * <b>Тип появляющегося ресурса</b>
     */
    protected BWPResource type;
    /**
     * <b>Место генератора</b>
     */
    protected Location location;
    /**
     * <b>Стойка для брони (Отсчёт)</b>
     */
    protected ArmorStand stand;

    //get & set Методы
    public ArmorStand getStand() {return stand;}
    public boolean isEnable() {return enable;}
    public void setEnable(boolean enable) {this.enable = enable;}
    public void setSpawnTime(byte spawnTime) {this.spawnTime = spawnTime;}
    public BWPResource getType() {return type;}

    /**
     * <b>Конструктор класса {@link GeneratorsResource}</b>
     * @param location <i>Место появления ресурса</i>
     * @param spawnTime <i>Чатота появления ресурса</i>
     * @param type <i>Тип ресурса</i>
     */
    public GeneratorsResource(Location location, byte spawnTime, BWPResource type) {
        this.location = location;
        this.spawnTime = spawnTime;
        this.type = type;

        stand = (ArmorStand)location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        stand.setInvisible(true);
        stand.setInvulnerable(true);
        stand.setGravity(false);
        stand.setCustomNameVisible(true);

        stand.setCustomName(setTimer());
    }

    /**
     * <b>Метод таймера</b>
     * <br>Генерирует строку прогресса путём деления {@link GeneratorsResource#time}/{@link GeneratorsResource#spawnTime} <b>(Получение прогресса в процентах)</b>.
     * @return Строка прогресса появления ресурса
     */
    protected String setTimer() {
        if(!enable) return ChatColor.translateAlternateColorCodes('&', "&c|GENERATOR IS OFF|");

        byte progress = (byte)((double)(time)/spawnTime*10);
        String name = "&8[";

        for(byte i = 0; i < progress; i++) name+="&a=";
        for(byte i = progress; i < 10; i++) name+="&7=";

        name+="&8]";

        return ChatColor.translateAlternateColorCodes('&', name);
    }

    /**
     * <b>Метод добавления времени</b>
     * <br>Добавляет указанное время к {@link GeneratorsResource#time}
     * @param sec <i>Секунды</i>
     */
    public void addTime(byte sec) {
        if(!enable) {return;}
        if(time < spawnTime) time+=sec;
        else {spawnResource(); time = 0;}
        stand.setCustomName(setTimer());
    }

    /**
     * <b>Метод появления ресурса</b>
     * <br>Создаёт ресурс ({@link GeneratorsResource#type}) в указанном месте ({@link GeneratorsResource#location})
     */
    protected void spawnResource() {
        location.getWorld().dropItem(new Location(location.getWorld(), location.getX(), location.getY()+0.5, location.getZ()), type);
    }
}
