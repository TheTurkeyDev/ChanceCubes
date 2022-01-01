package chanceCubes.containers;

import chanceCubes.items.CCubesItems;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

public class EmptyContainer extends AbstractContainerMenu
{
	public EmptyContainer(int windowId, Inventory inv)
	{
		super(CCubesItems.CREATIVE_PENDANT_CONTAINER, windowId);

		for(int k = 0; k < 3; ++k)
			for(int i1 = 0; i1 < 9; ++i1)
				this.addSlot(new Slot(inv, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));

		for(int l = 0; l < 9; ++l)
			this.addSlot(new Slot(inv, l, 8 + l * 18, 142));
	}

	@Override
	public boolean stillValid(Player player)
	{
		return true;
	}
}