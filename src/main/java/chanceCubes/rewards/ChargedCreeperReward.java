package chanceCubes.rewards;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

public class ChargedCreeperReward implements IChanceCubeReward
{

	@Override
	public void trigger(final World world, final BlockPos pos, EntityPlayer player)
	{
		world.setBlockToAir(pos.add(0, 1, 0));
		Entity ent = new EntityCreeper(world);
		ent.setLocationAndAngles(pos.getX(), pos.getY() + .5, pos.getZ(), 0, 0);
		world.spawnEntityInWorld(ent);

		Task task = new Task("Charged Creeper Reward", 2)
		{
			@Override
			public void callback()
			{
				world.addWeatherEffect(new EntityLightningBolt(world, pos.getX(), pos.getY(), pos.getZ()));
			}
		};

		Scheduler.scheduleTask(task);
	}

	@Override
	public int getChanceValue()
	{
		return -60;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Charged_Creeper";
	}

}
