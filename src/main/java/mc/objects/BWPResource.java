package mc.objects;

import org.bukkit.Material;

/**
 * <b>Класс описывающий любой ресурс игры BowWars</b>
 * @see BWPObject
 */
public class BWPResource extends BWPObject{

    /**
     * <b>Редкость</b>
     */
    Rarity rarity;
    /**
     * <b>Название</b>
     */
    String nameDisplay;

    /**
     * <b>Конструктор класса {@link BWPResource}</b>
     */
    public BWPResource(Material material, byte amount) {
        super(material, amount);
    }
}
