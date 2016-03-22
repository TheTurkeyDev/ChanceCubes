package chanceCubes.rewards.type;

import chanceCubes.rewards.rewardparts.SoundPart;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class SoundRewardType extends BaseRewardType<SoundPart>
{
	public SoundRewardType(SoundPart... sounds)
	{
		super(sounds);
	}
	
	@Override
	public void trigger(final SoundPart sound, final World world, final int x, final int y, final int z, EntityPlayer player)
	{
		if(sound.getDelay() != 0)
		{
			Task task = new Task("Message Reward Delay", sound.getDelay())
			{
				@Override
				public void callback()
				{
					world.playSound(x, y, z, sound.getSound(), SoundCategory.BLOCKS, 1f, 1f, false);
				}
			};
			Scheduler.scheduleTask(task);
		}
		else
		{
			world.playSound(x, y, z, sound.getSound(), SoundCategory.BLOCKS, 1f, 1f, false);
		}
		
	}

}
