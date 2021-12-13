package lekavar.lma.drinkbeer.block;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.util.mixedbeer.Spices;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.particle.DefaultParticleType;
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
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

public class SpiceBlock extends TransparentBlock {
    public final static VoxelShape DEFAULT_SHAPE = createCuboidShape(5.5, 0, 5.5, 10.5, 2, 10.5);
    public final static VoxelShape SPICE_FROZEN_PERSIMMON_SHAPE = createCuboidShape(5.5, 0, 5.5, 10.5, 3.5, 10.5);
    public final static VoxelShape SPICE_DRIED_SELAGINELLA = createCuboidShape(5.5, 0, 5.5, 10.5, 4.5, 10.5);

    public SpiceBlock(AbstractBlock.Settings settings) {
        super(settings.nonOpaque());
        setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        if (this.equals(DrinkBeer.SPICE_FROZEN_PERSIMMON)) {
            return SPICE_FROZEN_PERSIMMON_SHAPE;
        }
        if (this.equals(DrinkBeer.SPICE_DRIED_SELAGINELLA)) {
            return SPICE_DRIED_SELAGINELLA;
        }
        return DEFAULT_SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState) this.getDefaultState().with(HorizontalFacingBlock.FACING, ctx.getPlayerFacing());
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Item item = world.getBlockState(pos.offset(Direction.DOWN, 1)).getBlock().asItem();
        return !item.equals(Items.AIR);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return canPlaceAt(state, world, pos) ? super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom) : Blocks.AIR.getDefaultState();
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.DESTROY;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        DefaultParticleType particle = Spices.byItem(this.asItem()).getFlavor().getParticle();
        double x = (double) pos.getX() + 0.5D;
        double y = (double) pos.getY() + 0.3D + new Random().nextDouble() / 4;
        double z = (double) pos.getZ() + 0.5D;
        world.addParticle(particle, x, y, z, 0.0D, 0.0D, 0.0D);
        return ActionResult.SUCCESS;
    }
}
