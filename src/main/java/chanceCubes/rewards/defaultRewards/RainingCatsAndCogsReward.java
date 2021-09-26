package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;

public class RainingCatsAndCogsReward extends BaseCustomReward
{
	private final String[] names = {"Radiant_Sora", "Turkey", "MrComputerGhost", "Valsis", "Silver", "Amatt", "Musician", "ReNinjaKitteh", "QuirkyGeek17"};

	public RainingCatsAndCogsReward()
	{
		super(CCubesCore.MODID + ":cats_and_dogs", 15);
	}

	@Override
	public void trigger(final ServerLevel level, BlockPos position, Player player, JsonObject settings)
	{
		RewardsUtil.sendMessageToNearPlayers(level, position, 36, "It's raining Cats and dogs!");

		Scheduler.scheduleTask(new Task("Raining Cats and Dogs", 500, 5)
		{
			@Override
			public void callback()
			{
			}

			@Override
			public void update()
			{
				TamableAnimal ent;

				if(RewardsUtil.rand.nextBoolean())
					ent = EntityType.WOLF.create(level);
				else
					ent = EntityType.CAT.create(level);

				int xInc = RewardsUtil.rand.nextInt(10) * (RewardsUtil.rand.nextBoolean() ? -1 : 1);
				int zInc = RewardsUtil.rand.nextInt(10) * (RewardsUtil.rand.nextBoolean() ? -1 : 1);
				ent.moveTo(player.getX() + xInc, 256, player.getZ() + zInc, 0, 0);
				ent.setTame(true);
				ent.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 500, 1000));
				ent.setCustomName(new TextComponent(names[RewardsUtil.rand.nextInt(names.length)]));
				ent.setCustomNameVisible(true);

				level.addFreshEntity(ent);
				Scheduler.scheduleTask(new Task("Despawn Delay", 200)
				{
					@Override
					public void callback()
					{
						ent.remove(Entity.RemovalReason.DISCARDED);
					}
				});
			}
		});
	}
}