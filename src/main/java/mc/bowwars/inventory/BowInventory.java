package mc.bowwars.inventory;

import mc.objects.BWPBow;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BowInventory extends CommonInventory {

    public BowInventory(Player player, String name, byte index) {
        super(player, name);
        inventory.setItem(index, new ItemStack(Material.LIME_STAINED_GLASS_PANE));

        inventory.setItem(12, createRecipe(new BWPBow(BWPBow.BowType.COMMON_BOW, "Обычный лук"), "&fx1 &7&lПалка", null));
        inventory.setItem(21, createRecipe(new BWPBow(BWPBow.BowType.INFINITY_BOW, "Бесконечный лук"), "&fx8 &8&lКремний", null));
        inventory.setItem(13, createRecipe(new BWPBow(BWPBow.BowType.POWER_BOW_I, "Лук"), "&fx24 &7&lПалка", null));
        inventory.setItem(22, createRecipe(new BWPBow(BWPBow.BowType.POWER_BOW_II, "Лук"), "&fx6 &f&lНить", null));
        inventory.setItem(31, createRecipe(new BWPBow(BWPBow.BowType.POWER_BOW_III, "Лук"), "&fx16 &f&lНить", "&fx2 &8&lКремний"));
        inventory.setItem(14, createRecipe(new BWPBow(BWPBow.BowType.POWER_BOW_I, "Откидывающий лук"), "&fx20 &7&lПалка", "&fx6 &f&lНить"));
        inventory.setItem(23, createRecipe(new BWPBow(BWPBow.BowType.PUNCH_BOW_II, "Откидывающий лук"), "&fx8 &8&lКремний", null));
        inventory.setItem(32, createRecipe(new BWPBow(BWPBow.BowType.PUNCH_BOW_III, "Откидывающий лук"), "&fx6 &5&lАметист", null));
        inventory.setItem(15, createRecipe(new BWPBow(BWPBow.BowType.BUILDING_BOW, "Строительный лук"), "&fx12 &7&lПалка", "&fx6 &8&lКремний"));
        inventory.setItem(24, createRecipe(new BWPBow(BWPBow.BowType.TNT_BOW, "ТНТ-лук"), "&fx8 &5&lАметист", "&fx20 &8&lКремний"));
        inventory.setItem(33, createRecipe(new BWPBow(BWPBow.BowType.SPONGE_BOW, "Губка-лук"), "&fx2 &5&lАметист", "&fx8 &9&lЛазурит"));

    }


}
