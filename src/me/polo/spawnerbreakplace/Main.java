package me.polo.spawnerbreakplace;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
    }
    public void onDisable() {}

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Block block = e.getBlock();
        Material material = block.getType();
        Player p = (Player) e.getPlayer();
        Location loc = block.getLocation();


        if(material.equals(Material.SPAWNER)){
            if(p.getLevel() > 50) {
                e.setExpToDrop(0);

                CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();
                EntityType et = creatureSpawner.getSpawnedType();
                String ets = creatureSpawner.getSpawnedType().name();
                getConfig().set("spawner." + p.getUniqueId(), ets);
                saveConfig();

                ItemStack item = new ItemStack(Material.SPAWNER);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.LIGHT_PURPLE + et.toString() + " SPAWNER");
                item.setItemMeta(meta);

                block.getWorld().dropItemNaturally(loc, item);

                p.setLevel(p.getLevel() - 50);
            } else {
                p.sendMessage(ChatColor.RED + "You need at least 50 levels for this...");
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent f){
        Block block = f.getBlock();
        Material material = block.getType();
        Player p = (Player) f.getPlayer();

        if(material.equals(Material.SPAWNER)){
            CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();

            EntityType entityType = EntityType.fromName(getConfig().getString("spawner." + p.getUniqueId()));
            Bukkit.broadcastMessage(String.valueOf(entityType));

            creatureSpawner.setSpawnedType(entityType);
            creatureSpawner.update();
            saveConfig();
        }
    }
}
