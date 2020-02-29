package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public class CreeperSurroundedReward extends BaseCustomReward
{
	public CreeperSurroundedReward()
	{
		super(CCubesCore.MODID + ":surrounded_creeper", -85);
	}

	@Override
	public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		final int chargedChance = super.getSettingAsInt(settings, "charged_chance", 10, 0, 100);
		int px = (int) player.posX;
		int pz = (int) player.posZ;
		player.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 100, 1, true, false));
		boolean skip = false;
		CreeperEntity creeper;
		for(int xx = 0; xx < 2; xx++)
		{
			int xValue = px + (xx == 0 ? -4 : 4);
			for(int zz = -4; zz < 5; zz++)
			{
				if(!skip)
				{
					BlockState blockState = world.getBlockState(new BlockPos(xValue, pos.getY(), pz + zz));
					BlockState blockState2 = world.getBlockState(new BlockPos(xValue, pos.getY() + 1, pz + zz));
					BlockState blockState3 = world.getBlockState(new BlockPos(xValue, pos.getY() + 2, pz + zz));
					if(!blockState.isSolid() && !blockState2.isSolid() && !blockState3.isSolid())
					{
						creeper = EntityType.CREEPER.create(world);
						creeper.setLocationAndAngles(xValue, pos.getY(), pos.getZ() + zz, xx == 1 ? 90 : -90, 0);
						creeper.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 60, 5));
						if(RewardsUtil.rand.nextInt(100) < chargedChance)
							creeper.onStruckByLightning(null);
						world.addEntity(creeper);
					}
				}
				skip = !skip;
			}
		}

		for(int zz = 0; zz < 2; zz++)
		{
			int zValue = pz + (zz == 0 ? -4 : 4);
			for(int xx = -4; xx < 5; xx++)
			{
				if(!skip)
				{
					BlockState blockState = world.getBlockState(new BlockPos(px + xx, pos.getY(), zValue));
					BlockState blockState2 = world.getBlockState(new BlockPos(px + xx, pos.getY() + 1, zValue));
					BlockState blockState3 = world.getBlockState(new BlockPos(px + xx, pos.getY() + 2, zValue));
					if(!blockState.isSolid() && !blockState2.isSolid() && !blockState3.isSolid())
					{
						creeper = EntityType.CREEPER.create(world);
						creeper.setLocationAndAngles(pos.getX() + xx, pos.getY(), zValue, zz == 1 ? 180 : 0, 0);
						creeper.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 60, 5));
						if(RewardsUtil.rand.nextInt(100) < chargedChance)
							creeper.onStruckByLightning(null);
						world.addEntity(creeper);
					}
				}
				skip = !skip;
			}
		}
	}
}
