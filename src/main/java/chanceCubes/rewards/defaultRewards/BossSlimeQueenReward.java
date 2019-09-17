package chanceCubes.rewards.defaultRewards;

import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class BossSlimeQueenReward extends BossBaseReward
{
	public BossSlimeQueenReward()
	{
		super("Slime_Queen");
	}

	@Override
	public void spawnBoss(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
	{
		EntitySlime queen = new EntitySlime(world);
		queen.setCustomNameTag("Slime Queen");
		queen.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());

		queen.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Chance Cubes Boss Modifier", 10, 1));
		queen.setHealth(queen.getMaxHealth());

		// Lol ok
		NBTTagCompound nbt = queen.writeToNBT(new NBTTagCompound());
		nbt.setInteger("Size", 10);
		queen.readFromNBT(nbt);

		queen.addPotionEffect(new PotionEffect(MobEffects.GLOWING, Integer.MAX_VALUE, 0, true, false));

		world.spawnEntity(queen);

		super.trackEntities(queen);
	}

	@Override
	public void onBossFightEnd(World world, BlockPos pos, EntityPlayer player)
	{
		Scheduler.scheduleTask(new Task("boss_fight_slime_queen_kill_all", 200, 20)
		{
			@Override
			public void callback()
			{

			}

			@Override
			public void update()
			{
				List<EntitySlime> slimes = world.getEntitiesWithinAABB(EntitySlime.class, new AxisAlignedBB(pos.add(-15, -10, -15), pos.add(15, 15, 15)));
				for(EntitySlime slime : slimes)
					slime.setDead();
			}
		});

	}
}
