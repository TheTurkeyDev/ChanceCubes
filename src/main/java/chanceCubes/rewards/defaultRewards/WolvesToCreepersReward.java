package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.mcwrapper.ComponentWrapper;
import chanceCubes.mcwrapper.EntityWrapper;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class WolvesToCreepersReward extends BaseCustomReward
{
	public WolvesToCreepersReward()
	{
		super(CCubesCore.MODID + ":wolves_to_creepers", -20);
	}

	@Override
	public void trigger(final ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		final List<Entity> wolves = new ArrayList<>();
		for(int i = 0; i < 10; i++)
		{
			for(int yy = 0; yy < 4; yy++)
				for(int xx = -1; xx < 2; xx++)
					for(int zz = -1; zz < 2; zz++)
						RewardsUtil.placeBlock(Blocks.AIR.defaultBlockState(), level, pos.offset(xx, yy, zz));

			Wolf wolf = EntityWrapper.spawnEntityAt(EntityType.WOLF, level, pos);
			wolf.setTame(true);
			wolf.setOwnerUUID(player.getUUID());
			wolf.setCustomName(ComponentWrapper.string("Kehaan"));
			wolves.add(wolf);
		}

		RewardsUtil.sendMessageToNearPlayers(level, pos, 32, "Do they look weird to you?");

		Scheduler.scheduleTask(new Task("Mob_Switch", 200)
		{
			@Override
			public void callback()
			{
				for(Entity wolf : wolves)
				{
					wolf.remove(Entity.RemovalReason.DISCARDED);
					EntityWrapper.spawnNamedEntityAt(EntityType.CREEPER, level, "Jacky", wolf.getX(), wolf.getY(), wolf.getZ());
				}
			}
		});
	}
}
