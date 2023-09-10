package de.ethosprojekt.blockstructure.listener;

import de.ethosprojekt.blockstructure.register.WorldRegistry;
import de.ethosprojekt.blockstructure.structures.Structure;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractionListener implements Listener {

    private final WorldRegistry register;

    public InteractionListener(WorldRegistry register) {
        this.register = register;
    }

    @EventHandler(ignoreCancelled = true)
    public void onKlick(PlayerInteractEvent event) {
        final Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }
        final Location location = block.getLocation();
        final Structure structure = register.getChunkRegistry(event.getClickedBlock().getWorld()).getStructure(location);
        if (structure == null) {
            return;
        }
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            structure.onClick(event);
        }
    }
}
