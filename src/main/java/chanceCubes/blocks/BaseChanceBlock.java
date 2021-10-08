package chanceCubes.blocks;

import chanceCubes.CCubesCore;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;

public class BaseChanceBlock extends Block
{
	public BaseChanceBlock(Properties builder, String name)
	{
		super(builder);
		this.setRegistryName(CCubesCore.MODID, name);
	}


	protected static Properties getBuilder()
	{
		return Properties.of(Material.DIRT).strength(0.5f, Float.MAX_VALUE);
	}
}