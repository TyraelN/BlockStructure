package de.ethosprojekt.blockstructure;



import de.ethosprojekt.blockstructure.structures.Structure;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;



public class StructureListener implements Listener {

    private final StructureRegister register;

    public StructureListener(StructureRegister register){
        this.register = register;
    }

    @EventHandler(ignoreCancelled = true)
    public void onKlick(PlayerInteractEvent event){
        final Block block = event.getClickedBlock();
        if (block == null){
            return;
        }
        final Location location = block.getLocation();
        final Structure structure = register.getStructure(location);
        if (structure == null){
            return;
        }
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
            structure.onClick(event);
        }
    }


    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event){
        register.load(event.getChunk());
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event){
        register.save(event.getChunk());
    }
}
