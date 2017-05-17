package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
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
import net.minecraft.world.World;

public class RainingCatsAndCogsReward implements IChanceCubeReward
{
	private String[] names = { "EmoKiba", "Turkey", "MrComputerGhost", "Valsis", "Silver", "Amatt", "Musician", "ReNinjaKitteh" };

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
				ent.setCustomNameTag(names[RewardsUtil.rand.nextInt(names.length)]);
				ent.setAlwaysRenderNameTag(true);

				world.spawnEntityInWorld(ent);
				Scheduler.scheduleTask(new Task("Despawn Delay", 200)
				{
					@Override
					public void callback()
					{
						ent.setDead();
					}
				});
			}
		});
	}

	@Override
	public int getChanceValue()
	{
		return -45;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Cats_And_Dogs";
	}
}