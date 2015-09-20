package chanceCubes.rewards.type;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import chanceCubes.rewards.rewardparts.SoundPart;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

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
					world.playSoundEffect(x, y, z, sound.getSound(), 1, 1);
				}
			};
			Scheduler.scheduleTask(task);
		}
		else
		{
			world.playSoundEffect(x, y, z, sound.getSound(), 1, 1);
		}
		
	}

}
