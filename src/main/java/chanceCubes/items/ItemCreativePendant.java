package chanceCubes.items;

import chanceCubes.CCubesCore;
import chanceCubes.client.gui.CCubesGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class ItemCreativePendant extends BaseChanceCubesItem
{
	public ItemCreativePendant()
	{
		super((new Item.Builder()).maxStackSize(1), "creative_Pendant");
		super.addLore("Right click to change the chance");
		super.addLore("of the inserted cubes.");
	}

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		player.setActiveHand(hand);
		FMLNetworkHandler.openGui(player, CCubesCore.instance, CCubesGuiHandler.CREATIVE_PENDANT_ID, world, (int) player.posX, (int) player.posY, (int) player.posZ);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
}