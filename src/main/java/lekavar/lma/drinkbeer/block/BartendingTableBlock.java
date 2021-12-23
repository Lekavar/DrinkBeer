package lekavar.lma.drinkbeer.block;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.block.entity.BartendingTableEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BartendingTableBlock extends BlockWithEntity implements BlockEntityProvider {
    public final static VoxelShape SHAPE = createCuboidShape(0, 0.001, 0, 16, 16, 16);
    public static final BooleanProperty OPENED = BooleanProperty.of("opened");
    public static final IntProperty TYPE = IntProperty.of("type", 1, 2);

    public BartendingTableBlock(AbstractBlock.Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState()
                .with(Properties.HORIZONTAL_FACING, Direction.NORTH)
                .with(OPENED, true)
                .with(TYPE, 1));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING).add(OPENED).add(TYPE);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState) this.getDefaultState().with(HorizontalFacingBlock.FACING, ctx.getPlayerFacing());
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        this.setDefaultState(stateManager.getDefaultState().with(TYPE, new Random().nextInt(2) + 1));
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        try {
            ItemStack mainHandStack = player.getMainHandStack();
            //Open screen when beer in main hand
            if (mainHandStack.getItem().getGroup() == DrinkBeer.DRINK_BEER) {
                world.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
                if (screenHandlerFactory != null) {
                    //Take 1 beer and set into bartending table
                    BartendingTableEntity bartendingTableEntity = (BartendingTableEntity) world.getBlockEntity(pos);
                    if (bartendingTableEntity.setBeer(mainHandStack)) {
                        mainHandStack.decrement(1);
                        player.openHandledScreen(screenHandlerFactory);
                    }
                }
            }
            //Or change apperance
            else {
                boolean currentOpenedState = state.get(OPENED);
                if (!world.isClient) {
                    world.playSound(null, pos, currentOpenedState ? DrinkBeer.BARTENDING_TABLE_CLOSE_EVENT : DrinkBeer.BARTENDING_TABLE_OPEN_EVENT, SoundCategory.BLOCKS, 1f, 1f);
                }
                world.setBlockState(pos, state.with(OPENED, !currentOpenedState));
            }
            return ActionResult.SUCCESS;
        } catch (Exception e) {
            world.setBlockState(pos, state.with(OPENED, !state.get(OPENED)));
            return ActionResult.SUCCESS;
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BartendingTableEntity(pos, state);
    }
}
