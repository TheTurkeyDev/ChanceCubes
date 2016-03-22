package chanceCubes.rewards.giantRewards;

import java.util.List;
import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.biodomeGen.BasicTreesBiome;
import chanceCubes.rewards.biodomeGen.DesertBiome;
import chanceCubes.rewards.biodomeGen.EndBiome;
import chanceCubes.rewards.biodomeGen.IBioDomeBiome;
import chanceCubes.rewards.defaultRewards.IChanceCubeReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BioDomeReward implements IChanceCubeReward
{
	private Random rand = new Random();

	private IBioDomeBiome[] biomes = new IBioDomeBiome[] { new BasicTreesBiome(), new DesertBiome(), new EndBiome() };

	@Override
	public void trigger(final World world, final BlockPos pos, EntityPlayer player)
	{
		// player.addChatMessage(new ChatComponentText("Hey! I can be a Pandora's Box to!"));

				final IBioDomeBiome spawnedBiome = biomes[rand.nextInt(biomes.length)];
				List<OffsetBlock> blocks = spawnedBiome.genDome(pos, world);

				int lastTime = 0;
				for(OffsetBlock b : blocks)
				{
					b.spawnInWorld(world, pos.getX(), pos.getY(), pos.getZ());
					if(lastTime < b.getDelay())
						lastTime = b.getDelay();
				}

				Scheduler.scheduleTask(new Task("Entity_Delays", lastTime)
				{

					@Override
					public void callback()
					{
						spawnedBiome.spawnEntities(pos, world);
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