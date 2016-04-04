package chanceCubes.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import chanceCubes.CCubesCore;

public class ItemSilkTouchPendant extends Item
{
	public String itemNameID = "silk_Touch_Pendant";
	
	public ItemSilkTouchPendant()
	{
		this.setUnlocalizedName(CCubesCore.MODID + "_" + itemNameID);
		this.setMaxStackSize(1);
		this.setCreativeTab(CCubesCore.modTab);
		this.setRegistryName(CCubesCore.MODID, this.itemNameID);
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
