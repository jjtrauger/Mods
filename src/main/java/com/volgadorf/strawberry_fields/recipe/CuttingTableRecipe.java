package com.volgadorf.strawberry_fields.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.volgadorf.strawberry_fields.Main;
import com.volgadorf.strawberry_fields.block.entity.CuttingTableBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.server.ServerLifecycleHooks;

public class CuttingTableRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;

    /*public CuttingTableRecipe(ResourceLocation id, ItemStack output,
                                    NonNullList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;

    } */

    public CuttingTableRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = NonNullList.withSize(9, Ingredient.EMPTY);
        for (int i = 0; i < recipeItems.size() && i < this.recipeItems.size(); i++) {
            this.recipeItems.set(i, recipeItems.get(i));
        }
    }


    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {

        if(pLevel.isClientSide()) {
            return false;
        }
        return recipeItems.get(0).test(pContainer.getItem(1));
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess p_267165_) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return output.copy();
    }


    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }


    public static class Serializer implements RecipeSerializer<CuttingTableRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Main.MOD_ID, "cutting_table_recipes");

        @Override
        public CuttingTableRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
           /* ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(9, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new CuttingTableRecipe(pRecipeId, output, inputs); */

            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "result"));

            NonNullList<Ingredient> inputs = NonNullList.create();
            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");

            for (int i = 0; i < ingredients.size(); i++) {
                Ingredient ingredient = Ingredient.fromJson(ingredients.get(i));
                if (!ingredient.isEmpty()) {
                    inputs.add(ingredient);
                }
            }

            if (inputs.isEmpty()) {
                throw new IllegalStateException("No ingredients for shapeless recipe " + pRecipeId);
            }

            return new CuttingTableRecipe(pRecipeId, output, inputs);
        }

        @Override
        public @Nullable CuttingTableRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            return new CuttingTableRecipe(id, output, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CuttingTableRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());

            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeItemStack(recipe.getResultItem(ServerLifecycleHooks.getCurrentServer().registryAccess()), false);
        }
    }

    public static class Type implements RecipeType<CuttingTableRecipe> {
        private Type() { }


        public static final Type INSTANCE = new Type();
        public static final String ID = "cutting_table_recipes";
    }
}
