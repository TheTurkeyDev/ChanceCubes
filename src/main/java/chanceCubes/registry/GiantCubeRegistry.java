package chanceCubes.registry;

import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;
import chanceCubes.config.ConfigLoader;
import chanceCubes.profiles.ProfileManager;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.rewards.defaultRewards.BasicReward;
import chanceCubes.rewards.giantRewards.*;
import chanceCubes.rewards.rewardtype.SchematicRewardType;
import chanceCubes.util.RewardData;
import chanceCubes.util.SchematicUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

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

		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Village", 0, new SchematicRewardType(SchematicUtil.loadCustomSchematic(RewardData.getVillageSchematic(), 0, -1, 0, 0.1f, false, false, false, true, 0))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Woodland_Mansion", 0, new SchematicRewardType(SchematicUtil.loadCustomSchematic(RewardData.getWoodlandMansionSchematic(), 0, -1, 0, 0.05f, false, false, true, true, 0))));

		INSTANCE.registerReward(new BioDomeReward());
		INSTANCE.registerReward(new TNTSlingReward());
		INSTANCE.registerReward(new ThrowablesReward());
		INSTANCE.registerReward(new OrePillarReward());
		INSTANCE.registerReward(new ChunkReverserReward());
		INSTANCE.registerReward(new FloorIsLavaReward());
		INSTANCE.registerReward(new ChunkFlipReward());
		INSTANCE.registerReward(new OreSphereReward());
		INSTANCE.registerReward(new PotionsReward());
		INSTANCE.registerReward(new FluidSphereReward());
		INSTANCE.registerReward(new MixedFluidSphereReward());
		INSTANCE.registerReward(new FireworkShowReward());
		INSTANCE.registerReward(new SphereSnakeReward());
		INSTANCE.registerReward(new RandomExplosionReward());
		INSTANCE.registerReward(new BeaconArenaReward());
		INSTANCE.registerReward(new BlockInfectionReward());
		INSTANCE.registerReward(new BlockThrowerReward());
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
		IChanceCubeReward reward = nameToReward.remove(name);
		if(reward != null)
			return sortedRewards.remove(reward);
		return false;
	}

	@Override
	public IChanceCubeReward getRewardByName(String name)
	{
		return nameToReward.get(name);
	}

	@Override
	public void triggerRandomReward(World world, BlockPos pos, EntityPlayer player, int chance)
	{
		if(pos == null)
			return;
		if(this.sortedRewards.size() == 0)
		{
			CCubesCore.logger.log(Level.WARN, "There are no registered rewards with the Giant Chance Cubes and no reward was able to be given");
			return;
		}

		int pick = world.rand.nextInt(sortedRewards.size());
		CCubesCore.logger.log(Level.INFO, "Triggered the reward with the name of: " + sortedRewards.get(pick).getName());
		triggerReward(sortedRewards.get(pick), world, pos, player);
	}

	public void triggerReward(IChanceCubeReward reward, World world, BlockPos pos, EntityPlayer player)
	{
		Map<String, Object> settings = ProfileManager.getRewardSpawnSettings(reward);
		reward.trigger(world, pos, player, settings);
	}

	private void redoSort(@Nullable IChanceCubeReward newReward)
	{
		if(newReward != null)
			sortedRewards.add(newReward);

		sortedRewards.sort((o1, o2) -> o1.getChanceValue() - o2.getChanceValue());
	}

	public void ClearRewards()
	{
		this.sortedRewards.clear();
		this.nameToReward.clear();
	}
}