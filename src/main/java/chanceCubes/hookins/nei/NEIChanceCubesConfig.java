package chanceCubes.hookins.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import net.minecraft.item.ItemStack;
import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;

public class NEIChanceCubesConfig implements IConfigureNEI
{
	@Override
	public void loadConfig()
	{
		API.hideItem(new ItemStack(CCubesBlocks.chanceGiantCube));
	}

	@Override
	public String getName()
	{
		return "Chance Cubes";
	}

	@Override
	public String getVersion()
	{
		return CCubesCore.MODID;
	}
}