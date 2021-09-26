package chanceCubes.rewards.biodomeGen;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BioDomeGen
{
	public static final int delayShorten = 10;

	// @formatter:off
	public static final IBioDomeBiome[] biomes = new IBioDomeBiome[] { new BasicTreesBiome("trees_plains"),
			new DesertBiome("desert"), new EndBiome("end"), new OceanBiome("ocean"), new SnowGlobeBiome("snow_globe"), 
			new NetherBiome("nether") };
	// @formatter:on

	private final Player player;
	private RewardBlockCache blockCache;
	private int yinc;
	private int xinc;
	private final List<String> blackListBiomes;

	public BioDomeGen(Player player)
	{
		this(player, new ArrayList<>());
	}

	public BioDomeGen(Player player, List<String> blackListBiomes)
	{
		this.player = player;
		this.blackListBiomes = blackListBiomes;
	}

	public void genRandomDome(final BlockPos pos, final ServerLevel level, int radius, boolean spawnEntities)
	{
		IBioDomeBiome biome = biomes[0];
		List<IBioDomeBiome> biomesFiltered = Arrays.stream(biomes).filter(line -> !this.blackListBiomes.contains(line.getBiomeName())).collect(Collectors.toList());
		if(biomesFiltered.size() != 0)
			biome = biomesFiltered.get(RewardsUtil.rand.nextInt(biomesFiltered.size()));
		this.genDome(biome, pos, level, radius, spawnEntities);
	}

	public void genDome(IBioDomeBiome spawnedBiome, BlockPos pos, final ServerLevel level, int radius, boolean spawnEntities)
	{
		yinc = 0;
		xinc = -radius;
		blockCache = new RewardBlockCache(level, pos, player.getOnPos());
		this.genDomePart(spawnedBiome, pos, level, radius, spawnEntities);
	}

	private void genDomePart(final IBioDomeBiome spawnedBiome, final BlockPos pos, final ServerLevel level, int radius, boolean spawnEntities)
	{
		List<OffsetBlock> blocks = new ArrayList<>();
		int delay = 0;
		for(int z = -radius; z <= radius; z++)
		{
			BlockPos loc = new BlockPos(xinc, yinc, z);
			float dist = (float) (Math.sqrt(Math.abs(loc.distSqr(0, 0, 0, false))) - radius);
			if(dist < 1)
			{
				if(dist >= 0)
				{
					blocks.add(new OffsetBlock(xinc, yinc, z, Blocks.GLASS, false, (delay / delayShorten)));
					delay++;
				}
				else if(yinc == 0)
				{
					blocks.add(new OffsetBlock(xinc, yinc, z, spawnedBiome.getFloorBlock(), false, (delay / delayShorten)));
					delay++;
				}
				spawnedBiome.getRandomGenBlock(dist, RewardsUtil.rand, xinc, yinc, z, blocks, delay);
			}
		}

		xinc = xinc + 1 > radius ? (-radius) : xinc + 1;
		int yIncTemp = yinc;
		if(xinc == -radius)
		{
			yIncTemp = yIncTemp + 1 > radius ? -1 : yIncTemp + 1;
		}

		if(yIncTemp == -1)
		{
			if(spawnEntities)
			{
				Scheduler.scheduleTask(new Task("Entity_Delays", delay)
				{
					@Override
					public void callback()
					{
						spawnedBiome.spawnEntities(pos, level);
					}
				});
			}
			if(spawnedBiome instanceof OceanBiome)
				player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 1200));
			return;
		}

		yinc = yIncTemp;

		for(OffsetBlock b : blocks)
			b.spawnInWorld(level, pos.getX(), pos.getY(), pos.getZ(), blockCache);

		Scheduler.scheduleTask(new Task("BioDome Reward", (delay / delayShorten))
		{

			@Override
			public void callback()
			{
				genDomePart(spawnedBiome, pos, level, radius, spawnEntities);
			}

		});
	}

	public void removeDome(boolean resetPlayer)
	{
		blockCache.restoreBlocks(resetPlayer ? player : null);
	}
}
