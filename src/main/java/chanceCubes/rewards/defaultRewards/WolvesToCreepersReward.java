package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WolvesToCreepersReward implements IChanceCubeReward
{

	@Override
	public void trigger(final World world, BlockPos pos, EntityPlayer player)
	{
		final List<Entity> wolves = new ArrayList<Entity>();
		for(int i = 0; i < 10; i++)
		{
			for(int yy = 0; yy < 4; yy++)
				for(int xx = -1; xx < 2; xx++)
					for(int zz = -1; zz < 2; zz++)
						RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(xx, yy, zz));

			EntityWolf wolf = new EntityWolf(world);
			wolf.setPosition(pos.getX(), pos.getY(), pos.getZ());
			wolf.setTamed(true);
			wolf.setOwnerId(player.getUniqueID());
			wolf.setCustomNameTag("Kehaan");
			wolves.add(wolf);
			world.spawnEntityInWorld(wolf);
		}
		
		RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "Do they look weird to you?");

		Task task = new Task("Mob_Switch", 200)
		{
			@Override
			public void callback()
			{
				for(Entity wolf : wolves)
				{
					wolf.setDead();
					EntityCreeper creeper = new EntityCreeper(world);
					creeper.setPositionAndRotation(wolf.posX, wolf.posY, wolf.posZ, wolf.rotationYaw, wolf.rotationPitch);
					creeper.setCustomNameTag("Jacky");
					world.spawnEntityInWorld(creeper);
				}
			}
		};

		Scheduler.scheduleTask(task);
	}

	@Override
	public int getChanceValue()
	{
		return -20;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Wolves_To_Creepers";
	}

}
