package chanceCubes.rewards.rewardparts;

import chanceCubes.blocks.BlockFallingCustom;
import chanceCubes.config.CCubesSettings;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class OffsetTileEntity extends OffsetBlock
{

	private TileEntity te;

	public OffsetTileEntity(int x, int y, int z, Block block, TileEntity te, boolean falling)
	{
		this(x, y, z, block, te, falling, 0);
	}

	public OffsetTileEntity(int x, int y, int z, Block block, TileEntity te, boolean falling, int delay)
	{
		super(x, y, z, block, falling, delay);
		this.te = te;
	}

	@Override
	protected void spawnFallingBlock(World world, int x, int y, int z)
	{
		double yy = (((double) (y + yOff + CCubesSettings.dropHeight)) + 0.5) >= 256 ? 255 : (((double) (y + yOff + CCubesSettings.dropHeight)) + 0.5);
		BlockFallingCustom entityfallingblock = new BlockFallingCustom(world, ((double) (x + xOff)) + 0.5, yy, ((double) (z + zOff)) + 0.5, te.getBlockType().getDefaultState(), 0, y + yOff, this);
		world.spawnEntityInWorld(entityfallingblock);
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
				Task task = new Task("Falling_TileEntity_At_(" + xOff + "," + yOff + "," + zOff + ")", delay)
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
		te.getBlockMetadata();
		if(offset)
			world.setTileEntity(new BlockPos(x + xOff, y + yOff, z + zOff), te);
		else
			world.setTileEntity(new BlockPos(x, y, z), te);
		te.markDirty();
	}
}
