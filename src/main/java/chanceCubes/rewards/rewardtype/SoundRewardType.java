package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.SoundPart;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class SoundRewardType extends BaseRewardType<SoundPart>
{
	public SoundRewardType(SoundPart... sounds)
	{
		super(sounds);
	}

	public SoundRewardType(SoundEvent... sounds)
	{
		super(convertToCommandParts(sounds));
	}

	private static SoundPart[] convertToCommandParts(SoundEvent... sounds)
	{
		SoundPart[] toReturn = new SoundPart[sounds.length];
		for(int i = 0; i < sounds.length; i++)
			toReturn[i] = new SoundPart(sounds[i]);
		return toReturn;
	}

	@Override
	public void trigger(final SoundPart sound, final World world, final int x, final int y, final int z, final PlayerEntity player)
	{
		Scheduler.scheduleTask(new Task("Sound Reward Delay", sound.getDelay())
		{
			@Override
			public void callback()
			{
				if(sound.playAtPlayersLocation())
					world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), sound.getSound(), SoundCategory.BLOCKS, sound.getVolume(), sound.getPitch());
				else
					world.playSound(null, x, y, z, sound.getSound(), SoundCategory.BLOCKS, sound.getVolume(), sound.getPitch());
			}
		});
	}
}