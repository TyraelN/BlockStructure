package de.ethosprojekt.blockstructure.structures;


import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public interface Structure {
    void onClick(PlayerInteractEvent event);

    void onBreak(BlockBreakEvent event);

    String getID();


}
