package lekavar.lma.drinkbeer.block;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.block.entity.MixedBeerEntity;
import lekavar.lma.drinkbeer.manager.MixedBeerManager;
import lekavar.lma.drinkbeer.manager.SpiceAndFlavorManager;
import lekavar.lma.drinkbeer.util.beer.Beers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MixedBeerBlock extends BlockWithEntity {
    public final static VoxelShape ONE_MUG_SHAPE = createCuboidShape(4, 0, 4, 12, 6, 12);

    public static final IntProperty BEER_ID = IntProperty.of("beer_id", 0, Beers.SIZE);
    public List<Integer> spiceList;

    public MixedBeerBlock(AbstractBlock.Settings settings) {
        super(settings.nonOpaque());
        setDefaultState(this.stateManager.getDefaultState().with(BEER_ID, Beers.DEFAULT_BEER_ID));
        this.spiceList = new ArrayList<>();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(BEER_ID);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return ONE_MUG_SHAPE;
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

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getMainHandStack();
        if (itemStack.isEmpty()) {
            if (!world.isClient()) {
                world.removeBlock(pos, false);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.FAIL;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        try {
            MixedBeerEntity mixedBeerEntity = (MixedBeerEntity) world.getBlockEntity(pos);
            ItemStack mixedBeerItemStack = mixedBeerEntity.getPickStack(state);

            dropStack(world.getWorld(), pos, mixedBeerItemStack);
        } catch (Exception e) {
            System.out.println("Somthing wrong with dropping mixed beer item stack!");
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    //entity
    @Override
    public @Nullable BlockEntity createBlockEntity(net.minecraft.world.BlockView world) {
        return new MixedBeerEntity(this.getDefaultState().get(BEER_ID), this.spiceList);
    }

    public void setBeerId(int beerId) {
        this.setDefaultState(this.getDefaultState().with(BEER_ID, beerId));
    }

    public void setSpiceList(List<Integer> spiceList) {
        this.spiceList.clear();
        this.spiceList.addAll(spiceList);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        if (random.nextInt(5) == 0) {
            MixedBeerEntity mixedBeerEntity = (MixedBeerEntity) world.getBlockEntity(pos);
            DefaultParticleType particle = SpiceAndFlavorManager.getLastSpiceFlavorParticle(mixedBeerEntity.getSpiceList());
            if (random.nextInt(5) == 0) {
                double x = (double) pos.getX() + 0.5D;
                double y = (double) pos.getY() + 0.5D + random.nextDouble() / 4;
                double z = (double) pos.getZ() + 0.5D;
                world.addParticle(particle, x, y, z, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
