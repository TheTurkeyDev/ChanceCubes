package chanceCubes.rewards.rewardparts;

import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OffsetTileEntity extends OffsetBlock
{

	private NBTTagCompound teNBT;

	public OffsetTileEntity(int x, int y, int z, Block b, NBTTagCompound te, boolean falling, int delay)
	{
		this(x, y, z, b.getDefaultState(), te, falling, delay);
	}

	public OffsetTileEntity(int x, int y, int z, IBlockState state, NBTTagCompound te, boolean falling)
	{
		this(x, y, z, state, te, falling, 0);
	}

	public OffsetTileEntity(int x, int y, int z, IBlockState state, NBTTagCompound te, boolean falling, int delay)
	{
		super(x, y, z, state, falling, delay);
		this.teNBT = te;
	}

	public void spawnInWorld(final World world, final int x, final int y, final int z)
	{
		if(!falling)
		{
			Scheduler.scheduleTask(new Task("Delayed_Block_At_(" + xOff + "," + yOff + "," + zOff + ")", delay)
			{
				@Override
				public void callback()
				{
					placeInWorld(world, x, y, z, true);
				}
			});
		}
		else
		{
			Scheduler.scheduleTask(new Task("Falling_TileEntity_At_(" + xOff + "," + yOff + "," + zOff + ")", delay)
			{
				@Override
				public void callback()
				{
					spawnFallingBlock(world, x, y, z);
				}
			});
		}
	}

	public void placeInWorld(World world, int x, int y, int z, boolean offset)
	{
		super.placeInWorld(world, x, y, z, offset);
		// te.me = this.data;
		if(offset)
			world.setTileEntity(new BlockPos(x + xOff, y + yOff, z + zOff), TileEntity.create(world, teNBT));
		else
			world.setTileEntity(new BlockPos(x, y, z), TileEntity.create(world, teNBT));
	}
}
