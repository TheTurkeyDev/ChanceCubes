package chanceCubes.blocks;

import chanceCubes.CCubesCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.state.IProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;

public class BaseChanceBlock extends Block
{
	public BaseChanceBlock(Properties builder, String name)
	{
		super(builder);
		this.setRegistryName(CCubesCore.MODID, name);
	}

	@Override
	public float getExplosionResistance(BlockState state, IWorldReader world, BlockPos pos, @Nullable Entity exploder, Explosion explosion)
	{
		return this.blockResistance;
	}

	@Override
	public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn)
	{

	}

	@Override
	public boolean canDropFromExplosion(BlockState state, IBlockReader world, BlockPos pos, Explosion explosion)
	{
		return false;
	}

	protected static Properties getBuilder()
	{
		return Properties.create(Material.EARTH).hardnessAndResistance(0.5f, Float.MAX_VALUE);
	}

	/**
	 * Grabbed from 1.12
	 */
	public <T extends Comparable<T>> BlockState cycleProperty(BlockState state, IProperty<T> property)
	{
		return state.with(property, cyclePropertyValue(property.getAllowedValues(), state.get(property)));
	}

	protected static <T> T cyclePropertyValue(Collection<T> values, T currentValue)
	{
		Iterator<T> iterator = values.iterator();

		while(iterator.hasNext())
		{
			if(iterator.next().equals(currentValue))
			{
				if(iterator.hasNext())
				{
					return iterator.next();
				}

				return values.iterator().next();
			}
		}

		return iterator.next();
	}
}