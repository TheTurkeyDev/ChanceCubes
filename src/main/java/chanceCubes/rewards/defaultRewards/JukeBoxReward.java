package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;

public class JukeBoxReward extends BaseCustomReward
{
	private ItemStack[] discs = new ItemStack[]{new ItemStack(Items.MUSIC_DISC_11), new ItemStack(Items.MUSIC_DISC_13), new ItemStack(Items.MUSIC_DISC_BLOCKS), new ItemStack(Items.MUSIC_DISC_CAT), new ItemStack(Items.MUSIC_DISC_CHIRP), new ItemStack(Items.MUSIC_DISC_FAR), new ItemStack(Items.MUSIC_DISC_MALL), new ItemStack(Items.MUSIC_DISC_MELLOHI), new ItemStack(Items.MUSIC_DISC_STAL), new ItemStack(Items.MUSIC_DISC_STRAD), new ItemStack(Items.MUSIC_DISC_WAIT), new ItemStack(Items.MUSIC_DISC_WARD)};

	public JukeBoxReward()
	{
		super(CCubesCore.MODID + ":juke_box", 5);
	}

	@Override
	public void trigger(ServerWorld world, BlockPos pos, PlayerEntity player, Map<String, Object> settings)
	{
		RewardsUtil.placeBlock(Blocks.JUKEBOX.getDefaultState(), world, pos);
		BlockState iblockstate = world.getBlockState(pos);
		ItemStack disc = discs[RewardsUtil.rand.nextInt(discs.length)];
		((JukeboxBlock) Blocks.JUKEBOX).insertRecord(world, pos, iblockstate, disc);
		world.playEvent(null, 1010, pos, Item.getIdFromItem(disc.getItem()));
	}
}