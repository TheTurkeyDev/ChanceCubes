package chanceCubes.rewards.rewardparts;

import chanceCubes.blocks.BlockFallingCustom;
import chanceCubes.config.CCubesSettings;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OffsetBlock
{
	public static String[] elements = new String[] { "XOffSet:I", "YOffSet:I", "ZOffSet:I", "Block:S", "Falling:B", "delay:I", "RelativeToPlayer:B" };

	protected boolean relativeToPlayer = false;
	public int xOff;
	public int yOff;
	public int zOff;

	@Deprecated
	protected byte data = 0;

	protected IBlockState state = null;

	protected boolean falling;
	protected int delay = 0;

	protected boolean causeUpdate = false;

	protected Block block;

	public OffsetBlock(int x, int y, int z, Block b, boolean falling)
	{
		this.xOff = x;
		this.yOff = y;
		this.zOff = z;
		this.block = b;
		this.falling = falling;
	}

	public OffsetBlock(int x, int y, int z, Block b, boolean falling, int delay)
	{
		this.xOff = x;
		this.yOff = y;
		this.zOff = z;
		this.block = b;
		this.falling = falling;
		this.delay = delay;
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

	protected void spawnFallingBlock(World world, int x, int y, int z)
	{
		double yy = (((double) (y + yOff + CCubesSettings.dropHeight)) + 0.5) >= 256 ? 255 : (((double) (y + yOff + CCubesSettings.dropHeight)) + 0.5);
		for(int yyy = (int) yy; yyy >= y + yOff; yyy--)
			RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, new BlockPos((x + xOff), yyy, (z + zOff)));
		BlockFallingCustom entityfallingblock = new BlockFallingCustom(world, ((double) (x + xOff)) + 0.5, yy, ((double) (z + zOff)) + 0.5, block.getDefaultState(), data, y + yOff, this);
		world.spawnEntityInWorld(entityfallingblock);
	}

	public Block getBlock()
	{
		return this.block;
	}

	public void setDelay(int delay)
	{
		this.delay = delay;
	}

	@Deprecated
	public void setData(byte d)
	{
		this.data = d;
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
		RewardsUtil.placeBlock(state == null ? block.getDefaultState() : state, world, new BlockPos(xx, yy, zz), causeUpdate ? 3 : 2);
		Block bSurface = world.getBlockState(new BlockPos(xx, yy - 1, zz)).getBlock();
		world.playSound(null, (double) ((float) xx + 0.5F), (double) ((float) yy + 0.5F), (double) ((float) zz + 0.5F), bSurface.getSoundType().getPlaceSound(), SoundCategory.BLOCKS, (bSurface.getSoundType().getVolume() + 1.0F) / 2.0F, bSurface.getSoundType().getVolume() * 0.5F);
	}

	public void placeInWorld(World world, BlockPos position, boolean offset)
	{
		this.placeInWorld(world, position.getX(), position.getY(), position.getZ(), offset);
	}
}
