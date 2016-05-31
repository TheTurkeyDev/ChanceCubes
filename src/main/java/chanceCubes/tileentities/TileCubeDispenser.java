package chanceCubes.tileentities;

import chanceCubes.blocks.CCubesBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileCubeDispenser extends TileEntity
{
	private EntityItem entityItem;
	private int currentMeta = 0;
	
	public float rot = 0;
	public float wave = 0;

	public EntityItem getRenderEntityItem(int meta)
	{
		if(entityItem == null)
			this.entityItem = new EntityItem(this.worldObj, xCoord, yCoord, zCoord, new ItemStack(CCubesBlocks.chanceCube, 1));
		if(this.currentMeta != meta)
		{
			this.currentMeta = meta;
			if(meta == 1)
				this.entityItem.setEntityItemStack(new ItemStack(CCubesBlocks.chanceIcosahedron, 1));
			else if(meta == 2)
				this.entityItem.setEntityItemStack(new ItemStack(CCubesBlocks.chanceCompactGiantCube, 1));
			else
				this.entityItem.setEntityItemStack(new ItemStack(CCubesBlocks.chanceCube, 1));
		}

		return this.entityItem;
	}

	public EntityItem getNewEntityItem(int meta)
	{
		EntityItem ent;
		
		if(meta == 1)
			ent = new EntityItem(this.worldObj, xCoord, yCoord, zCoord, new ItemStack(CCubesBlocks.chanceIcosahedron, 1));
		else if(meta == 2)
			ent = new EntityItem(this.worldObj, xCoord, yCoord, zCoord, new ItemStack(CCubesBlocks.chanceCompactGiantCube, 1));
		else
			ent = new EntityItem(this.worldObj, xCoord, yCoord, zCoord, new ItemStack(CCubesBlocks.chanceCube, 1));

		return ent;
	}

	public Block getCurrentBlock(int meta)
	{
		Block b = Blocks.air;
		if(entityItem == null || this.currentMeta != meta)
		{
			if(meta == 1)
				b = CCubesBlocks.chanceIcosahedron;
			else if(meta == 2)
				b = CCubesBlocks.chanceCompactGiantCube;
			else
				b = CCubesBlocks.chanceCube;
		}

		return b;
	}
}
