package chanceCubes.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import chanceCubes.blocks.BlockFallingCustom;
import chanceCubes.config.CCubesSettings;

public class OffsetTileEntity extends OffsetBlock
{

	private TileEntity te;

	public OffsetTileEntity(int x, int y, int z, TileEntity te, boolean falling)
	{
		super(x, y, z, te.blockType, falling);
		this.te = te;
	}

	@Override
	protected void spawnFallingBlock(World world, int x, int y, int z)
	{
		double yy = (((double) (y + yOff + CCubesSettings.dropHeight)) + 0.5) >= 256 ? 255 : (((double) (y + yOff + CCubesSettings.dropHeight)) + 0.5);
		BlockFallingCustom entityfallingblock = new BlockFallingCustom(world, ((double) (x + xOff)) + 0.5, yy, ((double) (z + zOff)) + 0.5, te.blockType, 0, y + yOff, this);
		world.spawnEntityInWorld(entityfallingblock);
	}

	public void spawnInWorld(final World world, final int x, final int y, final int z)
	{
		if(!falling)
		{
			this.placeInWorld(world, x, y, z, true);
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
		te.blockMetadata = this.data;
		if(offset)
			world.setTileEntity(x + xOff, y + yOff, z + zOff, te);
		else
			world.setTileEntity(x, y, z, te);
		te.markDirty();
	}
}
