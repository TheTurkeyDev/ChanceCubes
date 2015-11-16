package chanceCubes.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;
import chanceCubes.items.CCubesItems;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.network.PacketTriggerD20;
import chanceCubes.tileentities.TileChanceD20;

public class BlockChanceD20 extends Block implements ITileEntityProvider
{
	private final String blockNameID = "chanceIcosahedron";

	public BlockChanceD20()
	{
		super(Material.iron);
		GameRegistry.registerBlock(this, ItemChanceCube.class, blockNameID);
		super.setHardness(-1F);
		this.setUnlocalizedName(blockNameID);
		this.setCreativeTab(CCubesCore.modTab);
		this.setLightLevel(7);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileChanceD20();
	}

	@Override
	public int getRenderType()
	{
		return CCubesSettings.d20RenderID;
	}

	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
	{
		return false;
	}

	public boolean isOpaqueCube()
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random rand)
	{
	}

	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
	{
		if(!world.isRemote && player != null && !(player instanceof FakePlayer))
		{
			BlockPos position = new BlockPos(x, y, z);
			TileChanceD20 te = (TileChanceD20) world.getTileEntity(position);
			if(player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant))
			{
				ItemStack stack = new ItemStack(Item.getItemFromBlock(CCubesBlocks.chanceIcosahedron), 1);
				((ItemChanceCube) stack.getItem()).setChance(stack, te.getChance());
				EntityItem blockstack = new EntityItem(world, x, y, z, stack);
				world.setBlockToAir(position);
				world.removeTileEntity(position);
				world.spawnEntityInWorld(blockstack);
				return;
			}

			if(te != null)
			{
				te.startBreaking(player);
				CCubesPacketHandler.INSTANCE.sendToAllAround(new PacketTriggerD20(x, y, z), new TargetPoint(world.provider.getDimensionId(), x, y, z, 50));
			}
		}
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		BlockPos position = new BlockPos(x, y, z);
		TileChanceD20 te = (TileChanceD20) world.getTileEntity(position);
		if(player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant))
		{
			ItemStack stack = new ItemStack(Item.getItemFromBlock(CCubesBlocks.chanceIcosahedron), 1);
			((ItemChanceCube) stack.getItem()).setChance(stack, te.getChance());
			EntityItem blockstack = new EntityItem(world, x, y, z, stack);
			world.setBlockToAir(position);
			world.removeTileEntity(position);
			world.spawnEntityInWorld(blockstack);
			return true;
		}

		if(te != null)
		{
			te.startBreaking(player);
			CCubesPacketHandler.INSTANCE.sendToAllAround(new PacketTriggerD20(x, y, z), new TargetPoint(world.provider.getDimensionId(), x, y, z, 50));
			return true;
		}
		return false;
	}

}