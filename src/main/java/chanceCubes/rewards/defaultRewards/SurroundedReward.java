package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;

public class SurroundedReward extends BaseCustomReward
{
	public SurroundedReward()
	{
		super(CCubesCore.MODID + ":surrounded", -45);
	}

	@Override
	public void trigger(ServerWorld world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		int px = (int) player.getPosX();
		int pz = (int) player.getPosZ();
		EndermanEntity enderman;
		for(int xx = 0; xx < 2; xx++)
		{
			int xValue = px + (xx == 0 ? -4 : 4);
			for(int zz = -4; zz < 5; zz++)
			{

				BlockState blockState = world.getBlockState(new BlockPos(xValue, pos.getY(), pz + zz));
				BlockState blockState2 = world.getBlockState(new BlockPos(xValue, pos.getY() + 1, pz + zz));
				BlockState blockState3 = world.getBlockState(new BlockPos(xValue, pos.getY() + 2, pz + zz));
				if(!blockState.isSolid() && !blockState2.isSolid() && !blockState3.isSolid())
				{
					enderman = EntityType.ENDERMAN.create(world);
					enderman.setLocationAndAngles(xValue, pos.getY(), pos.getZ() + zz, xx == 1 ? 90 : -90, 0);
					world.addEntity(enderman);
				}
			}
		}

		for(int zz = 0; zz < 2; zz++)
		{
			int zValue = pz + (zz == 0 ? -4 : 4);
			for(int xx = -4; xx < 5; xx++)
			{
				BlockState blockState = world.getBlockState(new BlockPos(px + xx, pos.getY(), zValue));
				BlockState blockState2 = world.getBlockState(new BlockPos(px + xx, pos.getY() + 1, zValue));
				BlockState blockState3 = world.getBlockState(new BlockPos(px + xx, pos.getY() + 2, zValue));
				if(!blockState.isSolid() && !blockState2.isSolid() && !blockState3.isSolid())
				{
					enderman = EntityType.ENDERMAN.create(world);
					enderman.setLocationAndAngles(pos.getX() + xx, pos.getY(), zValue, zz == 1 ? 180 : 0, 0);
					world.addEntity(enderman);
				}
			}
		}
	}
}
