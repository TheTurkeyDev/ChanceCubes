package chanceCubes.registry.global;

import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;
import chanceCubes.config.ConfigLoader;
import chanceCubes.profiles.GlobalProfileManager;
import chanceCubes.registry.player.PlayerCCRewardRegistry;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.rewards.defaultRewards.CustomUserReward;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GlobalCCRewardRegistry
{
	public static GlobalCCRewardRegistry DEFAULT = new GlobalCCRewardRegistry();
	public static GlobalCCRewardRegistry GIANT = new GlobalCCRewardRegistry();

	private Map<String, GlobalRewardInfo> nameToReward = new HashMap<>();
	private Map<String, PlayerCCRewardRegistry> playerToRewards = new HashMap<>();

	public static void loadCustomUserRewards(MinecraftServer server)
	{
		for(EntityPlayerMP player : server.getPlayerList().getPlayers())
			CustomUserReward.getCustomUserReward(player.getUniqueID());
	}

	public void registerReward(IChanceCubeReward reward)
	{
		if(!this.nameToReward.containsKey(reward.getName()))
			nameToReward.put(reward.getName(), new GlobalRewardInfo(reward, true));
	}

	public boolean unregisterReward(String name)
	{
		for(PlayerCCRewardRegistry playerRewards : this.playerToRewards.values())
			playerRewards.disableReward(name);
		return nameToReward.remove(name) != null;
	}

	public void removePlayerRewards(String playerUUID)
	{
		this.playerToRewards.remove(playerUUID);
	}

	public boolean enableReward(String rewardName)
	{
		IChanceCubeReward reward = this.getRewardByName(rewardName);
		if(reward != null)
			return this.enableReward(reward);
		return false;
	}

	public boolean enableReward(IChanceCubeReward reward)
	{
		if(reward != null)
		{
			String rewardName = reward.getName();
			if(this.nameToReward.containsKey(rewardName))
			{
				if(!this.nameToReward.get(rewardName).enabled)
				{
					this.nameToReward.get(rewardName).enabled = true;
					for(PlayerCCRewardRegistry registry : this.playerToRewards.values())
						registry.enableReward(reward);
					return true;
				}
			}
		}
		return false;
	}

	public boolean disableReward(String rewardName)
	{
		IChanceCubeReward reward = this.getRewardByName(rewardName);
		if(reward != null)
			return this.disableReward(reward);
		return false;
	}

	public boolean disableReward(IChanceCubeReward reward)
	{
		if(reward != null)
		{
			String rewardName = reward.getName();
			if(this.nameToReward.containsKey(rewardName))
			{
				if(this.nameToReward.get(rewardName).enabled)
				{
					this.nameToReward.get(rewardName).enabled = false;
					for(PlayerCCRewardRegistry registry : this.playerToRewards.values())
						registry.disableReward(reward);
					return true;
				}
			}
		}
		return false;
	}

	public boolean isRewardEnabled(String reward)
	{
		return isValidRewardName(reward) && nameToReward.get(reward).enabled;
	}

	public boolean isValidRewardName(String reward)
	{
		return nameToReward.containsKey(reward);
	}

	public IChanceCubeReward getRewardByName(String name)
	{
		if(nameToReward.containsKey(name))
			return nameToReward.get(name).reward;
		return null;
	}

	public Set<String> getRewardNames()
	{
		return nameToReward.keySet();
	}

	public PlayerCCRewardRegistry getPlayerRewardRegistry(String playerUUID)
	{
		return this.playerToRewards.computeIfAbsent(playerUUID, (k) ->
		{
			PlayerCCRewardRegistry playerRewardRegistry = new PlayerCCRewardRegistry();
			for(GlobalRewardInfo reward : this.nameToReward.values())
				if(reward.enabled)
					playerRewardRegistry.enableReward(reward.reward);

			return playerRewardRegistry;
		});
	}

	public void triggerRandomReward(World world, BlockPos pos, EntityPlayer player, int chance)
	{
		if(CCubesSettings.testRewards)
		{
			CCubesCore.logger.log(Level.INFO, "This feature has been temporarily removed!");
			return;
		}
		else if(CCubesSettings.testCustomRewards)
		{
			CCubesCore.logger.log(Level.INFO, "This feature has been temporarily removed!");
			return;
		}

		if(CCubesSettings.doesHolidayRewardTrigger && CCubesSettings.holidayReward != null)
		{
			triggerReward(CCubesSettings.holidayReward, world, pos, player);
			CCubesCore.logger.log(Level.INFO, "The " + CCubesSettings.holidayReward.getName() + " holiday reward has been triggered!!!!");
			CCubesSettings.doesHolidayRewardTrigger = false;
			CCubesSettings.holidayRewardTriggered = true;
			ConfigLoader.config.get(ConfigLoader.genCat, "HolidayRewardTriggered", false, "Don't touch! Well I mean you can touch it, if you want. I can't stop you. I'm only text.").setValue(true);
			ConfigLoader.config.save();
			return;
		}

		if(player != null)
			this.getPlayerRewardRegistry(player.getUniqueID().toString()).triggerRandomReward(world, pos, player, chance);
	}

	public void triggerReward(IChanceCubeReward reward, World world, BlockPos pos, EntityPlayer player)
	{
		Map<String, Object> settings = GlobalProfileManager.getPlayerProfileManager(player).getRewardSpawnSettings(reward);
		reward.trigger(world, pos, player, settings);
	}


	public int getNumberOfLoadedRewards()
	{
		return this.nameToReward.size();
	}

	public void ClearRewards()
	{
		this.nameToReward.clear();
		this.playerToRewards.clear();
	}
}