package de.ethosprojekt.blockstructure;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public final class BlockStructure extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        StructureRegister register = new StructureRegister(this);
        Listener listener = new StructureListener(register);
        getServer().getPluginManager().registerEvents(new StructureBreakListener(this,register),this);
        getServer().getPluginManager().registerEvents(listener,this);
        getServer().getPluginManager().registerEvents(new PlacementListener(register),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
