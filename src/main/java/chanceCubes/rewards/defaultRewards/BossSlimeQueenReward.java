package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.List;
import java.util.Map;

public class BossSlimeQueenReward extends BossBaseReward
{
	public BossSlimeQueenReward()
	{
		super("slime_queen");
	}

	@Override
	public void spawnBoss(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		SlimeEntity queen = EntityType.SLIME.create(world);
		queen.setCustomName(new StringTextComponent("Slime Queen"));
		queen.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());

		queen.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getBossHealthDynamic(player, settings));
		queen.setHealth(queen.getMaxHealth());

		// Lol ok
		CompoundNBT nbt = new CompoundNBT();
		queen.writeAdditional(nbt);
		nbt.putInt("Size", 10);
		queen.readAdditional(nbt);

		queen.addPotionEffect(new EffectInstance(Effects.GLOWING, Integer.MAX_VALUE, 0, true, false));

		world.addEntity(queen);

		super.trackEntities(queen);
	}

	@Override
	public void onBossFightEnd(World world, BlockPos pos, PlayerEntity player)
	{
		CCubesCore.logger.log(Level.INFO, "End Fight!");
		Scheduler.scheduleTask(new Task("boss_fight_slime_queen_kill_all", 200, 20)
		{
			@Override
			public void callback()
			{

			}

			@Override
			public void update()
			{
				List<SlimeEntity> slimes = world.getEntitiesWithinAABB(SlimeEntity.class, new AxisAlignedBB(pos.add(-25, -25, -25), pos.add(25, 25, 25)));
				CCubesCore.logger.log(Level.INFO, slimes.size());
				for(SlimeEntity slime : slimes)
					slime.remove();
			}
		});

	}
}
