package mc.bowwars.inventory;

import mc.objects.BWPArrow;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArrowInventory extends CommonInventory{

    public ArrowInventory(Player player, String name, byte index) {
        super(player, name);
        inventory.setItem(index, new ItemStack(Material.LIME_STAINED_GLASS_PANE));

        inventory.setItem(12, createRecipe(new BWPArrow(BWPArrow.ArrowType.COMMON_ARROW, "Обычная стрела", (byte)2), "&fx4 &7&lПалка", null));
        inventory.setItem(21, createRecipe(new BWPArrow(BWPArrow.ArrowType.SPECTRAL_ARROW, "Спектральная стрела", (byte)4), "&fx4 &f&lСтрела", "&fx1 &9&lЛазурит"));
        inventory.setItem(13, createRecipe(new BWPArrow(BWPArrow.ArrowType.INSTANT_DAMAGE_I, "Стрела моментального урона I", (byte)4), "&fx4 &f&lСтрела", "&fx2 &9&lЛазурит"));
        inventory.setItem(22, createRecipe(new BWPArrow(BWPArrow.ArrowType.INSTANT_DAMAGE_II, "Стрела моментального урона II", (byte)4), "&fx4 &f&lСтрела", "&fx4 &9&lЛазурит"));
        inventory.setItem(14, createRecipe(new BWPArrow(BWPArrow.ArrowType.POISON_ARROW, "Стрела отравления", (byte)4), "&fx4 &f&lСтрела", "&fx2 &9&lЛазурит"));
        inventory.setItem(23, createRecipe(new BWPArrow(BWPArrow.ArrowType.WEAKNESS_ARROW, "Стрела слабости", (byte)4), "&fx4 &f&lСтрела", "&fx1 &9&lЛазурит"));
        inventory.setItem(32, createRecipe(new BWPArrow(BWPArrow.ArrowType.SLOWNESS_ARROW, "Стрела замедления", (byte)4), "&fx4 &f&lСтрела", "&fx3 &9&lЛазурит"));
        inventory.setItem(15, createRecipe(new BWPArrow(BWPArrow.ArrowType.TNT_ARROW, "Взрывная стрела", (byte)1), "&fx1 &5&lАметист", "&fx3 &9&lЛазурит"));
        inventory.setItem(24, createRecipe(new BWPArrow(BWPArrow.ArrowType.BUILDING_ARROW, "Строительная стрела", (byte)1), "&fx2 &8&lКремний", "&fx6 &f&lНить"));
        inventory.setItem(33, createRecipe(new BWPArrow(BWPArrow.ArrowType.SPONGE_ARROW, "Губка-стрела", (byte)1), "&fx2 &8&lКремний", "&fx1 &f&lНить"));
    }
}
