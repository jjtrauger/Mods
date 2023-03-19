package com.volgadorf.strawberry_fields.screen;

import com.volgadorf.strawberry_fields.block.entity.CuttingTableBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class CuttingTableMenu extends AbstractContainerMenu {

    public final CuttingTableBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;
    protected CuttingTableMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
    this(id, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(0));
    }

    protected CuttingTableMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super (, id);
        checkContainerSize(inv, 10);
        blockEntity = (CuttingTableBlockEntity) entity;
        this.level = inv.player.level;
        this.data = data;
        
        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        //this builds the slots- need to figure expand this to make 10 and need to test what each number means
        //in terms of position
        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, 0, 12, 15));
            this.addSlot(new SlotItemHandler(handler, 1, 86, 15));
            this.addSlot(new SlotItemHandler(handler, 2, 86, 60));
            this.addSlot(new SlotItemHandler(handler, 3, 86, 60));
            this.addSlot(new SlotItemHandler(handler, 4, 86, 60));
            this.addSlot(new SlotItemHandler(handler, 5, 86, 60));
            this.addSlot(new SlotItemHandler(handler, 6, 86, 60));
            this.addSlot(new SlotItemHandler(handler, 7, 86, 60));
            this.addSlot(new SlotItemHandler(handler, 8, 86, 60));
            this.addSlot(new SlotItemHandler(handler, 9, 86, 60));
        });

        addDataSlots(data);
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 10;  // must be the number of slots you have!

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
        return null;
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return false;
    }
}
