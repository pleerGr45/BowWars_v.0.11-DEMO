package mc.objects;

import org.bukkit.Material;

/**
 * <b>Класс описывающий ресурс 'Аметист'</b>
 * @see BWPResource
 */
public class BWPResAmethyst extends BWPResource{

    /**
     * Конструктор класса {@link BWPResAmethyst}
     * @param amount <i>Колличество в стаке</i>
     * @param nameDisplay <i>Название</i>
     */
    public BWPResAmethyst(byte amount, String nameDisplay) {
        super(Material.AMETHYST_SHARD, amount);
        this.rarity = Rarity.EPIC;
        this.nameDisplay = nameDisplay;
        setItemMeta(BWPObject.createBWPObjectMeta(this, rarity, nameDisplay));
    }
}
