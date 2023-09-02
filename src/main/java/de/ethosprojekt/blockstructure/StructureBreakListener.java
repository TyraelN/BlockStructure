package de.ethosprojekt.blockstructure;

import de.ethosprojekt.blockstructure.structures.ItemDisplayStruct;
import de.ethosprojekt.blockstructure.structures.Structure;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageAbortEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class StructureBreakListener implements Listener {
    private final StructureRegister register;
    private final Map<ItemDisplayStruct, BukkitTask> tasks = Collections.synchronizedMap(new HashMap<>());
    private final JavaPlugin plugin;

    public StructureBreakListener(JavaPlugin plugin, StructureRegister register) {
        this.plugin = plugin;
        this.register = register;
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        Structure structure = register.getStructure(event.getBlock().getLocation());
        if (structure instanceof ItemDisplayStruct struct) {
            tasks.put(struct, new StructureBreakTask(event.getBlock(), event.getPlayer(), struct).runTaskTimer(plugin, 0, struct.getBreakTicks(event.getItemInHand())));
        }
    }

    @EventHandler
    public void onBlockDamageAbort(BlockDamageAbortEvent event) {
        Structure structure = register.getStructure(event.getBlock().getLocation());
        if (structure instanceof ItemDisplayStruct struct) {
            struct.updateItem(struct.getItem().asOne());
            if (tasks.get(struct) != null) {
                tasks.get(struct).cancel();
                tasks.remove(struct);
            }
        }
    }

    private class StructureBreakTask extends BukkitRunnable {
        private final Block block;
        private final Player player;
        private final ItemDisplayStruct structure;
        private int index = 1;

        public StructureBreakTask(Block block, Player player, ItemDisplayStruct structure) {
            this.block = block;
            this.player = player;
            this.structure = structure;
        }

        @Override
        public void run() {
            index++;
            if (index > 16) {
                BlockBreakEvent event = new BlockBreakEvent(block, player);
                event.setDropItems(false);
                this.structure.onBreak(event);
                tasks.remove(structure);
                this.cancel();
                //TODO Partikel
            } else {
                final ItemStack item = structure.getItem();
                item.setAmount(index);
                structure.updateItem(item);
                //TODO Partikel
            }
        }
    }
}
