package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.biodomeGen.BioDomeGen;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Arrays;

public class BioDomeReward extends BaseCustomReward
{
	public BioDomeReward()
	{
		super(CCubesCore.MODID + ":biodome", 0);
	}

	@Override
	public void trigger(final ServerWorld world, final BlockPos pos, PlayerEntity player, JsonObject settings)
	{
		String[] blacklist = super.getSettingAsStringList(settings, "biomesBlacklist", new String[0]);
		boolean spawnEntities = super.getSettingAsBoolean(settings, "spawnEntities", true);
		(new BioDomeGen(player, Arrays.asList(blacklist))).genRandomDome(pos, world, 25, spawnEntities);
	}
}