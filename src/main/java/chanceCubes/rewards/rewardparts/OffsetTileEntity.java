package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.BoolVar;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.NBTVar;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OffsetTileEntity extends OffsetBlock
{
	private NBTVar teNBT;

	public OffsetTileEntity(int x, int y, int z, Block b, CompoundNBT te, boolean falling, int delay)
	{
		this(x, y, z, b.getDefaultState(), te, falling, delay);
	}

	public OffsetTileEntity(int x, int y, int z, BlockState state, CompoundNBT te, boolean falling)
	{
		this(x, y, z, state, te, falling, 0);
	}

	public OffsetTileEntity(int x, int y, int z, BlockState state, CompoundNBT te, BoolVar falling)
	{
		this(new IntVar(x), new IntVar(y), new IntVar(z), state, new NBTVar(te), falling, new IntVar(0));
	}

	public OffsetTileEntity(int x, int y, int z, BlockState state, CompoundNBT te, boolean falling, int delay)
	{
		this(new IntVar(x), new IntVar(y), new IntVar(z), state, new NBTVar(te), new BoolVar(falling), new IntVar(delay));
	}

	public OffsetTileEntity(IntVar x, IntVar y, IntVar z, BlockState state, NBTVar te, BoolVar falling, IntVar delay)
	{
		super(x, y, z, state, falling, delay);
		this.teNBT = te;
	}

	public void spawnInWorld(final World world, final int x, final int y, final int z, RewardBlockCache blockCache)
	{
		if(!falling.getBoolValue())
		{
			Scheduler.scheduleTask(new Task("Delayed_Block", this.getDelay())
			{
				@Override
				public void callback()
				{
					placeInWorld(world, new BlockPos(x, y, z), true, blockCache);
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
					spawnFallingBlock(world, x, y, z, blockCache);
				}
			});
		}
	}

	public BlockPos placeInWorld(World world, BlockPos placeLoc, boolean offset, RewardBlockCache blockCache)
	{
		BlockPos pos = super.placeInWorld(world, placeLoc, offset, blockCache);
		TileEntity te = TileEntity.create(teNBT.getNBTValue());
		if(!offset)
			pos = placeLoc;

		te.setPos(pos);
		world.setTileEntity(pos, te);

		return pos;
	}
}
