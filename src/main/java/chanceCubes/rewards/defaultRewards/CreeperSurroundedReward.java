package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.mcwrapper.BlockWrapper;
import chanceCubes.mcwrapper.EntityWrapper;
import chanceCubes.util.RewardsUtil;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;

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
					BlockPos pos1 = new BlockPos(xValue, pos.getY(), pz + zz);
					BlockPos pos2 = new BlockPos(xValue, pos.getY() + 1, pz + zz);
					BlockPos pos3 = new BlockPos(xValue, pos.getY() + 2, pz + zz);
					if(!BlockWrapper.isBlockSolid(level, pos1) && !BlockWrapper.isBlockSolid(level, pos2) && !BlockWrapper.isBlockSolid(level, pos3))
					{
						creeper = EntityWrapper.spawnEntityAt(EntityType.CREEPER, level, xValue, pos.getY(), pos.getZ() + zz);
						creeper.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 5));
						if(RewardsUtil.rand.nextInt(100) < chargedChance)
							EntityWrapper.setCreeperPowered(creeper);
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
					BlockPos pos1 = new BlockPos(px + xx, pos.getY(), zValue);
					BlockPos pos2 = new BlockPos(px + xx, pos.getY() + 1, zValue);
					BlockPos pos3 = new BlockPos(px + xx, pos.getY() + 2, zValue);
					if(!BlockWrapper.isBlockSolid(level, pos1) && !BlockWrapper.isBlockSolid(level, pos2) && !BlockWrapper.isBlockSolid(level, pos3))
					{
						creeper = EntityWrapper.spawnEntityAt(EntityType.CREEPER, level, pos.getX() + xx, pos.getY(), zValue);
						creeper.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 5));
						if(RewardsUtil.rand.nextInt(100) < chargedChance)
							EntityWrapper.setCreeperPowered(creeper);
					}
				}
				skip = !skip;
			}
		}
	}
}
