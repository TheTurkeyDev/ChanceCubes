package chanceCubes.tileentities;

import chanceCubes.blocks.CCubesBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class TileChanceCube extends BlockEntity
{
	private static final Random random = new Random();

	private int chance;
	private boolean isScanned = false;

	public TileChanceCube(BlockPos pos, BlockState state)
	{
		this(Math.round((float) (random.nextGaussian() * 40)), pos, state);
	}

	public TileChanceCube(int initialChance, BlockPos pos, BlockState state)
	{
		super(CCubesBlocks.TILE_CHANCE_CUBE.get(), pos, state);
		while(initialChance > 100 || initialChance < -100)
			initialChance = Math.round((float) (random.nextGaussian() * 40));
		this.setChance(initialChance);
	}

	public void setChance(int newChance)
	{
		this.chance = newChance;
	}

	public int getChance()
	{
		return this.chance;
	}

	@Override
	protected void saveAdditional(CompoundTag nbt, Provider registries)
	{
		super.saveAdditional(nbt, registries);
		nbt.putInt("chance", this.getChance());
	}

	@Override
	public void loadAdditional(CompoundTag nbt, Provider registries)
	{
		super.loadAdditional(nbt, registries);
		this.chance = nbt.getInt("chance");
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries)
	{
		CompoundTag nbt = new CompoundTag();
		this.saveAdditional(nbt, registries);
		return nbt;
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider registries)
	{
		loadAdditional(pkt.getTag(), registries);
	}

	public boolean isScanned()
	{
		return isScanned;
	}

	public void setScanned(boolean isScanned)
	{
		this.isScanned = isScanned;
	}
}
