package mc.bowwars.inventory;

import mc.objects.BWPItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemsInventory extends CommonInventory{
    public ItemsInventory(Player player, String name, byte index) {
        super(player, name);
        inventory.setItem(index, new ItemStack(Material.LIME_STAINED_GLASS_PANE));

        inventory.setItem(12, createRecipe(new BWPItem(BWPItem.ItemType.WOOL, "Шерсть"), "&fx2 &7&lПалка", null));
        inventory.setItem(21, createRecipe(new BWPItem(BWPItem.ItemType.SPONGE, "Губка"), "&fx2 &8&lКремний", "&fx1 &f&lНить"));
        inventory.setItem(13, createRecipe(new BWPItem(BWPItem.ItemType.PLATFORM, "Платформа"), "&fx4 &7&lПалка", null));
        inventory.setItem(22, createRecipe(new BWPItem(BWPItem.ItemType.SUPER_PLATFORM, "Супер платформа"), "&fx3 &5&lАметист", null));
        inventory.setItem(14, createRecipe(new BWPItem(BWPItem.ItemType.SWORD, "Меч"), "&fx2 &8&lКремний", null));
        inventory.setItem(23, createRecipe(new BWPItem(BWPItem.ItemType.KNOCKBACK_STICK, "Откидывающая палочка"), "&fx6 &f&lНить", null));
        inventory.setItem(32, createRecipe(new BWPItem(BWPItem.ItemType.SHIELD, "Щит"), "&fx3 &8&lКремний", "&fx3 &f&lНить"));
        inventory.setItem(15, createRecipe(new BWPItem(BWPItem.ItemType.CHAINMAIL_CHESTPLATE, "Кольчужный нагрудник"), "&fx24 &7&lПалка", null));
        inventory.setItem(24, createRecipe(new BWPItem(BWPItem.ItemType.IRON_CHESTPLATE, "Железный нагрудник"), "&fx2 &8&lКремний", "&fx8 &f&lНить"));
        inventory.setItem(33, createRecipe(new BWPItem(BWPItem.ItemType.DIAMOND_CHESTPLATE, "Алмазный нагрудник"), "&fx5 &5&lАметист", null));
        inventory.setItem(16, createRecipe(new BWPItem(BWPItem.ItemType.WATER_BUCKET, "Ведро воды"), "&fx1 &5&lАметист", null));
        inventory.setItem(25, createRecipe(new BWPItem(BWPItem.ItemType.GOLDEN_APPLE, "Золотое яблоко"), "&fx3 &f&lНить", null));
    }
}
