package mc.bowwars.inventory;

import mc.objects.BWPInstrument;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InstrumentsInventory extends CommonInventory {
    public InstrumentsInventory(Player player, String name, byte index) {
        super(player, name);
        inventory.setItem(index, new ItemStack(Material.LIME_STAINED_GLASS_PANE));

        inventory.setItem(13, createRecipe(new BWPInstrument(BWPInstrument.InstrumentType.WOODEN_PICKAXE, "Кирка I", (byte)1), "&fx6 &7&lПалка", null));
        inventory.setItem(22, createRecipe(new BWPInstrument(BWPInstrument.InstrumentType.STONE_PICKAXE, "Кирка II", (byte)1), "&fx2 &f&lНить", null));
        inventory.setItem(31, createRecipe(new BWPInstrument(BWPInstrument.InstrumentType.IRON_PICKAXE, "Кирка III", (byte)1), "&fx2 &8&lКремний", "&fx2 &f&lНить"));
        inventory.setItem(40, createRecipe(new BWPInstrument(BWPInstrument.InstrumentType.DIAMOND_PICKAXE, "Кирка IV", (byte)1), "&fx4 &8&lКремний", "&fx4 &9&lЛазурит"));
        inventory.setItem(15, createRecipe(new BWPInstrument(BWPInstrument.InstrumentType.SUPER_PICKAXE, "Супер кирка", (byte)1), "&fx2 &5&lАметист", "&fx4 &9&lЛазурит"));
        inventory.setItem(14, createRecipe(new BWPInstrument(BWPInstrument.InstrumentType.WOODEN_AXE, "Топор I", (byte)1), "&fx6 &7&lПалка", null));
        inventory.setItem(23, createRecipe(new BWPInstrument(BWPInstrument.InstrumentType.STONE_AXE, "Топор II", (byte)1), "&fx2 &f&lНить", null));
        inventory.setItem(32, createRecipe(new BWPInstrument(BWPInstrument.InstrumentType.IRON_AXE, "Топор III", (byte)1), "&fx1 &8&lКремний", "&fx1 &f&lНить"));
        inventory.setItem(41, createRecipe(new BWPInstrument(BWPInstrument.InstrumentType.DIAMOND_AXE, "Топор IV", (byte)1), "&fx3 &8&lКремний", "&fx2 &9&lЛазурит"));
        inventory.setItem(24, createRecipe(new BWPInstrument(BWPInstrument.InstrumentType.SUPER_AXE, "Супер топор", (byte)1), "&fx2 &5&lАметист", "&fx2 &9&lЛазурит"));
        inventory.setItem(33, createRecipe(new BWPInstrument(BWPInstrument.InstrumentType.SHEARS, "Ножницы", (byte)1), "&fx15 &7&lПалка", null));
    }
}
