package de.ethosprojekt.blockstructure;

import de.ethosprojekt.blockstructure.structures.Structure;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockVector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;


import java.util.*;
import java.util.stream.Collectors;


public class StructureRegister {

    private final StructureLoader loader ;

    public StructureRegister(JavaPlugin plugin){
        this.loader = new StructureLoader(plugin);
    }

    private final HashMap<Chunk,Map<BlockVector, Structure>> map = new HashMap<>();

    public @Nullable Structure getStructure(@NotNull Location location){
        Map<BlockVector,Structure> submap = map.get(location.getChunk());
        if (submap == null){
            return null;
        }
        return submap.get(new BlockVector(location.toVector()));
    }

    public @NotNull Collection<Structure> getStructures(Chunk chunk){
        Map<BlockVector,Structure> submap = map.get(chunk);
        if (submap == null){
            return Collections.emptyList();
        }
        return submap.values();
    }

    public void register(Structure structure, Chunk chunk, Set<BlockVector> vectorSet){
        Map<BlockVector, Structure> submap = map.computeIfAbsent(chunk, k -> new HashMap<>());
        for (BlockVector vector : vectorSet) {
            submap.put(vector,structure);
        }
    }

    public void load(Chunk chunk){
        final Map<BlockVector,Structure> structures = loader.load(chunk);
        if (map.get(chunk) == null){
            map.put(chunk,structures);
        } else {
            throw new RuntimeException("Fehler beim laden von Strukturen für Chunk: " + chunk.getChunkKey());
        }
    }

    public void save(Chunk chunk){
        final Map<Structure,Set<BlockVector>> inverseMap = inverse(map.get(chunk));
        map.remove(chunk);
        loader.save(chunk,inverseMap);
    }

    private <T,R> @UnmodifiableView @NotNull Map<R, Set<T>> inverse(Map<T,R> map){
        return Collections.unmodifiableMap(map.keySet().stream().collect(Collectors.groupingBy(map::get, Collectors.toSet())));
    }

}
