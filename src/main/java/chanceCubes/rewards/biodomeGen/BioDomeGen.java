package chanceCubes.rewards.biodomeGen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardBlockCache;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BioDomeGen
{
	public static final int delayShorten = 10;

	// @formatter:off
	public static final IBioDomeBiome[] biomes = new IBioDomeBiome[] { new BasicTreesBiome("trees_plains"),
			new DesertBiome("desert"), new EndBiome("end"), new OceanBiome("ocean"), new SnowGlobeBiome("snow_globe"), 
			new NetherBiome("nether") };
	// @formatter:on

	private EntityPlayer player;
	private RewardBlockCache blockCache;
	private int yinc;
	private int xinc;
	private List<String> blackListBiomes;

	public BioDomeGen(EntityPlayer player)
	{
		this(player, new ArrayList<String>());
	}

	public BioDomeGen(EntityPlayer player, List<String> blackListBiomes)
	{
		this.player = player;
		this.blackListBiomes = blackListBiomes;
	}

	public void genRandomDome(final BlockPos pos, final World world, int radius, boolean spawnEntities)
	{
		IBioDomeBiome biome = biomes[0];
		List<IBioDomeBiome> biomesFiltered = Arrays.asList(biomes).stream().filter(line -> !this.blackListBiomes.contains(line.getBiomeName())).collect(Collectors.toList());
		if(biomesFiltered.size() != 0)
			biome = biomesFiltered.get(RewardsUtil.rand.nextInt(biomesFiltered.size()));
		this.genDome(biome, pos, world, radius, spawnEntities);
	}

	public void genDome(IBioDomeBiome spawnedBiome, BlockPos pos, final World world, int radius, boolean spawnEntities)
	{
		yinc = 0;
		xinc = -radius;
		blockCache = new RewardBlockCache(world, pos, player.getPosition());
		this.genDomePart(spawnedBiome, pos, world, radius, spawnEntities);
	}

	private void genDomePart(final IBioDomeBiome spawnedBiome, final BlockPos pos, final World world, int radius, boolean spawnEntities)
	{
		List<OffsetBlock> blocks = new ArrayList<>();
		int delay = 0;
		for(int z = -radius; z <= radius; z++)
		{
			BlockPos loc = new BlockPos(xinc, yinc, z);
			float dist = (float) (Math.abs(loc.getDistance(0, 0, 0)) - radius);
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
		int Yinctemp = yinc;
		if(xinc == -radius)
		{
			Yinctemp = Yinctemp + 1 > radius ? -1 : Yinctemp + 1;
		}

		if(Yinctemp == -1)
		{
			if(spawnEntities)
			{
				Scheduler.scheduleTask(new Task("Entity_Delays", delay)
				{
					@Override
					public void callback()
					{
						spawnedBiome.spawnEntities(pos, world);
					}
				});
			}
			return;
		}

		yinc = Yinctemp;

		for(OffsetBlock b : blocks)
			b.spawnInWorld(world, pos.getX(), pos.getY(), pos.getZ(), blockCache);

		Scheduler.scheduleTask(new Task("BioDome Reward", (delay / delayShorten))
		{

			@Override
			public void callback()
			{
				genDomePart(spawnedBiome, pos, world, radius, spawnEntities);
			}

		});
	}

	public void removeDome(boolean resetPlayer)
	{
		blockCache.restoreBlocks(resetPlayer ? player : null);
	}
}
