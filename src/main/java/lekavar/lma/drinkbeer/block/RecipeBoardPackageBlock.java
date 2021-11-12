package lekavar.lma.drinkbeer.block;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import static lekavar.lma.drinkbeer.DrinkBeer.RECIPE_BOARD_PACKAGE_CONTENT;
import static lekavar.lma.drinkbeer.DrinkBeer.UNPACKING_EVENT;

public class RecipeBoardPackageBlock extends HorizontalFacingBlock {
    public final static VoxelShape NORTH_SHAPE = createCuboidShape(0, 1, 1, 16, 10, 15);
    public final static VoxelShape EAST_SHAPE = createCuboidShape(1, 0, 0, 15, 10, 16);

    public RecipeBoardPackageBlock(AbstractBlock.Settings settings) {
        super(settings.nonOpaque());
        setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        return switch (dir) {
            case NORTH, SOUTH -> NORTH_SHAPE;
            case EAST, WEST -> EAST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState) this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient()) {
            world.playSound(null, pos, UNPACKING_EVENT, SoundCategory.BLOCKS, 0.8f, 1f);
        }
        for (Block recipeBoardBlock : RECIPE_BOARD_PACKAGE_CONTENT) {
            ItemStack recipeBoardItemStack = new ItemStack(recipeBoardBlock.asItem(), 1);
            if (player.isCreative() || !player.giveItemStack(recipeBoardItemStack))
                player.dropItem(recipeBoardItemStack, false);
        }
        world.removeBlock(pos, false);
        return ActionResult.SUCCESS;
    }
}
