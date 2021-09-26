package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;

public class FireworkShowReward extends BaseCustomReward
{
	public FireworkShowReward()
	{
		super(CCubesCore.MODID + ":firework_show", 0);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		RewardsUtil.executeCommand(level, player, pos, "/time set 15000");
		stage1(level, pos, player);
	}

	public void stage1(ServerLevel level, BlockPos pos, Player player)
	{
		Scheduler.scheduleTask(new Task("Firework_Show_Task_Stage_1", 200, 5)
		{
			double angle = 0;

			@Override
			public void callback()
			{
				stage2(level, pos, player);
			}

			@Override
			public void update()
			{
				angle += 0.5;
				spawnFirework(level, pos.getX() + ((angle / 3f) * Math.cos(angle)), pos.getY(), pos.getZ() + ((angle / 3f) * Math.sin(angle)));
				spawnFirework(level, pos.getX() + ((angle / 3f) * Math.cos(angle + Math.PI)), pos.getY(), pos.getZ() + ((angle / 3f) * Math.sin(angle + Math.PI)));
			}

		});
	}

	public void stage2(ServerLevel level, BlockPos pos, Player player)
	{
		Scheduler.scheduleTask(new Task("Firework_Show_Task_Stage_2", 200, 5)
		{
			double tick = 0;

			@Override
			public void callback()
			{
				stage3(level, pos, player);
			}

			@Override
			public void update()
			{
				tick += 0.5;
				spawnFirework(level, pos.getX() + (tick - 20), pos.getY(), pos.getZ() + 1);
				spawnFirework(level, pos.getX() + (20 - tick), pos.getY(), pos.getZ() - 1);
			}

		});
	}

	public void stage3(ServerLevel level, BlockPos pos, Player player)
	{
		Scheduler.scheduleTask(new Task("Firework_Show_Task_Stage_2", 200, 3)
		{

			@Override
			public void callback()
			{

			}

			@Override
			public void update()
			{
				spawnFirework(level, pos.getX() + (RewardsUtil.rand.nextInt(10) - 5), pos.getY(), pos.getZ() + (RewardsUtil.rand.nextInt(10) - 5));
			}

		});
	}

	public void spawnFirework(ServerLevel level, double x, double y, double z)
	{
		level.addFreshEntity(new FireworkRocketEntity(level, x, y, z, RewardsUtil.getRandomFirework()));
	}
}