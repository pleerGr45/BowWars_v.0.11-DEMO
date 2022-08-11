package mc.objects;

import org.bukkit.Material;

/**
 * <b>Класс описывающий ресурс 'Палка'</b>
 * @see BWPResource
 */
public class BWPResStick extends BWPResource{

    /**
     * Конструктор класса {@link BWPResStick}
     * @param amount <i>Колличество в стаке</i>
     * @param nameDisplay <i>Название</i>
     */
    public BWPResStick(byte amount, String nameDisplay) {
        super(Material.STICK, amount);
        this.rarity = Rarity.COMMON;
        this.nameDisplay = nameDisplay;
        setItemMeta(BWPObject.createBWPObjectMeta(this, rarity, nameDisplay));
    }
}
