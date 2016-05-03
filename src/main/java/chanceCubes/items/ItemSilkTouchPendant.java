package chanceCubes.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSilkTouchPendant extends BaseChanceCubesItem
{
	public String itemNameID = "silk_Touch_Pendant";

	public ItemSilkTouchPendant()
	{
		super("silk_Touch_Pendant");
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
