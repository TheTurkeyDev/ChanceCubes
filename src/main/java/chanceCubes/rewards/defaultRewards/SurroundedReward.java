package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.mcwrapper.BlockWrapper;
import chanceCubes.mcwrapper.EntityWrapper;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

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
		for(int xx = 0; xx < 2; xx++)
		{
			int xValue = px + (xx == 0 ? -4 : 4);
			for(int zz = -4; zz < 5; zz++)
			{

				BlockPos pos1 = new BlockPos(xValue, pos.getY(), pz + zz);
				BlockPos pos2 = new BlockPos(xValue, pos.getY() + 1, pz + zz);
				BlockPos pos3 = new BlockPos(xValue, pos.getY() + 2, pz + zz);
				if(!BlockWrapper.isBlockSolid(level, pos1) && !BlockWrapper.isBlockSolid(level, pos2) && !BlockWrapper.isBlockSolid(level, pos3))
					EntityWrapper.spawnEntityAt(EntityType.ENDERMAN, level, xValue, pos.getY(), pos.getZ() + zz);
			}
		}

		for(int zz = 0; zz < 2; zz++)
		{
			int zValue = pz + (zz == 0 ? -4 : 4);
			for(int xx = -4; xx < 5; xx++)
			{
				BlockPos pos1 = new BlockPos(px + xx, pos.getY(), zValue);
				BlockPos pos2 = new BlockPos(px + xx, pos.getY() + 1, zValue);
				BlockPos pos3 = new BlockPos(px + xx, pos.getY() + 2, zValue);
				if(!BlockWrapper.isBlockSolid(level, pos1) && !BlockWrapper.isBlockSolid(level, pos2) && !BlockWrapper.isBlockSolid(level, pos3))
					EntityWrapper.spawnEntityAt(EntityType.ENDERMAN, level, pos.getX() + xx, pos.getY(), zValue);
			}
		}
	}
}
