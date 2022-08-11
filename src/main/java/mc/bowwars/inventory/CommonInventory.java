package mc.bowwars.inventory;

import mc.objects.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommonInventory {

    public final static ItemStack PANEL = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
    Player player;
    String name;
    Inventory  inventory;

    public Inventory getInventory() {
        return inventory;
    }

    public CommonInventory(Player player, String name) {
        this.player = player;
        this.name = name;
        inventory = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', this.name));

        ItemMeta meta = PANEL.getItemMeta();
        meta.setDisplayName(" ");
        PANEL.setItemMeta(meta);

        for(byte i = 0; i < inventory.getSize(); i++) {
            if(i % 9 == 0 || i % 9 == 8 || i % 9 == 2 || (i > 0 && i < 8) || (i > 45 && i < 53)) inventory.setItem(i, PANEL);
        }

        inventory.setItem(8, createItem(Material.BARRIER, "&l&cВыход"));
        inventory.setItem(7, createItem(Material.CHEST, "&l&cСтандартная панель"));

        inventory.setItem(10, createItem(Material.BOW, "&l&2Луки"));
        inventory.setItem(19, createItem(Material.SPECTRAL_ARROW, "&l&eСтрелы"));
        inventory.setItem(28, createItem(Material.COBWEB, "&l&3Предметы"));
        inventory.setItem(37, createItem(Material.WOODEN_PICKAXE, "&l&dИнструменты"));

        PANEL.setItemMeta(meta);
    }

    public static ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(meta);
        return item;
    }

    protected static ItemStack createRecipe(BWPObject item, @NotNull String value1, @Nullable String value2) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = item.getItemMeta().getLore();
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6Цена: "));
        lore.add(ChatColor.translateAlternateColorCodes('&', value1));
        if (value2 != null) lore.add(ChatColor.translateAlternateColorCodes('&', value2));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static boolean tryBuy(ItemStack item, Player player){
        if(item == null) return false;

        if(item.getItemMeta().getLore().size() == 4) {
            if(contains(player.getInventory(), parseRes(item.getItemMeta().getLore().get(3)), parseAmount(item.getItemMeta().getLore().get(3)))) return true;
        } else if(item.getItemMeta().getLore().size() == 5){
            if(contains(player.getInventory(), parseRes(item.getItemMeta().getLore().get(3)), parseAmount(item.getItemMeta().getLore().get(3))) &&
               contains(player.getInventory(), parseRes(item.getItemMeta().getLore().get(4)), parseAmount(item.getItemMeta().getLore().get(4)))) return true;
        }
        return false;
    }

    @Nullable
    public static BWPObject parseRes(String s) {
        String resource = "";

        for(byte i = 0; i < s.length() ; i++) {
            if(s.charAt(i) == ' ') {
                for(byte j = (byte)(i+1); j < s.length(); j++) {
                    resource = resource + s.charAt(j);
                }
                break;
            }
        }

        if(ChatColor.translateAlternateColorCodes('&', "&7&lПалка").equals(resource)) return new BWPResStick((byte)1, "Палка");
        else if(ChatColor.translateAlternateColorCodes('&', "&f&lНить").equals(resource)) return new BWPResString((byte)1, "Нить");
        else if(ChatColor.translateAlternateColorCodes('&', "&8&lКремний").equals(resource)) return new BWPResFlint((byte)1, "Кремний");
        else if(ChatColor.translateAlternateColorCodes('&', "&9&lЛазурит").equals(resource)) return new BWPResLapis((byte)1, "Лазурит");
        else if(ChatColor.translateAlternateColorCodes('&', "&5&lАметист").equals(resource)) return new BWPResAmethyst((byte)1, "Аметист");
        else if(ChatColor.translateAlternateColorCodes('&', "&f&lСтрела").equals(resource)) return new BWPArrow(BWPArrow.ArrowType.COMMON_ARROW, "Обычная стрела", (byte)1);

        return null;
    }

    public static byte parseAmount(String s){
        String amount = "";

        amount = amount + s.charAt(3);
        if(Character.isDigit(s.charAt(4))) amount = amount + s.charAt(4);

        return Byte.parseByte(amount);
    }

    protected static boolean contains(Inventory inventory, ItemStack item, byte amount){

        byte invAmount = 0;

        for(byte i = 0; i < inventory.getSize(); i++) {
            if(inventory.getItem(i) != null) if(inventory.getItem(i).getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
               invAmount += inventory.getItem(i).getAmount();
            }
        }

        return invAmount >= amount ? true : false;
    }
}
