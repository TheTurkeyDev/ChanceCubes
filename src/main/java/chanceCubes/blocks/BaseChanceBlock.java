package chanceCubes.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;

public class BaseChanceBlock extends Block
{
	public BaseChanceBlock(Properties builder)
	{
		super(builder);
	}


	protected static Properties getBuilder()
	{
		return Properties.of().mapColor(MapColor.COLOR_BLUE).strength(0.5f, Float.MAX_VALUE);
	}
}