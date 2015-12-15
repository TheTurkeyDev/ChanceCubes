package chanceCubes.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.RenderEvent;
import chanceCubes.tileentities.TileChanceCube;

public class ItemScanner extends Item
{
	public String itemNameID = "scanner";
	
	public ItemScanner()
	{
		this.setUnlocalizedName(CCubesCore.MODID + "_" + itemNameID);
		this.setMaxStackSize(1);
		this.setCreativeTab(CCubesCore.modTab);
	}

	public EnumAction getItemUseAction(ItemStack p_77661_1_)
	{
		return EnumAction.BLOCK;
	}

	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
		return stack;
	}

	public void onUpdate(ItemStack stack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_)
	{
		if(!world.isRemote)
			return;
		RenderEvent.setLookingAt(false);
		if(entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;
			if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().equals(stack) && player.getItemInUse() != null && player.getItemInUse().equals(stack))
			{
				MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);

				if(movingobjectposition == null)
					return;
				if(movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
				{
					int i = movingobjectposition.getBlockPos().getX();
					int j = movingobjectposition.getBlockPos().getY();
					int k = movingobjectposition.getBlockPos().getZ();
					boolean flag = false;
					
					BlockPos position = new BlockPos(i, j, k);
					
					if(world.getBlockState(position).getBlock().equals(CCubesBlocks.chanceCube))
					{
						flag = true;
						RenderEvent.setLookingAtChance(((TileChanceCube) world.getTileEntity(position)).getChance());
					}
					/*else if(world.getBlockState(position).getBlock().equals(CCubesBlocks.chanceIcosahedron))
					{
						flag = true;
						RenderEvent.setLookingAtChance(((TileChanceD20) world.getTileEntity(position)).getChance());
					}*/

					if(flag)
					{
						RenderEvent.setLookingAt(true);
						int chanceInc = 0;
						for(ItemStack s : player.inventory.mainInventory)
							if(s != null && s.getItem() instanceof ItemChancePendant)
								chanceInc += ((ItemChancePendant) s.getItem()).getChanceIncrease();
						RenderEvent.setChanceIncrease(chanceInc);
					}
				}
			}
		}
	}
}
