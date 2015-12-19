package chanceCubes.rewards.biodomeGen;

import java.util.List;

import net.minecraft.world.World;
import chanceCubes.rewards.rewardparts.OffsetBlock;

public interface IBioDomeBiome
{
	public List<OffsetBlock> genDome(int centerX, int centerY, int centerZ, World world);
	
	public void spawnEntities(int centerX, int centerY, int centerZ, World world);
}
