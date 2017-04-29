package chanceCubes.tileentities;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.blocks.BlockCubeDispenser;
import chanceCubes.blocks.BlockCubeDispenser.DispenseType;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileCubeDispenser extends TileEntity
{
	private EntityItem entityItem;

	public float rot = 0;
	public float wave = 0;

	public EntityItem getRenderEntityItem(DispenseType type)
	{
		if(entityItem == null)
			this.entityItem = new EntityItem(this.worldObj, super.getPos().getX(), super.getPos().getY(), super.getPos().getZ(), new ItemStack(CCubesBlocks.CHANCE_CUBE, 1));
		if(!entityItem.getEntityItem().getItem().equals(Item.getItemFromBlock(getCurrentBlock(type))))
		{
			if(type == DispenseType.CHANCE_ICOSAHEDRON)
				this.entityItem.setEntityItemStack(new ItemStack(CCubesBlocks.CHANCE_ICOSAHEDRON, 1));
			else if(type == DispenseType.COMPACT_GAINTCUBE)
				this.entityItem.setEntityItemStack(new ItemStack(CCubesBlocks.COMPACT_GIANT_CUBE, 1));
			else
				this.entityItem.setEntityItemStack(new ItemStack(CCubesBlocks.CHANCE_CUBE, 1));
		}

		return this.entityItem;
	}

	public EntityItem getNewEntityItem(DispenseType type)
	{
		EntityItem ent;

		if(type == DispenseType.CHANCE_ICOSAHEDRON)
			ent = new EntityItem(this.worldObj, super.getPos().getX(), super.getPos().getY(), super.getPos().getZ(), new ItemStack(CCubesBlocks.CHANCE_ICOSAHEDRON, 1));
		else if(type == DispenseType.COMPACT_GAINTCUBE)
			ent = new EntityItem(this.worldObj, super.getPos().getX(), super.getPos().getY(), super.getPos().getZ(), new ItemStack(CCubesBlocks.COMPACT_GIANT_CUBE, 1));
		else
			ent = new EntityItem(this.worldObj, super.getPos().getX(), super.getPos().getY(), super.getPos().getZ(), new ItemStack(CCubesBlocks.CHANCE_CUBE, 1));

		return ent;
	}

	public Block getCurrentBlock(DispenseType type)
	{
		Block b = Blocks.AIR;
		if(entityItem == null || BlockCubeDispenser.getCurrentState(this.worldObj.getBlockState(this.pos)) != type)
		{
			if(type == DispenseType.CHANCE_ICOSAHEDRON)
				b = CCubesBlocks.CHANCE_ICOSAHEDRON;
			else if(type == DispenseType.COMPACT_GAINTCUBE)
				b = CCubesBlocks.COMPACT_GIANT_CUBE;
			else
				b = CCubesBlocks.CHANCE_CUBE;
		}

		return b;
	}
}