package chanceCubes.rewards.giantRewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockThrowerReward extends BaseCustomReward
{
	public BlockThrowerReward()
	{
		super(CCubesCore.MODID + ":Block_Thrower", 0);
	}

	@Override
	public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		for(int x = -20; x < 21; x++)
		{
			for(int z = -20; z < 21; z++)
			{
				if(!world.isAirBlock(pos.add(x, 11, z)))
					for(int y = -1; y < 12; y++)
						world.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState());
			}
		}

		Scheduler.scheduleTask(new Task("Throw_Block", 450, 2)
		{
			private List<FallingBlockEntity> blocks = new ArrayList<>();

			@Override
			public void update()
			{
				if(this.delayLeft > 100)
				{
					int x = RewardsUtil.rand.nextInt(41) - 21;
					int z = RewardsUtil.rand.nextInt(41) - 21;
					int y = -2;
					for(y = 12; y > -2; y--)
						if(!world.isAirBlock(pos.add(x, y, z)))
							break;
					BlockPos newPos = pos.add(x, y, z);
					BlockState state = world.getBlockState(newPos);

					if(CCubesSettings.nonReplaceableBlocks.contains(state) || state.getBlock().equals(Blocks.AIR))
						state = Blocks.DIRT.getDefaultState();
					else
						world.setBlockState(newPos, Blocks.AIR.getDefaultState());

					FallingBlockEntity block = new FallingBlockEntity(world, newPos.getX() + 0.5, newPos.getY(), newPos.getZ() + 0.5, state);
					block.fallTime = 1;
					block.setNoGravity(true);
					block.setMotion(0, 0.25f, 0);

					world.addEntity(block);
					blocks.add(block);
				}

				for(FallingBlockEntity b : blocks)
				{
					if(b.posY > pos.getY() + 8)
					{
						b.posY = pos.getY() + 8;
						b.setMotion(0, 0, 0);
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
					world.addParticle(ParticleTypes.EXPLOSION, b.posX, b.posY, b.posZ, 0, 0, 0);

					b.remove();

					if(rand == 0)
					{
						ent = EntityType.CREEPER.create(world);
						((LivingEntity) ent).addPotionEffect(new EffectInstance(Effects.RESISTANCE, 20, 3));
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
						((LivingEntity) ent).addPotionEffect(new EffectInstance(Effects.RESISTANCE, 20, 3));
					}
					else
					{
						ent = EntityType.ITEM.create(world);
						((ItemEntity) ent).setItem(new ItemStack(Items.DIAMOND));
					}

					ent.setPosition(b.posX, b.posY, b.posZ);
					world.addEntity(ent);
				}
				world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1f, 1f);
			}
		});

	}
}