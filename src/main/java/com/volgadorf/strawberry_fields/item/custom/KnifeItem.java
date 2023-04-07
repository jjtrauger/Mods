package com.volgadorf.strawberry_fields.item.custom;

import com.volgadorf.strawberry_fields.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class KnifeItem extends SwordItem {

    public KnifeItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    //set damage in getter
    @Override
    public float getDamage() {
        return 4.0f;
    }

    //has twice the speed of a sword
    @Override
    public int getUseDuration(ItemStack stack) {
        // Set the use duration to half of a sword
        return super.getUseDuration(stack) / 2;
    }

    //reduce hitbox for sweep attack - entities practically must be on same spot
    @Override
    public @NotNull AABB getSweepHitBox(@NotNull ItemStack stack, @NotNull Player player, @NotNull Entity target) {
        return target.getBoundingBox().inflate(0, 0, 0);
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState pBlock) {
        return pBlock.is(Blocks.MELON) || pBlock.is(Blocks.PUMPKIN);
    }

    //cuts plants faster but not cobwebs like a sword can
    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
            Material material = pState.getMaterial();
            return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && !pState.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 8.0F;

    }

    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (pState.is(Blocks.MELON) || pState.is(Blocks.PUMPKIN) || pState.is(Blocks.BAMBOO) || pState.is(Blocks.RED_MUSHROOM_BLOCK)
        || pState.is(Blocks.BROWN_MUSHROOM_BLOCK)){
            pStack.hurtAndBreak(1, pEntityLiving, (p_43276_) -> {
                p_43276_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        else if (pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
            pStack.hurtAndBreak(2, pEntityLiving, (p_43276_) -> {
                p_43276_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }

        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        Block block = blockstate.getBlock();

        if (block.equals(Blocks.PUMPKIN)) {
            // Replace the pumpkin block with a carved pumpkin block
            BlockState carvedPumpkinState = Blocks.CARVED_PUMPKIN.defaultBlockState()
                    .setValue(CarvedPumpkinBlock.FACING, pContext.getClickedFace());
            level.setBlockAndUpdate(blockpos, carvedPumpkinState);

            // Consume one use of the knife item
            ItemStack itemstack = pContext.getItemInHand();
            if (!pContext.getPlayer().isCreative()) {
                itemstack.hurt(1, level.getRandom(), null);
            }

            return InteractionResult.SUCCESS;
        }

        return super.useOn(pContext);
    }
}
