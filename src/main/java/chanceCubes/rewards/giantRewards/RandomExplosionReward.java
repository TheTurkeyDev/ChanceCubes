package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.network.PacketParticle;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class RandomExplosionReward extends BaseCustomReward
{
	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.AMBIENT_CAVE, SoundCategory.BLOCKS, 1f, 1f);
		Scheduler.scheduleTask(new Task("Random Explosion", 300, 2)
		{
			int delay = 12;
			int count = 0;

			@Override
			public void callback()
			{
				RewardsUtil.placeBlock(Blocks.AIR.getDefaultState(), world, pos);
				Entity ent;
				int rand = RewardsUtil.rand.nextInt(6);
				for(double xx = 1; xx > -1; xx -= 0.25)
				{
					for(double zz = 1; zz > -1; zz -= 0.25)
					{
						if(rand == 0)
						{
							ent = new EntityCreeper(world);
						}
						else if(rand == 1)
						{
							ent = new EntityTNTPrimed(world);
						}
						else if(rand == 2)
						{
							ent = new EntityItem(world);
							((EntityItem) ent).setItem(new ItemStack(Items.DIAMOND));
						}
						else if(rand == 3)
						{
							ent = new EntityItem(world);
							((EntityItem) ent).setItem(new ItemStack(Items.MELON));
						}
						else if(rand == 4)
						{
							ent = new EntityBat(world);
						}
						else if(rand == 5)
						{
							ent = new EntityZombie(world);
						}
						else
						{
							ent = new EntityItem(world);
							((EntityItem) ent).setItem(new ItemStack(Items.DIAMOND));
						}

						ent.setPosition(pos.getX(), pos.getY() + 1D, pos.getZ());
						world.spawnEntity(ent);
						ent.motionX = xx;
						ent.motionY = Math.random();
						ent.motionZ = zz;
					}
				}
			}

			@Override
			public void update()
			{
				count++;
				if(count >= delay)
				{
					if(delay > 2)
					{
						delay--;
					}

					count = 0;
					int xInc = RewardsUtil.rand.nextInt(2) * (RewardsUtil.rand.nextBoolean() ? -1 : 1);
					int yInc = RewardsUtil.rand.nextInt(2) * (RewardsUtil.rand.nextBoolean() ? -1 : 1);
					int zInc = RewardsUtil.rand.nextInt(2) * (RewardsUtil.rand.nextBoolean() ? -1 : 1);
					if(delay < 3)
					{
						CCubesPacketHandler.INSTANCE.sendToAllAround(new PacketParticle("hugeexplosion", pos.getX() + 0.5 + xInc, pos.getY() + 0.5 + yInc, pos.getZ() + 0.5 + zInc, 0, 0, 0), new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 50));
						world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1f, 1f);
					}
					else
					{
						if(RewardsUtil.rand.nextBoolean())
						{
							world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1f, 1f);
						}
						else
						{
							world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_BLAZE_HURT, SoundCategory.BLOCKS, 1f, 1f);
						}
						CCubesPacketHandler.INSTANCE.sendToAllAround(new PacketParticle("lava", pos.getX() + 0.5 + xInc, pos.getY() + 0.5 + yInc, pos.getZ() + 0.5 + zInc, 0, 0, 0), new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 50));
					}

				}
			}
		});
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Random_Explosion";
	}
}