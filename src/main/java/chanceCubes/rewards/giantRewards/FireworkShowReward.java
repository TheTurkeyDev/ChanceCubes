package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.CCubesCommandSender;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FireworkShowReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		CCubesCommandSender sender = new CCubesCommandSender(player, pos);
		MinecraftServer server = world.getServer();
		server.getCommandManager().handleCommand(sender, "/time set 15000");
		stage1(world, pos, player);
	}

	public void stage1(World world, BlockPos pos, EntityPlayer player)
	{
		Scheduler.scheduleTask(new Task("Firework_Show_Task_Stage_1", 200, 5)
		{
			double angle = 0;

			@Override
			public void callback()
			{
				stage2(world, pos, player);
			}

			@Override
			public void update()
			{
				angle += 0.5;
				spawnFirework(world, pos.getX() + ((angle / 3f) * Math.cos(angle)), pos.getY(), pos.getZ() + ((angle / 3f) * Math.sin(angle)));
				spawnFirework(world, pos.getX() + ((angle / 3f) * Math.cos(angle + Math.PI)), pos.getY(), pos.getZ() + ((angle / 3f) * Math.sin(angle + Math.PI)));
			}

		});
	}

	public void stage2(World world, BlockPos pos, EntityPlayer player)
	{
		Scheduler.scheduleTask(new Task("Firework_Show_Task_Stage_2", 200, 5)
		{
			double tick = 0;

			@Override
			public void callback()
			{
				stage3(world, pos, player);
			}

			@Override
			public void update()
			{
				tick += 0.5;
				spawnFirework(world, pos.getX() + (tick - 20), pos.getY(), pos.getZ() + 1);
				spawnFirework(world, pos.getX() + (20 - tick), pos.getY(), pos.getZ() - 1);
			}

		});
	}

	public void stage3(World world, BlockPos pos, EntityPlayer player)
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
				spawnFirework(world, pos.getX() + (RewardsUtil.rand.nextInt(10) - 5), pos.getY(), pos.getZ() + (RewardsUtil.rand.nextInt(10) - 5));
			}

		});
	}

	public void spawnFirework(World world, double x, double y, double z)
	{
		world.spawnEntity(new EntityFireworkRocket(world, x, y, z, RewardsUtil.getRandomFirework()));
	}

	@Override
	public int getChanceValue()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Firework_Show";
	}

}