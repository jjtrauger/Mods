package com.volgadorf.strawberry_fields.item;

import com.volgadorf.strawberry_fields.Main;
import com.volgadorf.strawberry_fields.block.custom.Cheems_Wheel_Block;
import com.volgadorf.strawberry_fields.item.ModFoodItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTabs {
    public static CreativeModeTab VOLG_TAB;

    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event) {
        VOLG_TAB = event.registerCreativeModeTab(new ResourceLocation(Main.MOD_ID, "food_tab"),
                builder -> builder.icon(() -> new ItemStack(ModFoodItems.CHEEMS.get()))
                        .title(Component.translatable("creativemodetab.food_tab")));
    }



 public void registerTabs(CreativeModeTabEvent.Register event)
 {
     VOLG_TAB = event.registerCreativeModeTab(new ResourceLocation(Main.MOD_ID, "food_tab"), builder -> builder
             .icon(() -> new ItemStack(ModFoodItems.CHEEMS.get()))
             .title(Component.translatable("creativemodetab.food_tab"))
             .displayItems((featureFlags, output, hasOp) -> {
                 output.accept(ModFoodItems.CHEEMS.get());
                 output.accept(ModFoodItems.PAST_MILK.get());
             })
     );
 }
}
