package chanceCubes.containers;

import chanceCubes.items.CCubesItems;
import chanceCubes.items.ItemChanceCube;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CreativePendantContainer extends AbstractContainerMenu
{
	private final Container pendantSlot = new SimpleContainer(1)
	{
		public boolean canPlaceItem(int slot, ItemStack stack)
		{
			return stack.getItem() instanceof ItemChanceCube;
		}
	};

	public CreativePendantContainer(int windowId, Inventory inv)
	{
		super(CCubesItems.CREATIVE_PENDANT_CONTAINER, windowId);

		this.addSlot(new Slot(pendantSlot, 0, 80, 50)
		{
			@Override
			public boolean mayPlace(ItemStack stack)
			{
				return stack.getItem() instanceof ItemChanceCube;
			}
		});

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

	@Override
	public ItemStack quickMoveStack(Player player, int slot)
	{
		Slot slotObject = getSlot(slot);

		if(slotObject.getItem().isEmpty())
			return ItemStack.EMPTY;

		ItemStack stackInSlot = slotObject.getItem();
		ItemStack stack = stackInSlot.copy();

		if(slot > 35)
		{
			if(!this.moveItemStackTo(stackInSlot, 0, 36, true))
				return ItemStack.EMPTY;
			else if(stack.getItem().equals(CCubesItems.CHANCE_CUBE) && !this.moveItemStackTo(stackInSlot, 36, 37, true))
				return ItemStack.EMPTY;
		}

		if(stackInSlot.getCount() == 0)
			slotObject.set(ItemStack.EMPTY);
		else
			slotObject.setChanged();

		if(stackInSlot.getCount() == stack.getCount())
			return ItemStack.EMPTY;
		slotObject.onTake(player, stackInSlot);
		return stack;
	}

	/**
	 * Called when the container is closed.
	 */
	@Override
	public void removed(Player player)
	{
		super.removed(player);

		if(!player.level.isClientSide())
		{
			ItemStack itemstack = this.pendantSlot.getItem(0);
			if(!itemstack.isEmpty())
				player.drop(itemstack, false);
		}
	}

	public ItemStack getChanceCubesInPendant()
	{
		return this.pendantSlot.getItem(0);
	}
}