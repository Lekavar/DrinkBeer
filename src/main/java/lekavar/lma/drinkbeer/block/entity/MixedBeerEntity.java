package lekavar.lma.drinkbeer.block.entity;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.manager.MixedBeerManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MixedBeerEntity extends BlockEntity {
    private int beerId;
    private List<Integer> spiceList = new ArrayList<>();

    public MixedBeerEntity() {
        super(DrinkBeer.MIXED_BEER_ENTITY);
    }

    public MixedBeerEntity(int beerId, List<Integer> spiceList) {
        super(DrinkBeer.MIXED_BEER_ENTITY);
        this.beerId = beerId;
        this.spiceList.clear();
        this.spiceList.addAll(spiceList);
        this.markDirty();
    }

    public List<Integer> getSpiceList() {
        return spiceList;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        //Write beerId
        tag.putShort("beerId", (short) this.beerId);

        //Write spiceList
        ListTag listTag = MixedBeerManager.genSpiceListTag(spiceList);
        if (listTag != null) {
            tag.put("Spices", listTag);
        }

        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);

        //Read beerId
        this.beerId = tag.getShort("beerId");

        //Read spiceList
        this.spiceList.clear();
        ListTag listTag = tag.getList("Spices", 10);
        for (int i = 0; i < listTag.size() && i < MixedBeerManager.MAX_SPICES_COUNT; ++i) {
            CompoundTag tag2 = listTag.getCompound(i);
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
        return new BlockEntityUpdateS2CPacket(this.pos, 6, this.toInitialChunkDataTag());
    }

    @Override
    public CompoundTag toInitialChunkDataTag() {
        return this.toTag(new CompoundTag());
    }
}
