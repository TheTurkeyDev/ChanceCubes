package chanceCubes.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import chanceCubes.CCubesCore;
import chanceCubes.registry.GiantCubeRegistry;
import chanceCubes.tileentities.TileGiantCube;
import chanceCubes.util.GiantCubeUtil;

public class BlockGiantCube extends BlockContainer
{
	public BlockGiantCube()
	{
		super(Material.ground);
		this.setHardness(0.5f);
		this.setBlockName("Giant_Chance_Cube");
		this.setCreativeTab(CCubesCore.modTab);
		this.setBlockTextureName("chancecubes:chanceCube");
		this.setLightLevel(2);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileGiantCube();
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
	{
		if(!world.isRemote && player != null && !(player instanceof FakePlayer))
		{
			TileGiantCube te = (TileGiantCube) world.getTileEntity(x, y, z);

			if(te != null)
			{
				GiantCubeUtil.removeStructure(te.getMasterX(), te.getMasterY(), te.getMasterZ(), world);
				GiantCubeRegistry.INSTANCE.triggerRandomReward(world, x, y, z, player, 0);
			}
		}
		return true;
	}
}