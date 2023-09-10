package de.ethosprojekt.blockstructure.register;

import de.ethosprojekt.blockstructure.StructureLoader;
import de.ethosprojekt.blockstructure.structures.Structure;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.stream.Collectors;


public class ChunkRegistry implements Listener {

    private final StructureLoader loader;
    private final Map<Chunk, Map<BlockVector, Structure>> map = new HashMap<>();

    public ChunkRegistry(JavaPlugin plugin, World world) {
        this.loader = new StructureLoader(plugin);
    }


    public @Nullable Structure getStructure(@NotNull Location location) {
        Map<BlockVector, Structure> subsub = map.get(location.getChunk());
        if (subsub == null || subsub.isEmpty()) {
            return null;
        }
        return subsub.get(new BlockVector(location.toVector()));
    }

    public @NotNull Collection<Structure> getStructures(Chunk chunk) {
        Map<BlockVector, Structure> submap = map.get(chunk);
        if (submap == null) {
            return Collections.emptyList();
        }
        return submap.values();
    }

    public void register(Structure structure, Chunk chunk, Set<BlockVector> vectorSet) {
        Map<BlockVector, Structure> submap = map.computeIfAbsent(chunk, k -> new HashMap<>());
        for (BlockVector vector : vectorSet) {
            submap.put(vector, structure);
        }
    }

    public void remove() {
        ChunkLoadEvent.getHandlerList().unregister(this);
        ChunkUnloadEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        map.put(event.getChunk(), loader.load(event.getChunk()));
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        loader.save(event.getChunk(), inverse(map.get(event.getChunk())));
    }

    private <T, R> @UnmodifiableView @NotNull Map<R, Set<T>> inverse(Map<T, R> map) {
        return Collections.unmodifiableMap(map.keySet().stream().collect(Collectors.groupingBy(map::get, Collectors.toSet())));
    }


}
