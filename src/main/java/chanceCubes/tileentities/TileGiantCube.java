package chanceCubes.tileentities;

import chanceCubes.blocks.CCubesBlocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileGiantCube extends TileEntity
{
	private boolean hasMaster, isMaster;
	private BlockPos masterPos;

	public TileGiantCube()
	{
		super(CCubesBlocks.TILE_CHANCE_GIANT);
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
		TileEntity tile = world.getTileEntity(this.pos.add(masterPos));
		return (tile instanceof TileGiantCube);
	}

	@Override
	public CompoundNBT write(CompoundNBT data)
	{
		data = super.write(data);
		if(masterPos == null)
			masterPos = new BlockPos(0, 0, 0);
		data.putInt("masterX", masterPos.getX());
		data.putInt("masterY", masterPos.getY());
		data.putInt("masterZ", masterPos.getZ());
		data.putBoolean("hasMaster", hasMaster);
		data.putBoolean("isMaster", isMaster);
		return data;
	}

	@Override
	public void read(CompoundNBT data)
	{
		super.read(data);
		int masterX = data.getInt("masterX");
		int masterY = data.getInt("masterY");
		int masterZ = data.getInt("masterZ");
		this.masterPos = new BlockPos(masterX, masterY, masterZ);
		hasMaster = data.getBoolean("hasMaster");
		isMaster = data.getBoolean("isMaster");
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.pos, 0, getUpdateTag());
	}

	@Override
	public CompoundNBT getUpdateTag()
	{
		return this.write(new CompoundNBT());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		read(pkt.getNbtCompound());
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
		return this.pos.add(masterPos);
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
		this.masterPos = pos.subtract(this.pos);
	}
}