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
import net.minecraft.util.EnumFacing;
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

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if(player.isSneaking() && world.isRemote)
			FMLCommonHandler.instance().showGuiScreen(new RewardSelectorPendantGui(player, stack));
		return stack;
	}

	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(world.isRemote)
			return false;
		if(player.isSneaking())
			return false;
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
					return false;
				TileGiantCube giant = (TileGiantCube) ent;
				IChanceCubeReward reward = GiantCubeRegistry.INSTANCE.getRewardByName(stack.getTagCompound().getString("Reward"));
				if(reward != null)
					reward.trigger(world, giant.getMasterPostion(), player);
				else
					player.addChatMessage(new TextComponentString("That reward does not exist for this cube!"));
				GiantCubeUtil.removeStructure(giant.getMasterPostion(), world);
			}
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		list.add("Shift right click to change the reward.");
		list.add("Right click a Chance Cube to summon the reward.");
	}
}
