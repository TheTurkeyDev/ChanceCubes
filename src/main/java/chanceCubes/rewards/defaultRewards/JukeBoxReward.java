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
	private ItemStack[] discs = new ItemStack[] { new ItemStack(Items.record_11), new ItemStack(Items.record_13), new ItemStack(Items.record_blocks), new ItemStack(Items.record_cat), new ItemStack(Items.record_chirp), new ItemStack(Items.record_far), new ItemStack(Items.record_mall), new ItemStack(Items.record_mellohi), new ItemStack(Items.record_stal), new ItemStack(Items.record_strad), new ItemStack(Items.record_wait), new ItemStack(Items.record_ward) };

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		world.setBlockState(pos, Blocks.jukebox.getDefaultState());
		IBlockState iblockstate = world.getBlockState(pos);
		ItemStack disc = discs[random.nextInt(discs.length)];
		((BlockJukebox) Blocks.jukebox).insertRecord(world, pos, iblockstate, disc);
		world.playAuxSFXAtEntity((EntityPlayer) null, 1010, pos, Item.getIdFromItem(disc.getItem()));
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