package chanceCubes.items;

import chanceCubes.CCubesCore;
import chanceCubes.client.gui.CCubesGuiHandler;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCreativePendant extends BaseChanceCubesItem
{
	public ItemCreativePendant()
	{
		super("creativePendant");
		this.setMaxStackSize(1);
		super.addLore("Right click to change the chance");
		super.addLore("of the inserted cubes.");
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		FMLNetworkHandler.openGui(player, CCubesCore.instance, CCubesGuiHandler.CREATIVE_PENDANT_ID, world, (int) player.posX, (int) player.posY, (int) player.posZ);
		return stack;
	}
}