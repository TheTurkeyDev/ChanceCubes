package chanceCubes.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import chanceCubes.util.GiantCubeUtil;

public class TileGiantCube extends TileEntity
{
	private boolean hasMaster, isMaster;
	private int masterX, masterY, masterZ;

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if(!worldObj.isRemote)
		{
			if(hasMaster())
			{
				if(isMaster())
				{
					// Put stuff you want the multiblock to do here!
				}
			}
			else
			{
				// Constantly check if structure is formed until it is.
				if(!GiantCubeUtil.checkMultiBlockForm(masterX, masterY, masterZ, worldObj, false))
					GiantCubeUtil.resetStructure(masterX, masterY, masterZ, worldObj);
			}
		}
	}

	/** Reset method to be run when the master is gone or tells them to */
	public void reset()
	{
		masterX = 0;
		masterY = 0;
		masterZ = 0;
		hasMaster = false;
		isMaster = false;
	}

	/** Check that the master exists */
	public boolean checkForMaster()
	{
		TileEntity tile = worldObj.getTileEntity(masterX, masterY, masterZ);
		return(tile != null && (tile instanceof TileGiantCube));
	}

	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		super.writeToNBT(data);
		data.setInteger("masterX", masterX);
		data.setInteger("masterY", masterY);
		data.setInteger("masterZ", masterZ);
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
		masterX = data.getInteger("masterX");
		masterY = data.getInteger("masterY");
		masterZ = data.getInteger("masterZ");
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

	public int getMasterX()
	{
		return masterX;
	}

	public int getMasterY()
	{
		return masterY;
	}

	public int getMasterZ()
	{
		return masterZ;
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
		masterX = x;
		masterY = y;
		masterZ = z;
	}
}