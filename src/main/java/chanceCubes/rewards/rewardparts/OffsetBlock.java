package chanceCubes.rewards.rewardparts;

import chanceCubes.blocks.BlockFallingCustom;
import chanceCubes.config.CCubesSettings;
import chanceCubes.rewards.variableTypes.BoolVar;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class OffsetBlock extends BasePart
{
	protected BoolVar relativeToPlayer = new BoolVar(false);
	public IntVar xOff;
	public IntVar yOff;
	public IntVar zOff;

	protected BlockState state;

	protected BoolVar falling;
	protected BoolVar causeUpdate = new BoolVar(false);
	private BoolVar removeUnbreakableBlocks = new BoolVar(false);
	protected BoolVar playSound = new BoolVar(true);

	public OffsetBlock(int x, int y, int z, Block b, boolean falling)
	{
		this(x, y, z, b.defaultBlockState(), falling);
	}

	public OffsetBlock(int x, int y, int z, Block b, BoolVar falling)
	{
		this(x, y, z, b, falling, new IntVar(0));
	}

	public OffsetBlock(int x, int y, int z, Block b, BoolVar falling, IntVar delay)
	{
		this(new IntVar(x), new IntVar(y), new IntVar(z), b.defaultBlockState(), falling, delay);
	}

	public OffsetBlock(int x, int y, int z, Block b, boolean falling, int delay)
	{
		this(x, y, z, b.defaultBlockState(), falling, delay);
	}

	public OffsetBlock(int x, int y, int z, BlockState state, boolean falling)
	{
		this(x, y, z, state, falling, 0);
	}

	public OffsetBlock(int x, int y, int z, BlockState state, boolean falling, int delay)
	{
		this(new IntVar(x), new IntVar(y), new IntVar(z), state, new BoolVar(falling), new IntVar(delay));
	}

	public OffsetBlock(IntVar x, IntVar y, IntVar z, Block b, BoolVar falling)
	{
		this(x, y, z, b.defaultBlockState(), falling, new IntVar(0));
	}

	public OffsetBlock(IntVar x, IntVar y, IntVar z, BlockState state, BoolVar falling, IntVar delay)
	{
		this.xOff = x;
		this.yOff = y;
		this.zOff = z;
		this.falling = falling;
		this.setDelay(delay);
		this.state = state;
	}

	public void spawnInWorld(final Level level, final int x, final int y, final int z)
	{
		this.spawnInWorld(level, x, y, z, null);
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
			Scheduler.scheduleTask(new Task("Falling_Block", this.getDelay())
			{
				@Override
				public void callback()
				{
					spawnFallingBlock(level, x, y, z, blockCache);
				}
			});
		}
	}

	protected void spawnFallingBlock(Level level, int x, int y, int z, RewardBlockCache blockCache)
	{
		int xOffVal = xOff.getIntValue();
		int yOffVal = yOff.getIntValue();
		int zOffVal = zOff.getIntValue();
		double yy = (((double) (y + yOffVal + CCubesSettings.dropHeight.get())) + 0.5) >= 256 ? 255 : (((double) (y + yOffVal + CCubesSettings.dropHeight.get())) + 0.5);
		for(int yyy = (int) yy; yyy >= y + yOffVal; yyy--)
		{
			BlockPos offsetPos = new BlockPos((x + xOffVal), yyy, (z + zOffVal));
			if(blockCache != null)
				blockCache.cacheBlock(offsetPos, Blocks.AIR.defaultBlockState());
			else
				RewardsUtil.placeBlock(Blocks.AIR.defaultBlockState(), level, offsetPos, removeUnbreakableBlocks.getBoolValue());
		}
		BlockFallingCustom entityfallingblock = new BlockFallingCustom(level, ((double) (x + xOffVal)) + 0.5, yy, ((double) (z + zOffVal)) + 0.5, this.state, y + yOffVal, this);
		level.addFreshEntity(entityfallingblock);
	}

	public OffsetBlock setBlockState(BlockState state)
	{
		this.state = state;
		return this;
	}

	public BlockState getBlockState()
	{
		return this.state;
	}

	public OffsetBlock setRelativeToPlayer(boolean relative)
	{
		return this.setRelativeToPlayer(new BoolVar(relative));
	}

	public OffsetBlock setRelativeToPlayer(BoolVar relative)
	{
		this.relativeToPlayer = relative;
		return this;
	}

	public boolean isRelativeToPlayer()
	{
		return this.relativeToPlayer.getBoolValue();
	}

	public IntVar getDelayVar()
	{
		return this.delay;
	}

	public OffsetBlock setCausesBlockUpdate(boolean flag)
	{
		return this.setCausesBlockUpdate(new BoolVar(flag));
	}

	public OffsetBlock setCausesBlockUpdate(BoolVar flag)
	{
		this.causeUpdate = flag;
		return this;
	}

	public boolean isFalling()
	{
		return this.falling.getBoolValue();
	}

	public BoolVar isFallingVar()
	{
		return this.falling;
	}

	public void setFalling(boolean falling)
	{
		this.setFalling(new BoolVar(falling));
	}

	public void setFalling(BoolVar falling)
	{
		this.falling = falling;
	}

	public void setRemoveUnbreakableBlocks(boolean remove)
	{
		this.setRemoveUnbreakableBlocks(new BoolVar(remove));
	}

	public void setRemoveUnbreakableBlocks(BoolVar remove)
	{
		removeUnbreakableBlocks = remove;
	}

	public boolean doesRemoveUnbreakableBlocks()
	{
		return this.removeUnbreakableBlocks.getBoolValue();
	}

	public void setPlaysSound(BoolVar playSound)
	{
		this.playSound = playSound;
	}

	public boolean doesPlaySound()
	{
		return this.playSound.getBoolValue();
	}

	public BlockPos placeInWorld(Level level, BlockPos position, boolean offset, RewardBlockCache blockCache)
	{
		BlockPos offsetPos = new BlockPos(0, 0, 0);
		if(offset)
			offsetPos = offsetPos.offset(xOff.getIntValue(), yOff.getIntValue(), zOff.getIntValue());
		BlockPos placePos = position.offset(offsetPos);

		if(blockCache != null)
			blockCache.cacheBlock(offsetPos, state, causeUpdate.getBoolValue() ? 3 : 2);
		else
			RewardsUtil.placeBlock(state, level, placePos, causeUpdate.getBoolValue() ? 3 : 2, this.removeUnbreakableBlocks.getBoolValue());

		if(this.playSound.getBoolValue())
		{
			BlockPos surfacefPos = placePos.offset(0, -1, 0);
			Block bSurface = level.getBlockState(surfacefPos).getBlock();
			SoundType sound = bSurface.getSoundType(level.getBlockState(surfacefPos), level, surfacefPos, null);
			level.playSound(null, ((float) placePos.getX() + 0.5F), ((float) placePos.getY() + 0.5F), ((float) placePos.getZ() + 0.5F), sound.getPlaceSound(), SoundSource.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getVolume() * 0.5F);
		}

		return placePos;
	}
}
