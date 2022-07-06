package chanceCubes.tileentities;

import chanceCubes.blocks.BlockCubeDispenser;
import chanceCubes.blocks.BlockCubeDispenser.DispenseType;
import chanceCubes.blocks.CCubesBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileCubeDispenser extends BlockEntity
{
	private ItemEntity entityItem;

	public float rot = 0;
	public float wave = 0;

	public TileCubeDispenser(BlockPos pos, BlockState state)
	{
		super(CCubesBlocks.TILE_CUBE_DISPENSER.get(), pos, state);
	}

	public ItemEntity getRenderEntityItem(DispenseType type)
	{
		if(entityItem == null)
			this.entityItem = new ItemEntity(this.level, super.getBlockPos().getX(), super.getBlockPos().getY(), super.getBlockPos().getZ(), new ItemStack(CCubesBlocks.CHANCE_CUBE.get(), 1));
		if(!entityItem.getItem().getItem().equals(getCurrentBlock(type).asItem()))
		{
			if(type == DispenseType.CHANCE_ICOSAHEDRON)
				this.entityItem.setItem(new ItemStack(CCubesBlocks.CHANCE_ICOSAHEDRON.get(), 1));
			else if(type == DispenseType.COMPACT_GIANTCUBE)
				this.entityItem.setItem(new ItemStack(CCubesBlocks.COMPACT_GIANT_CUBE.get(), 1));
			else
				this.entityItem.setItem(new ItemStack(CCubesBlocks.CHANCE_CUBE.get(), 1));
		}

		return this.entityItem;
	}

	public ItemStack getCurrentStack(DispenseType type)
	{
		if(type == DispenseType.CHANCE_ICOSAHEDRON)
			return new ItemStack(CCubesBlocks.CHANCE_ICOSAHEDRON.get(), 1);
		else if(type == DispenseType.COMPACT_GIANTCUBE)
			return new ItemStack(CCubesBlocks.COMPACT_GIANT_CUBE.get(), 1);
		else
			return new ItemStack(CCubesBlocks.CHANCE_CUBE.get(), 1);
	}

	public ItemEntity getNewEntityItem(DispenseType type)
	{
		ItemEntity ent;

		if(type == DispenseType.CHANCE_ICOSAHEDRON)
			ent = new ItemEntity(this.level, super.getBlockPos().getX(), super.getBlockPos().getY(), super.getBlockPos().getZ(), new ItemStack(CCubesBlocks.CHANCE_ICOSAHEDRON.get(), 1));
		else if(type == DispenseType.COMPACT_GIANTCUBE)
			ent = new ItemEntity(this.level, super.getBlockPos().getX(), super.getBlockPos().getY(), super.getBlockPos().getZ(), new ItemStack(CCubesBlocks.COMPACT_GIANT_CUBE.get(), 1));
		else
			ent = new ItemEntity(this.level, super.getBlockPos().getX(), super.getBlockPos().getY(), super.getBlockPos().getZ(), new ItemStack(CCubesBlocks.CHANCE_CUBE.get(), 1));

		return ent;
	}

	public Block getCurrentBlock(DispenseType type)
	{
		Block b = Blocks.AIR;
		if(entityItem == null || BlockCubeDispenser.getCurrentState(this.level.getBlockState(this.getBlockPos())) != type)
		{
			if(type == DispenseType.CHANCE_ICOSAHEDRON)
				b = CCubesBlocks.CHANCE_ICOSAHEDRON.get();
			else if(type == DispenseType.COMPACT_GIANTCUBE)
				b = CCubesBlocks.COMPACT_GIANT_CUBE.get();
			else
				b = CCubesBlocks.CHANCE_CUBE.get();
		}

		return b;
	}
}