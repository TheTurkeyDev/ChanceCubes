package chanceCubes.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSilkTouchPendant extends BaseChanceCubesItem
{
	public ItemSilkTouchPendant()
	{
		super("silk_touch_pendant");
		this.setMaxStackSize(1);
		super.addLore("Use this pendant to retrieve Chance Cubes");
		super.addLore("Player must hold this in hand to get the cube!");
	}

	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}
}
