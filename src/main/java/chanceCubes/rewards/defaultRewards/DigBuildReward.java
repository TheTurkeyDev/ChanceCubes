package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.CCubesDamageSource;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;

public class DigBuildReward extends BaseCustomReward
{

	public DigBuildReward()
	{
		super(CCubesCore.MODID + ":dig_build", -5);
	}

	@Override
	public void trigger(ServerLevel world, BlockPos pos, Player player, JsonObject settings)
	{
		int min = super.getSettingAsInt(settings, "min", 5, 0, 100);
		int max = super.getSettingAsInt(settings, "max", 25, 0, 100);

		//Because someone will do it...
		if(min > max)
		{
			int swap = min;
			min = max;
			max = swap;
		}

		int initalY = player.getOnPos().getY();
		int distance = RewardsUtil.rand.nextInt(max - min) + min;
		boolean up = (initalY + distance <= 150) && ((initalY - distance < 2) || RewardsUtil.rand.nextBoolean());

		RewardsUtil.sendMessageToPlayer(player, "Quick! Go " + (up ? "up " : "down ") + distance + " blocks!");
		RewardsUtil.sendMessageToPlayer(player, "You have " + (distance + 3) + " seconds!");

		Scheduler.scheduleTask(new Task("Dig_Build_Reward_Delay", (distance + 3) * 20, 5)
		{
			@Override
			public void callback()
			{
				player.level.createExplosion(player, player.getX(), player.getY(), player.getZ(), 1.0F, Explosion.Mode.NONE);
				player.attackEntityFrom(CCubesDamageSource.DIG_BUILD_FAIL, Float.MAX_VALUE);
			}

			@Override
			public void update()
			{
				if(up && player.getOnPos().getY() >= initalY + distance)
				{
					RewardsUtil.sendMessageToPlayer(player, "Good Job!");
					RewardsUtil.sendMessageToPlayer(player, "Here, have a item!");
					player.level.addFreshEntity(new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), new ItemStack(RewardsUtil.getRandomItem(), 1)));
					Scheduler.removeTask(this);
				}
				else if(!up && player.getOnPos().getY() <= initalY - distance)
				{
					RewardsUtil.sendMessageToPlayer(player, "Good Job!");
					RewardsUtil.sendMessageToPlayer(player, "Here, have a item!");
					player.level.addFreshEntity(new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), new ItemStack(RewardsUtil.getRandomItem(), 1)));
					Scheduler.removeTask(this);
				}

				if(this.delayLeft % 20 == 0)
					this.showTimeLeft(player, STitlePacket.Type.ACTIONBAR);
			}
		});
	}
}