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

public class BeerMugBlock extends HorizontalFacingBlock {
    public final static VoxelShape ONE_MUG_SHAPE = createCuboidShape(4, 0, 4, 12, 6, 12);
    public final static VoxelShape TWO_MUGS_SHAPE = createCuboidShape(2, 0, 2, 14, 6, 14);
    public final static VoxelShape THREE_MUGS_SHAPE = createCuboidShape(1, 0, 1, 15, 6, 15);

    public static final IntProperty AMOUNT = IntProperty.of("amount", 1, 3);

    public BeerMugBlock(AbstractBlock.Settings settings) {
        super(settings.nonOpaque());
        setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(AMOUNT, 1));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
        stateManager.add(AMOUNT);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        switch ((Integer) state.get(AMOUNT)) {
            case 1:
            default:
                return ONE_MUG_SHAPE;
            case 2:
                return TWO_MUGS_SHAPE;
            case 3:
                return THREE_MUGS_SHAPE;
        }
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState) this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        /*Fix: It should be main hand stack*/
        //ItemStack itemStack itemStack = player.getStackInHand(hand);
        ItemStack itemStack = player.getMainHandStack();
        if (itemStack.getItem().asItem() == state.getBlock().asItem()) {
            int amount = state.get(AMOUNT);
            int mugInHandCount = player.getStackInHand(hand).getCount();
            boolean isCreative = player.isCreative();
            switch (amount) {
                case 1:
                    world.setBlockState(pos, state.with(AMOUNT, 2));
                    if (!isCreative) {
                        player.getStackInHand(hand).setCount(mugInHandCount - 1);
                    }
                    if (!world.isClient) {
                        world.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                    }
                    return ActionResult.SUCCESS;
                case 2:
                    world.setBlockState(pos, state.with(AMOUNT, 3));
                    if (!isCreative) {
                        player.getStackInHand(hand).setCount(mugInHandCount - 1);
                    }
                    if (!world.isClient) {
                        world.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                    }
                    return ActionResult.SUCCESS;
                default:
                    return ActionResult.FAIL;
            }
        } else if (itemStack.isEmpty()) {
            if (!world.isClient()) {
                ItemStack takeBackBeer = new ItemStack(state.getBlock().asItem());
                player.giveItemStack(takeBackBeer);
            }
            int amount = state.get(AMOUNT);
            switch (amount) {
                case 3:
                case 2:
                    world.setBlockState(pos, state.with(AMOUNT, amount - 1));
                    if (!world.isClient()) {
                        world.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.AMBIENT, 0.5f, 0.5f);
                    }
                    return ActionResult.SUCCESS;
                case 1:
                    world.setBlockState(pos, Blocks.AIR.getDefaultState(), 0);
                    if (!world.isClient()) {
                        world.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.AMBIENT, 0.5f, 0.5f);
                    }
                    return ActionResult.SUCCESS;
                default:
                    return ActionResult.FAIL;
            }
        } else {
            return ActionResult.FAIL;
        }
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
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
    }
}
