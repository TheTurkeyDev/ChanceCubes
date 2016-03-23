package chanceCubes.items;

import java.util.List;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.gui.RewardSelectorPendantGui;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.registry.GiantCubeRegistry;
import chanceCubes.rewards.defaultRewards.IChanceCubeReward;
import chanceCubes.tileentities.TileGiantCube;
import chanceCubes.util.GiantCubeUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRewardSelectorPendant extends Item
{
	public String itemNameID = "reward_Selector_Pendant";

	public ItemRewardSelectorPendant()
	{
		this.setUnlocalizedName(CCubesCore.MODID + "_" + itemNameID);
		this.setMaxStackSize(1);
		this.setCreativeTab(CCubesCore.modTab);
	}

	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
	{
		player.setActiveHand(hand);
		if(player.isSneaking() && world.isRemote)
			FMLCommonHandler.instance().showGuiScreen(new RewardSelectorPendantGui(player, stack));
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(world.isRemote)
			return EnumActionResult.FAIL;
		if(player.isSneaking())
			return EnumActionResult.FAIL;
		if(stack.getTagCompound() != null && stack.getTagCompound().hasKey("Reward"))
		{
			if(world.getBlockState(pos).getBlock().equals(CCubesBlocks.chanceCube))
			{
				world.setBlockToAir(pos);
				IChanceCubeReward reward = ChanceCubeRegistry.INSTANCE.getRewardByName(stack.getTagCompound().getString("Reward"));
				if(reward != null)
					reward.trigger(world, pos, player);
				else
					player.addChatMessage(new TextComponentString("That reward does not exist for this cube!"));
			}
			else if(world.getBlockState(pos).getBlock().equals(CCubesBlocks.chanceGiantCube))
			{
				TileEntity ent = world.getTileEntity(pos);
				if(ent == null || !(ent instanceof TileGiantCube))
					return EnumActionResult.FAIL;
				TileGiantCube giant = (TileGiantCube) ent;
				IChanceCubeReward reward = GiantCubeRegistry.INSTANCE.getRewardByName(stack.getTagCompound().getString("Reward"));
				if(reward != null)
					reward.trigger(world, giant.getMasterPostion(), player);
				else
					player.addChatMessage(new TextComponentString("That reward does not exist for this cube!"));
				GiantCubeUtil.removeStructure(giant.getMasterPostion(), world);
			}
		}
		return EnumActionResult.PASS;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		list.add("Shift right click to change the reward.");
		list.add("Right click a Chance Cube to summon the reward.");
	}
}
