package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class MagicFeetReward extends BaseCustomReward
{
	public MagicFeetReward()
	{
		super(CCubesCore.MODID + ":magic_feet", 85);
	}

	@Override
	public void trigger(ServerWorld world, BlockPos pos, PlayerEntity player, JsonObject settings)
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
				BlockPos beneth = player.getPosition().add(0, -1, 0);
				if(!world.isAirBlock(beneth) && world.getTileEntity(beneth) == null && !last.equals(beneth))
				{
					Block block = RewardsUtil.getRandomOre();
					RewardsUtil.placeBlock(block.getDefaultState(), world, beneth);
					last = beneth;
				}

				if(this.delayLeft % 20 == 0)
					this.showTimeLeft(player, STitlePacket.Type.ACTIONBAR);
			}
		});
	}
}