package chanceCubes.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileGiantCube extends TileEntity
{
	private boolean hasMaster, isMaster;
	private BlockPos masterPos;

	/** Reset method to be run when the master is gone or tells them to */
	public void reset()
	{
		masterPos = new BlockPos(0, 0, 0);
		hasMaster = false;
		isMaster = false;
	}

	/** Check that the master exists */
	public boolean checkForMaster()
	{
		TileEntity tile = world.getTileEntity(masterPos);
		return(tile != null && (tile instanceof TileGiantCube));
	}

	@Override
	public NBTTagCompound write(NBTTagCompound data)
	{
		data = super.write(data);
		if(masterPos == null)
			masterPos = new BlockPos(0, 0, 0);
		data.setInt("masterX", masterPos.getX());
		data.setInt("masterY", masterPos.getY());
		data.setInt("masterZ", masterPos.getZ());
		data.setBoolean("hasMaster", hasMaster);
		data.setBoolean("isMaster", isMaster);
		return data;
	}

	@Override
	public void read(NBTTagCompound data)
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
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(this.pos, 0, getUpdateTag());
	}

	public NBTTagCompound getUpdateTag()
	{
		return this.write(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
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
		return masterPos;
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
		this.masterPos = pos;
	}
}