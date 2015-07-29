package chanceCubes.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import chanceCubes.CCubesCore;
import chanceCubes.items.CCubesItems;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.tileentities.TileChanceCube;

public class BlockChanceCube extends Block implements ITileEntityProvider
{ 
	public BlockChanceCube()
	{
		super(Material.ground);
		this.setHardness(0.5f);
		this.setBlockName("Chance_Cube");
		this.setCreativeTab(CCubesCore.modTab);
		this.setBlockTextureName("chancecubes:chanceCube");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_)
	{
		return new TileChanceCube();
	}

	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int side, EntityPlayer player)
	{
		if (!world.isRemote && player != null && !(player instanceof FakePlayer))
		{
			TileChanceCube te = (TileChanceCube) world.getTileEntity(x, y, z);
			if(player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant))
			{
				ItemStack stack = new ItemStack(Item.getItemFromBlock(CCubesBlocks.chanceCube), 1);
				((ItemChanceCube)stack.getItem()).setChance(stack, te.getChance());
				this.dropBlockAsItem(world, x, y, z, stack);
				world.setBlockToAir(x, y, z);
				world.removeTileEntity(x, y, z);
				return;
			}

			if (te != null)
			{
				ChanceCubeRegistry.INSTANCE.triggerRandomReward(world, x, y, z, player, te.getChance());
			}
		}
	}

	@Override
	public int quantityDropped(Random p_149745_1_)
	{
		return 0;
	}
}