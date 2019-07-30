package chanceCubes.tileentities;

import chanceCubes.blocks.BlockCubeDispenser;
import chanceCubes.blocks.BlockCubeDispenser.DispenseType;
import chanceCubes.blocks.CCubesBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileCubeDispenser extends TileEntity
{
	private ItemEntity entityItem;

	public float rot = 0;
	public float wave = 0;

	public TileCubeDispenser()
	{
		super(CCubesBlocks.TILE_CUBE_DISPENSER);
	}

	public ItemEntity getRenderEntityItem(DispenseType type)
	{
		if(entityItem == null)
			this.entityItem = new ItemEntity(this.world, super.getPos().getX(), super.getPos().getY(), super.getPos().getZ(), new ItemStack(CCubesBlocks.CHANCE_CUBE, 1));
		if(!entityItem.getItem().getItem().equals(Item.getItemFromBlock(getCurrentBlock(type))))
		{
			if(type == DispenseType.COMPACT_GAINTCUBE)
				this.entityItem.setItem(new ItemStack(CCubesBlocks.COMPACT_GIANT_CUBE, 1));
			else if(type == DispenseType.CHANCE_ICOSAHEDRON)
				this.entityItem.setItem(new ItemStack(CCubesBlocks.CHANCE_ICOSAHEDRON, 1));
			else
				this.entityItem.setItem(new ItemStack(CCubesBlocks.CHANCE_CUBE, 1));
		}

		return this.entityItem;
	}

	public ItemEntity getNewEntityItem(DispenseType type)
	{
		ItemEntity ent;

		/*
		 * if(type == DispenseType.CHANCE_ICOSAHEDRON) ent = new EntityItem(this.world,
		 * super.getPos().getX(), super.getPos().getY(), super.getPos().getZ(), new
		 * ItemStack(CCubesBlocks.CHANCE_ICOSAHEDRON, 1)); else
		 */
		if(type == DispenseType.COMPACT_GAINTCUBE)
			ent = new ItemEntity(this.world, super.getPos().getX(), super.getPos().getY(), super.getPos().getZ(), new ItemStack(CCubesBlocks.COMPACT_GIANT_CUBE, 1));
		else
			ent = new ItemEntity(this.world, super.getPos().getX(), super.getPos().getY(), super.getPos().getZ(), new ItemStack(CCubesBlocks.CHANCE_CUBE, 1));

		return ent;
	}

	public Block getCurrentBlock(DispenseType type)
	{
		Block b = Blocks.AIR;
		if(entityItem == null || BlockCubeDispenser.getCurrentState(this.world.getBlockState(this.pos)) != type)
		{
			/*
			 * if(type == DispenseType.CHANCE_ICOSAHEDRON) b = CCubesBlocks.CHANCE_ICOSAHEDRON; else
			 */
			if(type == DispenseType.COMPACT_GAINTCUBE)
				b = CCubesBlocks.COMPACT_GIANT_CUBE;
			else
				b = CCubesBlocks.CHANCE_CUBE;
		}

		return b;
	}
}