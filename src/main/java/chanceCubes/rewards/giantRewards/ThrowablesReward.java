package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;

public class ThrowablesReward extends BaseCustomReward
{
	public ThrowablesReward()
	{
		super(CCubesCore.MODID + ":throwables", 0);
	}

	@Override
	public void trigger(ServerWorld world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		Scheduler.scheduleTask(new Task("Throw TNT", 250, 5)
		{

			@Override
			public void callback()
			{

			}

			@Override
			public void update()
			{
				int entChoice = RewardsUtil.rand.nextInt(4);
				Entity throwEnt;
				if(entChoice == 0)
				{
					throwEnt = EntityType.ARROW.create(world);
				}
				else if(entChoice == 1)
				{
					throwEnt = EntityType.FIREBALL.create(world);
					((FireballEntity) throwEnt).accelerationX = 0.1f * (-1 + (Math.random() * 2));
					((FireballEntity) throwEnt).accelerationY = 0.1f * (-1 + (Math.random() * 2));
					((FireballEntity) throwEnt).accelerationZ = 0.1f * (-1 + (Math.random() * 2));
				}
				else if(entChoice == 2)
				{
					throwEnt = EntityType.EGG.create(world);
				}
				else
				{
					throwEnt = EntityType.TNT.create(world);
					((TNTEntity) throwEnt).setFuse(20);
				}
				throwEnt.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
				throwEnt.setMotion(-1 + (Math.random() * 2), -1 + (Math.random() * 2), -1 + (Math.random() * 2));
				world.addEntity(throwEnt);
			}
		});
	}
}