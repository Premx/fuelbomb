/*
 * Copyright (C) 2015 Premx <premx at web.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package it.premx.fuelbomb;

/**
 *
 * @author Premx
 */
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import org.bukkit.inventory.ItemStack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public final class fuelbomb extends JavaPlugin
        implements Listener {

    protected Logger l0g;

    public static final String ITEM_IDENTIFIER = "fuelbomb";
    public static String ITEM_NAME = ChatColor.GOLD + "Fuelbomb";
    public static final Material ITEM_MATERIAL = Material.GLASS_BOTTLE;
    private static final String Plugin = null;
    public String commandname;
    private Random random;
    public String worldguardsupport;
    public static WorldGuardPlugin wg;
    public static String broadcastString;
    public static boolean news = false;
    public static String prefix = ChatColor.AQUA + "[Fuelbomb] ";
    private static fuelbomb plugin;

    public void onEnable() {
        plugin = this;
        /*
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        String s = fuelbomb.this.getConfig().getString("item_name");

        ITEM_NAME = ChatColor.translateAlternateColorCodes('&', s);

        commandname = fuelbomb.this.getConfig().getString("give_command_name");
        if (commandname == " " || commandname == null) {
            commandname = "give";
        }

        worldguardsupport = fuelbomb.this.getConfig().getString("worldguard_support");
        if (!(worldguardsupport == "true" || worldguardsupport == "false")) {
            worldguardsupport = "false";
        }
        if (worldguardsupport == "true") {

            wg = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
            if (wg == null) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Fuelbomb] " + ChatColor.RED + "!! Fatal Error !!");
                Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Fuelbomb] " + ChatColor.RED + "!! Config -> worldguard_support: true !! Worldguard not found :(");
                Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Fuelbomb] " + ChatColor.RED + "!! Fatal Error !!");
                worldguardsupport = "false";
            }
        }

        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        this.l0g = this.getLogger();
        this.random = new Random(System.currentTimeMillis());
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Fuelbomb]" + ChatColor.GOLD + " has been" + ChatColor.GREEN + " enabled" + ChatColor.WHITE + " | " + ChatColor.RED + "Plugin by Premx");
        this.saveDefaultConfig();
        addRecipe();

    }

    public void addRecipe() {
        ItemStack is = new ItemStack(ITEM_MATERIAL);
        ItemMeta im = is.getItemMeta();

        im.setDisplayName(ITEM_NAME);
        is.setItemMeta(im);

        ShapedRecipe fuelbomb = new ShapedRecipe(is);
        fuelbomb.shape(new String[]{"P", "G", "B"});
        fuelbomb.setIngredient('P', Material.PAPER);
        fuelbomb.setIngredient('G', Material.COAL);
        fuelbomb.setIngredient('B', Material.GLASS_BOTTLE);

        Bukkit.addRecipe(fuelbomb);
    }

    public void onDisable() {
        //fuelbomb.this.saveConfig(); 
        plugin = null;
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Fuelbomb]" + ChatColor.GOLD + " has been" + ChatColor.RED + " disabled" + ChatColor.WHITE + " | " + ChatColor.BLUE + "Plugin by Premx");

        //HandlerList.unregisterAll(this);
    }

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fuelbomb")) {

            if (args.length == 0) {
                PluginDescriptionFile pdf = this.getDescription();
                sender.sendMessage(ChatColor.BLUE + "==========" + ChatColor.GREEN + "[FuelBomb]" + ChatColor.BLUE + "==========");
                sender.sendMessage(ChatColor.GREEN + "Plugin by Premx");
                sender.sendMessage(ChatColor.GREEN + "Version: " + ChatColor.DARK_GREEN + pdf.getVersion());
                sender.sendMessage(ChatColor.GOLD + "Have Fun");
                sender.sendMessage(ChatColor.BLUE + "==========" + ChatColor.GREEN + "[FuelBomb]" + ChatColor.BLUE + "==========");

            } else {
                if (args[0].equalsIgnoreCase("r") || args[0].equalsIgnoreCase("reload")) {

                    if (sender.hasPermission("fuelbomb.reload")) {
                        try {
                            fuelbomb.this.reloadConfig();
                            String s = fuelbomb.this.getConfig().getString("item_name");
                            commandname = fuelbomb.this.getConfig().getString("give_command_name");
                            if (commandname == "") {
                                commandname = "give";
                            }
                            ITEM_NAME = ChatColor.translateAlternateColorCodes('&', s);
                            worldguardsupport = fuelbomb.this.getConfig().getString("worldguard_support");
                            if (!(worldguardsupport == "true" || worldguardsupport == "false")) {
                                worldguardsupport = "false";
                            }
                            sender.sendMessage(ChatColor.AQUA + "[Fuelbomb]" + ChatColor.GOLD + " Config reloaded!");
                        } catch (Exception e) {
                            sender.sendMessage(ChatColor.AQUA + "[Fuelbomb]" + ChatColor.GOLD + " Could not reload.");
                        }

                    } else {
                        sender.sendMessage(ChatColor.AQUA + "[Fuelbomb]" + ChatColor.GOLD + " Sorry, but you don't have permission to do that.");
                    }
                }

                if (args[0].equalsIgnoreCase(commandname)) {
                    if (args.length == 1) {
                        if (sender.hasPermission("fuelbomb.give")) {
                            Player player = (Player) sender;
                            ItemStack is = new ItemStack(ITEM_MATERIAL);
                            ItemMeta im = is.getItemMeta();
                            im.setDisplayName(ITEM_NAME);
                            is.setItemMeta(im);
                            player.getInventory().addItem(is);
                            sender.sendMessage(ChatColor.AQUA + "[Fuelbomb]" + ChatColor.GOLD + " You got a Fuelbomb.");
                        } else {
                            sender.sendMessage(ChatColor.AQUA + "[Fuelbomb]" + ChatColor.RED + " You're not allowed to do this.");
                        }
                    } else {

                        try {

                            Player p = Bukkit.getServer().getPlayer(args[1]);

                            ItemStack is = new ItemStack(ITEM_MATERIAL);
                            ItemMeta im = is.getItemMeta();
                            im.setDisplayName(ITEM_NAME);
                            is.setItemMeta(im);
                            p.getInventory().addItem(is);
                            p.sendMessage(ChatColor.AQUA + "[Fuelbomb]" + ChatColor.GOLD + " You got a Fuelbomb.");
                        } catch (Exception e) {
                            sender.sendMessage(ChatColor.AQUA + "[Fuelbomb]" + ChatColor.GOLD + " Player not found.");
                        }

                    }
                }

            }
            return true;

        }
        return false;
    }

    @EventHandler
    public void onfuelbomb(PlayerInteractEvent e) {

        final Player p = e.getPlayer();

        if (e.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }
        if (!p.hasPermission("fuelbomb.use")) {
            return;
        }
        ItemStack item = p.getItemInHand();

        if (item == null) {
            return;
        }
        if (item.getType() != ITEM_MATERIAL) {
            return;
        }
        if ((!item.hasItemMeta()) || (!item.getItemMeta().hasDisplayName())) {
            return;
        }
        if (!p.getItemInHand().getItemMeta().getDisplayName().equals(ITEM_NAME)) {
            return;
        }

        World w = p.getWorld();

        Location loc = p.getLocation();

        if (worldguardsupport == "true") {
            if (!(worldguard_check.checkPerms(p, loc))) {
                return;
            }
        }

        ItemStack is = new ItemStack(ITEM_MATERIAL);

        ItemMeta im = is.getItemMeta();
        im.setDisplayName("fuelbomb" + ChatColor.RED + this.random.nextInt(10000));
        is.setItemMeta(im);

        final Item i = w.dropItem(p.getEyeLocation(), is);

        i.setMetadata("fuelbomb", new FixedMetadataValue(this, Integer.valueOf(0)));

        i.setVelocity(p.getEyeLocation().getDirection());

        if (p.getGameMode() != GameMode.CREATIVE) {

            int anzahl = item.getAmount();

            if (anzahl >= 2) {

                item.setAmount(item.getAmount() - 1);

            } else {
                p.setItemInHand(null);

            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {

                Location groundloc = i.getLocation();

                groundloc.setY(groundloc.getY() - 1);

                Material groundtex = groundloc.getBlock().getType();

                Location directloc = i.getLocation();
                Vector directvec = i.getLocation().getDirection();
                directvec.multiply(2);
                directloc = directvec.toLocation(i.getWorld());

                Material directtex = directloc.getBlock().getType();

                if (groundtex == Material.AIR || groundtex == Material.LONG_GRASS) {
                    return;
                }

                if (worldguardsupport == "true") {
                    if (!(worldguard_check.checkPerms(p, i.getLocation()))) {
                        i.remove();
                        return;
                    }
                }

                switch (groundtex.name()) {

                    case "WATER":

                    case "STATIONARY_LAVA":

                    case "STATIONARY_WATER":

                    case "LAVA": {

                        i.remove();

                        return;

                    }

                    case "LongGrass": {

                        i.getLocation().getBlock().setType(Material.FIRE);

                    }

                    default: {

                        i.getLocation().getBlock().setType(Material.FIRE);

                        break;

                    }

                }

                for (Entity entity : i.getNearbyEntities(3, 3, 3)) {

                    entity.setFireTicks(300);

                }

                Location droploc = i.getLocation();
                droploc.setY(i.getLocation().getY() + 1);

                FallingBlock fb = i.getWorld().spawnFallingBlock(droploc, Material.FIRE, (byte) 0);
                FallingBlock fb1 = i.getWorld().spawnFallingBlock(droploc, Material.FIRE, (byte) 0);
                FallingBlock fb2 = i.getWorld().spawnFallingBlock(droploc, Material.FIRE, (byte) 0);

                FallingBlock fb3 = i.getWorld().spawnFallingBlock(droploc, Material.FIRE, (byte) 0);
                FallingBlock fb4 = i.getWorld().spawnFallingBlock(droploc, Material.FIRE, (byte) 0);
                FallingBlock fb5 = i.getWorld().spawnFallingBlock(droploc, Material.FIRE, (byte) 0);

                float x = (float) (Math.random() * 0.4D);
                float y = (float) (Math.random() * 0.4D);
                float z = (float) (Math.random() * 0.4D);

                float x1 = (float) (Math.random() * 0.4D);
                float y1 = (float) (Math.random() * 0.4D);
                float z1 = (float) (Math.random() * 0.4D);

                float x2 = (float) (Math.random() * 0.4D);
                float y2 = (float) (Math.random() * 0.4D);
                float z2 = (float) (Math.random() * 0.4D);

                float x3 = (float) (Math.random() * -0.4D);
                float y3 = (float) (Math.random() * 0.4D);
                float z3 = (float) (Math.random() * -0.4D);

                float x4 = (float) (Math.random() * -0.4D);
                float y4 = (float) (Math.random() * 0.4D);
                float z4 = (float) (Math.random() * -0.4D);

                float x5 = (float) (Math.random() * -0.4D);
                float y5 = (float) (Math.random() * 0.4D);
                float z5 = (float) (Math.random() * -0.4D);

                fb.setVelocity(new Vector(x, y, z));
                fb1.setVelocity(new Vector(x1, y1, z1));
                fb2.setVelocity(new Vector(x2, y2, z2));

                fb3.setVelocity(new Vector(x3, y3, z3));
                fb4.setVelocity(new Vector(x4, y4, z4));
                fb5.setVelocity(new Vector(x5, y5, z5));

                fb.setDropItem(false);
                fb1.setDropItem(false);
                fb2.setDropItem(false);
                fb3.setDropItem(false);
                fb4.setDropItem(false);
                fb5.setDropItem(false);

                i.getLocation().getWorld().playEffect(i.getLocation(), Effect.POTION_BREAK, 5);

                i.getLocation().getWorld().playEffect(i.getLocation(), Effect.MOBSPAWNER_FLAMES, 6);

                i.remove();

                this.cancel();

                return;

            }

        }.runTaskTimer(plugin, 1L, 1L);

    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        if (event.getItem().hasMetadata("fuelbomb")) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void Playjoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();

        if (p.hasPermission("fuelbomb.news") && news == true) {

            p.sendMessage(prefix + broadcastString);
        }
    }
}
