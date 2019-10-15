package chanceCubes.rewards.giantRewards;

import java.util.Arrays;
import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.biodomeGen.BioDomeGen;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BioDomeReward extends BaseCustomReward
{
	public BioDomeReward()
	{
		super(CCubesCore.MODID + ":BioDome", 0);
	}

	@Override
	public void trigger(final World world, final BlockPos pos, EntityPlayer player, Map<String, Object> settings)
	{
		String[] blacklist = super.getSettingAsStringList(settings, "biomes_blacklist", new String[0]);
		boolean spawnEntities = super.getSettingAsBoolean(settings, "spawn_entities", true);
		(new BioDomeGen(player, Arrays.asList(blacklist))).genRandomDome(pos, world, 25, spawnEntities);
	}
}