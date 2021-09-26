package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.state.BlockState;

public class JukeBoxReward extends BaseCustomReward
{
	private final ItemStack[] discs = new ItemStack[]{new ItemStack(Items.MUSIC_DISC_11), new ItemStack(Items.MUSIC_DISC_13), new ItemStack(Items.MUSIC_DISC_BLOCKS), new ItemStack(Items.MUSIC_DISC_CAT), new ItemStack(Items.MUSIC_DISC_CHIRP), new ItemStack(Items.MUSIC_DISC_FAR), new ItemStack(Items.MUSIC_DISC_MALL), new ItemStack(Items.MUSIC_DISC_MELLOHI), new ItemStack(Items.MUSIC_DISC_STAL), new ItemStack(Items.MUSIC_DISC_STRAD), new ItemStack(Items.MUSIC_DISC_WAIT), new ItemStack(Items.MUSIC_DISC_WARD)};

	public JukeBoxReward()
	{
		super(CCubesCore.MODID + ":juke_box", 5);
	}

	@Override
	public void trigger(ServerLevel level, BlockPos pos, Player player, JsonObject settings)
	{
		RewardsUtil.placeBlock(Blocks.JUKEBOX.defaultBlockState(), level, pos);
		BlockState iblockstate = level.getBlockState(pos);
		ItemStack disc = discs[RewardsUtil.rand.nextInt(discs.length)];
		((JukeboxBlock) Blocks.JUKEBOX).setRecord(level, pos, iblockstate, disc);
		level.playEvent(null, 1010, pos, Item.getId(disc.getItem()));
	}
}