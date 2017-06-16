package chanceCubes.rewards.rewardparts;

import chanceCubes.blocks.BlockFallingCustom;
import chanceCubes.config.CCubesSettings;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OffsetBlock
{
	public static String[] elements = new String[] { "XOffSet:I", "YOffSet:I", "ZOffSet:I", "Block:S", "Falling:B", "delay:I", "RelativeToPlayer:B", "removeUnbreakableBlocks:B" };

	protected boolean relativeToPlayer = false;
	public int xOff;
	public int yOff;
	public int zOff;

	protected IBlockState state = null;

	protected boolean falling;
	protected int delay = 0;

	protected boolean causeUpdate = false;

	private boolean removeUnbreakableBlocks = false;

	public OffsetBlock(int x, int y, int z, Block b, boolean falling)
	{
		this(x, y, z, b.getDefaultState(), falling);
	}

	public OffsetBlock(int x, int y, int z, Block b, boolean falling, int delay)
	{
		this(x, y, z, b.getDefaultState(), falling, delay);
	}

	public OffsetBlock(int x, int y, int z, IBlockState state, boolean falling)
	{
		this(x, y, z, state, falling, 0);
	}

	public OffsetBlock(int x, int y, int z, IBlockState state, boolean falling, int delay)
	{
		this.xOff = x;
		this.yOff = y;
		this.zOff = z;
		this.falling = falling;
		this.delay = delay;
		this.state = state;
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
			Scheduler.scheduleTask(new Task("Falling_Block_At_(" + xOff + "," + yOff + "," + zOff + ")", delay)
			{
				@Override
				public void callback()
				{
					spawnFallingBlock(world, x, y, z);
				}
			});
		}
	}

	protected void spawnFallingBlock(World world, int x, int y, int z)
	{
		double yy = (((double) (y + yOff + CCubesSettings.dropHeight)) + 0.5) >= 256 ? 255 : (((double) (y + yOff + CCubesSettings.dropHeight)) + 0.5);
		for(int yyy = (int) yy; yyy >= y + yOff; yyy--)
			RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, new BlockPos((x + xOff), yyy, (z + zOff)), removeUnbreakableBlocks);
		BlockFallingCustom entityfallingblock = new BlockFallingCustom(world, ((double) (x + xOff)) + 0.5, yy, ((double) (z + zOff)) + 0.5, this.state, y + yOff, this);
		world.spawnEntity(entityfallingblock);
	}

	public void setDelay(int delay)
	{
		this.delay = delay;
	}

	public OffsetBlock setBlockState(IBlockState state)
	{
		this.state = state;
		return this;
	}

	public IBlockState getBlockState()
	{
		return this.state;
	}

	public OffsetBlock setRelativeToPlayer(boolean relative)
	{
		this.relativeToPlayer = relative;
		return this;
	}

	public boolean isRelativeToPlayer()
	{
		return this.relativeToPlayer;
	}

	public int getDelay()
	{
		return this.delay;
	}

	public OffsetBlock setCausesBlockUpdate(boolean flag)
	{
		this.causeUpdate = flag;
		return this;
	}

	public boolean isFalling()
	{
		return this.falling;
	}

	public void setFalling(boolean falling)
	{
		this.falling = falling;
	}

	public void setRemoveUnbreakableBlocks(boolean remove)
	{
		removeUnbreakableBlocks = remove;
	}

	public boolean doesRemoveUnbreakableBlocks()
	{
		return this.removeUnbreakableBlocks;
	}

	public void placeInWorld(World world, int x, int y, int z, boolean offset)
	{
		int xx = x;
		int yy = y;
		int zz = z;
		if(offset)
		{
			xx += xOff;
			yy += yOff;
			zz += zOff;
		}
		RewardsUtil.placeBlock(state, world, new BlockPos(xx, yy, zz), causeUpdate ? 3 : 2, this.removeUnbreakableBlocks);
		BlockPos surfacefPos = new BlockPos(xx, yy - 1, zz);
		Block bSurface = world.getBlockState(surfacefPos).getBlock();
		SoundType sound = bSurface.getSoundType(world.getBlockState(surfacefPos), world, surfacefPos, null);
		world.playSound(null, (double) ((float) xx + 0.5F), (double) ((float) yy + 0.5F), (double) ((float) zz + 0.5F), sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getVolume() * 0.5F);
	}

	public void placeInWorld(World world, BlockPos position, boolean offset)
	{
		this.placeInWorld(world, position.getX(), position.getY(), position.getZ(), offset);
	}
}
