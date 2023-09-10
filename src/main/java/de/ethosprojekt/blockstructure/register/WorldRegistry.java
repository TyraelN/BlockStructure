package de.ethosprojekt.blockstructure.register;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class WorldRegistry implements Listener {
    private final Map<World, ChunkRegistry> map = new HashMap<>();
    private final JavaPlugin plugin;

    public WorldRegistry(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public @NotNull ChunkRegistry getChunkRegistry(World world) {
        return map.get(world);
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        map.put(event.getWorld(), new ChunkRegistry(plugin, event.getWorld()));
    }

    @EventHandler
    public void onWorldSave(WorldSaveEvent event) {
        map.get(event.getWorld()).remove();
        map.remove(event.getWorld());
    }

}
