package chanceCubes.blocks;

import chanceCubes.util.GiantCubeUtil;
import net.minecraft.world.World;

public class BlockCompactGiantCube extends BaseChanceBlock
{
	public static final String blockName = "Compact_Giant_Chance_Cube";

	public BlockCompactGiantCube()
	{
		super("Compact_Giant_Chance_Cube");
		this.setHardness(0.5f);
		this.setBlockTextureName("chancecubes:chanceCube");
	}

	public void onPostBlockPlaced(World world, int x, int y, int z, int p_149714_5_)
	{
		super.onPostBlockPlaced(world, x, y, z, p_149714_5_);
		if(world.isRemote)
			return;
		GiantCubeUtil.setupStructure(x - 1, y, z - 1, world, true);
	}
}