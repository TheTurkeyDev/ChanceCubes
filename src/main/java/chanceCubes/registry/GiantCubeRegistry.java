package chanceCubes.registry;

import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;
import chanceCubes.config.ConfigLoader;
import chanceCubes.profiles.GlobalProfileManager;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.rewards.defaultRewards.BasicReward;
import chanceCubes.rewards.giantRewards.*;
import chanceCubes.rewards.rewardparts.SchematicPart;
import chanceCubes.rewards.rewardtype.SchematicRewardType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class GiantCubeRegistry
{
	public static GiantCubeRegistry INSTANCE = new GiantCubeRegistry();

	private Map<String, IChanceCubeReward> nameToReward = Maps.newHashMap();
	private List<IChanceCubeReward> sortedRewards = Lists.newArrayList();
	private Map<String, IChanceCubeReward> disabledNameToReward = Maps.newHashMap();

	/**
	 * loads the default rewards of the Chance Cube
	 */
	public static void loadDefaultRewards()
	{
		if(!CCubesSettings.enableHardCodedRewards)
			return;

		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Village", 0, new SchematicRewardType(new SchematicPart("/assets/chancecubes/schematics/village.ccs", true))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID + ":Woodland_Mansion", 0, new SchematicRewardType(new SchematicPart("/assets/chancecubes/schematics/mansion.ccs", true).setSpacingdelay(0.05f).shouldPlaceAitBlocks(true))));

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

	public void registerReward(IChanceCubeReward reward)
	{
		if(ConfigLoader.config.getBoolean(reward.getName(), ConfigLoader.giantRewardCat, true, "") && !this.nameToReward.containsKey(reward.getName()))
		{
			nameToReward.put(reward.getName(), reward);
			redoSort(reward);
		}
		else
		{
			this.disabledNameToReward.put(reward.getName(), reward);
		}
	}

	public boolean unregisterReward(String name)
	{
		IChanceCubeReward reward = nameToReward.remove(name);
		if(reward != null)
			return sortedRewards.remove(reward);
		return false;
	}

	public boolean enableReward(String reward)
	{
		if(this.disabledNameToReward.containsKey(reward) && this.nameToReward.containsKey(reward))
			return this.enableReward(this.nameToReward.get(reward));
		return this.isRewardEnabled(reward);
	}

	public boolean enableReward(IChanceCubeReward reward)
	{
		this.disabledNameToReward.remove(reward.getName());
		redoSort(reward);
		return this.isRewardEnabled(reward.getName());
	}

	public boolean disableReward(String reward)
	{
		if(!this.disabledNameToReward.containsKey(reward) && this.nameToReward.containsKey(reward))
			return this.disableReward(this.nameToReward.get(reward));
		return !this.isRewardEnabled(reward);
	}

	public boolean disableReward(IChanceCubeReward reward)
	{
		if(reward != null)
		{
			this.disabledNameToReward.put(reward.getName(), reward);
			return sortedRewards.remove(reward);
		}
		return false;
	}

	public boolean isRewardEnabled(String reward)
	{
		return !this.disabledNameToReward.containsKey(reward);
	}

	public IChanceCubeReward getRewardByName(String name)
	{
		return nameToReward.get(name);
	}

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
		Map<String, Object> settings = GlobalProfileManager.getPlayerProfileManager(player).getRewardSpawnSettings(reward);
		reward.trigger(world, pos, player, settings);
	}

	private void redoSort(@Nullable IChanceCubeReward newReward)
	{
		if(newReward != null)
			sortedRewards.add(newReward);

		sortedRewards.sort(Comparator.comparingInt(IChanceCubeReward::getChanceValue));
	}

	public int getNumberOfLoadedRewards()
	{
		return this.sortedRewards.size();
	}

	public int getNumberOfDisabledRewards()
	{
		return this.disabledNameToReward.size();
	}

	public void ClearRewards()
	{
		this.sortedRewards.clear();
		this.nameToReward.clear();
		this.disabledNameToReward.clear();
	}
}