package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

public class TrollTNTReward extends BaseCustomReward
{
	public TrollTNTReward()
	{
		super(CCubesCore.MODID + ":troll_tnt", -5);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, final Player player, JsonObject settings)
	{
		for(int x = -1; x < 2; x++)
			for(int z = -1; z < 2; z++)
				RewardsUtil.placeBlock(Blocks.COBWEB.defaultBlockState(), level, new BlockPos(player.getX() + x, player.getY(), player.getZ() + z));

		final PrimedTnt entitytntprimed = new PrimedTnt(level, player.getX() + 1D, player.getY() + 1D, player.getZ(), player);
		entitytntprimed.setFuse(80);
		level.addFreshEntity(entitytntprimed);
		level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);

		int outOf = super.getSettingAsInt(settings, "isReal", 2, 0, 100);
		if(RewardsUtil.rand.nextInt(100) > outOf)
		{
			Scheduler.scheduleTask(new Task("TrollTNT", 77)
			{
				@Override
				public void callback()
				{
					RewardsUtil.sendMessageToPlayer(player, "BOOM");
					entitytntprimed.remove(Entity.RemovalReason.DISCARDED);
				}
			});
		}
	}
}