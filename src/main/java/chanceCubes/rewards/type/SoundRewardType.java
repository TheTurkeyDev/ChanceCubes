package chanceCubes.rewards.type;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class SoundRewardType extends BaseRewardType<String>
{
	public SoundRewardType(String... sounds)
	{
		super(sounds);
	}
	
	@Override
	public void trigger(String sound, World world, int x, int y, int z, EntityPlayer player)
	{
		world.playSoundEffect(x, y, z, sound, 1, 1);
	}

}
