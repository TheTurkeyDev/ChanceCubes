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

	private Block block;

	public OffsetBlock(int x, int y, int z, Block b, boolean falling)
	{
		this.xOff = x;
		this.yOff = y;
		this.zOff = z;
		this.block = b;
		this.falling = falling;
	}

	public void spawnInWorld(World world, int x, int y, int z)
	{
		if(!falling)
		{
			world.setBlock(x+xOff, y+yOff, z+zOff, block);
		}
		else
		{
			double yy = (((double)(y+yOff+CCubesSettings.dropHeight)) + 0.5) >= 256 ? 255 : (((double)(y+yOff+CCubesSettings.dropHeight)) + 0.5);
			BlockFallingCustom entityfallingblock = new BlockFallingCustom(world, ((double)(x+xOff)) + 0.5, yy, ((double)(z+zOff)) + 0.5 , block, y+yOff);
			world.spawnEntityInWorld(entityfallingblock);
		}
	}
}
