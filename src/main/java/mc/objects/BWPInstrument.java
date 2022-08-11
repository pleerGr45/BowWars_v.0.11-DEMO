package mc.objects;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Класс описывающий любой инструмент игры BowWars</b>
 *
 * @see BWPObject
 */
public class BWPInstrument extends BWPObject {

    /**
     * <b>Перечисление для типов интсрументов в игре BowWars</b>
     * @see mc.objects.BWPObject.Rarity Rarity
     * @see mc.objects.BWPBow.BowType BowType
     * @see mc.objects.BWPItem.ItemType ItemType
     * @see mc.objects.BWPArrow.ArrowType ArrowType
     */
    public enum InstrumentType {
        //Кирки
        WOODEN_PICKAXE(Material.WOODEN_PICKAXE, Rarity.COMMON),
        STONE_PICKAXE(Material.STONE_PICKAXE, Rarity.UNCOMMON),
        IRON_PICKAXE(Material.IRON_PICKAXE, Rarity.RARE),
        DIAMOND_PICKAXE(Material.DIAMOND_PICKAXE, Rarity.EPIC),
        SUPER_PICKAXE(Material.DIAMOND_PICKAXE, Rarity.LEGENDARY, Enchantment.DIG_SPEED, (byte)3),
        //Топоры
        WOODEN_AXE(Material.WOODEN_AXE, Rarity.COMMON),
        STONE_AXE(Material.STONE_AXE, Rarity.UNCOMMON),
        IRON_AXE(Material.IRON_AXE, Rarity.RARE),
        DIAMOND_AXE(Material.DIAMOND_AXE, Rarity.EPIC),
        SUPER_AXE(Material.DIAMOND_AXE, Rarity.LEGENDARY, Enchantment.DIG_SPEED, (byte)3),
        //Ножницы
        SHEARS(Material.SHEARS, Rarity.UNCOMMON);

        /**
         * <b>Уровень зачарования</b>
         */
        byte p;
        /**
         * <b>Зачарование</b>
         * @see Enchantment
         */
        Enchantment enchantment;
        /**
         * <b>Редкость предмета</b>
         * @see mc.objects.BWPObject.Rarity Rarity
         */
        Rarity rarity;
        /**
         * <b>Материал</b>
         * @see Material
         */
        Material material;

        /**
         * <b>Конструктор перечисления {@link BWPInstrument.InstrumentType InstrumentType}</b>
         */
        InstrumentType(Material material, Rarity rarity) {
            this.rarity = rarity;
            this.material = material;
        }

        /**
         * <b>Конструктор перечисления {@link BWPInstrument.InstrumentType InstrumentType}</b>
         */
        InstrumentType(Material material, Rarity rarity, Enchantment enchantment, byte p) {
            this.enchantment = enchantment;
            this.p = p;
            this.rarity = rarity;
            this.material = material;
        }
    }

    /**
     * <b>Тип инструмента</b>
     */
    InstrumentType type;

    /**
     * <b>Конструктор класса {@link BWPInstrument}</b>
     */
    public BWPInstrument(InstrumentType type, String nameDisplay, byte amount) {
        super(type.material, amount);
        this.type = type;
        ItemMeta meta = getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', this.type.rarity.loreDisplay));
        meta.setUnbreakable(true);
        if(type.enchantment != null) meta.addEnchant(type.enchantment, type.p, true);
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.type.rarity.color+nameDisplay));
        setItemMeta(meta);
    }
}
