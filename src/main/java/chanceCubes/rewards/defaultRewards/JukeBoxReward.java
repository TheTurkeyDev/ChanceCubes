package chanceCubes.rewards.defaultRewards;

import java.util.Random;

import chanceCubes.CCubesCore;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class JukeBoxReward implements IChanceCubeReward
{
	private Random random = new Random();
	private ItemStack[] discs = new ItemStack[] { new ItemStack(Items.RECORD_11), new ItemStack(Items.RECORD_13), new ItemStack(Items.RECORD_BLOCKS), new ItemStack(Items.RECORD_CAT), new ItemStack(Items.RECORD_CHIRP), new ItemStack(Items.RECORD_FAR), new ItemStack(Items.RECORD_MALL), new ItemStack(Items.RECORD_MELLOHI), new ItemStack(Items.RECORD_STAL), new ItemStack(Items.RECORD_STRAD), new ItemStack(Items.RECORD_WAIT), new ItemStack(Items.RECORD_WARD) };

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		world.setBlockState(pos, Blocks.JUKEBOX.getDefaultState());
		IBlockState iblockstate = world.getBlockState(pos);
		ItemStack disc = discs[random.nextInt(discs.length)];
		((BlockJukebox) Blocks.JUKEBOX).insertRecord(world, pos, iblockstate, disc);
		world.playEvent((EntityPlayer) null, 1010, pos, Item.getIdFromItem(disc.getItem()));
	}

	@Override
	public int getChanceValue()
	{
		return 5;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Juke_Box";
	}

}