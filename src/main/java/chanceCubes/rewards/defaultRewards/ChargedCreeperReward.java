package chanceCubes.rewards.defaultRewards;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
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
		EntityCreeper ent = new EntityCreeper(world);
		ent.setLocationAndAngles(x, y + .5, z, 0, 0);
		ent.addPotionEffect(new PotionEffect(Potion.resistance.id, 1, 99, true));
		ent.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 10, 99, true));
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
		return -60;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Charged_Creeper";
	}

}
