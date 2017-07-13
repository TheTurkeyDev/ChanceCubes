package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.rewards.rewardparts.CommandPart;
import chanceCubes.util.CCubesCommandSender;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WitherReward implements IChanceCubeReward
{
	@Override
	public void trigger(World world, BlockPos pos, final EntityPlayer player)
	{
		final EntityWither wither = new EntityWither(world);
		wither.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 1.5D, 90.0F, 0.0F);
		wither.renderYawOffset = 90.0F;
		world.spawnEntity(wither);
		wither.ignite();
		if(RewardsUtil.rand.nextBoolean())
			wither.setCustomNameTag("Kiwi");
		else
			wither.setCustomNameTag("Kehaan");

		RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "\"You've got to ask yourself one question: 'Do I feel lucky?' Well, do ya, punk?\"");

		Scheduler.scheduleTask(new Task("Wither Reward", 180)
		{
			@Override
			public void callback() {
				removeEnts(wither);
				if (!removeEnts(wither)) {
					MinecraftServer server = world.getMinecraftServer();
					Boolean rule = server.worlds[0].getGameRules().getBoolean("commandBlockOutput");
					server.worlds[0].getGameRules().setOrCreateGameRule("commandBlockOutput", "false");
					CCubesCommandSender sender = new CCubesCommandSender(player, pos);
					String advancement = "/advancement grant @p only chancecubes:wither";
					server.getCommandManager().executeCommand(sender, advancement);
					server.worlds[0].getGameRules().setOrCreateGameRule("commandBlockOutput", rule.toString());
				}
			}

			private boolean removeEnts(Entity ent)
			{
				if(RewardsUtil.rand.nextInt(10) != 1)
				{
					ent.setDead();
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public int getChanceValue()
	{
		return -100;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Wither";
	}

}
