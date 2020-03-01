package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.Map;

public class RainingCatsAndCogsReward extends BaseCustomReward
{
	private String[] names = {"Radiant_Sora", "Turkey", "MrComputerGhost", "Valsis", "Silver", "Amatt", "Musician", "ReNinjaKitteh", "QuirkyGeek17"};

	public RainingCatsAndCogsReward()
	{
		super(CCubesCore.MODID + ":cats_and_dogs", 15);
	}

	@Override
	public void trigger(final World world, BlockPos position, PlayerEntity player, Map<String, Object> settings)
	{
		RewardsUtil.sendMessageToNearPlayers(world, position, 36, "It's raining Cats and dogs!");

		Scheduler.scheduleTask(new Task("Raining Cats and Dogs", 500, 5)
		{
			@Override
			public void callback()
			{
			}

			@Override
			public void update()
			{
				TameableEntity ent;

				if(RewardsUtil.rand.nextBoolean())
					ent = EntityType.WOLF.create(world);
				else
					ent = EntityType.CAT.create(world);

				int xInc = RewardsUtil.rand.nextInt(10) * (RewardsUtil.rand.nextBoolean() ? -1 : 1);
				int zInc = RewardsUtil.rand.nextInt(10) * (RewardsUtil.rand.nextBoolean() ? -1 : 1);
				ent.setPositionAndRotation(player.getPosition().getX() + xInc, 256, player.getPosition().getZ() + zInc, 0, 0);
				ent.setTamed(true);
				ent.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 500, 1000));
				ent.setCustomName(new StringTextComponent(names[RewardsUtil.rand.nextInt(names.length)]));
				ent.setCustomNameVisible(true);

				world.addEntity(ent);
				Scheduler.scheduleTask(new Task("Despawn Delay", 200)
				{
					@Override
					public void callback()
					{
						ent.remove();
					}
				});
			}
		});
	}
}