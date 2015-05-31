package chanceCubes.tileentities;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileChanceCube extends TileEntity {
	
	private int chance;
	
	public TileChanceCube()
	{
		this(new Random().nextInt(201)-100);
	}
	
	public TileChanceCube(int initialChance)
	{
		this.chance = initialChance;
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
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("chance", this.chance);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.chance = nbt.getInteger("chance");
	}
}
