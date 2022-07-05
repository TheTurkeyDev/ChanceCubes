package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.mcwrapper.ComponentWrapper;
import chanceCubes.util.RewardsUtil;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.ArrayUtils;

public class ChanceCubeRenameReward extends BaseCustomReward
{

	// @formatter:off
	private final String[] chanceSyn = {"Lucky", "Fortune", "Unforseen", "Probabalistic", "Favored",
			"Charmed", "Auspicious", "Advantageous", "Random"};
	
	private final String[] cubeSyn = {"Blocks", "Squares", "Boxes", "Bricks", "Hunks", "Solids", "Voxels"};
	
	// @formatter:on
	public ChanceCubeRenameReward()
	{
		super(CCubesCore.MODID + ":cube_rename", 0);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		ItemStack stack = new ItemStack(CCubesBlocks.CHANCE_CUBE.get(), 2);
		String[] allChanceSyn = ArrayUtils.addAll(chanceSyn, super.getSettingAsStringList(settings, "chanceSynonym", new String[0]));
		String name = allChanceSyn[RewardsUtil.rand.nextInt(allChanceSyn.length)];
		String[] allCubeSyn = ArrayUtils.addAll(cubeSyn, super.getSettingAsStringList(settings, "cubeSynonym", new String[0]));
		String adj = allCubeSyn[RewardsUtil.rand.nextInt(allCubeSyn.length)];

		String newName = name + " " + adj;
		stack.setHoverName(ComponentWrapper.string(newName));

		RewardsUtil.sendMessageToPlayer(player, "Chance Cubes are sooooo 2017. Here have some " + newName + " instead!");

		level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack));
	}
}