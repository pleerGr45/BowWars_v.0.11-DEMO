package mc.objects;

import org.bukkit.Material;

/**
 * <b>Класс описывающий ресурс 'Нитка'</b>
 * @see BWPResource
 */
public class BWPResString extends BWPResource{

    /**
     * Конструктор класса {@link BWPResString}
     * @param amount <i>Колличество в стаке</i>
     * @param nameDisplay <i>Название</i>
     */
    public BWPResString(byte amount, String nameDisplay) {
        super(Material.STRING, amount);
        this.rarity = Rarity.UNCOMMON;
        this.nameDisplay = nameDisplay;
        setItemMeta(BWPObject.createBWPObjectMeta(this, rarity, nameDisplay));
    }
}
