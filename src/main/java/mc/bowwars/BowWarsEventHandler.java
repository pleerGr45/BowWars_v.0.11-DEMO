package mc.bowwars;

import mc.bowwars.inventory.*;
import mc.commands.CommandTeam;
import mc.commands.ConstructTeamSelector;
import mc.game.Game;
import mc.game.TeamBase;
import mc.objects.*;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BowWarsEventHandler implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        if(CommandTeam.contentInTeam(e.getEntity()) == null) return;

        if(BowWars.getGame().isGameStatus()) {

            if (e.getEntity().getKiller() != null) e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', DeathMessages.getRandomDeathMessage(e.getEntity().getDisplayName(), e.getEntity().getKiller().getDisplayName())));
            else e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', e.getEntity().getPlayer().getDisplayName()+" &rумер"));
            e.getEntity().setGameMode(GameMode.SPECTATOR);

            if (CommandTeam.contentInTeam(e.getEntity()).getBaseStatus().equals(TeamBase.BaseStatus.ALIVE)) {
                AtomicReference<Integer> idDeathThread = new AtomicReference<>(0);
                AtomicReference<Byte> deathTime = new AtomicReference<>((byte) 3);

                idDeathThread.set(Bukkit.getScheduler().scheduleSyncRepeatingTask(BowWars.getPlugin(), () -> {
                    if (deathTime.get() == 3) {
                        try {
                            e.getEntity().teleport(BowWars.getGame().getGameCenter());
                        } catch (Exception exception) {
                            e.getEntity().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cЦентр не найден!"));
                        }
                    }
                    e.getEntity().sendTitle(ChatColor.translateAlternateColorCodes('&', "&c" + deathTime.get()), null, 1, 20, 1);
                    if (deathTime.get() <= 0) {
                        try {
                            e.getEntity().teleport(CommandTeam.contentInTeam(e.getEntity()).getLocation());
                            e.getEntity().getInventory().setItem(0, new BWPBow(BWPBow.BowType.COMMON_BOW, "Обычный лук"));
                            e.getEntity().getInventory().setItem(9, new BWPArrow(BWPArrow.ArrowType.COMMON_ARROW, "Обычная стрела", (byte) 8));
                            if (CommandTeam.contentInTeam(e.getEntity()) != null) {
                                e.getEntity().getInventory().setBoots(Game.createItem(Material.LEATHER_BOOTS, CommandTeam.contentInTeam(e.getEntity()), "Ботинки"));
                                e.getEntity().getInventory().setLeggings(Game.createItem(Material.LEATHER_LEGGINGS, CommandTeam.contentInTeam(e.getEntity()), "Поножи"));
                                e.getEntity().getInventory().setChestplate(Game.createItem(Material.LEATHER_CHESTPLATE, CommandTeam.contentInTeam(e.getEntity()), "Куртка"));
                            }
                        } catch (Exception exception) {
                            e.getEntity().sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&lВнимание!!! &6⚠ &cВы не состоите в команде поэтому вы были перемещены в стандартную точку &6⚠"));
                            e.getEntity().teleport(e.getEntity().getWorld().getSpawnLocation());
                        }
                        e.getEntity().setGameMode(GameMode.SURVIVAL);
                        cancelTask(idDeathThread.get());
                    }
                    if (deathTime.get() > 0) deathTime.set((byte) (deathTime.get() - 1));
                    else deathTime.set((byte) 3);
                }, 0, 20));
            } else if(CommandTeam.contentInTeam(e.getEntity()).getBaseStatus().equals(TeamBase.BaseStatus.BREAK)) {
                if (e.getEntity().getKiller() != null)
                    e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&3&lФИНАЛЬНАЯ СМЕРТЬ! &r"+DeathMessages.getRandomDeathMessage(e.getEntity().getDisplayName(), e.getEntity().getKiller().getDisplayName())));
                else e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&3&lФИНАЛЬНАЯ СМЕРТЬ! &r"+e.getEntity().getPlayer().getDisplayName()+" &rумер"));
                e.getEntity().setGameMode(GameMode.SPECTATOR);


                if(CommandTeam.contentInTeam(e.getEntity()).getTeamStructure() == 1) {
                    CommandTeam.contentInTeam(e.getEntity()).setBaseStatus(TeamBase.BaseStatus.DEATH);
                }

                CommandTeam.contentInTeam(e.getEntity()).removePlayer(e.getEntity());
            }

        }
    }

    private void cancelTask(int id) {Bukkit.getScheduler().cancelTask(id);}

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        if(e.getItemDrop().getItemStack().equals(CommonInventory.createItem(Material.CLOCK, "&6Выбор команды"))) {
            e.setCancelled(true);
            e.getPlayer().performCommand("bwpts");
        } else if (new BWPItem(BWPItem.ItemType.PLATFORM, "Платформа").equals(e.getItemDrop().getItemStack())) {
            e.getItemDrop().remove();
            replace(e.getPlayer().getLocation(), Material.COBWEB);
        } else if (new BWPItem(BWPItem.ItemType.SUPER_PLATFORM, "Супер платформа").equals(e.getItemDrop().getItemStack())) {
            e.getItemDrop().remove();
            replace(e.getPlayer().getLocation(), Material.HAY_BLOCK);
        }
    }

    @EventHandler
    public void onCrafting(CraftItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            if (CommandTeam.contentInTeam(((Player) e.getEntity())) == null || CommandTeam.contentInTeam(((Player) e.getDamager())) == null)
                return;
            if (CommandTeam.contentInTeam(((Player) e.getEntity())).equals(CommandTeam.contentInTeam(((Player) e.getDamager())))) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemSelect(InventoryClickEvent e) {

        if (new ConstructTeamSelector(BowWars.getGame().getRed()).item.equals(e.getCurrentItem())) {
            Bukkit.getPlayer(e.getWhoClicked().getName()).performCommand("bwpt join red");
            e.getWhoClicked().closeInventory();
            e.setCancelled(true);
        } else if (new ConstructTeamSelector(BowWars.getGame().getBlue()).item.equals(e.getCurrentItem())) {
            Bukkit.getPlayer(e.getWhoClicked().getName()).performCommand("bwpt join blue");
            e.getWhoClicked().closeInventory();
            e.setCancelled(true);
        } else if (new ConstructTeamSelector(BowWars.getGame().getGreen()).item.equals(e.getCurrentItem())) {
            Bukkit.getPlayer(e.getWhoClicked().getName()).performCommand("bwpt join green");
            e.getWhoClicked().closeInventory();
            e.setCancelled(true);
        } else if (new ConstructTeamSelector(BowWars.getGame().getYellow()).item.equals(e.getCurrentItem())) {
            Bukkit.getPlayer(e.getWhoClicked().getName()).performCommand("bwpt join yellow");
            e.getWhoClicked().closeInventory();
            e.setCancelled(true);
        } else if (new ConstructTeamSelector().item.equals(e.getCurrentItem())) {
            Bukkit.getPlayer(e.getWhoClicked().getName()).performCommand("bwpt leave");
            e.getWhoClicked().closeInventory();
            e.setCancelled(true);
        } else if(CommonInventory.PANEL.equals(e.getCurrentItem())) {
            e.setCancelled(true);
        } else {
            CommonInventory inventory = new CommonInventory((Player) e.getWhoClicked(), "&6Магазин");
            if (inventory.getInventory().getItem(8).equals(e.getCurrentItem())) {
                e.setCancelled(true);
                e.getWhoClicked().closeInventory();
            } else if (inventory.getInventory().getItem(7).equals(e.getCurrentItem())) {
                e.setCancelled(true);
                e.getWhoClicked().openInventory(new CommonInventory((Player) e.getWhoClicked(), "&6Магазин").getInventory());
            } else if (inventory.getInventory().getItem(10).equals(e.getCurrentItem())) {
                e.setCancelled(true);
                e.getWhoClicked().openInventory(new BowInventory((Player) e.getWhoClicked(), "&6Луки", (byte) 11).getInventory());
            } else if (inventory.getInventory().getItem(19).equals(e.getCurrentItem())) {
                e.setCancelled(true);
                e.getWhoClicked().openInventory(new ArrowInventory((Player) e.getWhoClicked(), "&6Стрелы", (byte) 20).getInventory());
            } else if (inventory.getInventory().getItem(28).equals(e.getCurrentItem())) {
                e.setCancelled(true);
                e.getWhoClicked().openInventory(new ItemsInventory((Player) e.getWhoClicked(), "&6Предметы", (byte) 29).getInventory());
            } else if (inventory.getInventory().getItem(37).equals(e.getCurrentItem())) {
                e.setCancelled(true);
                e.getWhoClicked().openInventory(new InstrumentsInventory((Player) e.getWhoClicked(), "&6Инструметы", (byte) 38).getInventory());
            } else if(e.getInventory().getItem(8) != null && e.getInventory().getItem(8).equals(CommonInventory.createItem(Material.BARRIER, "&l&cВыход"))){

                e.setCancelled(true);

                if(CommonInventory.tryBuy(e.getCurrentItem(), ((Player) e.getWhoClicked()).getPlayer())) {
                    e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&2Куплено: &r&l"+e.getCurrentItem().getItemMeta().getDisplayName()));

                    ItemStack item = CommonInventory.parseRes(e.getCurrentItem().getItemMeta().getLore().get(3));
                    byte amount = CommonInventory.parseAmount(e.getCurrentItem().getItemMeta().getLore().get(3));

                    for(byte i = 0; i < e.getWhoClicked().getInventory().getSize(); i++){
                        if(e.getWhoClicked().getInventory().getItem(i) != null) if(e.getWhoClicked().getInventory().getItem(i).getItemMeta().equals(item.getItemMeta())) {
                            if(e.getWhoClicked().getInventory().getItem(i).getAmount() > amount) {
                                item.setAmount(e.getWhoClicked().getInventory().getItem(i).getAmount()-amount);
                                e.getWhoClicked().getInventory().setItem(i, item); break;}
                            else if(e.getWhoClicked().getInventory().getItem(i).getAmount() == amount){ e.getWhoClicked().getInventory().clear(i); break;}
                            else e.getWhoClicked().getInventory().clear(i);
                        }
                    }

                    if(e.getCurrentItem().getItemMeta().getLore().size() == 5){
                        item = CommonInventory.parseRes(e.getCurrentItem().getItemMeta().getLore().get(4));
                        amount = CommonInventory.parseAmount(e.getCurrentItem().getItemMeta().getLore().get(4));
                        for(byte i = 0; i < e.getWhoClicked().getInventory().getSize(); i++){
                            if(e.getWhoClicked().getInventory().getItem(i) != null) if(e.getWhoClicked().getInventory().getItem(i).getItemMeta().equals(item.getItemMeta())) {
                                if(e.getWhoClicked().getInventory().getItem(i).getAmount() > amount) {
                                    item.setAmount(e.getWhoClicked().getInventory().getItem(i).getAmount()-amount);
                                    e.getWhoClicked().getInventory().setItem(i, item);  break;}
                                else if(e.getWhoClicked().getInventory().getItem(i).getAmount() == amount){e.getWhoClicked().getInventory().clear(i); break;}
                                else e.getWhoClicked().getInventory().clear(i);
                            }
                        }
                    }

                    //ItemMeta oldMeta = e.getCurrentItem().getItemMeta();

                    ItemStack buyItem = e.getCurrentItem().clone();

                    if(e.getCurrentItem().getType().equals(Material.WHITE_WOOL)) {
                        if (CommandTeam.contentInTeam(((Player) e.getWhoClicked()).getPlayer()) == null) {
                            buyItem.setType(Material.WHITE_WOOL);
                        } else if (CommandTeam.contentInTeam(((Player) e.getWhoClicked()).getPlayer()).getTc().equals(TeamBase.TeamColor.RED)) {
                            buyItem.setType(Material.RED_WOOL);
                        } else if (CommandTeam.contentInTeam(((Player) e.getWhoClicked()).getPlayer()).getTc().equals(TeamBase.TeamColor.BLUE)) {
                            buyItem.setType(Material.BLUE_WOOL);
                        } else if (CommandTeam.contentInTeam(((Player) e.getWhoClicked()).getPlayer()).getTc().equals(TeamBase.TeamColor.YELLOW)) {
                            buyItem.setType(Material.YELLOW_WOOL);
                        } else {
                            buyItem.setType(Material.GREEN_WOOL);
                        }
                    }

                    ItemMeta newMeta = buyItem.getItemMeta();
                    List<String> newLore = new ArrayList<>();
                    newLore.add(newMeta.getLore().get(0));
                    newMeta.setLore(newLore);
                    buyItem.setItemMeta(newMeta);

                    e.getWhoClicked().getInventory().addItem(buyItem);
                }
            }
        }
    }

    private static void replace(Location location, Material material) {

        Location p1 = new Location(location.getWorld(), location.getBlockX()-1, location.getBlockY()-1, location.getBlockZ()-1);
        Location p2 = new Location(location.getWorld(), location.getBlockX()+1, location.getBlockY()-1, location.getBlockZ()+1);

        for(int x = p1.getBlockX(); x <= p2.getBlockX(); x++) {
            for(int z = p1.getBlockZ(); z <= p2.getBlockZ(); z++) {
                if(location.getWorld().getBlockAt(x, location.getBlockY()-1, z).getType().equals(Material.AIR)) {
                    location.getWorld().setBlockData(x, location.getBlockY()-1, z, Bukkit.createBlockData(material));}
            }
        }
    }

    @EventHandler
    public void onWaterPlaced(PlayerBucketEmptyEvent e) {
        e.getPlayer().getInventory().remove(Material.BUCKET);
    }

    @EventHandler
    public void onBlockDestroyed(BlockBreakEvent e) {

        if(e.getBlock().getType().equals(Material.WET_SPONGE)) {
            e.setDropItems(false);
        }
        for(Material block : BowWars.getGame().getBlocks()) {
            if(e.getBlock().getType().equals(block)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplode(BlockExplodeEvent e) {

        for(byte i = 0; i < e.blockList().size(); i++) {
            for(Material blocks : BowWars.getGame().getBlocks()){
                e.blockList().removeIf((block) -> block.getType() == blocks);
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if ((e.getEntityType() == EntityType.ARROW || e.getEntityType() == EntityType.SPECTRAL_ARROW) && e.getEntity().getShooter() instanceof Player) {
            if (e.getEntity().hasMetadata("tnt_arrow")) {
                e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 3, false, true);
            } else if (e.getEntity().hasMetadata("sponge_arrow")) {
                if (e.getEntity().getLocation().getBlock().getType().equals(Material.AIR) || e.getEntity().getLocation().getBlock().getType().equals(Material.WATER)) {
                    e.getEntity().getWorld().setBlockData(e.getEntity().getLocation(), Bukkit.createBlockData(Material.SPONGE));
                    e.getEntity().remove();
                }
            } else if (e.getEntity().hasMetadata("build_arrow")) {
                e.getEntity().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, e.getEntity().getLocation(), 100);
                e.getEntity().setMetadata("cancel", new FixedMetadataValue(BowWars.getPlugin(), true));
            }

            checkTarget(BowWars.getGame().getRed(), e, ((Player) e.getEntity().getShooter()).getPlayer());
            checkTarget(BowWars.getGame().getBlue(), e, ((Player) e.getEntity().getShooter()).getPlayer());
            checkTarget(BowWars.getGame().getGreen(), e, ((Player) e.getEntity().getShooter()).getPlayer());
            checkTarget(BowWars.getGame().getYellow(), e, ((Player) e.getEntity().getShooter()).getPlayer());
        }
        e.getEntity().remove();
    }

    private void checkTarget(TeamBase team, ProjectileHitEvent e, Player player) {
        if(CommandTeam.contentInTeam(player).getTc().equals(team.getTc())) return;

        if(e.getHitBlock() != null && e.getHitBlock().getType().equals(Material.TARGET)) {
            for(byte i = 0; i < team.getTargetStructure(); i++) {
                if (e.getHitBlock().getLocation().equals(team.getTargets()[i].getLocation())) {
                    if(!team.getTargets()[i].isDestroyed()) {
                        team.getTargets()[i].destroy("&"+CommandTeam.contentInTeam(((Player)e.getEntity().getShooter())).getTc().colorSelector+((Player)e.getEntity().getShooter()).getName());
                        e.getEntity().remove();
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        if((e.getEntityType() == EntityType.ARROW || e.getEntityType() == EntityType.SPECTRAL_ARROW) && e.getEntity().getShooter() instanceof Player) {
            if(((Player) e.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().equals(new BWPBow(BWPBow.BowType.TNT_BOW, "ТНТ-лук").getItemMeta())) {
                if(((Player)e.getEntity().getShooter()).getInventory().getItemInOffHand().getType().equals(Material.ARROW) && ((Player)e.getEntity().getShooter()).getInventory().getItemInOffHand().getItemMeta().getDisplayName().equals(new BWPArrow(BWPArrow.ArrowType.TNT_ARROW, "Взрывная стрела", (byte)1).getItemMeta().getDisplayName()))
                    e.getEntity().setMetadata("tnt_arrow", new FixedMetadataValue(BowWars.getPlugin(), true));
                else e.setCancelled(true);
            } else if(((Player) e.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().equals(new BWPBow(BWPBow.BowType.SPONGE_BOW, "Губка-лук").getItemMeta())) {
                if(((Player)e.getEntity().getShooter()).getInventory().getItemInOffHand().getType().equals(Material.ARROW) && ((Player)e.getEntity().getShooter()).getInventory().getItemInOffHand().getItemMeta().getDisplayName().equals(new BWPArrow(BWPArrow.ArrowType.SPONGE_ARROW, "Губка-стрела", (byte)1).getItemMeta().getDisplayName()))
                    e.getEntity().setMetadata("sponge_arrow", new FixedMetadataValue(BowWars.getPlugin(), true));
                else e.setCancelled(true);
            } else if(((Player) e.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().equals(new BWPBow(BWPBow.BowType.BUILDING_BOW, "Строительный лук").getItemMeta())) {
                if(((Player)e.getEntity().getShooter()).getInventory().getItemInOffHand().getType().equals(Material.ARROW) && ((Player)e.getEntity().getShooter()).getInventory().getItemInOffHand().getItemMeta().getDisplayName().equals(new BWPArrow(BWPArrow.ArrowType.BUILDING_ARROW, "Строительная стрела", (byte)1).getItemMeta().getDisplayName()))
                    e.getEntity().setMetadata("build_arrow", new FixedMetadataValue(BowWars.getPlugin(), true));
                else e.setCancelled(true);
                AtomicReference<Integer> buildArrowThread = new AtomicReference<>();
                AtomicReference<Location> location = new AtomicReference<>();

                buildArrowThread.set(Bukkit.getScheduler().scheduleSyncRepeatingTask(BowWars.getPlugin(), () -> {

                    if(e.getEntity().hasMetadata("cancel")) cancelTask(buildArrowThread.get());

                    location.set(e.getLocation().clone());

                    try {Thread.sleep(150);
                    } catch (InterruptedException interruptedException) {interruptedException.printStackTrace();}
                    e.getEntity().getWorld().setBlockData(location.get(), (Bukkit.createBlockData(
                        CommandTeam.contentInTeam(((Player) e.getEntity().getShooter())) != null
                        ? CommandTeam.contentInTeam(((Player) e.getEntity().getShooter())).getTc().material
                        : Material.WHITE_WOOL)));
                }, 2, 10));
            }
        }
    }

    @EventHandler
    public void onOpenShop(PlayerInteractEntityEvent e) {
        if(e.getRightClicked().hasMetadata("shop")) {
            e.setCancelled(true);
            e.getPlayer().openInventory(new CommonInventory(e.getPlayer(), "&6Магазин").getInventory());
        }
    }

}
