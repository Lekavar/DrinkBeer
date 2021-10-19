package lekavar.lma.drinkbeer.block;

import lekavar.lma.drinkbeer.DrinkBeer;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class BeerRecipeBoardBlock extends HorizontalFacingBlock {
    public final static VoxelShape NORTH_SHAPE = createCuboidShape(1, 0, 0, 15, 16, 1.5);
    public final static VoxelShape SOUTH_SHAPE = createCuboidShape(1, 0, 14.5, 15, 16, 16);
    public final static VoxelShape EAST_SHAPE = createCuboidShape(14.5, 0, 1, 16, 16, 15);
    public final static VoxelShape WEST_SHAPE = createCuboidShape(0, 0, 1, 1.5, 16, 15);

    public BeerRecipeBoardBlock(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        switch (dir) {
            case NORTH:
                return NORTH_SHAPE;
            case SOUTH:
                return SOUTH_SHAPE;
            case EAST:
                return EAST_SHAPE;
            case WEST:
                return WEST_SHAPE;
            default:
                return NORTH_SHAPE;
        }
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState) this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }

    //TODO 放置规则
    /*public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Item item = world.getBlockState(pos.offset(Direction.DOWN, 1)).getBlock().asItem();
        try {
            return !item.getGroup().equals(DrinkBeer.DRINK_BEER);
        } catch (Exception e) {
            //System.out.println(e.getMessage());
            return !item.equals(Items.AIR);
        }
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return canPlaceAt(state, world, pos) ? super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom) : Blocks.AIR.getDefaultState();
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.DESTROY;
    }*/
}
