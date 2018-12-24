package chanceCubes.blocks;

import java.util.Random;

import chanceCubes.CCubesCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BaseChanceBlock extends Block
{
	private String blockName = "Chance_Cube_Unnamed";

	public BaseChanceBlock(String name)
	{
		super(Material.GROUND);
		this.blockName = name;
		this.setHardness(0.5f);
		this.setUnlocalizedName(blockName);
		this.setCreativeTab(CCubesCore.modTab);
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
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
	{
		return false;
	}

	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn)
	{

	}

	@Override
	public boolean canDropFromExplosion(Explosion explosion)
	{
		return false;
	}

	@Override
	public int quantityDropped(Random rand)
	{
		return 0;
	}
}