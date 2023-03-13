package com.volgadorf.strawberry_fields.block;

import com.volgadorf.strawberry_fields.Main;
import com.volgadorf.strawberry_fields.block.custom.Cheems_Wheel_Block;
import com.volgadorf.strawberry_fields.item.ModCreativeModeTabs;
import com.volgadorf.strawberry_fields.item.ModFoodItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MOD_ID);

    public static final RegistryObject<Block> CHEEMS_FULL = registerBlock("cheems_full",
            () -> new Cheems_Wheel_Block(BlockBehaviour.Properties.of(Material.CAKE).noOcclusion()
                    .strength(1f)), ModCreativeModeTabs.VOLG_TAB);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModFoodItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().stacksTo(64)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
