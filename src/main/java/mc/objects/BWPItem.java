package mc.objects;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Класс описывающий любой предмет игры BowWars</b>
 *
 * @see BWPObject
 */
public class BWPItem extends BWPObject {

    /**
     * <b>Перечисление для типов предметов в игре BowWars</b>
     * @see mc.objects.BWPObject.Rarity Rarity
     * @see mc.objects.BWPArrow.ArrowType ArrowType
     * @see mc.objects.BWPBow.BowType BowType
     * @see mc.objects.BWPInstrument.InstrumentType InstrumentType
     */
    public enum ItemType {
        /**
         * <b>Ведро воды</b>
         */
        WATER_BUCKET(Rarity.LEGENDARY, Material.WATER_BUCKET, (byte)1, true),
        /**
         * <b>Губка</b>
         */
        SPONGE(Rarity.UNCOMMON, Material.SPONGE, (byte)1, true),
        /**
         * <b>Шерсть</b>
         */
        WOOL(Rarity.COMMON, Material.WHITE_WOOL, (byte)4, true),
        /**
         * <b>Платформа</b>
         */
        PLATFORM(Rarity.COMMON, Material.COBWEB, (byte)1, true),
        /**
         * <b>Супер плаформа</b>
         */
        SUPER_PLATFORM(Rarity.LEGENDARY, Material.ANVIL, (byte)1, true),
        /**
         * <b>Меч</b>
         */
        SWORD(Enchantment.DAMAGE_ALL, (byte)2, Rarity.UNCOMMON, Material.WOODEN_SWORD, (byte)1, true),
        /**
         * <b>Откидывающая палочка</b>
         */
        KNOCKBACK_STICK(Enchantment.KNOCKBACK, (byte) 1, Rarity.EPIC, Material.STICK, (byte)1, true),

        SHIELD(Rarity.RARE, Material.SHIELD, (byte)1, false),
        /**
         * <b>Кольчужный нагрудник</b>
         */
        CHAINMAIL_CHESTPLATE(Enchantment.PROTECTION_PROJECTILE, (byte)1, Rarity.EPIC, Material.CHAINMAIL_CHESTPLATE, (byte)1, true),
        /**
         * <b>Железный нагрудник</b>
         */
        IRON_CHESTPLATE(Enchantment.PROTECTION_PROJECTILE, (byte)2, Rarity.RARE, Material.IRON_CHESTPLATE, (byte)1, true),
        /**
         * <b>Алмазный нагрудник</b>
         */
        DIAMOND_CHESTPLATE(Enchantment.PROTECTION_PROJECTILE, (byte)3, Rarity.LEGENDARY, Material.DIAMOND_CHESTPLATE, (byte)1, true),

        GOLDEN_APPLE(Rarity.UNCOMMON, Material.GOLDEN_APPLE, (byte)1, true);

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
         * <b>Количество предмета</b>
         */
        byte amount;
        /**
         * <b>Редкость предмета</b>
         *
         * @see mc.objects.BWPObject.Rarity Rarity
         */
        Rarity rarity;
        /**
         * <b>Материал</b>
         *
         * @see Material
         */
        Material material;
        /**
         * <b>Разрушаемость</b>
         */
        boolean unbreakable;

        /**
         * <b>Конструктор перечисления {@link BWPBow.BowType BowType}</b>
         */
        ItemType(Rarity rarity, Material material, byte amount, boolean unbreakable) {
            this.rarity = rarity;
            this.material = material;
            this.amount = amount;
            this.unbreakable = unbreakable;
        }

        /**
         * <b>Конструктор перечисления {@link BWPBow.BowType BowType}</b>
         */
        ItemType(Enchantment enchantment, byte p, Rarity rarity, Material material, byte amount, boolean unbreakable) {
            this.enchantment = enchantment;
            this.p = p;
            this.rarity = rarity;
            this.material = material;
            this.amount = amount;
            this.unbreakable = unbreakable;
        }
    }

    /**
     * <b>Тип предмета</b>
     */
    private ItemType type;

    /**
     * <b>Конструктор класса {@link BWPItem}</b>
     */
    public BWPItem(ItemType type, String nameDisplay) {
        super(type.material, type.amount);
        this.type = type;
        ItemMeta meta = getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', this.type.rarity.loreDisplay));
        if(type.unbreakable) meta.setUnbreakable(true);
        if(type.enchantment != null) meta.addEnchant(type.enchantment, type.p, true);
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.type.rarity.color+nameDisplay));
        if(meta instanceof Damageable && this.type == ItemType.SHIELD) ((Damageable) meta).setDamage(284);
        setItemMeta(meta);
    }
}
