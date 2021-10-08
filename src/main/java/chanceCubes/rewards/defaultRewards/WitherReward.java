package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;

public class WitherReward extends BaseCustomReward
{
	public WitherReward()
	{
		super(CCubesCore.MODID + ":wither", -100);
	}

	@Override
	public void trigger(ServerWorld world, BlockPos pos, final PlayerEntity player, JsonObject settings)
	{
		int isReal = super.getSettingAsInt(settings, "isReal", 10, 0, 100);
		final WitherEntity wither = EntityType.WITHER.create(world);
		wither.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 1.5D, 90.0F, 0.0F);
		wither.renderYawOffset = 90.0F;
		world.addEntity(wither);
		wither.ignite();
		if(RewardsUtil.rand.nextBoolean())
			wither.setCustomName(new StringTextComponent("Kiwi"));
		else
			wither.setCustomName(new StringTextComponent("Kehaan"));

		RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "\"You've got to ask yourself one question: 'Do I feel lucky?' Well, do ya, punk?\"");

		Scheduler.scheduleTask(new Task("Wither Reward", 180)
		{
			@Override
			public void callback()
			{
				if(!removeEnts(wither))
					RewardsUtil.executeCommand(world, player, player.getPosition(), "/advancement grant @p only chancecubes:wither");
			}

			private boolean removeEnts(Entity ent)
			{
				if(RewardsUtil.rand.nextInt(100) < isReal)
					return false;

				ent.remove();
				return true;
			}
		});
	}
}
