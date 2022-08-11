package mc.objects;

import org.bukkit.Material;

/**
 * <b>Класс описывающий ресурс 'Лазурит'</b>
 * @see BWPResource
 */
public class BWPResLapis extends BWPResource{

    /**
     * Конструктор класса {@link BWPResLapis}
     * @param amount <i>Колличество в стаке</i>
     * @param nameDisplay <i>Название</i>
     */
    public BWPResLapis(byte amount, String nameDisplay) {
        super(Material.LAPIS_LAZULI, amount);
        this.rarity = Rarity.RARE;
        this.nameDisplay = nameDisplay;
        setItemMeta(BWPObject.createBWPObjectMeta(this, rarity, nameDisplay));
    }
}
