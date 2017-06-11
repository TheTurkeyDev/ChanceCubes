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
	public void trigger(final SoundPart sound, final World world, final int x, final int y, final int z, final EntityPlayer player)
	{
		Scheduler.scheduleTask(new Task("Message Reward Delay", sound.getDelay())
		{
			@Override
			public void callback()
			{
				if(sound.playAtPlayersLocation())
					world.playSound(null, player.posX, player.posY, player.posZ, sound.getSound(), SoundCategory.BLOCKS, sound.getVolume(), sound.getPitch());
				else
					world.playSound(null, x, y, z, sound.getSound(), SoundCategory.BLOCKS, sound.getVolume(), sound.getPitch());
			}
		});
	}
}