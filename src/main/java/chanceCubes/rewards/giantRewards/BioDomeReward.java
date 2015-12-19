package chanceCubes.rewards.giantRewards;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.rewards.biodomeGen.BasicTreesBiome;
import chanceCubes.rewards.biodomeGen.DesertBiome;
import chanceCubes.rewards.biodomeGen.EndBiome;
import chanceCubes.rewards.biodomeGen.IBioDomeBiome;
import chanceCubes.rewards.defaultRewards.IChanceCubeReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

public class BioDomeReward implements IChanceCubeReward
{
	private Random rand = new Random();

	private IBioDomeBiome[] biomes = new IBioDomeBiome[] { new BasicTreesBiome(), new DesertBiome(), new EndBiome() };

	@Override
	public void trigger(final World world, final int x, final int y, final int z, EntityPlayer player)
	{
		// player.addChatMessage(new ChatComponentText("Hey! I can be a Pandora's Box to!"));

		final IBioDomeBiome spawnedBiome = biomes[rand.nextInt(biomes.length)];
		List<OffsetBlock> blocks = spawnedBiome.genDome(x, y, z, world);

		int lastTime = 0;
		for(OffsetBlock b : blocks)
		{
			b.spawnInWorld(world, x, y, z);
			if(lastTime < b.getDelay())
				lastTime = b.getDelay();
		}

		Scheduler.scheduleTask(new Task("Entity_Delays", lastTime)
		{

			@Override
			public void callback()
			{
				spawnedBiome.spawnEntities(x, y, z, world);
			}

		});

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
