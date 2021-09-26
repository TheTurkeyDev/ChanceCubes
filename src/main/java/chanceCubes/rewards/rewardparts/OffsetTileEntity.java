package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.BoolVar;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.NBTVar;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class OffsetTileEntity extends OffsetBlock
{
	private final NBTVar teNBT;

	public OffsetTileEntity(int x, int y, int z, Block b, CompoundTag te, boolean falling, int delay)
	{
		this(x, y, z, b.defaultBlockState(), te, falling, delay);
	}

	public OffsetTileEntity(int x, int y, int z, BlockState state, CompoundTag te, boolean falling)
	{
		this(x, y, z, state, te, falling, 0);
	}

	public OffsetTileEntity(int x, int y, int z, BlockState state, CompoundTag te, BoolVar falling)
	{
		this(new IntVar(x), new IntVar(y), new IntVar(z), state, new NBTVar(te), falling, new IntVar(0));
	}

	public OffsetTileEntity(int x, int y, int z, BlockState state, CompoundTag te, boolean falling, int delay)
	{
		this(new IntVar(x), new IntVar(y), new IntVar(z), state, new NBTVar(te), new BoolVar(falling), new IntVar(delay));
	}

	public OffsetTileEntity(IntVar x, IntVar y, IntVar z, BlockState state, NBTVar te, BoolVar falling, IntVar delay)
	{
		super(x, y, z, state, falling, delay);
		this.teNBT = te;
	}

	public void spawnInWorld(final Level level, final int x, final int y, final int z, RewardBlockCache blockCache)
	{
		if(!falling.getBoolValue())
		{
			Scheduler.scheduleTask(new Task("Delayed_Block", this.getDelay())
			{
				@Override
				public void callback()
				{
					placeInWorld(level, new BlockPos(x, y, z), true, blockCache);
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
					spawnFallingBlock(level, x, y, z, blockCache);
				}
			});
		}
	}

	public BlockPos placeInWorld(Level level, BlockPos placeLoc, boolean offset, RewardBlockCache blockCache)
	{
		BlockPos pos = super.placeInWorld(level, placeLoc, offset, blockCache);
		if(!offset)
			pos = placeLoc;

		BlockEntity te = BlockEntity.loadStatic(pos, level.getBlockState(pos), teNBT.getNBTValue());

		level.setBlockEntity(te);

		return pos;
	}
}
