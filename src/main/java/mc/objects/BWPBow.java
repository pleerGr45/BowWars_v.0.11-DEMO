package mc.objects;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Класс описывающий любой лук игры BowWars</b>
 *
 * @see BWPObject
 */
public class BWPBow extends BWPObject {

    /**
     * <b>Перечисление для типов луков в игре BowWars</b>
     * @see mc.objects.BWPObject.Rarity Rarity
     * @see mc.objects.BWPArrow.ArrowType ArrowType
     * @see mc.objects.BWPItem.ItemType ItemType
     * @see mc.objects.BWPInstrument.InstrumentType InstrumentType
     */
   public enum BowType {
       /**
        * <b>Обычный лук</b>
        */
        COMMON_BOW(Rarity.COMMON),
       /**
        * <b>Луки на силу </b>
        */
        POWER_BOW_I((byte)1, Enchantment.ARROW_DAMAGE, Rarity.UNCOMMON),
        POWER_BOW_II((byte)2, Enchantment.ARROW_DAMAGE, Rarity.UNCOMMON),
        POWER_BOW_III((byte)3, Enchantment.ARROW_DAMAGE, Rarity.RARE),
       /**
        * <b>Луки на откидывание</b>
        */
        PUNCH_BOW_I((byte)1, Enchantment.ARROW_KNOCKBACK, Rarity.UNCOMMON),
        PUNCH_BOW_II((byte)2, Enchantment.ARROW_KNOCKBACK, Rarity.RARE),
        PUNCH_BOW_III((byte)3, Enchantment.ARROW_KNOCKBACK, Rarity.EPIC),
       /**
        * <b>Строительный лук</b>
        */
        BUILDING_BOW(Rarity.EPIC),
       /**
        * <b>Бесконечный лук</b>
        */
        INFINITY_BOW(Rarity.RARE),
       /**
        * <b>Тнт-лук</b>
        */
        TNT_BOW(Rarity.LEGENDARY),
       /**
        * <b>Губка-лук</b>
        */
        SPONGE_BOW(Rarity.EPIC);


       /**
        * <b>Уровень зачарования</b>
        */
       byte p;
       Enchantment ench;
        /**
         * <b>Редкость предмета</b>
         * @see mc.objects.BWPObject.Rarity Rarity
         */
       Rarity rarity;

       /**
        * <b>Конструктор перечисления {@link BowType BowType}</b>
        */
       BowType(Rarity rarity) {this.rarity = rarity;}
       /**
        * <b>Конструктор перечисления {@link BowType BowType}</b>
        */
       BowType(byte p, Enchantment ench, Rarity rarity) {
           this.p = p;
           this.ench = ench;
           this.rarity = rarity;
       }
    }

    /**
     * <b>Тип лука</b>
     */
    private BowType type;

    /**
     * <b>Конструктор класса {@link BWPBow}</b>
     */
    public BWPBow(BowType type, String nameDisplay) {
        super(Material.BOW, (byte) 1);
        this.type = type;
        ItemMeta meta = getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', this.type.rarity.loreDisplay));
        switch (type) {
            case POWER_BOW_I: case POWER_BOW_II: case POWER_BOW_III: case PUNCH_BOW_I: case PUNCH_BOW_II: case PUNCH_BOW_III:
            meta.addEnchant(type.ench, type.p, true);
        }
        meta.setUnbreakable(true);
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.type.rarity.color+nameDisplay));
        setItemMeta(meta);
    }
}
