package mc.commands;

import mc.bowwars.BowWars;
import mc.game.TeamBase;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ConstructTeamSelector {

    public ItemStack item;

    public ConstructTeamSelector(TeamBase team){
        if(team.equals(BowWars.getGame().getRed())) {
            item = new ItemStack(Material.RED_WOOL, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cКоманда красных"));
            meta.setLore(CommandTeamSelection.setItemLore(BowWars.getGame().getRed()));
            item.setItemMeta(meta);
        } else if(team.equals(BowWars.getGame().getBlue())) {
            item = new ItemStack(Material.BLUE_WOOL, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9Команда синих"));
            meta.setLore(CommandTeamSelection.setItemLore(BowWars.getGame().getBlue()));
            item.setItemMeta(meta);
        } else if(team.equals(BowWars.getGame().getYellow())) {
            item = new ItemStack(Material.YELLOW_WOOL, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eКоманда жёлтых"));
            meta.setLore(CommandTeamSelection.setItemLore(BowWars.getGame().getYellow()));
            item.setItemMeta(meta);
        } else {
            item = new ItemStack(Material.GREEN_WOOL, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aКоманда зеленых"));
            meta.setLore(CommandTeamSelection.setItemLore(BowWars.getGame().getGreen()));
            item.setItemMeta(meta);
        }
    }

    public ConstructTeamSelector() {
        item = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cВыход из текущей команды"));
        item.setItemMeta(meta);
    }
}
