package chanceCubes.rewards;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;

public class FiveProngReward implements IChanceCubeReward
{
	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		for(int xx = x-3; xx <= x+3; xx++)
		{
			for(int zz = z-3; zz <= z+3; zz++)
			{
				for(int yy = y; yy <= y+4; yy++)
				{
					world.setBlockToAir(xx, yy, zz);
				}
			}
		}
		
		world.setBlock(x, y, z, Blocks.quartz_block);
		world.setBlock(x, y+1, z, Blocks.quartz_block);
		world.setBlock(x, y+2, z, CCubesBlocks.chanceIcosahedron);
		
		world.setBlock(x-3, y, z-3, Blocks.quartz_block);
		world.setBlock(x-3, y+1, z-3, CCubesBlocks.chanceCube);
		
		world.setBlock(x-3, y, z+3, Blocks.quartz_block);
		world.setBlock(x-3, y+1, z+3, CCubesBlocks.chanceCube);
		
		world.setBlock(x+3, y, z-3, Blocks.quartz_block);
		world.setBlock(x+3, y+1, z-3, CCubesBlocks.chanceCube);
		
		world.setBlock(x+3, y, z+3, Blocks.quartz_block);
		world.setBlock(x+3, y+1, z+3, CCubesBlocks.chanceCube);
	}

	@Override
	public int getChanceValue()
	{
		return -10;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID+":5_Prongs";
	}
}