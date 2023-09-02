package de.ethosprojekt.blockstructure;

import de.ethosprojekt.blockstructure.structures.ItemDisplayStruct;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.BlockVector;

import java.util.Set;


public class PlacementListener implements Listener {
    private final StructureRegister register;

    public PlacementListener(StructureRegister register) {
        this.register = register;
    }

    @EventHandler
    public void onPlacement(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null || event.getItem() == null) {
            return;
        }
        if (event.getPlayer().isSneaking()) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                BlockFace face = event.getBlockFace();
                ItemDisplayStruct custom = new ItemDisplayStruct(event.getItem().clone());
                BlockVector vector = new BlockVector(block.getX(), block.getY(), block.getZ());
                vector.add(face.getDirection());
                custom.spawn(block.getChunk(), vector);
                register.register(custom, block.getChunk(), Set.of(vector));
                event.getPlayer().sendMessage(vector.toString());
                event.setCancelled(true);
            }
        }
    }
}
