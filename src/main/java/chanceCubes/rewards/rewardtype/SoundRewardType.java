package chanceCubes.rewards.rewardtype;

import chanceCubes.rewards.rewardparts.SoundPart;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

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
	public void trigger(final SoundPart sound, final ServerLevel level, final int x, final int y, final int z, final Player player)
	{
		Scheduler.scheduleTask(new Task("Sound Reward Delay", sound.getDelay())
		{
			@Override
			public void callback()
			{
				if(sound.playAtPlayersLocation())
					level.playSound(null, player.getX(), player.getY(), player.getZ(), sound.getSound(), SoundSource.BLOCKS, sound.getVolume(), sound.getPitch());
				else
					level.playSound(null, x, y, z, sound.getSound(), SoundSource.BLOCKS, sound.getVolume(), sound.getPitch());
			}
		});
	}
}