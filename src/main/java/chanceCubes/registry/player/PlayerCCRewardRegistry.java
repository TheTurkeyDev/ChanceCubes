package chanceCubes.registry.player;

import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;
import chanceCubes.items.ItemChancePendant;
import chanceCubes.profiles.GlobalProfileManager;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.rewards.defaultRewards.StreamerReward;
import chanceCubes.util.RewardsUtil;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerCCRewardRegistry
{
	private List<PlayerRewardInfo> sortedRewards = Lists.newArrayList();
	private List<IChanceCubeReward> cooldownList = new ArrayList<>();

	public static StreamerReward streamerReward = null;

	public boolean enableReward(String reward)
	{
		if(GlobalCCRewardRegistry.DEFAULT.isValidRewardName(reward))
			return this.enableReward(GlobalCCRewardRegistry.DEFAULT.getRewardByName(reward));
		else if(GlobalCCRewardRegistry.GIANT.isValidRewardName(reward))
			return this.enableReward(GlobalCCRewardRegistry.GIANT.getRewardByName(reward));
		return false;
	}

	public boolean enableReward(IChanceCubeReward reward)
	{
		if(reward != null)
		{
			int i = 0;
			while(i < sortedRewards.size() && sortedRewards.get(i).getChanceValue() <= reward.getChanceValue())
			{
				if(sortedRewards.get(i).reward.getName().equals(reward.getName()))
					return false;
				i++;
			}
			sortedRewards.add(i, new PlayerRewardInfo(reward));
			return true;
		}
		return false;
	}

	public boolean disableReward(String reward)
	{
		if(GlobalCCRewardRegistry.DEFAULT.isValidRewardName(reward))
			return this.disableReward(GlobalCCRewardRegistry.DEFAULT.getRewardByName(reward));
		else if(GlobalCCRewardRegistry.GIANT.isValidRewardName(reward))
			return this.disableReward(GlobalCCRewardRegistry.GIANT.getRewardByName(reward));
		return false;
	}

	public boolean disableReward(IChanceCubeReward reward)
	{
		for(int i = sortedRewards.size() - 1; i >= 0; i--)
			if(sortedRewards.get(i).reward == reward)
				return sortedRewards.remove(i) != null;
		return false;
	}

	public void setRewardChanceValue(String rewardName, int chance)
	{
		PlayerRewardInfo reward = getRewardByName(rewardName);
		if(reward != null)
			reward.setRewardChanceValue(chance);
	}

	public void resetRewardChanceValue(String rewardName, int chanceFrom)
	{
		PlayerRewardInfo reward = getRewardByName(rewardName);
		if(reward != null)
			reward.resetRewardChanceValue(chanceFrom);
	}

	private PlayerRewardInfo getRewardByName(String name)
	{
		for(PlayerRewardInfo rewardInfo : this.sortedRewards)
			if(rewardInfo.reward.getName().equals(name))
				return rewardInfo;
		return null;
	}

	public void triggerRandomReward(World world, BlockPos pos, PlayerEntity player, int chance)
	{
		if(streamerReward != null)
		{
			//TODO: % chance
			streamerReward.trigger(world, pos, player);
			return;
		}

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

		if(this.sortedRewards.size() == 0)
		{
			CCubesCore.logger.log(Level.WARN, "There are no registered rewards with ChanceCubes and no reward was able to be given");
			return;
		}

//		if(CCubesSettings.doesHolidayRewardTrigger && CCubesSettings.holidayReward != null)
//		{
//			triggerReward(CCubesSettings.holidayReward, world, pos, player);
//			CCubesCore.logger.log(Level.INFO, "The " + CCubesSettings.holidayReward.getName() + " holiday reward has been triggered!!!!");
//			CCubesSettings.doesHolidayRewardTrigger = false;
//			CCubesSettings.holidayRewardTriggered = true;
//			ConfigLoader.config.get(ConfigLoader.genCat, "HolidayRewardTriggered", false, "Don't touch! Well I mean you can touch it, if you want. I can't stop you. I'm only text.").setValue(true);
//			ConfigLoader.config.save();
//			return;
//		}

		if(player != null)
		{
			for(int i = 0; i < player.inventory.mainInventory.size(); i++)
			{
				ItemStack stack = player.inventory.mainInventory.get(i);
				if(!stack.isEmpty() && stack.getItem() instanceof ItemChancePendant)
				{
					ItemChancePendant pendant = (ItemChancePendant) stack.getItem();
					pendant.damage(stack);
					if(stack.getDamage() >= CCubesSettings.pendantUses.get())
						player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
					chance += pendant.getChanceIncrease();
					if(chance > 100)
						chance = 100;
					break;
				}
			}
		}

		int lowerIndex = 0;
		int upperIndex = sortedRewards.size() - 1;
		int lowerRange = Math.max(chance - CCubesSettings.rangeMin.get(), -100);
		int upperRange = Math.min(chance + CCubesSettings.rangeMax.get(), 100);

		while(sortedRewards.get(lowerIndex).getChanceValue() < lowerRange)
		{
			lowerIndex++;
			if(lowerIndex >= sortedRewards.size())
			{
				lowerIndex--;
				break;
			}
		}
		while(sortedRewards.get(upperIndex).getChanceValue() > upperRange)
		{
			upperIndex--;
			if(upperIndex < 0)
			{
				upperIndex++;
				break;
			}
		}
		int range = upperIndex - lowerIndex > 0 ? upperIndex - lowerIndex : 1;
		int pick = RewardsUtil.rand.nextInt(range) + lowerIndex;
		IChanceCubeReward pickedReward = sortedRewards.get(pick).reward;
		if(cooldownList.contains(pickedReward))
		{
			byte atempts = 0;
			while(atempts < 5 && cooldownList.contains(pickedReward))
			{
				pick = RewardsUtil.rand.nextInt(range) + lowerIndex;
				pickedReward = sortedRewards.get(pick).reward;
				atempts++;
			}
		}
		CCubesCore.logger.log(Level.INFO, "Triggered the reward with the name of: " + pickedReward.getName());
		triggerReward(pickedReward, world, pos, player);
		cooldownList.add(pickedReward);
		if(cooldownList.size() > 15)
		{
			cooldownList.remove(0);
		}
	}

	public void triggerReward(IChanceCubeReward reward, World world, BlockPos pos, PlayerEntity player)
	{
		Map<String, Object> settings = GlobalProfileManager.getPlayerProfileManager(player).getRewardSpawnSettings(reward);
		reward.trigger(world, pos, player, settings);
	}

	public List<PlayerRewardInfo> getPlayersRewards()
	{
		return new ArrayList<>(this.sortedRewards);
	}
}
