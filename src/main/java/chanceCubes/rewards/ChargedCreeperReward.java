package chanceCubes.rewards;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

public class ChargedCreeperReward implements IChanceCubeReward
{

	@Override
	public void trigger(final World world, final int x, final int y, final int z, EntityPlayer player)
	{
		world.setBlockToAir(x, y + 1, z);
		Entity ent = new EntityCreeper(world);
		ent.setLocationAndAngles(x, y + .5, z, 0, 0);
		world.spawnEntityInWorld(ent);

		Task task = new Task("Charged Creeper Reward", 2)
		{
			@Override
			public void callback()
			{
				world.addWeatherEffect(new EntityLightningBolt(world, x, y, z));
			}
		};

		Scheduler.scheduleTask(task);
	}

	@Override
	public int getChanceValue()
	{
		return -25;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Charged_Creeper";
	}

}
