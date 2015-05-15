package chanceCubes.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileChanceCube extends TileEntity {
	
	private double luck;
	
	public TileChanceCube()
	{
		this(0);
	}
	
	public TileChanceCube(int initialLuck)
	{
		this.luck = initialLuck;
	}
	
	public void setLuck(int newLuck)
	{
		this.luck = newLuck;
	}
	
	public double getLuck()
	{
		return this.luck;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setDouble("luck", this.luck);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.luck = nbt.getDouble("luck");
	}

}
