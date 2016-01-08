package chanceCubes.rewards.giantRewards;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.IChanceCubeReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;

public class ChunkFlipReward implements IChanceCubeReward
{

	public ChunkFlipReward()
	{

	}

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		List<OffsetBlock> blocks = new ArrayList<OffsetBlock>();
		int delay = 0;
		int zBase = z - (z % 16);
		int xBase = x - (x % 16);
		for(int yy = 0; yy <= 256; yy++)
		{
			for(int zz = 0; zz < 16; zz++)
			{
				for(int xx = 0; xx < 16; xx++)
				{
					Block b = world.getBlock(xBase + xx, yy, zBase + zz);
					if(!b.equals(Blocks.gravel) && !b.equals(Blocks.air) && !world.getBlock(xBase + xx, 256 - yy, zBase + zz).equals(Blocks.air))
					{
						OffsetBlock osb = new OffsetBlock((xBase + xx) - x, (256 - yy) - y, (zBase + zz) - z, b, false, delay);
						blocks.add(osb);
					}
				}
			}
			delay+=5;
		}

		for(OffsetBlock b : blocks)
			b.spawnInWorld(world, x, y, z);

		world.playSoundEffect(x, y, z, CCubesCore.MODID + ":giant_Cube_Spawn", 1, 1);
		player.addChatMessage(new ChatComponentText("Inception!!!!"));
	}

	@Override
	public int getChanceValue()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Chuck_Flip";
	}

}
