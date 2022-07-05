package chanceCubes.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemSilkTouchPendant extends BaseChanceCubesItem
{
	public ItemSilkTouchPendant()
	{
		super((new Item.Properties()).stacksTo(1));
		super.addLore("Use this pendant to retrieve Chance Cubes");
		super.addLore("Player must hold this in hand to get the cube!");
	}

	@Override
	public boolean isFoil(ItemStack stack)
	{
		return true;
	}
}
