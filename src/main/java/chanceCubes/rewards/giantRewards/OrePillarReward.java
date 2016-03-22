package chanceCubes.rewards.giantRewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.IChanceCubeReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OrePillarReward implements IChanceCubeReward
{
	private Random rand = new Random();

	private Block[] ores = new Block[] { Blocks.iron_ore, Blocks.coal_ore, Blocks.diamond_ore, Blocks.gold_ore, Blocks.redstone_ore, Blocks.lapis_ore, Blocks.emerald_ore, Blocks.quartz_ore };

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		List<OffsetBlock> blocks = new ArrayList<OffsetBlock>();
		int delay = 0;
		for(int i = 0; i < rand.nextInt(15) + 5; i++)
		{
			int xx = rand.nextInt(30) - 15;
			int zz = rand.nextInt(30) - 15;
			for(int yy = 0; yy < 256; yy++)
			{
				blocks.add(new OffsetBlock(xx, yy - pos.getY(), zz, ores[rand.nextInt(ores.length)], false, delay / 3));
				delay++;
			}
		}

		for(OffsetBlock b : blocks)
			b.spawnInWorld(world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public int getChanceValue()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Ore_Pillars";
	}

}