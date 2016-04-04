package chanceCubes.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import chanceCubes.CCubesCore;

public class BaseChanceBlock extends Block
{
	private String blockName = "Chance_Cube_Unnamed";

	public BaseChanceBlock(String name)
	{
		super(Material.ground);
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

}