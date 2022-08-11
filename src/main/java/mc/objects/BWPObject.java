package mc.objects;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Класс описывающий любой объект игры BowWars</b>
 *
 * @see ItemStack
 */
public class BWPObject extends ItemStack {

    /**
     * <b>Перечисление для редкости объектов в игре BowWars</b>
     * @see mc.objects.BWPArrow.ArrowType ArrowType
     * @see mc.objects.BWPBow.BowType BowType
     * @see mc.objects.BWPItem.ItemType ItemType
     */
    public enum Rarity {
        /**
         * <b>Обычная редкость</b>
         * <br>Цвет - Белый <i>(&f)</i>
         * <br>Видимость - [C]
         */
        COMMON("&f", "&l[C]"),
        /**
         * <b>Необычная редкость</b>
         * <br>Цвет - Зелёный <i>(&a)</i>
         * <br>Видимость - [U]
         */
        UNCOMMON("&a", "&l[U]"),
        /**
         * <b>Редкая редкость</b>
         * <br>Цвет - Жёлтый <i>(&e)</i>
         * <br>Видимость - [R]
         */
        RARE("&e", "&l[R]"),
        /**
         * <b>Епическая редкость</b>
         * <br>Цвет - Синий <i>(&9)</i>
         * <br>Видимость - [E]
         */
        EPIC("&9", "&l[E]"),
        /**
         * <b>Легендарная редкость</b>
         * <br>Цвет - Фиолетовый <i>(&5)</i>
         * <br>Видимость - [L]
         */
        LEGENDARY("&5", "&l[L]");

        /**
         * <b>Описание</b>
         */
        String loreDisplay;
        /**
         * <b>Цвет</b>
         */
        String color;

        /**
         * <b>Конструктор перечисления {@link Rarity Rarity}</b>
         */
        Rarity(String color, String loreDisplay) {
            this.color = color;
            this.loreDisplay = ChatColor.translateAlternateColorCodes('&', color+loreDisplay);
        }
    }

    /**
     * <b>Конструктор класса {@link BWPObject}</b>
     * @param material <i>Материал</i>
     * @param amount <i>Колличество в стаке</i>
     */
    public BWPObject(Material material, byte amount) {
        super(material, amount);
    }

    /**
     * <b>Метод создания меты объекта</b>
     * @param object <i>Объект (экземпляр {@link BWPObject})</i>
     * @param rarity <i>Редкость</i>
     * @param nameDisplay <i>Название</i>
     * @return Мета объекта
     */
    public static ItemMeta createBWPObjectMeta(BWPObject object, Rarity rarity, String nameDisplay) {
        ItemMeta meta = object.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', rarity.loreDisplay));
        meta.setUnbreakable(true);
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', rarity.color+nameDisplay));
        return meta;
    }

}
