package lekavar.lma.drinkbeer.block;

import lekavar.lma.drinkbeer.DrinkBeer;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
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

public class CallBellBlock extends Block {
    public final static VoxelShape SHAPE = createCuboidShape(5.5f, 0, 5.5f, 10.5f, 4, 10.5f);

    public CallBellBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            if (state.getBlock() == DrinkBeer.IRON_CALL_BELL) {
                world.playSound(null, pos, DrinkBeer.IRON_CALL_BELL_TINKLE_EVENT, SoundCategory.BLOCKS, 1.5f, 1f);
            } else if (state.getBlock() == DrinkBeer.GOLDEN_CALL_BELL) {
                world.playSound(null, pos, DrinkBeer.GOLDEN_CALL_BELL_TINKLE_EVENT, SoundCategory.BLOCKS, 1.8f, 1f);
            } else if (state.getBlock() == DrinkBeer.LEKAS_CALL_BELL) {
                world.playSound(null, pos, DrinkBeer.LEKAS_CALL_BELL_TINKLE_EVENT, SoundCategory.BLOCKS, 0.9f, 1f);
            }
        } else {
            double x = (double) pos.getX() + 0.5D;
            double y = (double) pos.getY() + 0.2D + new Random().nextDouble() / 4;
            double z = (double) pos.getZ() + 0.5D;
            if (state.getBlock() == DrinkBeer.IRON_CALL_BELL) {
                world.addParticle(ParticleTypes.NOTE, x, y, z, 0.0D, 0.0D, 0.0D);
            } else if (state.getBlock() == DrinkBeer.GOLDEN_CALL_BELL) {
                world.addParticle(ParticleTypes.NOTE, x, y, z, 0.0D, 0.0D, 0.0D);
            } else if (state.getBlock() == DrinkBeer.LEKAS_CALL_BELL) {
                world.addParticle(DrinkBeer.CALL_BELL_TINKLE_PAW, x, y, z, 0.0D, 0.0D, 0.0D);
            }
        }
        return ActionResult.SUCCESS;
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        try {
            Item item = world.getBlockState(pos.offset(Direction.DOWN, 1)).getBlock().asItem();
            return !item.equals(Items.AIR);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return true;
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
