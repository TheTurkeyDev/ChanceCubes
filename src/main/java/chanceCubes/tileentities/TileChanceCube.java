package chanceCubes.tileentities;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileChanceCube extends TileEntity {
	
	private int luck;
	
	public TileChanceCube()
	{
		this(new Random().nextInt(201)-100);
	}
	
	public TileChanceCube(int initialLuck)
	{
		this.luck = initialLuck;
	}
	
	public void setLuck(int newLuck)
	{
		this.luck = newLuck;
	}
	
	public int getLuck()
	{
		return this.luck;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("luck", this.luck);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.luck = nbt.getInteger("luck");
	}
}
