package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

public class ItemChestReward extends BaseCustomReward
{
	public ItemChestReward()
	{
		super(CCubesCore.MODID + ":item_chest", 25);
	}

	//@formatter:off
	private final ItemStack[] stacks = new ItemStack[] { new ItemStack(Blocks.GLASS), new ItemStack(Items.APPLE), new ItemStack(Items.BREAD),
			new ItemStack(Items.CAKE) , new ItemStack(Items.COOKIE), new ItemStack(Items.COOKED_BEEF), new ItemStack(Items.DIAMOND),
			new ItemStack(Items.EGG), new ItemStack(Items.FEATHER), new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.IRON_SWORD),
			new ItemStack(Items.LEATHER), new ItemStack(Items.EMERALD), new ItemStack(Items.MELON), new ItemStack(Items.OAK_DOOR),
			new ItemStack(Items.PAPER), new ItemStack(Items.POTATO), new ItemStack(Items.PUMPKIN_PIE), new ItemStack(Items.QUARTZ),
			new ItemStack(Items.MUSIC_DISC_13), new ItemStack(Items.REDSTONE), new ItemStack(Items.SUGAR_CANE), new ItemStack(Items.OAK_SIGN),
			new ItemStack(Items.SLIME_BALL), new ItemStack(Items.SNOWBALL), new ItemStack(Items.SPIDER_EYE), new ItemStack(Items.WHEAT),
			new ItemStack(Items.EXPERIENCE_BOTTLE), new ItemStack(Items.CLAY_BALL), new ItemStack(Items.BLAZE_ROD), 
			new ItemStack(Items.ENDER_PEARL)};
	//@formatter:on

	@Override
	public void trigger(ServerLevel world, BlockPos pos, Player player, JsonObject settings)
	{
		world.setBlockAndUpdate(pos, Blocks.CHEST.defaultBlockState());
		ChestBlockEntity chest = (ChestBlockEntity) world.getBlockEntity(pos);
		Scheduler.scheduleTask(new Task("Item_Chest_Init_Delay", 60)
		{
			@Override
			public void callback()
			{
				spawnItems(world, pos, chest);
				chest.startOpen(player);
			}
		});
	}

	public void spawnItems(ServerLevel world, BlockPos pos, ChestBlockEntity chest)
	{
		Scheduler.scheduleTask(new Task("Item_Chest_Squids", 250, 5)
		{
			@Override
			public void callback()
			{
				world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			}

			@Override
			public void update()
			{
				ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ(), stacks[RewardsUtil.rand.nextInt(stacks.length)].copy());
				world.addFreshEntity(item);
				item.setDeltaMovement(0, 1.5, (RewardsUtil.rand.nextDouble() * -2) - 1);
				item.setPickUpDelay(60);
			}
		});
	}
}