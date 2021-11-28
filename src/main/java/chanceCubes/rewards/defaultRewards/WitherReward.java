package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;

public class WitherReward extends BaseCustomReward
{
	public WitherReward()
	{
		super(CCubesCore.MODID + ":wither", -100);
	}

	@Override
	public void trigger(ServerLevel world, BlockPos pos, final Player player, JsonObject settings)
	{
		int isReal = super.getSettingAsInt(settings, "isReal", 10, 0, 100);
		final WitherBoss wither = EntityType.WITHER.create(world);
		wither.moveTo(pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 1.5D, 90.0F, 0.0F);
		//TODO
		//wither.renderYawOffset = 90.0F;
		world.addFreshEntity(wither);
		//TODO
		//wither.ignite();
		if(RewardsUtil.rand.nextBoolean())
			wither.setCustomName(new TextComponent("Kiwi"));
		else
			wither.setCustomName(new TextComponent("Kehaan"));

		RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "\"You've got to ask yourself one question: 'Do I feel lucky?' Well, do ya, punk?\"");

		Scheduler.scheduleTask(new Task("Wither Reward", 180)
		{
			@Override
			public void callback()
			{
				if(!removeEnts(wither))
					RewardsUtil.executeCommand(world, player, player.getOnPos(), "/advancement grant @p only chancecubes:wither");
			}

			private boolean removeEnts(Entity ent)
			{
				if(RewardsUtil.rand.nextInt(100) < isReal)
				{
					ent.remove(Entity.RemovalReason.DISCARDED);
					return true;
				}
				return false;
			}
		});
	}
}
