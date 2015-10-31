package chanceCubes.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
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
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) 
	{
		list.add("Use this pendant to retrieve Chance Cubes");
		list.add("Player must hold this in hand to get the cube!");
	}
}
