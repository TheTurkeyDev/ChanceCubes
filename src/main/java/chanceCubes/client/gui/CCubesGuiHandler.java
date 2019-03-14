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
		switch(ID)
		{
			case CREATIVE_PENDANT_ID:
				if(player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem().equals(CCubesItems.creativePendant))
					return new CreativePendantGui(player, world);
				break;
			default:
				return null;
		}
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch(ID)
		{
			case CREATIVE_PENDANT_ID:
				if(player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem().equals(CCubesItems.creativePendant))
					return new CreativePendantContainer(player.inventory, world);
				break;
			default:
				return null;
		}
		return null;
	}
}
