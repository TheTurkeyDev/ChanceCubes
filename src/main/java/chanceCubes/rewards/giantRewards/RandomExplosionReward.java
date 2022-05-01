package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class RandomExplosionReward extends BaseCustomReward
{
	public RandomExplosionReward()
	{
		super(CCubesCore.MODID + ":random_explosion", 0);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.AMBIENT_CAVE, SoundSource.BLOCKS, 1f, 1f);
		Scheduler.scheduleTask(new Task("Random Explosion", 300, 2)
		{
			int delay = 12;
			int count = 0;

			@Override
			public void callback()
			{
				RewardsUtil.placeBlock(Blocks.AIR.defaultBlockState(), level, pos);
				Entity ent;
				int rand = RewardsUtil.rand.nextInt(6);
				for(double xx = 1; xx > -1; xx -= 0.25)
				{
					for(double zz = 1; zz > -1; zz -= 0.25)
					{
						if(rand == 0)
						{
							ent = EntityType.CREEPER.create(level);
						}
						else if(rand == 1)
						{
							ent = EntityType.TNT.create(level);
						}
						else if(rand == 2)
						{
							ent = EntityType.ITEM.create(level);
							((ItemEntity) ent).setItem(new ItemStack(Items.DIAMOND));
						}
						else if(rand == 3)
						{
							ent = EntityType.ITEM.create(level);
							((ItemEntity) ent).setItem(new ItemStack(Items.MELON_SLICE));
						}
						else if(rand == 4)
						{
							ent = EntityType.BAT.create(level);
						}
						else
						{
							ent = EntityType.ZOMBIE.create(level);
						}

						ent.moveTo(pos.getX(), pos.getY() + 1D, pos.getZ());
						level.addFreshEntity(ent);
						ent.setDeltaMovement(xx, Math.random(), zz);
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
						level.sendParticles(ParticleTypes.EXPLOSION, pos.getX() + 0.5 + xInc, pos.getY() + 0.5 + yInc, pos.getZ() + 0.5 + zInc, 3, 0, 0, 0, .125);
						level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1f, 1f);
					}
					else
					{
						if(RewardsUtil.rand.nextBoolean())
							level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1f, 1f);
						else
							level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLAZE_HURT, SoundSource.BLOCKS, 1f, 1f);
						level.sendParticles(ParticleTypes.LAVA, pos.getX() + 0.5 + xInc, pos.getY() + 0.5 + yInc, pos.getZ() + 0.5 + zInc, 5, 0, 0, 0, .125f);
					}

				}
			}
		});
	}
}