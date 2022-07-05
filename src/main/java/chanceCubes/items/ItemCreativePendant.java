package chanceCubes.items;

import chanceCubes.client.ClientHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemCreativePendant extends BaseChanceCubesItem
{
	public ItemCreativePendant()
	{
		super((new Item.Properties()).stacksTo(1));
		super.addLore("Right click to change the chance");
		super.addLore("of the inserted cubes.");
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if(level.isClientSide())
			return InteractionResultHolder.pass(stack);

		ClientHelper.openCreativePendantGUI(player, stack);
		return InteractionResultHolder.success(stack);
	}
}