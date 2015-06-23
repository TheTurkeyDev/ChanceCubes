package chanceCubes.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import chanceCubes.containers.CreativePendantContainer;
import chanceCubes.items.CCubesItems;
import cpw.mods.fml.common.network.IGuiHandler;

public class CCubesGuiHandler implements IGuiHandler
{
	public final static int CREATIVE_PENDANT_ID = 0;

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch (ID)
		{
			case CREATIVE_PENDANT_ID:
				if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem().equals(CCubesItems.creativePendant))
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
		switch (ID)
		{
			case CREATIVE_PENDANT_ID:
				if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem().equals(CCubesItems.creativePendant))
					return new CreativePendantContainer(player.inventory, world);
				break;
			default:
				return null;
		}
		return null;
	}
}
