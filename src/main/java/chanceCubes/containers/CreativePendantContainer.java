package chanceCubes.containers;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.items.ItemChanceCube;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CreativePendantContainer extends Container
{
	private World theWorld;

	private IInventory pendantSlot = new Inventory(1)
	{
		public boolean isItemValidForSlot(int slot, ItemStack stack)
		{
			return stack.getItem() instanceof ItemChanceCube;
		}
	};

	public CreativePendantContainer(int id, PlayerInventory player, World world)
	{
		super(CCubesBlocks.CREATIVE_PENDANT_CONTAINER, id);
		this.theWorld = world;

		for(int i = 0; i < 9; i++)
			this.addSlot(new Slot(player, i, 8 + i * 18, 142));

		for(int y = 0; y < 3; y++)
			for(int x = 0; x < 9; x++)
				this.addSlot(new Slot(player, 9 + (9 * y + x), 8 + x * 18, 84 + y * 18));

		this.addSlot(new Slot(pendantSlot, 0, 80, 50)
		{
			public boolean isItemValid(ItemStack stack)
			{
				return stack.getItem() instanceof ItemChanceCube;
			}
		});
	}

	@Override
	public boolean canInteractWith(PlayerEntity p_75145_1_)
	{
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int slot)
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
	@Override
	public void onContainerClosed(PlayerEntity player)
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