package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.BoolVar;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.NBTVar;
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
	private NBTVar teNBT;

	public OffsetTileEntity(int x, int y, int z, Block b, NBTTagCompound te, boolean falling, int delay)
	{
		this(x, y, z, b.getDefaultState(), te, falling, delay);
	}

	public OffsetTileEntity(int x, int y, int z, IBlockState state, NBTTagCompound te, boolean falling)
	{
		this(x, y, z, state, te, falling, 0);
	}

	public OffsetTileEntity(int x, int y, int z, IBlockState state, NBTTagCompound te, BoolVar falling)
	{
		this(new IntVar(x), new IntVar(y), new IntVar(z), state, new NBTVar(te), falling, new IntVar(0));
	}

	public OffsetTileEntity(int x, int y, int z, IBlockState state, NBTTagCompound te, boolean falling, int delay)
	{
		this(new IntVar(x), new IntVar(y), new IntVar(z), state, new NBTVar(te), new BoolVar(falling), new IntVar(delay));
	}

	public OffsetTileEntity(IntVar x, IntVar y, IntVar z, IBlockState state, NBTVar te, BoolVar falling, IntVar delay)
	{
		super(x, y, z, state, falling, delay);
		this.teNBT = te;
	}

	public void spawnInWorld(final World world, final int x, final int y, final int z)
	{
		if(!falling.getBoolValue())
		{
			Scheduler.scheduleTask(new Task("Delayed_Block", this.getDelay())
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
			Scheduler.scheduleTask(new Task("Falling_TileEntity", this.getDelay())
			{
				@Override
				public void callback()
				{
					spawnFallingBlock(world, x, y, z);
				}
			});
		}
	}

	public BlockPos placeInWorld(World world, int x, int y, int z, boolean offset)
	{
		BlockPos pos = super.placeInWorld(world, x, y, z, offset);
		// te.me = this.data;
		if(offset)
			world.setTileEntity(pos, TileEntity.create(world, teNBT.getNBTValue()));
		else
			world.setTileEntity(new BlockPos(x, y, z), TileEntity.create(world, teNBT.getNBTValue()));
		return pos;
	}
}
