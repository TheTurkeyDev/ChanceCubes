package chanceCubes.rewards.type;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ExperienceRewardType extends BaseRewardType<Integer>
{

	public ExperienceRewardType(Integer... levels)
	{
		super(levels);
	}

	@Override
	public void trigger(Integer levels, World world, int x, int y, int z, EntityPlayer player)
	{
		Entity newEnt = new EntityXPOrb(world, x, y, z, levels);
		world.spawnEntityInWorld(newEnt);
	}
}
