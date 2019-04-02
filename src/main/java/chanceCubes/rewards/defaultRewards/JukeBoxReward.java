package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class JukeBoxReward extends BaseCustomReward
{
	private ItemStack[] discs = new ItemStack[] { new ItemStack(Items.MUSIC_DISC_11), new ItemStack(Items.MUSIC_DISC_13), new ItemStack(Items.MUSIC_DISC_BLOCKS), new ItemStack(Items.MUSIC_DISC_CAT), new ItemStack(Items.MUSIC_DISC_CHIRP), new ItemStack(Items.MUSIC_DISC_FAR), new ItemStack(Items.MUSIC_DISC_MALL), new ItemStack(Items.MUSIC_DISC_MELLOHI), new ItemStack(Items.MUSIC_DISC_STAL), new ItemStack(Items.MUSIC_DISC_STRAD), new ItemStack(Items.MUSIC_DISC_WAIT), new ItemStack(Items.MUSIC_DISC_WARD) };

	public JukeBoxReward()
	{
		super(CCubesCore.MODID + ":Juke_Box", 5);
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		RewardsUtil.placeBlock(Blocks.JUKEBOX.getDefaultState(), world, pos);
		IBlockState iblockstate = world.getBlockState(pos);
		ItemStack disc = discs[RewardsUtil.rand.nextInt(discs.length)];
		((BlockJukebox) Blocks.JUKEBOX).insertRecord(world, pos, iblockstate, disc);
		world.playEvent((EntityPlayer) null, 1010, pos, Item.getIdFromItem(disc.getItem()));
	}
}