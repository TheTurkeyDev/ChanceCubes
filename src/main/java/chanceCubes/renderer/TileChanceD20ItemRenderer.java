package chanceCubes.renderer;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.tileentities.TileChanceD20;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TileChanceD20ItemRenderer extends TileEntityItemStackRenderer
{

	private TileChanceD20 tileEntity = new TileChanceD20();

	public void func_192838_a(ItemStack stack, float p_192838_2_)
	{
		if(tileEntity.getWorld() == null)
			tileEntity.setWorld(Minecraft.getMinecraft().world);
		if(stack.getItem().equals(Item.getItemFromBlock(CCubesBlocks.CHANCE_ICOSAHEDRON)))
		{
			TileChanceD20Renderer.INSTANCE.render(tileEntity, 0, 0, 0, p_192838_2_, 0, 0);
		}

	}
}
