package chanceCubes.rewards.giantRewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.defaultRewards.IChanceCubeReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class OrePillarReward implements IChanceCubeReward
{
	private Random rand = new Random();

	public OrePillarReward()
	{
		
	}

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		List<OffsetBlock> blocks = new ArrayList<OffsetBlock>();
		int delay = 0;
		for(int i = 0; i < rand.nextInt(6) + 3; i++)
		{
			int xx = rand.nextInt(30) - 15;
			int zz = rand.nextInt(30) - 15;
			for(int yy = 1; yy < 255; yy++)
			{
				ArrayList<ItemStack> ores = OreDictionary.getOres(ChanceCubeRegistry.getRandomOreDict());
				if(ores.size() == 0)
					continue;
				ItemStack chosenStack = ores.get(rand.nextInt(ores.size()));
				Block b = Block.getBlockFromItem(chosenStack.getItem());
				OffsetBlock osb = new OffsetBlock(xx, yy - y, zz, b, false, delay / 3);
				osb.setData((byte) chosenStack.getItemDamage());
				blocks.add(osb);
				delay++;
			}
		}

		for(OffsetBlock b : blocks)
			b.spawnInWorld(world, x, y, z);
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
