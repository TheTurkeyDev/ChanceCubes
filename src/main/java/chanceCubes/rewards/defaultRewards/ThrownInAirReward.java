package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ThrownInAirReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, BlockPos pos, final EntityPlayer player)
	{
		int px = (int) Math.floor(player.posX);
		int py = (int) Math.floor(player.posY) + 1;
		int pz = (int) Math.floor(player.posZ);

		for(int y = 0; y < 40; y++)
			for(int x = -1; x < 2; x++)
				for(int z = -1; z < 2; z++)
					RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos.add(px + x, py + y, pz + z));

		Task task = new Task("Item_Of_Destiny_Reward", 5)
		{
			@Override
			public void callback()
			{
				player.isAirBorne = true;
				player.motionY = 20;
				((EntityPlayerMP) player).connection.sendPacket(new SPacketEntityVelocity(player.getEntityId(), player.motionX, player.motionY, player.motionZ));
			}
		};
		Scheduler.scheduleTask(task);
	}

	@Override
	public int getChanceValue()
	{
		return -35;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Thrown_In_Air";
	}

}
