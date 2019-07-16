package chanceCubes.rewards.giantRewards;

import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RandomExplosionReward extends BaseCustomReward
{
	public RandomExplosionReward()
	{
		super(CCubesCore.MODID + ":Random_Explosion", 0);
	}

	@Override
	public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
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
							ent = EntityType.CREEPER.create(world);
						}
						else if(rand == 1)
						{
							ent = EntityType.TNT.create(world);
						}
						else if(rand == 2)
						{
							ent = EntityType.ITEM.create(world);
							((ItemEntity) ent).setItem(new ItemStack(Items.DIAMOND));
						}
						else if(rand == 3)
						{
							ent = EntityType.ITEM.create(world);
							((ItemEntity) ent).setItem(new ItemStack(Items.MELON_SLICE));
						}
						else if(rand == 4)
						{
							ent = EntityType.BAT.create(world);
						}
						else if(rand == 5)
						{
							ent = EntityType.ZOMBIE.create(world);
						}
						else
						{
							ent = EntityType.ITEM.create(world);
							((ItemEntity) ent).setItem(new ItemStack(Items.DIAMOND));
						}

						ent.setPosition(pos.getX(), pos.getY() + 1D, pos.getZ());
						world.addEntity(ent);
						ent.setMotion(xx, Math.random(), zz);
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
						world.addParticle(ParticleTypes.EXPLOSION, pos.getX() + 0.5 + xInc, pos.getY() + 0.5 + yInc, pos.getZ() + 0.5 + zInc, 0, 0, 0);
						world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1f, 1f);
					}
					else
					{
						if(RewardsUtil.rand.nextBoolean())
							world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1f, 1f);
						else
							world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_BLAZE_HURT, SoundCategory.BLOCKS, 1f, 1f);
						world.addParticle(ParticleTypes.LAVA, pos.getX() + 0.5 + xInc, pos.getY() + 0.5 + yInc, pos.getZ() + 0.5 + zInc, 0, 0, 0);
					}

				}
			}
		});
	}
}