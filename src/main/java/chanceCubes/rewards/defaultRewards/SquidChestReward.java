package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SquidChestReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		world.setBlockState(pos, Blocks.CHEST.getDefaultState());
		Scheduler.scheduleTask(new Task("Squid_Chest_Init_Delay", 60)
		{

			@Override
			public void callback()
			{
				spawnSquids(world, pos);
			}
		});
	}

	public void spawnSquids(World world, BlockPos pos)
	{
		Scheduler.scheduleTask(new Task("Squid_Chest_Squids", 250, 5)
		{
			@Override
			public void callback()
			{

			}

			@Override
			public void update()
			{
				EntitySquid squid = new EntitySquid(world);
				squid.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ());
				squid.setVelocity(0, 1.5, -50);
				world.spawnEntityInWorld(squid);
			}
		});
	}

	@Override
	public int getChanceValue()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Squid_Chest";
	}

}
