package chanceCubes.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.RenderEvent;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;

public class ItemScanner extends Item
{
	public ItemScanner()
	{
		this.setUnlocalizedName("scanner");
		this.setTextureName(CCubesCore.MODID + ":Scanner");
		this.setMaxStackSize(1);
		this.setCreativeTab(CCubesCore.modTab);
	}
	
    public EnumAction getItemUseAction(ItemStack p_77661_1_)
    {
        return EnumAction.block;
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
				
				if (movingobjectposition == null)
					return;
				if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
				{
					int i = movingobjectposition.blockX;
					int j = movingobjectposition.blockY;
					int k = movingobjectposition.blockZ;
					if(world.getBlock(i, j, k).equals(CCubesBlocks.chanceCube))
					{
						RenderEvent.setLookingAt(true);
						TileChanceCube te = (TileChanceCube) world.getTileEntity(i, j, k);
						RenderEvent.setLookingAtChance(te.getChance());
					}
					else if(world.getBlock(i, j, k).equals(CCubesBlocks.chanceIcosahedron))
					{
						RenderEvent.setLookingAt(true);
						TileChanceD20 te = (TileChanceD20) world.getTileEntity(i, j, k);
						RenderEvent.setLookingAtChance(te.getChance());
					}	
				}
			}
		}
	}
}
