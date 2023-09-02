package de.ethosprojekt.blockstructure;

import de.ethosprojekt.blockstructure.structures.ItemDisplayStruct;
import de.ethosprojekt.blockstructure.structures.Structure;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockVector;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class StructureLoader {

    private final JavaPlugin plugin;

    public StructureLoader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public Map<BlockVector, Structure> load(Chunk chunk) {
        File dir = getChunkFolder(chunk);
        Map<BlockVector, Structure> map = new HashMap<>();
        File[] files = dir.listFiles();
        if (files == null) {
            return new HashMap<>();
        }
        for (File file : files) {
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
            List<BlockVector> positions = (List<BlockVector>) yml.getList("positions");
            UUID uuid = UUID.fromString(yml.getString("uuid"));
            for (BlockVector vec : positions) {
                map.put(vec, new ItemDisplayStruct(uuid));
            }
        }
        return map;
    }


    public File save(File dir, Structure structure, Set<BlockVector> vec) throws IOException {
        final File file = new File(dir, structure.getID() + ".yml");
        if (!file.exists()) {
            file.createNewFile();
        }
        final YamlConfiguration config = new YamlConfiguration();
        config.set("positions", vec);
        if (structure instanceof ItemDisplayStruct struct) {
            config.set("uuid", struct.getDisplay().getUniqueId());
        }
        config.save(file);
        return file;
    }

    public void save(Chunk chunk, Map<Structure, Set<BlockVector>> map) {
        File dir = getChunkFolder(chunk);
        HashSet<File> set = new HashSet<>();
        for (Map.Entry<Structure, Set<BlockVector>> entry : map.entrySet()) {
            try {
                final File file = save(dir, entry.getKey(), entry.getValue());
                set.add(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        File[] list = dir.listFiles();
        if (list == null) {
            return;
        }
        for (File file : list) {
            set.remove(file);
        }
        set.forEach(File::delete);
    }


    public File getStructureFolder() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
        File file = new File(dataFolder, "structures");
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public File getWorldFolder(World world) {
        File file = new File(getStructureFolder(), world.getName());
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public File getChunkFolder(Chunk chunk) {
        File file = new File(getWorldFolder(chunk.getWorld()), chunk.getZ() + "x" + chunk.getX());
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }
}
