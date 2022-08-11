package mc.objects;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Класс описывающий любую стрелу игры BowWars</b>
 *
 * @see BWPObject
 */
public class BWPArrow extends BWPObject {

    /**
     * <b>Перечисление для типов стрел в игре BowWars</b>
     * @see mc.objects.BWPObject.Rarity Rarity
     * @see mc.objects.BWPBow.BowType BowType
     * @see mc.objects.BWPItem.ItemType ItemType
     * @see mc.objects.BWPInstrument.InstrumentType InstrumentType
     */
    public enum ArrowType {

        /**
         * <b>Обычная стрела</b>
         */
        COMMON_ARROW(Rarity.COMMON, Material.ARROW),
        /**
         * <b>Стрелы на моментальный урон</b>
         */
        INSTANT_DAMAGE_I(PotionType.INSTANT_DAMAGE, (byte)1, Rarity.RARE, Material.TIPPED_ARROW),
        INSTANT_DAMAGE_II(PotionType.INSTANT_DAMAGE, (byte)2, Rarity.EPIC, Material.TIPPED_ARROW),
        /**
         * <b>Стрела на отравления</b>
         */
        POISON_ARROW(PotionType.POISON, (byte)1, Rarity.RARE, Material.TIPPED_ARROW),
        /**
         * <b>Страла на слабость</b>
         */
        WEAKNESS_ARROW(PotionType.WEAKNESS, (byte)1, Rarity.UNCOMMON, Material.TIPPED_ARROW),
        /**
         * <b>Стрена на замедление</b>
         */
        SLOWNESS_ARROW(PotionType.SLOWNESS, (byte)1, Rarity.RARE, Material.TIPPED_ARROW),
        /**
         * <b>Спектральная стрела</b>
         */
        SPECTRAL_ARROW(Rarity.UNCOMMON, Material.SPECTRAL_ARROW),
        /**
         * <b>Тнт-стрела</b>
         */
        TNT_ARROW(Rarity.LEGENDARY, Material.ARROW),
        /**
         * <b>Строительная стрела</b>
         */
        BUILDING_ARROW(Rarity.EPIC, Material.ARROW),

        SPONGE_ARROW(Rarity.UNCOMMON, Material.ARROW);

        /**
         * <b>Уровень зачарования</b>
         */
        byte p;
        /**
         * <b>Эффект зелья</b>
         * @see PotionType
         */
        PotionType potion;
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
         * <b>Конструктор перечисления {@link BWPArrow.ArrowType ArrowType}</b>
         */
        ArrowType(Rarity rarity, Material material) {
            this.rarity = rarity;
            this.material = material;
        }
        /**
         * <b>Конструктор перечисления {@link BWPArrow.ArrowType ArrowType}</b>
         */
        ArrowType(PotionType potion, byte p, Rarity rarity, Material material) {
            this.potion = potion;
            this.p = p;
            this.rarity = rarity;
            this.material = material;
        }
    }

    /**
     * <b>Тип стрелы</b>
     */
    private ArrowType type;

    /**
     * <b>Конструктор класса {@link BWPArrow}</b>
     */
    public BWPArrow(ArrowType type, String nameDisplay, byte amount) {
        super(type.material, amount);
        this.type = type;
        ItemMeta meta = getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', this.type.rarity.loreDisplay));
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.type.rarity.color+nameDisplay));
        if(meta instanceof PotionMeta) ((PotionMeta)meta).setBasePotionData(new PotionData(type.potion));

        setItemMeta(meta);
    }
}
