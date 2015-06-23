package chanceCubes.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.items.ItemChanceCube;

public class CreativePendantContainer extends Container
{

	private World theWorld;

	private IInventory pendantSlot = new InventoryBasic("CreativePendant", true, 1)
	{
		public boolean isItemValidForSlot(int slot, ItemStack stack)
		{
			return stack.getItem() instanceof ItemChanceCube || stack.getItem().equals(CCubesBlocks.chanceIcosahedron);
		}
	};

	public CreativePendantContainer(InventoryPlayer player, World world)
	{
		this.theWorld = world;

		for(int i = 0; i < 9; i++)
		{
			this.addSlotToContainer(new Slot(player, i, 8 + i * 18, 142));
		}

		for(int y = 0; y < 3; y++)
		{
			for(int x = 0; x < 9; x++)
			{
				this.addSlotToContainer(new Slot(player, 9 + (9 * y + x), 8 + x * 18, 84 + y * 18));
			}
		}

		this.addSlotToContainer(new Slot(pendantSlot, 0, 80, 50){
			public boolean isItemValid(ItemStack stack)
			{
				if (stack.getItem() instanceof ItemChanceCube)
					return true;
				return false;
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
		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);
		// System.out.println(slot);

		// null checks and checks if the item can be stacked (maxStackSize > 1)
		if(slotObject != null && slotObject.getHasStack())
		{
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();

			if(slot > 35)
			{
				if(!this.mergeItemStack(stackInSlot, 0, 36, true))
				{
					return null;
				}
			}
			else if(stack.getItem().equals(CCubesBlocks.chanceCube) || stack.getItem().equals(CCubesBlocks.chanceIcosahedron))
			{
				if(!this.mergeItemStack(stackInSlot, 36, 37, true))
				{
					return null;
				}
			}

			if(stackInSlot.stackSize == 0)
			{
				slotObject.putStack(null);
			}
			else
			{
				slotObject.onSlotChanged();
			}

			if(stackInSlot.stackSize == stack.stackSize)
			{
				return null;
			}
			slotObject.onPickupFromSlot(player, stackInSlot);
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
			ItemStack itemstack = this.pendantSlot.getStackInSlotOnClosing(0);

			if(itemstack != null)
			{
				player.dropPlayerItemWithRandomChoice(itemstack, false);
			}
		}
	}
	
	public ItemStack getChanceCubesInPendant()
	{
		return this.pendantSlot.getStackInSlot(0);
	}
}