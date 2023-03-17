package com.volgadorf.strawberry_fields.block.entity;

import com.volgadorf.strawberry_fields.Main;
import com.volgadorf.strawberry_fields.block.ModBlocks;
import com.volgadorf.strawberry_fields.block.custom.CuttingTableBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Main.MOD_ID);

    public static final RegistryObject<BlockEntityType<CuttingTableBlockEntity>> CUTTING_TABLE =
            BLOCK_ENTITIES.register("cutting_table", () ->
                    BlockEntityType.Builder.of(CuttingTableBlockEntity::new,
                            ModBlocks.CUTTING_TABLE.get()).build(null));

    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }

}
