package chanceCubes.tileentities;

import chanceCubes.blocks.CCubesBlocks;
import net.minecraft.core.BlockPos;
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
		super(CCubesBlocks.TILE_CHANCE_CUBE, pos, state);
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
	public void saveAdditional(CompoundTag nbt)
	{
		super.saveAdditional(nbt);
		nbt.putInt("chance", this.getChance());
	}

	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		this.chance = nbt.getInt("chance");
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag()
	{
		CompoundTag tag = new CompoundTag();
		tag.putInt("chance", this.getChance());
		return tag;
	}

	@Override
	public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket pkt)
	{
		load(pkt.getTag());
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
