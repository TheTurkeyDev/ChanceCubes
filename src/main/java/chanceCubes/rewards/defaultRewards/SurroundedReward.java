package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class SurroundedReward extends BaseCustomReward
{
	public SurroundedReward()
	{
		super(CCubesCore.MODID + ":surrounded", -45);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		int px = (int) player.getX();
		int pz = (int) player.getZ();
		EnderMan enderman;
		for(int xx = 0; xx < 2; xx++)
		{
			int xValue = px + (xx == 0 ? -4 : 4);
			for(int zz = -4; zz < 5; zz++)
			{

				BlockState blockState = level.getBlockState(new BlockPos(xValue, pos.getY(), pz + zz));
				BlockState blockState2 = level.getBlockState(new BlockPos(xValue, pos.getY() + 1, pz + zz));
				BlockState blockState3 = level.getBlockState(new BlockPos(xValue, pos.getY() + 2, pz + zz));
				if(!blockState.isSolid() && !blockState2.isSolid() && !blockState3.isSolid())
				{
					enderman = EntityType.ENDERMAN.create(level);
					enderman.moveTo(xValue, pos.getY(), pos.getZ() + zz, xx == 1 ? 90 : -90, 0);
					level.addFreshEntity(enderman);
				}
			}
		}

		for(int zz = 0; zz < 2; zz++)
		{
			int zValue = pz + (zz == 0 ? -4 : 4);
			for(int xx = -4; xx < 5; xx++)
			{
				BlockState blockState = level.getBlockState(new BlockPos(px + xx, pos.getY(), zValue));
				BlockState blockState2 = level.getBlockState(new BlockPos(px + xx, pos.getY() + 1, zValue));
				BlockState blockState3 = level.getBlockState(new BlockPos(px + xx, pos.getY() + 2, zValue));
				if(!blockState.isSolid() && !blockState2.isSolid() && !blockState3.isSolid())
				{
					enderman = EntityType.ENDERMAN.create(level);
					enderman.moveTo(pos.getX() + xx, pos.getY(), zValue, zz == 1 ? 180 : 0, 0);
					level.addFreshEntity(enderman);
				}
			}
		}
	}
}
