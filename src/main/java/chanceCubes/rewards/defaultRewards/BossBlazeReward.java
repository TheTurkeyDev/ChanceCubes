package chanceCubes.rewards.defaultRewards;

import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public class BossBlazeReward extends BossBaseReward
{
	public BossBlazeReward()
	{
		super("demonic_blaze");
	}

	@Override
	public void spawnBoss(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
	{
		EntityBlaze blaze = new EntityBlaze(world);
		blaze.setCustomNameTag("Demonic Blaze");
		blaze.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
		blaze.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getBossHealthDynamic(player, settings));
		blaze.setHealth(blaze.getMaxHealth());

		Scheduler.scheduleTask(new Task("blaze_abilities", -1, 20)
		{
			@Override
			public void callback()
			{
			}

			@Override
			public void update()
			{
				if(blaze.isDead)
				{
					Scheduler.removeTask(this);
					return;
				}

				if(RewardsUtil.rand.nextInt(15) == 4)
					goInvisible(blaze);
				if(RewardsUtil.rand.nextInt(10) == 4)
					setGroundOnFire(world, player.getPosition());
				if(RewardsUtil.rand.nextInt(5) == 4)
					shootFireballs(world, blaze, player);
			}
		});


		world.spawnEntity(blaze);
		super.trackEntities(blaze);
		super.trackedPlayers(player);
	}

	private void goInvisible(EntityBlaze blaze)
	{
		blaze.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 5));
	}

	private void setGroundOnFire(World world, BlockPos playerPos)
	{
		for(int xOff = -1; xOff <= 1; xOff++)
		{
			for(int zOff = -1; zOff <= 1; zOff++)
			{
				BlockPos offPos = playerPos.add(xOff, 0, zOff);
				RewardsUtil.placeBlock(Blocks.FIRE.getDefaultState(), world, offPos);
			}
		}
	}

	private void shootFireballs(World world, EntityBlaze blaze, EntityPlayer player)
	{
		double d1 = player.posX - blaze.posX;
		double d2 = player.getEntityBoundingBox().minY + (double) (player.height / 2.0F) - (blaze.posY + (double) (blaze.height / 2.0F));
		double d3 = player.posZ - blaze.posZ;
		for(int i = 0; i < 5; i++)
		{
			EntitySmallFireball entitysmallfireball = new EntitySmallFireball(world, blaze, d1 + blaze.getRNG().nextGaussian(), d2, d3 + blaze.getRNG().nextGaussian());
			entitysmallfireball.posY = blaze.posY + (double) (blaze.height / 2.0F) + 0.5D;
			world.spawnEntity(entitysmallfireball);
		}
	}

	@Override
	public void onBossFightEnd(World world, BlockPos pos, EntityPlayer player)
	{

	}
}
