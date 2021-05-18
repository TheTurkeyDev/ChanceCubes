package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.util.RewardsUtil;
import com.google.gson.JsonObject;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import org.apache.commons.lang3.ArrayUtils;

public class ChanceCubeRenameReward extends BaseCustomReward
{

	// @formatter:off
	private String[] chanceSyn = {"Lucky", "Fortune", "Unforseen", "Probabalistic", "Favored", 
			"Charmed", "Auspicious", "Advantageous", "Random"};
	
	private String[] cubeSyn = {"Blocks", "Squares", "Boxes", "Bricks", "Hunks", "Solids", "Voxels"};
	
	// @formatter:on
	public ChanceCubeRenameReward()
	{
		super(CCubesCore.MODID + ":cube_rename", 0);
	}

	@Override
	public void trigger(ServerWorld world, BlockPos pos, PlayerEntity player, JsonObject settings)
	{
		ItemStack stack = new ItemStack(CCubesBlocks.CHANCE_CUBE, 2);
		String[] allChanceSyn = ArrayUtils.addAll(chanceSyn, super.getSettingAsStringList(settings, "chanceSynonym", new String[0]));
		String name = allChanceSyn[RewardsUtil.rand.nextInt(allChanceSyn.length)];
		String[] allCubeSyn = ArrayUtils.addAll(cubeSyn, super.getSettingAsStringList(settings, "cubeSynonym", new String[0]));
		String adj = allCubeSyn[RewardsUtil.rand.nextInt(allCubeSyn.length)];

		String newName = name + " " + adj;
		stack.setDisplayName(new StringTextComponent(newName));

		RewardsUtil.sendMessageToPlayer(player, "Chance Cubes are sooooo 2017. Here have some " + newName + " instead!");

		world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack));
	}
}