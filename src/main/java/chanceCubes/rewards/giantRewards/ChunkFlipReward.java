package chanceCubes.rewards.giantRewards;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.rewards.defaultRewards.IChanceCubeReward;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

public class ChunkFlipReward implements IChanceCubeReward
{

	public ChunkFlipReward()
	{

	}

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		int zBase = z - (z % 16);
		int xBase = x - (x % 16);

		moveLayer(world, xBase, 0, zBase, player);

		world.playSoundEffect(x, y, z, CCubesCore.MODID + ":giant_Cube_Spawn", 1, 1);
		player.addChatMessage(new ChatComponentText("Inception!!!!"));
	}

	public void moveLayer(final World world, final int x, int y, final int z, final EntityPlayer player)
	{
		if(y > 256)
			return;
		for(int zz = 0; zz < 16; zz++)
		{
			for(int xx = 0; xx < 16; xx++)
			{
				Block b = world.getBlock(x + xx, y, z + zz);
				if(!b.equals(Blocks.gravel) && !b.equals(CCubesBlocks.chanceGiantCube))
					world.setBlock(x + xx, 256 - y, z + zz, b, 0, 2);
			}
		}

		final int nextY = y + 1;
		Task task = new Task("Chunk_Flip_Delay", 5)
		{
			public void callback()
			{
				moveLayer(world, x, nextY, z, player);
			}
		};

		Scheduler.scheduleTask(task);
	}

	@Override
	public int getChanceValue()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Chunk_Flip";
	}

}
