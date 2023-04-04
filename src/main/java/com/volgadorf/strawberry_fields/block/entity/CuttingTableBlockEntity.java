package com.volgadorf.strawberry_fields.block.entity;

import com.volgadorf.strawberry_fields.block.ModBlocks;
import com.volgadorf.strawberry_fields.item.ModFoodItems;
import com.volgadorf.strawberry_fields.screen.CuttingTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
//import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CuttingTableBlockEntity extends BlockEntity implements MenuProvider {


    private final ItemStackHandler itemHandler = new ItemStackHandler(10){

        CuttingTableBlockEntity pEntity = CuttingTableBlockEntity.this;
        BlockPos blockPos = CuttingTableBlockEntity.this.getBlockPos();
        BlockState state = CuttingTableBlockEntity.this.getBlockState();

        //currently if you take all items out the output, it removes 3 from both the input and output slots. weird
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);


            if(level.isClientSide()){
                return;
            }

            //code says what recipe to use
            int code = makeCode(pEntity);
            //1 is cheems_full


            if (code > 0) {
                setChanged(level, blockPos, state);


                int opAmount = pEntity.itemHandler.getStackInSlot(9).getCount(); //output slot amount

                //get the lowest possible output from the lowest input slot count, we know it isnt 0 because hasRecipe
                int lowest = 64;
                for (int i = 0; i < 9; i++){
                    if (pEntity.itemHandler.getStackInSlot(i).getCount() <= lowest && !pEntity.itemHandler.getStackInSlot(i).isEmpty()){
                        lowest = pEntity.itemHandler.getStackInSlot(i).getCount();
                    }
                }

                //only if the output doesnt equal the lowest input, craft the item
                if (code == 2){
                    if (lowest < opAmount / 4) {
                        checkItemCount(pEntity, lowest, code);
                    }

                    if (lowest > opAmount / 4){
                        if (slot == 9){
                            onCraft(pEntity, lowest - opAmount);
                        }
                        else{
                            checkItemCount(pEntity, lowest, code);
                        }
                    }
                }

                else {
                    if (lowest < opAmount) {
                        checkItemCount(pEntity, lowest, code);
                    }

                    if (lowest > opAmount){
                        if (slot == 9){
                            onCraft(pEntity, lowest - opAmount);
                        }
                        else{
                            checkItemCount(pEntity, lowest, code);
                        }
                    }
                }



            } else if (slot ==9) {
                //do nothing if player just puts a random item in the output slot and there is no recipe

            } else{
                //if no recipe, no output
                pEntity.itemHandler.extractItem(9, pEntity.itemHandler.getStackInSlot(9).getCount(), false);

                setChanged(level, blockPos, state);
            }

        }

    };

    private int makeCode(CuttingTableBlockEntity entity) {
        //my code for recipe detection signifies what recipe to use


        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        int nonemptySlots = 0;
        int fullslot = 0;
        //-1 because last slot is output so don't count it

        for (int i = entity.itemHandler.getSlots() - 1 - 1; i >= 0; i--) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
            if (!entity.itemHandler.getStackInSlot(i).isEmpty()) {
                nonemptySlots++;
                fullslot = i;
            }
        }



        if (nonemptySlots == 1){

            if (entity.itemHandler.getStackInSlot(fullslot).getItem().equals(ModFoodItems.PAST_MILK.get())
            && canInsertItemIntoOutputSlot(inventory, new ItemStack(ModBlocks.CHEEMS_FULL.get(), 1))
            && canInsertAmountIntoOutputSlot(inventory)) {
                return 1;
            }
            else if (entity.itemHandler.getStackInSlot(fullslot).getItem().equals((ModBlocks.CHEEMS_FULL.get().asItem()))
            && canInsertItemIntoOutputSlot(inventory, new ItemStack(ModFoodItems.CHEEMS.get(), 1))
            && canInsertAmountIntoOutputSlot(inventory)) {
                return 2;
            }
        }
        return 0;
    }


    //set output count to the lowest ingredient count, if hasRecipe
    private static void checkItemCount(CuttingTableBlockEntity pEntity, int lowest, int code) {
        if (code > 0){
            //pEntity.itemHandler.extractItem(1, 1, true);
            //pEntity.itemHandler.setStackInSlot(9, new ItemStack(ModBlocks.CHEEMS_FULL.get(),
                    //lowest));

            //current issue: does not check if more items can be placed into output slot like hasRecipe does
            switch (code){
                case 1: pEntity.itemHandler.setStackInSlot(9, new ItemStack(ModBlocks.CHEEMS_FULL.get(),
                        lowest)); break;
                case 2: pEntity.itemHandler.setStackInSlot(9, new ItemStack(ModFoodItems.CHEEMS.get(),
                        lowest * 4)); break;
                //case 2: pEntity.itemHandler.setStackInSlot(9, new ItemStack(ModFoodItems.CHEEMS.get(),
                 //       (lowest <= 16) ? lowest * 4 : 64)); break;

                default: break;
            }
        }

    }


    private static void onCraft(CuttingTableBlockEntity pEntity, int dif) {
        for (int i = 0; i < 9; i++) {
            pEntity.itemHandler.extractItem(i, dif, false);
        }
    }

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    //private int progress;

    public CuttingTableBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.CUTTING_TABLE.get(), p_155229_, p_155230_);
        this.data = new ContainerData() {
            @Override
            public int get(int p_39284_) {
                return 0;
            }

            @Override
            public void set(int p_39285_, int p_39286_) {

            }

            @Override
            public int getCount() {
                return 0;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Cutting Table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory,  Player p_39956_) {
        return new CuttingTableMenu(id, inventory,this, this.data);
        //return new CuttingTableMenu(id, inventory);
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER){
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        //nbt.putInt("cutting_table.progress", this.progress);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        //progress = nbt.getInt("cutting_table.progress");
    }

    public void drops(){
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());

        for (int i = 0; i < itemHandler.getSlots(); i++){
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }


    //method now handles shapeless crafting for milk to cheese
  /*  private static boolean hasRecipe(CuttingTableBlockEntity entity) {
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        int nonemptySlots = 0;
        int fullslot = 0;
        //-1 because last slot is output- don't count it

        for (int i = 0; i < entity.itemHandler.getSlots() - 1; i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
            if (!entity.itemHandler.getStackInSlot(i).isEmpty()){
                nonemptySlots++;
                fullslot = i;
            }
        }


        boolean hasPastMilkOnly = entity.itemHandler.getStackInSlot(fullslot).getItem() == ModFoodItems.PAST_MILK.get() &&
                nonemptySlots == 1;


        if (hasPastMilkOnly){
            return hasPastMilkOnly && canInsertAmountIntoOutputSlot(inventory) &&
                    canInsertItemIntoOutputSlot(inventory, new ItemStack(ModBlocks.CHEEMS_FULL.get(), 1));
        }
        else{
            return false;
        }
    } */

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack itemStack) {
        return inventory.getItem(9).getItem() == itemStack.getItem() || inventory.getItem(9).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        //once we hit max stack size, we want it to return false
        return inventory.getItem(9).getMaxStackSize() > inventory.getItem(9).getCount();
    }
}



