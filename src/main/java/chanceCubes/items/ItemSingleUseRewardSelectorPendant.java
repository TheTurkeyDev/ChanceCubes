package chanceCubes.items;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.gui.RewardSelectorPendantGui;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.registry.GiantCubeRegistry;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.tileentities.TileGiantCube;
import chanceCubes.util.GiantCubeUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ItemSingleUseRewardSelectorPendant extends BaseChanceCubesItem
{

	public ItemSingleUseRewardSelectorPendant()
	{
		super("single_use_reward_selector_pendant");
		this.setMaxStackSize(1);
		super.addLore("Right click a Chance Cube to summon the reward.");
	}

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		player.setActiveHand(hand);
		ItemStack stack = player.getHeldItem(hand);
		if(player.isSneaking() && world.isRemote && player.isCreative())
			FMLCommonHandler.instance().showGuiScreen(new RewardSelectorPendantGui(player, stack));
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		if(player.isSneaking())
			return EnumActionResult.FAIL;
		if(world.isRemote)
			return EnumActionResult.PASS;
		if(stack.getTagCompound() != null && stack.getTagCompound().hasKey("Reward"))
		{
			if(world.getBlockState(pos).getBlock().equals(CCubesBlocks.CHANCE_CUBE))
			{
				world.setBlockToAir(pos);
				IChanceCubeReward reward = ChanceCubeRegistry.INSTANCE.getRewardByName(stack.getTagCompound().getString("Reward"));
				if(reward != null)
				{
					reward.trigger(world, pos, player);
					player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
				}
				else
				{
					player.sendMessage(new TextComponentString("That reward does not exist for this cube!"));

				}
			}
			else if(world.getBlockState(pos).getBlock().equals(CCubesBlocks.GIANT_CUBE))
			{
				TileEntity ent = world.getTileEntity(pos);
				if(ent == null || !(ent instanceof TileGiantCube))
					return EnumActionResult.FAIL;
				TileGiantCube giant = (TileGiantCube) ent;
				IChanceCubeReward reward = GiantCubeRegistry.INSTANCE.getRewardByName(stack.getTagCompound().getString("Reward"));
				if(reward != null)
				{
					reward.trigger(world, giant.getMasterPostion(), player);
					GiantCubeUtil.removeStructure(giant.getMasterPostion(), world);
					player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
				}
				else
				{
					player.sendMessage(new TextComponentString("That reward does not exist for this cube!"));
				}

			}
		}
		return EnumActionResult.SUCCESS;
	}
}