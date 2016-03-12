package chanceCubes.rewards.giantRewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chanceCubes.CCubesCore;
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

	private List<String> oredicts = new ArrayList<String>();

	private String[] possibleModOres = new String[] { "oreAluminum", "oreCopper", "oreMythril", "oreLead", "orePlutonium", "oreQuartz", "oreRuby", "oreSalt", "oreSapphire", "oreSilver", "oreTin", "oreUranium", "oreZinc" };

	public OrePillarReward()
	{
		oredicts.add("oreGold");
		oredicts.add("oreIron");
		oredicts.add("oreLapis");
		oredicts.add("oreDiamond");
		oredicts.add("oreRedstone");
		oredicts.add("oreEmerald");
		oredicts.add("oreQuartz");
		oredicts.add("oreCoal");

		for(String oreDict : possibleModOres)
			if(OreDictionary.doesOreNameExist(oreDict))
				oredicts.add(oreDict);
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
				ArrayList<ItemStack> ores = OreDictionary.getOres(oredicts.get(rand.nextInt(oredicts.size())));
				if(ores.size() == 0)
					continue;
				Block b = Block.getBlockFromItem(ores.get(rand.nextInt(ores.size())).getItem());
				blocks.add(new OffsetBlock(xx, yy - y, zz, b, false, delay / 3));
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
