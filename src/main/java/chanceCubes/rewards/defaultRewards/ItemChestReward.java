package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
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

public class ItemChestReward implements IChanceCubeReward
{

	//@formatter:off
	ItemStack[] stacks = new ItemStack[] { new ItemStack(Blocks.GLASS), new ItemStack(Items.APPLE), new ItemStack(Items.BREAD), 
			new ItemStack(Items.CAKE) , new ItemStack(Items.COOKIE), new ItemStack(Items.COOKED_BEEF), new ItemStack(Items.DIAMOND), 
			new ItemStack(Items.EGG), new ItemStack(Items.FEATHER), new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.IRON_SWORD),
			new ItemStack(Items.LEATHER), new ItemStack(Items.EMERALD), new ItemStack(Items.MELON), new ItemStack(Items.OAK_DOOR),
			new ItemStack(Items.PAPER), new ItemStack(Items.POTATO), new ItemStack(Items.PUMPKIN_PIE), new ItemStack(Items.QUARTZ),
			new ItemStack(Items.RECORD_13), new ItemStack(Items.REDSTONE), new ItemStack(Items.REEDS), new ItemStack(Items.SIGN),
			new ItemStack(Items.SLIME_BALL), new ItemStack(Items.SNOWBALL), new ItemStack(Items.SPIDER_EYE), new ItemStack(Items.WHEAT),
			new ItemStack(Items.EXPERIENCE_BOTTLE), new ItemStack(Items.CLAY_BALL), new ItemStack(Items.BLAZE_ROD), 
			new ItemStack(Items.ENDER_PEARL)};
	//@formatter:on

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		world.setBlockState(pos, Blocks.CHEST.getDefaultState());
		TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos);
		Scheduler.scheduleTask(new Task("Item_Chest_Init_Delay", 60)
		{

			@Override
			public void callback()
			{
				spawnItems(world, pos, chest);
				chest.numPlayersUsing++;
				world.addBlockEvent(pos, chest.getBlockType(), 1, chest.numPlayersUsing);
				world.notifyNeighborsOfStateChange(pos, chest.getBlockType());
				world.notifyNeighborsOfStateChange(pos.down(), chest.getBlockType());
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
				world.setBlockToAir(pos);
			}

			@Override
			public void update()
			{
				chest.numPlayersUsing++;
				EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ(), stacks[RewardsUtil.rand.nextInt(stacks.length)].copy());
				world.spawnEntityInWorld(item);
				item.motionX = 0;
				item.motionY = 1.5;
				item.motionZ = -1;
				item.setPickupDelay(60);
			}
		});
	}

	@Override
	public int getChanceValue()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Item_Chest";
	}

}
