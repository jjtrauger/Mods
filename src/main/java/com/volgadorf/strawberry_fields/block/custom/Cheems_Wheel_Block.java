package com.volgadorf.strawberry_fields.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;

public class Cheems_Wheel_Block extends Block {

    public static final int MAX_CHEEMS_BITES = 3;
    public static final IntegerProperty CHEEMS_BITES = IntegerProperty.create("cheems_bites", 0, 3);

    protected static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 6, 14);

    public Cheems_Wheel_Block(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(CHEEMS_BITES, Integer.valueOf(0)));
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
    }

    protected static InteractionResult eat(LevelAccessor p_51186_, BlockPos blockPos, BlockState blockState, Player player) {
        //check if player has full hunger: if so, leave this method
        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        //player is hungry
        } else {
            //restore player hunger and determine saturation
            player.getFoodData().eat(1, 0.2F);

            //variable to determine what bite we are on
            int i = blockState.getValue(CHEEMS_BITES);
            //trigger gameEvent calling player hunger, then EAT happens, need to know block position to change the blockstate there
            p_51186_.gameEvent(player, GameEvent.EAT, blockPos);
            //if block is not supposed to be depleted
            if (i < MAX_CHEEMS_BITES) {
                //change the blockstate
                p_51186_.setBlock(blockPos, blockState.setValue(CHEEMS_BITES, Integer.valueOf(i + 1)), 3);
            //if block is now fully eaten
            } else {
                //remove the block from the game
                p_51186_.removeBlock(blockPos, false);
                p_51186_.gameEvent(player, GameEvent.BLOCK_DESTROY, blockPos);
            }

            return InteractionResult.SUCCESS;
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_51220_) {
        p_51220_.add(CHEEMS_BITES);
    }
}
