package chanceCubes.rewards.defaultRewards;

import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class WitherReward extends BaseCustomReward
{
	public WitherReward()
	{
		super(CCubesCore.MODID + ":Wither", -100);
	}

	@Override
	public void trigger(World world, BlockPos pos, final EntityPlayer player, Map<String, Object> settings)
	{
		final EntityWither wither = new EntityWither(world);
		wither.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 1.5D, 90.0F, 0.0F);
		wither.renderYawOffset = 90.0F;
		world.spawnEntity(wither);
		wither.ignite();
		if(RewardsUtil.rand.nextBoolean())
			wither.setCustomName(new TextComponentString("Kiwi"));
		else
			wither.setCustomName(new TextComponentString("Kehaan"));

		RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "\"You've got to ask yourself one question: 'Do I feel lucky?' Well, do ya, punk?\"");

		Scheduler.scheduleTask(new Task("Wither Reward", 180)
		{
			@Override
			public void callback()
			{
				removeEnts(wither);
				if(!removeEnts(wither))
					RewardsUtil.executeCommand(world, player, player.getPositionVector(), "/advancement grant @p only chancecubes:wither");
			}

			private boolean removeEnts(Entity ent)
			{
				if(RewardsUtil.rand.nextInt(10) != 1)
				{
					ent.remove();
					return true;
				}
				return false;
			}
		});
	}
}
