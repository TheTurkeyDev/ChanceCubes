package chanceCubes.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemCreativePendant extends BaseChanceCubesItem
{
	public ItemCreativePendant()
	{
		super((new Item.Properties()).stacksTo(1), "creative_pendant");
		super.addLore("Right click to change the chance");
		super.addLore("of the inserted cubes.");
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if(level.isClientSide())
			return new InteractionResultHolder<>(InteractionResult.PASS, stack);

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
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}
}