package chanceCubes.rewards.defaultRewards;

import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class WaitForItReward implements IChanceCubeReward
{
	private final Random rand = new Random();

	@Override
	public void trigger(final World world, BlockPos pos, final EntityPlayer player)
	{
		player.addChatMessage(new TextComponentString("Wait for it......."));

		Task task = new Task("Wait For It", rand.nextInt(4000) + 1000)
		{
			@Override
			public void callback()
			{
				triggerRealReward();
			}

			private void triggerRealReward()
			{
				int reward = rand.nextInt(3);
				player.addChatMessage(new TextComponentString("NOW!"));

				if(reward == 0)
				{
					world.spawnEntityInWorld(new EntityTNTPrimed(world, player.posX, player.posY + 1, player.posZ, null));
				}
				else if(reward == 1)
				{
					Entity ent = new EntityCreeper(world);
					ent.setLocationAndAngles(player.posX, player.posY + 1, player.posZ, 0, 0);
					world.spawnEntityInWorld(ent);
				}
				else if(reward == 2)
				{
					world.setBlockState(new BlockPos(player.posX, player.posY, player.posZ), Blocks.BEDROCK.getDefaultState());
				}
			}
		};

		Scheduler.scheduleTask(task);
	}

	@Override
	public int getChanceValue()
	{
		return -30;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Wait_For_It";
	}

}
