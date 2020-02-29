package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Map;

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
	public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		ItemStack stack = new ItemStack(CCubesBlocks.CHANCE_CUBE, 2);
		String[] allChanceSyn = ArrayUtils.addAll(chanceSyn, super.getSettingAsStringList(settings, "chance_syn", new String[0]));
		String name = allChanceSyn[RewardsUtil.rand.nextInt(allChanceSyn.length)];
		String[] allCubeSyn = ArrayUtils.addAll(cubeSyn, super.getSettingAsStringList(settings, "cube_syn", new String[0]));
		String adj = allCubeSyn[RewardsUtil.rand.nextInt(allCubeSyn.length)];

		String newName = name + " " + adj;
		stack.setDisplayName(new StringTextComponent(newName));

		player.sendMessage(new StringTextComponent("Chance Cubes are sooooo 2017. Here have some " + newName + " instead!"));

		world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack));
	}
}