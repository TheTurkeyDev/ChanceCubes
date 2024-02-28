package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.mcwrapper.ComponentWrapper;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.apache.logging.log4j.Level;

import java.util.List;

public class BossSlimeQueenReward extends BossBaseReward
{
	public BossSlimeQueenReward()
	{
		super("slime_queen");
	}

	@Override
	public LivingEntity initBoss(ServerLevel level, BlockPos pos, Player player, JsonObject settings, BattleWrapper battleWrapper)
	{
		Slime queen = EntityType.SLIME.create(level);
		queen.setCustomName(ComponentWrapper.string("Slime Queen"));

		// Lol ok
		CompoundTag nbt = new CompoundTag();
		queen.save(nbt);
		nbt.putInt("Size", 10);
		queen.load(nbt);

		queen.addEffect(new MobEffectInstance(MobEffects.GLOWING, Integer.MAX_VALUE, 0, true, false));

		return queen;
	}

	@Override
	public void onBossFightEnd(ServerLevel level, BlockPos pos, Player player)
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
				List<Slime> slimes = level.getEntities(EntityType.SLIME, new AABB(pos).inflate(25), EntitySelector.NO_SPECTATORS);
				CCubesCore.logger.log(Level.INFO, slimes.size());
				for(Slime slime : slimes)
					slime.remove(Entity.RemovalReason.DISCARDED);
			}
		});

	}
}
