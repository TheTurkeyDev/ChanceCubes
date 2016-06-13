package chanceCubes.rewards.defaultRewards;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;

public class ThrownInAirReward implements IChanceCubeReward
{

	@Override
	public void trigger(World world, int x, int y, int z, final EntityPlayer player)
	{
		int px = (int) Math.floor(player.posX);
		int py = (int) Math.floor(player.posY) + 1;
		int pz = (int) Math.floor(player.posZ);

		for(int yy = 0; yy < 40; yy++)
			for(int xx = -1; xx < 2; xx++)
				for(int zz = -1; zz < 2; zz++)
					RewardsUtil.placeBlock(Blocks.air, world, px + xx, py + yy, pz + zz);

		Task task = new Task("Item_Of_Destiny_Reward", 5)
		{
			@Override
			public void callback()
			{
				player.isAirBorne = true;
				player.motionY = 20;
				((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(player.getEntityId(), player.motionX, player.motionY, player.motionZ));
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
