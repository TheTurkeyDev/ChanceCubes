package chanceCubes.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.client.gui.CCubesGuiHandler;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCreativePendant extends Item
{
	public ItemCreativePendant()
	{
		this.setUnlocalizedName("creativePendant");
		this.setTextureName(CCubesCore.MODID + ":CreativePendant");
		this.setMaxStackSize(1);
		this.setCreativeTab(CCubesCore.modTab);
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		FMLNetworkHandler.openGui(player, CCubesCore.instance, CCubesGuiHandler.CREATIVE_PENDANT_ID, world, (int)player.posX, (int)player.posY, (int)player.posZ);
		return stack;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) 
	{
		list.add("Right click to change the chance the pendant sets chance cubes to.");
	}
}
