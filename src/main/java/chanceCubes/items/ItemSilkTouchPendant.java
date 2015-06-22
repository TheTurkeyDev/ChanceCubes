package chanceCubes.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import chanceCubes.CCubesCore;

public class ItemSilkTouchPendant extends Item
{
	public ItemSilkTouchPendant()
	{
		this.setUnlocalizedName("silkTouchPendant");
		this.setTextureName(CCubesCore.MODID + ":silkTouchPendant");
		this.setMaxStackSize(1);
		this.setCreativeTab(CCubesCore.modTab);
	}
	
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }
}
