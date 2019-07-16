package chanceCubes.rewards.defaultRewards;

import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class WaitForItReward extends BaseCustomReward
{
	public WaitForItReward()
	{
		super(CCubesCore.MODID + ":Wait_For_It", -30);
	}

	@Override
	public void trigger(final World world, BlockPos pos, final PlayerEntity player, Map<String, Object> settings)
	{
		player.sendMessage(new StringTextComponent("Wait for it......."));

		Scheduler.scheduleTask(new Task("Wait For It", RewardsUtil.rand.nextInt(4000) + 1000)
		{
			@Override
			public void callback()
			{
				int reward = RewardsUtil.rand.nextInt(3);
				player.sendMessage(new StringTextComponent("NOW!"));

				if(reward == 0)
				{
					world.addEntity(new TNTEntity(world, player.posX, player.posY + 1, player.posZ, null));
				}
				else if(reward == 1)
				{
					CreeperEntity ent = EntityType.CREEPER.create(world);
					ent.setLocationAndAngles(player.posX, player.posY + 1, player.posZ, 0, 0);
					ent.onStruckByLightning(null);
					world.addEntity(ent);
				}
				else if(reward == 2)
				{
					RewardsUtil.placeBlock(Blocks.BEDROCK.getDefaultState(), world, new BlockPos(player.posX, player.posY, player.posZ));
				}
				else if(reward == 3)
				{
					RewardsUtil.placeBlock(Blocks.EMERALD_ORE.getDefaultState(), world, new BlockPos(player.posX, player.posY, player.posZ));
				}
				else if(reward == 4)
				{
					ZombieEntity zomb = EntityType.ZOMBIE.create(world);
					zomb.setChild(true);
					zomb.addPotionEffect(new EffectInstance(Effects.SPEED, 100000, 0));
					zomb.addPotionEffect(new EffectInstance(Effects.STRENGTH, 100000, 0));
					world.addEntity(zomb);
				}
			}
		});
	}
}
