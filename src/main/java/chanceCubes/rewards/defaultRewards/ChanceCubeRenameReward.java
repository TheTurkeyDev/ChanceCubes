package chanceCubes.rewards.defaultRewards;

import java.util.Random;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.rewards.IChanceCubeReward;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ChanceCubeRenameReward implements IChanceCubeReward
{
	private Random rand = new Random();

	// @formatter:off
	private String[] chanceSyn = {"Lucky", "Fortune", "Unforseen", "Probabalistic", "Favored", 
			"Charmed", "Auspicious", "Advantageous"};
	
	private String[] cubeSyn = {"Blocks", "Squares", "Boxes", "Bricks", "Hunks", "Solids"};
	
	// @formatter:on

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		ItemStack stack = new ItemStack(CCubesBlocks.CHANCE_CUBE, 2);
		String name = chanceSyn[rand.nextInt(chanceSyn.length)];
		String adj = cubeSyn[rand.nextInt(cubeSyn.length)];

		String newName = name + " " + adj;
		stack.setStackDisplayName(newName);

		player.addChatMessage(new TextComponentString("Chance Cubes are sooooo 2017. Here have some " + newName + " instead!"));

		world.spawnEntityInWorld(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
	}

	@Override
	public int getChanceValue()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Cube_Rename";
	}
}
