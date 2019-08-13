package chanceCubes.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemCreativePendant extends BaseChanceCubesItem
{
	public ItemCreativePendant()
	{
		super((new Item.Properties()).maxStackSize(1), "creative_pendant");
		super.addLore("Right click to change the chance");
		super.addLore("of the inserted cubes.");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if(world.isRemote)
			return new ActionResult<>(ActionResultType.PASS, stack);

		//TODO Reimplement
//		NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider()
//		{
//			@Override
//			public ITextComponent getDisplayName()
//			{
//				return new StringTextComponent("Creative Pendant");
//			}
//
//			@Override
//			public Container createMenu(int p_createMenu_1_, PlayerInventory inv, PlayerEntity player)
//			{
//				return new CreativePendantContainer(0, inv);
//			}
//		});
		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}
}