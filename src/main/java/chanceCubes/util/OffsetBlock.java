package chanceCubes.util;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import chanceCubes.blocks.BlockFallingCustom;
import chanceCubes.config.CCubesSettings;

public class OffsetBlock
{
	public int xOff;
	public int yOff;
	public int zOff;
	
	private boolean falling;
	private int delay = 0;

	private Block block;

	public OffsetBlock(int x, int y, int z, Block b, boolean falling)
	{
		this.xOff = x;
		this.yOff = y;
		this.zOff = z;
		this.block = b;
		this.falling = falling;
	}

	public void spawnInWorld(final World world, final int x, final int y, final int z)
	{
		if(!falling)
		{
			world.setBlock(x+xOff, y+yOff, z+zOff, block);
		}
		else
		{
			if(delay != 0)
			{
				Task task = new Task()
				{
					@Override
					public void callback()
					{
						spawnFallingBlock(world, x, y, z);
					}
				};
				Scheduler.scheduleTask("Falling_Block_At_(" + xOff + "," + yOff + "," + zOff + ")", delay, task);
			}
			else
			{
				spawnFallingBlock(world, x, y, z);
			}
				
		}
	}
	
	private void spawnFallingBlock(World world, int x, int y, int z)
	{
		double yy = (((double)(y+yOff+CCubesSettings.dropHeight)) + 0.5) >= 256 ? 255 : (((double)(y+yOff+CCubesSettings.dropHeight)) + 0.5);
		BlockFallingCustom entityfallingblock = new BlockFallingCustom(world, ((double)(x+xOff)) + 0.5, yy, ((double)(z+zOff)) + 0.5 , block, y+yOff);
		world.spawnEntityInWorld(entityfallingblock);
	}
	
	public void setDealy(int delay)
	{
		this.delay = delay;
	}
}
