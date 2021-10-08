package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.GuiTextLocation;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;

public class MagicFeetReward extends BaseCustomReward
{
	public MagicFeetReward()
	{
		super(CCubesCore.MODID + ":magic_feet", 85);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		int duration = super.getSettingAsInt(settings, "duration", 300, 0, Integer.MAX_VALUE);
		RewardsUtil.sendMessageToPlayer(player, "<Dovah_Jun> You've got magic feet!!!");
		Scheduler.scheduleTask(new Task("Megic_Feet_Reward_Delay", duration, 2)
		{
			BlockPos last = pos;

			@Override
			public void callback()
			{
				RewardsUtil.sendMessageToPlayer(player, "<Dovah_Jun> You've used up all the magic in your feet!");
			}

			@Override
			public void update()
			{
				BlockPos beneth = player.getOnPos().offset(0, -1, 0);
				if(!level.getBlockState(beneth).isAir() && level.getBlockEntity(beneth) == null && !last.equals(beneth))
				{
					Block block = RewardsUtil.getRandomOre();
					RewardsUtil.placeBlock(block.defaultBlockState(), level, beneth);
					last = beneth;
				}

				if(this.delayLeft % 20 == 0)
					this.showTimeLeft(player, GuiTextLocation.ACTION_BAR);
			}
		});
	}
}