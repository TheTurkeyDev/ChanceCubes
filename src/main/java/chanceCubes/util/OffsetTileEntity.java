package chanceCubes.util;

import chanceCubes.blocks.BlockFallingCustom;
import chanceCubes.config.CCubesSettings;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class OffsetTileEntity extends OffsetBlock
{

	private TileEntity te;
	
	public OffsetTileEntity(int x, int y, int z, TileEntity te, boolean falling)
	{
		super(x, y, z, te.blockType, falling);
	}
	
	@Override
	protected void spawnFallingBlock(World world, int x, int y, int z)
	{
		double yy = (((double)(y+yOff+CCubesSettings.dropHeight)) + 0.5) >= 256 ? 255 : (((double)(y+yOff+CCubesSettings.dropHeight)) + 0.5);
		BlockFallingCustom entityfallingblock = new BlockFallingCustom(world, ((double)(x+xOff)) + 0.5, yy, ((double)(z+zOff)) + 0.5 , te.blockType, 0, y+yOff);
		te.writeToNBT(entityfallingblock.field_145810_d);
		world.spawnEntityInWorld(entityfallingblock);
	}
	
	public void spawnInWorld(final World world, final int x, final int y, final int z)
	{
		if(!falling)
		{
			world.setTileEntity(x+xOff, y+yOff, z+zOff, te);
		}
		else
		{
			//super.spawnInWorld(world, x, y, z);
		}
	}

}
