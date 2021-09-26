package chanceCubes.containers;

import chanceCubes.items.CCubesItems;
import chanceCubes.items.ItemChanceCube;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CreativePendantContainer extends Container
{
	static final ImmutableList<Item> field_217084_c = ImmutableList.of(CCubesItems.CHANCE_CUBE, CCubesItems.CHANCE_ICOSAHEDRON);

	private Container pendantSlot = new Inventory(1)
	{
		public boolean isItemValidForSlot(int slot, ItemStack stack)
		{
			return stack.getItem() instanceof ItemChanceCube;
		}
	};

	public CreativePendantContainer(int id, PlayerInventory player)
	{
		super(CCubesItems.CREATIVE_PENDANT_CONTAINER, id);

		this.addSlot(new Slot(pendantSlot, 0, 80, 50)
		{
			public boolean isItemValid(ItemStack stack)
			{
				return stack.getItem() instanceof ItemChanceCube;
			}
		});

		for(int k = 0; k < 3; ++k)
		{
			for(int i1 = 0; i1 < 9; ++i1)
			{
				this.addSlot(new Slot(player, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
			}
		}

		for(int l = 0; l < 9; ++l)
		{
			this.addSlot(new Slot(player, l, 8 + l * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(Player p_75145_1_)
	{
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(Player player, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if(slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			Item item = itemstack1.getItem();
			itemstack = itemstack1.copy();
			if(index == 0)
			{
				if(!this.mergeItemStack(itemstack1, 1, 37, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if(field_217084_c.contains(item))
			{
				if(!this.mergeItemStack(itemstack1, 0, 1, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if(index < 28)
			{
				if(!this.mergeItemStack(itemstack1, 28, 37, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if(index < 37 && !this.mergeItemStack(itemstack1, 1, 28, false))
			{
				return ItemStack.EMPTY;
			}

			if(itemstack1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}

			slot.onSlotChanged();
			if(itemstack1.getCount() == itemstack.getCount())
			{
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
			this.detectAndSendChanges();
		}

		return itemstack;
	}

	/**
	 * Called when the container is closed.
	 */
	@Override
	public void onContainerClosed(Player player)
	{
		super.onContainerClosed(player);

		if(!player.world.isRemote)
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