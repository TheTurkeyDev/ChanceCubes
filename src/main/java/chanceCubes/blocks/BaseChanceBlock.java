package chanceCubes.blocks;

import java.util.Random;

import chanceCubes.CCubesCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BaseChanceBlock extends Block
{
	private String blockName = "Chance_Cube_Unnamed";

	public BaseChanceBlock(Builder builder, String name)
	{
		super(builder);
		this.blockName = name;
		this.setRegistryName(CCubesCore.MODID, this.blockName);
	}

	public String getBlockName()
	{
		return this.blockName;
	}

	public float getExplosionResistance(Entity exploder)
	{
		return Float.MAX_VALUE;
	}

	@Override
	public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn)
	{

	}

	@Override
	public boolean canDropFromExplosion(Explosion explosion)
	{
		return false;
	}

	@Override
	public int getItemsToDropCount(IBlockState state, int fortune, World worldIn, BlockPos pos, Random random)
	{
		return 0;
	}
	
	protected static Builder getBuilder()
	{
		return Builder.create(Material.GROUND).hardnessAndResistance(0.5f, Float.MAX_VALUE);
	}
}