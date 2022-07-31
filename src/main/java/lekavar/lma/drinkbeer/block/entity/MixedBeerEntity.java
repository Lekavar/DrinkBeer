package lekavar.lma.drinkbeer.block.entity;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.manager.MixedBeerManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MixedBeerEntity extends BlockEntity {
    private int beerId;
    private List<Integer> spiceList = new ArrayList<>();

    public MixedBeerEntity(BlockPos pos, BlockState state) {
        super(DrinkBeer.MIXED_BEER_ENTITY, pos, state);
    }

    public MixedBeerEntity(BlockPos pos, BlockState state, int beerId, List<Integer> spiceList) {
        super(DrinkBeer.MIXED_BEER_ENTITY, pos, state);
        this.beerId = beerId;
        this.spiceList.clear();
        this.spiceList.addAll(spiceList);
        this.markDirty();
    }

    public List<Integer> getSpiceList() {
        return spiceList;
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        
        //Write beerId
        tag.putShort("beerId", (short) this.beerId);

        //Write spiceList
        NbtList listTag = MixedBeerManager.genSpiceListTag(spiceList);
        if (listTag != null) {
            tag.put("Spices", listTag);
        }

        super.writeNbt(tag);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        //Read beerId
        this.beerId = tag.getShort("beerId");

        //Read spiceList
        this.spiceList.clear();
        NbtList listTag = tag.getList("Spices", 10);
        for (int i = 0; i < listTag.size() && i < MixedBeerManager.MAX_SPICES_COUNT; ++i) {
            NbtCompound tag2 = listTag.getCompound(i);
            int spiceId = tag2.getInt("SpiceId");
            this.spiceList.add(spiceId);
        }
    }

    @Environment(EnvType.CLIENT)
    public ItemStack getPickStack(BlockState state) {
        //Generate mixed beer item stack for dropping
        ItemStack resultStack = MixedBeerManager.genMixedBeerItemStack(this.beerId, this.spiceList);
        return resultStack;
    }

    public int getBeerId() {
        return beerId;
    }

    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
}
