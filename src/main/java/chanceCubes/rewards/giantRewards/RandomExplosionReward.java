package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.network.PacketParticle;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class RandomExplosionReward implements IChanceCubeReward
{
	private ItemStack[] randomStuff = { new ItemStack(Items.DIAMOND) };

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		Scheduler.scheduleTask(new Task("Random Explosion", 600, 2)
		{
			@Override
			public void callback()
			{
				EntityItem item;
				ItemStack stack = randomStuff[RewardsUtil.rand.nextInt(randomStuff.length)];
				for(double xx = 1; xx > -1; xx -= 0.25)
				{
					for(double zz = 1; zz > -1; zz -= 0.25)
					{
						item = new EntityItem(world, pos.getX(), pos.getY() + 1D, pos.getZ(), stack.copy());
						world.spawnEntityInWorld(item);
						item.motionX = xx;
						item.motionY = Math.random();
						item.motionZ = zz;
					}
				}
			}

			@Override
			public void update()
			{
				int xInc = RewardsUtil.rand.nextInt(3) + 3 * (RewardsUtil.rand.nextBoolean() ? -1 : 1);
				int yInc = RewardsUtil.rand.nextInt(3) + 3 * (RewardsUtil.rand.nextBoolean() ? -1 : 1);
				int zInc = RewardsUtil.rand.nextInt(3) + 3 * (RewardsUtil.rand.nextBoolean() ? -1 : 1);
				CCubesPacketHandler.INSTANCE.sendToAllAround(new PacketParticle("cloud", pos.getX() + xInc, pos.getY() + yInc, pos.getZ() + zInc, (xInc * -1) / 4f, (yInc * -1) / 4f, (zInc * -1) / 4f), new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 50));
			}
		});
	}

	@Override
	public int getChanceValue()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Random_Explosion";
	}

}
