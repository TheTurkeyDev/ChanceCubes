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

public class TileGiantCube extends BlockEntity
{
	private boolean hasMaster, isMaster;
	private BlockPos masterPos = new BlockPos(0, 0, 0);

	public TileGiantCube(BlockPos pos, BlockState state)
	{
		super(CCubesBlocks.TILE_CHANCE_GIANT.get(), pos, state);
	}

	/**
	 * Reset method to be run when the master is gone or tells them to
	 */
	public void reset()
	{
		masterPos = new BlockPos(0, 0, 0);
		hasMaster = false;
		isMaster = false;
	}

	/**
	 * Check that the master exists
	 */
	public boolean checkForMaster()
	{
		BlockEntity tile = level.getBlockEntity(this.worldPosition.offset(masterPos));
		return (tile instanceof TileGiantCube);
	}

	@Override
	protected void saveAdditional(CompoundTag data, Provider registries)
	{
		super.saveAdditional(data, registries);
		data.putInt("masterX", masterPos.getX());
		data.putInt("masterY", masterPos.getY());
		data.putInt("masterZ", masterPos.getZ());
		data.putBoolean("hasMaster", hasMaster);
		data.putBoolean("isMaster", isMaster);
	}

	@Override
	public void loadAdditional(CompoundTag data, Provider registries)
	{
		super.loadAdditional(data, registries);
		int masterX = data.getInt("masterX");
		int masterY = data.getInt("masterY");
		int masterZ = data.getInt("masterZ");
		this.masterPos = new BlockPos(masterX, masterY, masterZ);
		hasMaster = data.getBoolean("hasMaster");
		isMaster = data.getBoolean("isMaster");
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

	public boolean hasMaster()
	{
		return hasMaster;
	}

	public boolean isMaster()
	{
		return isMaster;
	}

	public BlockPos getMasterPostion()
	{
		return this.worldPosition.offset(masterPos);
	}

	public BlockPos getMasterOffset()
	{
		return this.masterPos;
	}

	public void setHasMaster(boolean bool)
	{
		hasMaster = bool;
	}

	public void setIsMaster(boolean bool)
	{
		isMaster = bool;
	}

	public void setMasterCoords(int x, int y, int z)
	{
		this.setMasterCoords(new BlockPos(x, y, z));
	}

	public void setMasterCoords(BlockPos pos)
	{
		this.masterPos = pos.subtract(this.worldPosition);
	}
}