package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class RainingCatsAndCogsReward extends BaseCustomReward
{
	private String[] names = { "Radiant_Sora", "Turkey", "MrComputerGhost", "Valsis", "Silver", "Amatt", "Musician", "ReNinjaKitteh", "QuirkyGeek17" };

	public RainingCatsAndCogsReward()
	{
		super(CCubesCore.MODID + ":Cats_And_Dogs", 15);
	}

	@Override
	public void trigger(final World world, BlockPos position, EntityPlayer player)
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
				EntityTameable ent;

				if(RewardsUtil.rand.nextBoolean())
				{
					ent = new EntityWolf(world);
				}
				else
				{
					ent = new EntityOcelot(world);
					((EntityOcelot) ent).setTameSkin(1);
				}

				int xInc = RewardsUtil.rand.nextInt(10) * (RewardsUtil.rand.nextBoolean() ? -1 : 1);
				int zInc = RewardsUtil.rand.nextInt(10) * (RewardsUtil.rand.nextBoolean() ? -1 : 1);
				ent.setPositionAndRotation(player.getPosition().getX() + xInc, 256, player.getPosition().getZ() + zInc, 0, 0);
				ent.setTamed(true);
				ent.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 500, 1000));
				ent.setCustomName(new TextComponentString(names[RewardsUtil.rand.nextInt(names.length)]));
				ent.setCustomNameVisible(true);

				world.spawnEntity(ent);
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