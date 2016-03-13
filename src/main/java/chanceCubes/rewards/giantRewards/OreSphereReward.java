package chanceCubes.rewards.giantRewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.defaultRewards.IChanceCubeReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.Location3I;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class OreSphereReward implements IChanceCubeReward
{
	private Random rand = new Random();

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		List<OffsetBlock> blocks = new ArrayList<OffsetBlock>();

		ArrayList<ItemStack> ores = OreDictionary.getOres(ChanceCubeRegistry.oredicts.get(rand.nextInt(ChanceCubeRegistry.oredicts.size())));
		Block ore = null;
		if(ores.size() == 0)
			ore = Blocks.coal_ore;
		else
			ore = Block.getBlockFromItem(ores.get(rand.nextInt(ores.size())).getItem());

		int delay = 0;
		for(int i = 0; i < 5; i++)
		{
			for(int yy = -5; yy < 6; yy++)
			{
				for(int zz = -5; zz < 6; zz++)
				{
					for(int xx = -5; xx < 6; xx++)
					{
						Location3I loc = new Location3I(xx, yy, zz);
						float dist = Math.abs(loc.length());
						if(dist <= i && dist > i - 1)
						{
							blocks.add(new OffsetBlock(xx, yy, zz, ore, false, delay));
							delay++;
						}
					}
				}
			}
			delay += 10;
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
		return CCubesCore.MODID + ":Ore_Sphere";
	}

}
