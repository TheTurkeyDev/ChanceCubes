package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class CreeperSurroundedReward extends BaseCustomReward
{
	public CreeperSurroundedReward()
	{
		super(CCubesCore.MODID + ":surrounded_creeper", -85);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		final int chargedChance = super.getSettingAsInt(settings, "chargedChance", 10, 0, 100);
		int px = (int) player.getX();
		int pz = (int) player.getZ();
		player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 1, true, false));
		boolean skip = false;
		Creeper creeper;
		for(int xx = 0; xx < 2; xx++)
		{
			int xValue = px + (xx == 0 ? -4 : 4);
			for(int zz = -4; zz < 5; zz++)
			{
				if(!skip)
				{
					BlockState blockState = level.getBlockState(new BlockPos(xValue, pos.getY(), pz + zz));
					BlockState blockState2 = level.getBlockState(new BlockPos(xValue, pos.getY() + 1, pz + zz));
					BlockState blockState3 = level.getBlockState(new BlockPos(xValue, pos.getY() + 2, pz + zz));
					if(!blockState.isSolid() && !blockState2.isSolid() && !blockState3.isSolid())
					{
						creeper = EntityType.CREEPER.create(level);
						creeper.moveTo(xValue, pos.getY(), pos.getZ() + zz, xx == 1 ? 90 : -90, 0);
						creeper.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 5));
						if(RewardsUtil.rand.nextInt(100) < chargedChance)
							creeper.thunderHit(level, null);
						level.addFreshEntity(creeper);
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
					BlockState blockState = level.getBlockState(new BlockPos(px + xx, pos.getY(), zValue));
					BlockState blockState2 = level.getBlockState(new BlockPos(px + xx, pos.getY() + 1, zValue));
					BlockState blockState3 = level.getBlockState(new BlockPos(px + xx, pos.getY() + 2, zValue));
					if(!blockState.isSolid() && !blockState2.isSolid() && !blockState3.isSolid())
					{
						creeper = EntityType.CREEPER.create(level);
						creeper.moveTo(pos.getX() + xx, pos.getY(), zValue, zz == 1 ? 180 : 0, 0);
						creeper.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 5));
						if(RewardsUtil.rand.nextInt(100) < chargedChance)
							creeper.thunderHit(level, null);
						level.addFreshEntity(creeper);
					}
				}
				skip = !skip;
			}
		}
	}
}
