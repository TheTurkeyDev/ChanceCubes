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
	public void trigger(final SoundPart sound, final World world, final int x, final int y, final int z, final EntityPlayer player)
	{
		if(sound.getDelay() != 0)
		{
			Task task = new Task("Message Reward Delay", sound.getDelay())
			{
				@Override
				public void callback()
				{
					if(sound.playAtPlayersLocation())
						world.playSoundEffect(player.posX, player.posY, player.posZ, sound.getSound(), sound.getVolume(), sound.getPitch());
					else
						world.playSoundEffect(x, y, z, sound.getSound(), sound.getVolume(), sound.getPitch());
				}
			};
			Scheduler.scheduleTask(task);
		}
		else
		{
			if(sound.playAtPlayersLocation())
				world.playSoundEffect(player.posX, player.posY, player.posZ, sound.getSound(), sound.getVolume(), sound.getPitch());
			else
				world.playSoundEffect(x, y, z, sound.getSound(), sound.getVolume(), sound.getPitch());
		}

	}

}
