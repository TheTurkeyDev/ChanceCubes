package chanceCubes.rewards.type;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ExperienceRewardType implements IRewardType
{
	private int levels;

	public ExperienceRewardType(int levels)
	{
		this.levels = levels;
	}
	
	@Override
	public void trigger(World world, int x, int y, int z, EntityPlayer player)
	{
		if(!world.isRemote)
		{
			Entity newEnt = new EntityXPOrb(world, x, y, z, levels);
			world.spawnEntityInWorld(newEnt);
		}
	}

}
