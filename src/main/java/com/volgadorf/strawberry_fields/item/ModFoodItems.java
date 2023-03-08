package com.volgadorf.strawberry_fields.item;

import com.volgadorf.strawberry_fields.Main;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFoodItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Main.MOD_ID);

    public static final RegistryObject<Item> PAST_MILK = ITEMS.register("past_milk",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CHEEMS = ITEMS.register("cheems",
            () -> new Item(new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
