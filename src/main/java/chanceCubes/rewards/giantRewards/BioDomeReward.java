package chanceCubes.rewards.giantRewards;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.rewards.biodomeGen.BasicTreesBiome;
import chanceCubes.rewards.biodomeGen.IBioDomeBiome;
import chanceCubes.rewards.defaultRewards.IChanceCubeReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;

public class BioDomeReward implements IChanceCubeReward
{
	private Random rand = new Random();
	
	private IBioDomeBiome[] biomes = new IBioDomeBiome[]{new BasicTreesBiome()};

	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		player.addChatMessage(new ChatComponentText("Hey! I can be a Pandora's Box to!"));

		List<OffsetBlock> blocks = biomes[rand.nextInt(biomes.length)].genDome(x, y, z, world);

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
		return CCubesCore.MODID + ":BioDome";
	}

}
