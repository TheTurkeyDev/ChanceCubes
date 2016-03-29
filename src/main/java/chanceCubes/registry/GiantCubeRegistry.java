package chanceCubes.registry;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;
import chanceCubes.config.ConfigLoader;
import chanceCubes.rewards.defaultRewards.IChanceCubeReward;
import chanceCubes.rewards.giantRewards.BioDomeReward;
import chanceCubes.rewards.giantRewards.ChunkFlipReward;
import chanceCubes.rewards.giantRewards.ChunkReverserReward;
import chanceCubes.rewards.giantRewards.FloorIsLavaReward;
import chanceCubes.rewards.giantRewards.OrePillarReward;
import chanceCubes.rewards.giantRewards.OreSphereReward;
import chanceCubes.rewards.giantRewards.PotionsReward;
import chanceCubes.rewards.giantRewards.TNTSlingReward;
import chanceCubes.rewards.giantRewards.ThrowablesReward;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GiantCubeRegistry implements IRewardRegistry
{
	public static GiantCubeRegistry INSTANCE = new GiantCubeRegistry();

	private Map<String, IChanceCubeReward> nameToReward = Maps.newHashMap();
	private List<IChanceCubeReward> sortedRewards = Lists.newArrayList();

	/**
	 * loads the default rewards of the Chance Cube
	 */
	public static void loadDefaultRewards()
	{
		if(!CCubesSettings.enableHardCodedRewards)
			return;

		INSTANCE.registerReward(new BioDomeReward());
		INSTANCE.registerReward(new TNTSlingReward());
		INSTANCE.registerReward(new ThrowablesReward());
		INSTANCE.registerReward(new OrePillarReward());
		INSTANCE.registerReward(new ChunkReverserReward());
		INSTANCE.registerReward(new FloorIsLavaReward());
		INSTANCE.registerReward(new ChunkFlipReward());
		INSTANCE.registerReward(new OreSphereReward());
		INSTANCE.registerReward(new PotionsReward());
	}

	@Override
	public void registerReward(IChanceCubeReward reward)
	{
		if(ConfigLoader.config.getBoolean(reward.getName(), ConfigLoader.giantRewardCat, true, "") && !this.nameToReward.containsKey(reward.getName()))
		{
			nameToReward.put(reward.getName(), reward);
			redoSort(reward);
		}
	}

	@Override
	public boolean unregisterReward(String name)
	{
		Object o = nameToReward.remove(name);
		if(o != null)
			return sortedRewards.remove(o);
		return false;
	}

	@Override
	public IChanceCubeReward getRewardByName(String name)
	{
		return nameToReward.get(name);
	}

	@Override
	public void triggerRandomReward(World world, int x, int y, int z, EntityPlayer player, int chance)
	{
		if(this.sortedRewards.size() == 0)
		{
			CCubesCore.logger.log(Level.WARN, "There are no registered rewards with the Giant Chance Cubes and no reward was able to be given");
			return;
		}

		int pick = world.rand.nextInt(sortedRewards.size());
		CCubesCore.logger.log(Level.INFO, "Triggered the reward with the name of: " + sortedRewards.get(pick).getName());
		sortedRewards.get(pick).trigger(world, x, y, z, player);
	}

	private void redoSort(@Nullable IChanceCubeReward newReward)
	{
		if(newReward != null)
			sortedRewards.add(newReward);

		Collections.sort(sortedRewards, new Comparator<IChanceCubeReward>()
		{
			public int compare(IChanceCubeReward o1, IChanceCubeReward o2)
			{
				return o1.getChanceValue() - o2.getChanceValue();
			};
		});
	}

	public void ClearRewards()
	{
		this.sortedRewards.clear();
		this.nameToReward.clear();
	}
}