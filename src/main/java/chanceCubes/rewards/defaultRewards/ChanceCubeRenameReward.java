package chanceCubes.rewards.defaultRewards;

import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ChanceCubeRenameReward extends BaseCustomReward
{

	// @formatter:off
	private String[] chanceSyn = {"Lucky", "Fortune", "Unforseen", "Probabalistic", "Favored", 
			"Charmed", "Auspicious", "Advantageous"};
	
	private String[] cubeSyn = {"Blocks", "Squares", "Boxes", "Bricks", "Hunks", "Solids"};
	
	// @formatter:on
	public ChanceCubeRenameReward()
	{
		super(CCubesCore.MODID + ":Cube_Rename", 0);
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
	{
		ItemStack stack = new ItemStack(CCubesBlocks.CHANCE_CUBE, 2);
		String[] allChanceSyn = ArrayUtils.addAll(chanceSyn, super.getSettingAsStringList(settings, "chance_syn", new String[0]));
		String name = allChanceSyn[RewardsUtil.rand.nextInt(allChanceSyn.length)];
		String[] allCubeSyn = ArrayUtils.addAll(cubeSyn, super.getSettingAsStringList(settings, "cube_syn", new String[0]));
		String adj = allCubeSyn[RewardsUtil.rand.nextInt(allCubeSyn.length)];

		String newName = name + " " + adj;
		stack.setStackDisplayName(newName);

		player.sendMessage(new TextComponentString("Chance Cubes are sooooo 2017. Here have some " + newName + " instead!"));

		world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
	}
}