package de.ethosprojekt.blockstructure.structures;


import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;

import java.util.UUID;

public class ItemDisplayStruct implements Structure {

    private final ItemStack item;
    private ItemDisplay display;

    public ItemDisplayStruct(ItemStack item) {
        this.item = item;
    }

    public ItemDisplayStruct(UUID uuid) {
        if (Bukkit.getEntity(uuid) instanceof ItemDisplay display) {
            this.display = display;
            item = display.getItemStack();
        } else {
            throw new IllegalArgumentException("Kein DisplayEntity mit der UUID: " + uuid + " gefunden.");
        }
    }

    public ItemStack getItem() {
        return item;
    }

    public ItemDisplay getDisplay() {
        return display;
    }

    public void updateItem(ItemStack item) {
        this.item.setType(item.getType());
        this.item.setAmount(item.getAmount());
        this.item.setItemMeta(item.getItemMeta());
        this.display.setItemStack(item);
    }

    public void spawn(Chunk chunk, BlockVector vector) {
        Block block = chunk.getWorld().getBlockAt(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
        if (block.getType().equals(Material.AIR)) {
            block.setType(Material.BARRIER);
            display = chunk.getWorld().spawn(block.getLocation().add(0.5, 0.5, 0.5), ItemDisplay.class);
            display.setItemStack(item);
        }
    }

    @Override
    public void onClick(PlayerInteractEvent event) {
        event.getPlayer().sendMessage("Hallo");
    }

    @Override
    public void onBreak(BlockBreakEvent event) {
        display.remove();
        event.getBlock().setType(Material.AIR);
        event.getPlayer().sendMessage("Kapput");

    }

    public long getBreakTicks(ItemStack item) {
        BlockData data = item.getType().createBlockData();
        return (long) data.getDestroySpeed(item, true) * 20;
    }

    @Override
    public String getID() {
        return item.getType() + "#" + hashCode();
    }
}
