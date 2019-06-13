package chanceCubes.rewards.defaultRewards;

import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemChestReward extends BaseCustomReward
{
	//@formatter:off
	ItemStack[] stacks = new ItemStack[] { new ItemStack(Blocks.GLASS), new ItemStack(Items.APPLE), new ItemStack(Items.BREAD), 
			new ItemStack(Blocks.CAKE) , new ItemStack(Items.COOKIE), new ItemStack(Items.COOKED_BEEF), new ItemStack(Items.DIAMOND), 
			new ItemStack(Items.EGG), new ItemStack(Items.FEATHER), new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.IRON_SWORD),
			new ItemStack(Items.LEATHER), new ItemStack(Items.EMERALD), new ItemStack(Items.MELON_SLICE), new ItemStack(Blocks.OAK_DOOR),
			new ItemStack(Items.PAPER), new ItemStack(Items.POTATO), new ItemStack(Items.PUMPKIN_PIE), new ItemStack(Items.QUARTZ),
			new ItemStack(Items.MUSIC_DISC_13), new ItemStack(Items.REDSTONE), new ItemStack(Blocks.SUGAR_CANE), new ItemStack(Items.SIGN),
			new ItemStack(Items.SLIME_BALL), new ItemStack(Items.SNOWBALL), new ItemStack(Items.SPIDER_EYE), new ItemStack(Items.WHEAT),
			new ItemStack(Items.EXPERIENCE_BOTTLE), new ItemStack(Items.CLAY_BALL), new ItemStack(Items.BLAZE_ROD), 
			new ItemStack(Items.ENDER_PEARL)};
	//@formatter:on

	public ItemChestReward()
	{
		super(CCubesCore.MODID + ":Item_Chest", 25);
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
	{
		world.setBlockState(pos, Blocks.CHEST.getDefaultState());
		TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos);
		Scheduler.scheduleTask(new Task("Item_Chest_Init_Delay", 60)
		{

			@Override
			public void callback()
			{
				spawnItems(world, pos, chest);
				chest.openInventory(player);
				//				world.addBlockEvent(pos, chest.getType(), 1, TileEntityChest.getPlayersUsing(world, pos));
				//				world.notifyNeighborsOfStateChange(pos, chest.getType(), true);
				//				world.notifyNeighborsOfStateChange(pos.down(), chest.getType(), true);
			}
		});
	}

	public void spawnItems(World world, BlockPos pos, TileEntityChest chest)
	{
		Scheduler.scheduleTask(new Task("Item_Chest_Squids", 250, 5)
		{
			@Override
			public void callback()
			{
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
			}

			@Override
			public void update()
			{
				EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ(), stacks[RewardsUtil.rand.nextInt(stacks.length)].copy());
				world.spawnEntity(item);
				item.motionX = 0;
				item.motionY = 1.5;
				item.motionZ = -1;
				item.setPickupDelay(60);
			}
		});
	}
}