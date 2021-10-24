package lekavar.lma.drinkbeer.block;

import lekavar.lma.drinkbeer.DrinkBeer;
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
import net.minecraft.world.event.GameEvent;

import static lekavar.lma.drinkbeer.DrinkBeer.*;
import static lekavar.lma.drinkbeer.DrinkBeer.RECIPE_BOARD_BEER_MUG_NIGHT_HOWL_KVASS;

public class RecipeBoardPackageBlock extends HorizontalFacingBlock {
    public final static VoxelShape NORTH_SHAPE = createCuboidShape(0, 1, 1, 16, 10, 15);
    public final static VoxelShape EAST_SHAPE = createCuboidShape(1, 0, 0, 15, 10, 16);

    public static final Block[] RECIPE_BOARD_PACKAGE_CONTENT = {RECIPE_BOARD_BEER_MUG, RECIPE_BOARD_BEER_MUG_BLAZE_STOUT,
            RECIPE_BOARD_BEER_MUG_BLAZE_MILK_STOUT, RECIPE_BOARD_BEER_MUG_APPLE_LAMBIC,
            RECIPE_BOARD_BEER_MUG_SWEET_BERRY_KRIEK, RECIPE_BOARD_BEER_MUG_HAARS_ICEY_PALE_LAGER,
            RECIPE_BOARD_BEER_MUG_PUMPKIN_KVASS, RECIPE_BOARD_BEER_MUG_NIGHT_HOWL_KVASS};

    public RecipeBoardPackageBlock(AbstractBlock.Settings settings) {
        super(settings.nonOpaque());
        setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        switch (dir) {
            case NORTH:
                return NORTH_SHAPE;
            case SOUTH:
                return NORTH_SHAPE;
            case EAST:
                return EAST_SHAPE;
            case WEST:
                return EAST_SHAPE;
            default:
                return NORTH_SHAPE;
        }
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
            world.removeBlock(pos, false);
            world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            for (Block recipeBoardBlock : RECIPE_BOARD_PACKAGE_CONTENT) {
                ItemStack recipeBoardItemStack = new ItemStack(recipeBoardBlock.asItem(), 1);
                if (player.isCreative() || !player.giveItemStack(recipeBoardItemStack))
                    player.dropItem(recipeBoardItemStack, false);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }
}
