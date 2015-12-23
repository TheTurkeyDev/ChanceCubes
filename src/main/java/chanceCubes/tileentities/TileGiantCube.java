package chanceCubes.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

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
		TileEntity tile = worldObj.getTileEntity(masterPos);
		return(tile != null && (tile instanceof TileGiantCube));
	}

	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		super.writeToNBT(data);
		data.setInteger("masterX", masterPos.getX());
		data.setInteger("masterY", masterPos.getY());
		data.setInteger("masterZ", masterPos.getZ());
		data.setBoolean("hasMaster", hasMaster);
		data.setBoolean("isMaster", isMaster);
		if(hasMaster() && isMaster())
		{
			// Any other values should ONLY BE SAVED TO THE MASTER
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound data)
	{
		super.readFromNBT(data);
		int masterX = data.getInteger("masterX");
		int masterY = data.getInteger("masterY");
		int masterZ = data.getInteger("masterZ");
		this.masterPos = new BlockPos(masterX, masterY, masterZ);
		hasMaster = data.getBoolean("hasMaster");
		isMaster = data.getBoolean("isMaster");
		if(hasMaster() && isMaster())
		{
			// Any other values should ONLY BE READ BY THE MASTER
		}
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