package mc.commands;

import mc.bowwars.BowWars;
import mc.bowwars.ShowEnumConstant;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

public class CommandTrader implements CommandExecutor {



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Bukkit.getPlayer(sender.getName()).getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));

        //Если команда была отправлена не игроком
        if(!(sender instanceof Player)) return true;

        //Если недостаточно прав
        if(!sender.hasPermission("bwptrader")) {
            sender.sendMessage(BowWars.getString("messages.commands.do_not_have_permissions"));
            return true;
        }

        if(args.length == 0) return false;

        if(args[0].equalsIgnoreCase("set")) {
            if(args.length < 5) return false;

            Villager villager = (Villager) ((Player) sender).getWorld().spawnEntity(new Location(((Player) sender).getWorld() , CommandMain.tildaTransform(args[1], sender, 'x'), CommandMain.tildaTransform(args[2], sender, 'y'), CommandMain.tildaTransform(args[3], sender, 'z'), yawTrasform(args[4], sender), 0.0f), EntityType.VILLAGER);

            villager.setProfession(Villager.Profession.ARMORER);
            villager.setAI(false);
            villager.setCanPickupItems(false);
            villager.setBreed(false);
            villager.setCustomNameVisible(true);
            villager.setCustomName(ChatColor.translateAlternateColorCodes('&', "&l&eМагазин"));
            villager.setAdult();
            villager.setInvulnerable(true);
            villager.setGravity(false);
            villager.setMetadata("shop", new FixedMetadataValue(BowWars.getPlugin(), true));
            villager.setCollidable(false);

            return true;
        } else if(args[0].equalsIgnoreCase("remove")) {
            if(args.length < 2)  return false;
        }


        return false;
    }

    public static float yawTrasform(String param, CommandSender sender){
        return param.equals("~") ? Bukkit.getPlayer(sender.getName()).getLocation().getYaw() : Float.parseFloat(param);
    }
}
