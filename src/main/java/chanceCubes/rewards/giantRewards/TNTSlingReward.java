package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;

public class TNTSlingReward extends BaseCustomReward
{
	public TNTSlingReward()
	{
		super(CCubesCore.MODID + ":tnt_throw", 0);
	}

	@Override
	public void trigger(ServerLevel world, BlockPos pos, Player player, JsonObject settings)
	{
		Scheduler.scheduleTask(new Task("Throw TNT", 250, 10)
		{
			private PrimedTnt tnt;

			@Override
			public void callback()
			{
				for(double xx = 1; xx > -1; xx -= 0.25)
				{
					for(double zz = 1; zz > -1; zz -= 0.25)
					{
						tnt = new PrimedTnt(world, pos.getX(), pos.getY() + 1D, pos.getZ(), null);
						world.addFreshEntity(tnt);
						tnt.setFuse(60);
						tnt.setDeltaMovement(xx, Math.random(), zz);
					}
				}
			}

			@Override
			public void update()
			{
				tnt = new PrimedTnt(world, pos.getX(), pos.getY() + 1D, pos.getZ(), player);
				world.addFreshEntity(tnt);
				tnt.setFuse(60);
				tnt.setDeltaMovement(-1 + (Math.random() * 2), Math.random(), -1 + (Math.random() * 2));
			}
		});
	}
}