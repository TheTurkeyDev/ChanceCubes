package chanceCubes.items;

import chanceCubes.client.gui.CreativePendantGui;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemCreativePendant extends BaseChanceCubesItem
{
	public ItemCreativePendant()
	{
		super((new Item.Properties()).maxStackSize(1), "creative_pendant");
		super.addLore("Right click to change the chance");
		super.addLore("of the inserted cubes.");
	}

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if(!world.isRemote)
			return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		player.setActiveHand(hand);
		Minecraft.getInstance().displayGuiScreen(new CreativePendantGui(player, world));
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
}