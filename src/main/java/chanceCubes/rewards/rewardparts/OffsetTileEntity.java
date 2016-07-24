package chanceCubes.rewards.rewardparts;

import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class OffsetTileEntity extends OffsetBlock
{

	private NBTTagCompound nbt;

	public OffsetTileEntity(int x, int y, int z, Block block, NBTTagCompound nbt, boolean falling)
	{
		this(x, y, z, block, nbt, falling, 0);
	}
	public OffsetTileEntity(int x, int y, int z, Block block, NBTTagCompound nbt, boolean falling, int delay)
	{
		super(x, y, z, block, falling, delay);
		this.nbt = nbt;
	}

	public void spawnInWorld(final World world, final int x, final int y, final int z)
	{
		if(!falling)
		{
			if(delay != 0)
			{
				Task task = new Task("Delayed_Block_At_(" + xOff + "," + yOff + "," + zOff + ")", delay)
				{
					@Override
					public void callback()
					{
						placeInWorld(world, x, y, z, true);
					}
				};
				Scheduler.scheduleTask(task);
			}
			else
			{
				placeInWorld(world, x, y, z, true);
			}
		}
		else
		{
			if(delay != 0)
			{
				Task task = new Task("Falling_Block_At_(" + xOff + "," + yOff + "," + zOff + ")", delay)
				{
					@Override
					public void callback()
					{
						spawnFallingBlock(world, x, y, z);
					}
				};
				Scheduler.scheduleTask(task);
			}
			else
			{
				spawnFallingBlock(world, x, y, z);
			}
		}
	}

	public void placeInWorld(World world, int x, int y, int z, boolean offset)
	{
		super.placeInWorld(world, x, y, z, offset);
		TileEntity te = TileEntity.createAndLoadEntity(nbt);
		te.blockMetadata = this.data;
		if(offset)
			world.setTileEntity(x + xOff, y + yOff, z + zOff, te);
		else
			world.setTileEntity(x, y, z, te);
		te.markDirty();
	}
}
