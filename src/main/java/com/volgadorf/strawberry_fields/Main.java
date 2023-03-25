package com.volgadorf.strawberry_fields;


import com.volgadorf.strawberry_fields.block.ModBlocks;
import com.volgadorf.strawberry_fields.block.entity.ModBlockEntities;
import com.volgadorf.strawberry_fields.screen.CuttingTableScreen;
import com.volgadorf.strawberry_fields.screen.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.common.Mod;
import com.mojang.logging.LogUtils;
import com.volgadorf.strawberry_fields.item.ModCreativeModeTabs;
import com.volgadorf.strawberry_fields.item.ModFoodItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod("strawberry_fields")
public class Main {

    public static final String MOD_ID = "strawberry_fields";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Main() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModFoodItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        //modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::buildContents);
        MinecraftForge.EVENT_BUS.register(this);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

   /** private void addCreative(CreativeModeTabEvent.BuildContents event) {
        if(event.getTab() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ModFoodItems.PAST_MILK);
            event.accept(ModFoodItems.CHEEMS);
        }

        if(event.getTab() == ModCreativeModeTabs.VOLG_TAB) {
            event.accept(ModFoodItems.PAST_MILK);
            event.accept(ModFoodItems.CHEEMS);
            event.accept(ModBlocks.CHEEMS_FULL);
        }
    } **/

    @SubscribeEvent
    public void buildContents(CreativeModeTabEvent.BuildContents event) {
        // Add to ingredients tab
        if (event.getTab() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ModFoodItems.PAST_MILK);
            event.accept(ModBlocks.CHEEMS_FULL); // Takes in an ItemLike, assumes block has registered item
            event.accept(ModBlocks.CUTTING_TABLE);
        }
        if(event.getTab() == ModCreativeModeTabs.VOLG_TAB) {
            event.accept(ModFoodItems.PAST_MILK);
            event.accept(ModBlocks.CHEEMS_FULL);
            event.accept(ModBlocks.CUTTING_TABLE);
        }
    }





    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.CUTTING_TABLE_MENU.get(), CuttingTableScreen::new);
        }
    }
}
