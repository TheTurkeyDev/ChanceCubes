package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class BlockThrowerReward extends BaseCustomReward
{
	public BlockThrowerReward()
	{
		super(CCubesCore.MODID + ":block_thrower", 0);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		for(int x = -20; x < 21; x++)
		{
			for(int z = -20; z < 21; z++)
			{
				if(!level.getBlockState(pos.offset(x, 11, z)).isAir())
					for(int y = -1; y < 12; y++)
						level.setBlockAndUpdate(pos.offset(x, y, z), Blocks.AIR.defaultBlockState());
			}
		}

		Scheduler.scheduleTask(new Task("Throw_Block", 450, 2)
		{
			private final List<FallingBlockEntity> blocks = new ArrayList<>();

			@Override
			public void update()
			{
				if(this.delayLeft > 100)
				{
					int x = RewardsUtil.rand.nextInt(41) - 21;
					int z = RewardsUtil.rand.nextInt(41) - 21;
					int y;
					for(y = 12; y > -2; y--)
						if(!level.getBlockState(pos.offset(x, y, z)).isAir())
							break;
					BlockPos newPos = pos.offset(x, y, z);
					BlockState state = level.getBlockState(newPos);

					if(CCubesSettings.nonReplaceableBlocks.contains(state) || state.getBlock().equals(Blocks.AIR) /*|| state.getBlock() instanceof FluidBlock*/)
						state = Blocks.DIRT.defaultBlockState();
					else
						level.setBlockAndUpdate(newPos, Blocks.AIR.defaultBlockState());

					FallingBlockEntity block = FallingBlockEntity.fall(level, newPos.offset(0.5, 0, 0.5), state);
					block.fallDistance = 0;
					block.setNoGravity(true);
					block.setDeltaMovement(0, 0.25f, 0);

					level.addFreshEntity(block);
					blocks.add(block);
				}

				for(FallingBlockEntity b : blocks)
				{
					if(b.getY() > pos.getY() + 8)
					{
						b.moveTo(b.getX(), pos.getY() + 8, b.getZ());
						b.setDeltaMovement(0, 0, 0);
					}
				}
			}

			@Override
			public void callback()
			{
				Entity ent;
				int rand = RewardsUtil.rand.nextInt(6);
				for(FallingBlockEntity b : blocks)
				{
					level.addParticle(ParticleTypes.EXPLOSION, b.getX(), b.getY(), b.getZ(), 0, 0, 0);

					b.remove(Entity.RemovalReason.DISCARDED);

					if(rand == 0)
					{
						ent = EntityType.CREEPER.create(level);
						((LivingEntity) ent).addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20, 3));
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
						((LivingEntity) ent).addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20, 3));
					}

					ent.moveTo(b.getX(), b.getY(), b.getZ());
					level.addFreshEntity(ent);
				}
				level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1f, 1f);
			}
		});

	}
}