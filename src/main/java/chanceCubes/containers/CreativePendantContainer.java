package chanceCubes.containers;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.items.ItemChanceCube;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CreativePendantContainer extends Container
{

	private World theWorld;

	private IInventory pendantSlot = new InventoryBasic("CreativePendant", true, 1)
	{
		public boolean isItemValidForSlot(int slot, ItemStack stack)
		{
			return stack.getItem() instanceof ItemChanceCube;
		}
	};

	public CreativePendantContainer(InventoryPlayer player, World world)
	{
		this.theWorld = world;

		for(int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(player, i, 8 + i * 18, 142));

		for(int y = 0; y < 3; y++)
			for(int x = 0; x < 9; x++)
				this.addSlotToContainer(new Slot(player, 9 + (9 * y + x), 8 + x * 18, 84 + y * 18));

		this.addSlotToContainer(new Slot(pendantSlot, 0, 80, 50)
		{
			public boolean isItemValid(ItemStack stack)
			{
				return stack.getItem() instanceof ItemChanceCube;
			}
		});
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_)
	{
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot)
	{
		ItemStack stack = ItemStack.EMPTY;
		Slot slotObject = inventorySlots.get(slot);

		// null checks and checks if the item can be stacked (maxStackSize > 1)
		if(slotObject != null && slotObject.getHasStack())
		{
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();

			if(slot > 35)
				if(!this.mergeItemStack(stackInSlot, 0, 36, true))
					return ItemStack.EMPTY;
				else if(stack.getItem().equals(Item.getItemFromBlock(CCubesBlocks.CHANCE_CUBE)))
					if(!this.mergeItemStack(stackInSlot, 36, 37, true))
						return ItemStack.EMPTY;

			if(stackInSlot.getCount() == 0)
				slotObject.putStack(ItemStack.EMPTY);
			else
				slotObject.onSlotChanged();

			if(stackInSlot.getCount() == stack.getCount())
				return ItemStack.EMPTY;
			slotObject.onTake(player, stackInSlot);
		}
		return stack;
	}

	/**
	 * Called when the container is closed.
	 */
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);

		if(!this.theWorld.isRemote)
		{
			ItemStack itemstack = this.pendantSlot.getStackInSlot(0);

			if(!itemstack.isEmpty())
				player.dropItem(itemstack, false);
		}
	}

	public ItemStack getChanceCubesInPendant()
	{
		return this.pendantSlot.getStackInSlot(0);
	}
}