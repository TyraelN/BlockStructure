package de.ethosprojekt.blockstructure;

import de.ethosprojekt.blockstructure.listener.InteractionListener;
import de.ethosprojekt.blockstructure.listener.PlacementListener;
import de.ethosprojekt.blockstructure.listener.StructureBreakListener;
import de.ethosprojekt.blockstructure.register.WorldRegistry;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class BlockStructure extends JavaPlugin {
    private final WorldRegistry register = new WorldRegistry(this);


    @Override
    public void onEnable() {
        // Plugin startup logic
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new StructureBreakListener(this, register), this);
        manager.registerEvents(new PlacementListener(register), this);
        manager.registerEvents(new InteractionListener(register), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
