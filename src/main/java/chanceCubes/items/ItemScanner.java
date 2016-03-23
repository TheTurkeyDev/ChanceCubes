package chanceCubes.items;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.RenderEvent;
import chanceCubes.tileentities.TileChanceCube;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

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

	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        playerIn.setActiveHand(hand);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
    }
	
	public void onUpdate(ItemStack stack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_)
	{
		if(!world.isRemote)
			return;
		RenderEvent.setLookingAt(false);
		if(entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;
			if(player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().equals(stack) && player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().equals(stack))
			{
				RayTraceResult movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);

				if(movingobjectposition == null)
					return;
				if(movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK)
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
					/*
					 * else if(world.getBlockState(position).getBlock().equals(CCubesBlocks.chanceIcosahedron)) { flag = true; RenderEvent.setLookingAtChance(((TileChanceD20) world.getTileEntity(position)).getChance()); }
					 */

					else if(world.getBlockState(position).getBlock().equals(CCubesBlocks.chanceGiantCube))
					{
						flag = false;
						RenderEvent.setLookingAtChance(-201);
						RenderEvent.setLookingAt(true);
						RenderEvent.setChanceIncrease(0);
					}

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
