package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;

public class ThrowablesReward extends BaseCustomReward
{
	public ThrowablesReward()
	{
		super(CCubesCore.MODID + ":throwables", 0);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
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
					throwEnt = EntityType.ARROW.create(level);
				}
				else if(entChoice == 1)
				{
					throwEnt = EntityType.FIREBALL.create(level);
					((Fireball) throwEnt).xPower = 0.1f * (-1 + (Math.random() * 2));
					((Fireball) throwEnt).yPower = 0.1f * (-1 + (Math.random() * 2));
					((Fireball) throwEnt).zPower = 0.1f * (-1 + (Math.random() * 2));
				}
				else if(entChoice == 2)
				{
					throwEnt = EntityType.EGG.create(level);
				}
				else
				{
					throwEnt = EntityType.TNT.create(level);
					((PrimedTnt) throwEnt).setFuse(20);
				}
				throwEnt.moveTo(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
				throwEnt.setDeltaMovement(-1 + (Math.random() * 2), -1 + (Math.random() * 2), -1 + (Math.random() * 2));
				level.addFreshEntity(throwEnt);
			}
		});
	}
}