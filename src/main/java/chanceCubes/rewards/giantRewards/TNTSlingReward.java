package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;

public class TNTSlingReward extends BaseCustomReward
{
	public TNTSlingReward()
	{
		super(CCubesCore.MODID + ":tnt_throw", 0);
	}

	@Override
	public void trigger(ServerWorld world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		Scheduler.scheduleTask(new Task("Throw TNT", 250, 10)
		{
			private TNTEntity tnt;

			@Override
			public void callback()
			{
				for(double xx = 1; xx > -1; xx -= 0.25)
				{
					for(double zz = 1; zz > -1; zz -= 0.25)
					{
						tnt = new TNTEntity(world, pos.getX(), pos.getY() + 1D, pos.getZ(), null);
						world.addEntity(tnt);
						tnt.setFuse(60);
						tnt.setMotion(xx, Math.random(), zz);
					}
				}
			}

			@Override
			public void update()
			{
				tnt = new TNTEntity(world, pos.getX(), pos.getY() + 1D, pos.getZ(), player);
				world.addEntity(tnt);
				tnt.setFuse(60);
				tnt.setMotion(-1 + (Math.random() * 2), Math.random(), -1 + (Math.random() * 2));
			}
		});
	}
}