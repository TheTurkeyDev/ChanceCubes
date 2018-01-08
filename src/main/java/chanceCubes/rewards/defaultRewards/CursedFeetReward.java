package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class CursedFeetReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		player.addChatMessage(new TextComponentString("<TheKeeg> You've got cursed feet!!!"));
		Scheduler.scheduleTask(new Task("Cursed_Feet_Reward_Delay", 300, 20)
		{
			@Override
			public void callback()
			{
				player.addChatMessage(new TextComponentString("<TheKeeg> the curse has worn off!"));
			}

			@Override
			public void update()
			{
				EntityTNTPrimed tnt = new EntityTNTPrimed(world);
				world.spawnEntityInWorld(tnt);
			}
		});
	}

	@Override
	public int getChanceValue()
	{
		return 70;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Magic_Feet";
	}

}
