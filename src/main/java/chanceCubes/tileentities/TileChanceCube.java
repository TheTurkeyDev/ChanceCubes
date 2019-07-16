package chanceCubes.tileentities;

import java.util.Random;

import chanceCubes.blocks.CCubesBlocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

public class TileChanceCube extends TileEntity
{
	private static Random random = new Random();

	private int chance;
	private boolean isScanned = false;

	public TileChanceCube()
	{
		this(Math.round((float) (random.nextGaussian() * 40)));
	}

	public TileChanceCube(int initialChance)
	{
		super(CCubesBlocks.TILE_CHANCE_CUBE);
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
	public CompoundNBT write(CompoundNBT nbt)
	{
		super.write(nbt);
		nbt.putInt("chance", this.getChance());
		return nbt;
	}

	@Override
	public void read(CompoundNBT nbt)
	{
		super.read(nbt);
		this.chance = nbt.getInt("chance");
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.pos, 1, this.getUpdateTag());
	}

	public CompoundNBT getUpdateTag()
	{
		return this.write(new CompoundNBT());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		read(pkt.getNbtCompound());
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
