package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.block.Blocks;
import net.minecraft.block.CakeBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;

public class CakeIsALieReward extends BaseCustomReward
{
	public CakeIsALieReward()
	{
		super(CCubesCore.MODID + ":cake", 20);
	}

	@Override
	public void trigger(final ServerWorld world, final BlockPos pos, final PlayerEntity player, JsonObject settings)
	{
		RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "But is it a lie?");

		RewardsUtil.placeBlock(Blocks.CAKE.getDefaultState(), world, pos);

		final int lieChance = super.getSettingAsInt(settings, "lieChance", 10, 0, 100);

		if(RewardsUtil.rand.nextInt(3) == 1)
		{
			Scheduler.scheduleTask(new Task("Cake_Is_A_Lie", 6000, 20)
			{
				@Override
				public void callback()
				{
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
				}

				@Override
				public void update()
				{
					if(!world.getBlockState(pos).getBlock().equals(Blocks.CAKE))
					{
						Scheduler.removeTask(this);
					}
					else if(world.getBlockState(pos).get(CakeBlock.BITES) > 0)
					{
						world.setBlockState(pos, Blocks.AIR.getDefaultState());
						RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "It's a lie!!!");
						CreeperEntity creeper = EntityType.CREEPER.create(world);
						creeper.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), pos.getX() == 1 ? 90 : -90, 0);
						if(RewardsUtil.rand.nextInt(100) < lieChance)
							creeper.func_241841_a(world, null);
						creeper.addPotionEffect(new EffectInstance(Effects.SPEED, 9999, 2));
						creeper.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 60, 999));
						world.addEntity(creeper);
						RewardsUtil.executeCommand(world, player, player.getPosition(), "/advancement grant @p only chancecubes:its_a_lie");
						Scheduler.removeTask(this);
					}
				}
			});
		}
	}
}