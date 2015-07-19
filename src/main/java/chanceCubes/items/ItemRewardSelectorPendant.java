package chanceCubes.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.gui.RewardSelectorPendantGui;
import chanceCubes.registry.ChanceCubeRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRewardSelectorPendant  extends Item
{
	public ItemRewardSelectorPendant()
	{
		this.setUnlocalizedName("reward_Selector_Pendant");
		this.setTextureName(CCubesCore.MODID + ":RewardSelectorPendant");
		this.setMaxStackSize(1);
		this.setCreativeTab(CCubesCore.modTab);
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if(player.isSneaking() && world.isRemote)
			FMLCommonHandler.instance().showGuiScreen(new RewardSelectorPendantGui(player, stack));
		return stack;
	}

	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if(world.isRemote)
			return false;
		if(player.isSneaking())
			return false;
		if(stack.getTagCompound() != null && stack.getTagCompound().hasKey("Reward"))
		{
			if(world.getBlock(x, y, z).equals(CCubesBlocks.chanceCube))
			{
				world.setBlockToAir(x, y, z);
				ChanceCubeRegistry.INSTANCE.getRewardByName(stack.getTagCompound().getString("Reward")).trigger(world, x, y, z, player);
			}
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) 
	{
		list.add("Shift right click to change the reward that this item will spawn in.");
		list.add("Right a lucky block to summon the reward.");
	}
}
