package mc.objects;

import org.bukkit.Material;

/**
 * <b>Класс описывающий ресурс 'Кремний'</b>
 * @see BWPResource
 */
public class BWPResFlint extends BWPResource{

    /**
     * Конструктор класса {@link BWPResFlint}
     * @param amount <i>Колличество в стаке</i>
     * @param nameDisplay <i>Название</i>
     */
    public BWPResFlint(byte amount, String nameDisplay) {
        super(Material.FLINT, amount);
        this.rarity = Rarity.RARE;
        this.nameDisplay = nameDisplay;
        setItemMeta(BWPObject.createBWPObjectMeta(this, rarity, nameDisplay));
    }
}
