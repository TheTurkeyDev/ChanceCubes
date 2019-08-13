package chanceCubes.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import chanceCubes.containers.CreativePendantContainer;
import chanceCubes.items.CCubesItems;

public class CCubesGuiHandler implements IGuiHandler
{
	public final static int CREATIVE_PENDANT_ID = 0;

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID == CREATIVE_PENDANT_ID && player.inventory.getCurrentItem().getItem().equals(CCubesItems.creativePendant))
			return new CreativePendantGui(player, world);
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID == CREATIVE_PENDANT_ID && player.inventory.getCurrentItem().getItem().equals(CCubesItems.creativePendant))
			return new CreativePendantContainer(player.inventory, world);
		return null;
	}
}
