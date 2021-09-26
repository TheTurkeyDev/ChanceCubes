package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.biodomeGen.BioDomeGen;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

import java.util.Arrays;

public class BioDomeReward extends BaseCustomReward
{
	public BioDomeReward()
	{
		super(CCubesCore.MODID + ":biodome", 0);
	}

	@Override
	public void trigger(final ServerLevel level, final BlockPos pos, Player player, JsonObject settings)
	{
		String[] blacklist = super.getSettingAsStringList(settings, "biomesBlacklist", new String[0]);
		boolean spawnEntities = super.getSettingAsBoolean(settings, "spawnEntities", true);
		(new BioDomeGen(player, Arrays.asList(blacklist))).genRandomDome(pos, level, 25, spawnEntities);
	}
}