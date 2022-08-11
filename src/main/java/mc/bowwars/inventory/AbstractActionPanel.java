package mc.bowwars.inventory;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Deprecated
public abstract class AbstractActionPanel extends ItemStack {

    @Deprecated
    public AbstractActionPanel(Material material, String name){
        setType(material);
        setAmount(1);

        ItemMeta meta = getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        setItemMeta(meta);
    }

    @Deprecated
    public abstract Inventory changeInventory();

}
