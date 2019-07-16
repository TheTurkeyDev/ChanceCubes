package chanceCubes.rewards.defaultRewards;

import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
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
	public void trigger(World world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		ItemStack stack = new ItemStack(CCubesBlocks.CHANCE_CUBE, 2);
		String name = chanceSyn[RewardsUtil.rand.nextInt(chanceSyn.length)];
		String adj = cubeSyn[RewardsUtil.rand.nextInt(cubeSyn.length)];

		String newName = name + " " + adj;
		stack.setDisplayName(new StringTextComponent(newName));

		player.sendMessage(new StringTextComponent("Chance Cubes are sooooo 2017. Here have some " + newName + " instead!"));

		world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack));
	}
}