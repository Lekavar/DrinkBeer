package lekavar.lma.drinkbeer.block;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

import static lekavar.lma.drinkbeer.DrinkBeer.*;

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
}
