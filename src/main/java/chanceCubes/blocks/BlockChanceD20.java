package chanceCubes.blocks;

import java.util.Random;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import chanceCubes.config.CCubesSettings;
import chanceCubes.items.CCubesItems;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.network.PacketTriggerD20;
import chanceCubes.tileentities.TileChanceD20;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockChanceD20 extends BaseChanceBlock implements ITileEntityProvider
{
	
	public BlockChanceD20()
	{
		super("Chance_Icosahedron");
		super.setHardness(-1F);
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

	@Override
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
		this.startd20(world, x, y, z, player);
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		return this.startd20(world, x, y, z, player);
	}

	public boolean startd20(World world, int x, int y, int z, EntityPlayer player)
	{
		if(!world.isRemote && player != null && !(player instanceof FakePlayer))
			return false;

		TileChanceD20 te = (TileChanceD20) world.getTileEntity(x, y, z);
		if(player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant))
		{
			ItemStack stack = new ItemStack(Item.getItemFromBlock(CCubesBlocks.chanceIcosahedron), 1);
			((ItemChanceCube) stack.getItem()).setChance(stack, te.getChance());
			this.dropBlockAsItem(world, x, y, z, stack);
			world.setBlockToAir(x, y, z);
			world.removeTileEntity(x, y, z);
			return true;
		}

		if(te != null)
		{
			te.startBreaking(player);
			CCubesPacketHandler.INSTANCE.sendToAllAround(new PacketTriggerD20(x, y, z), new TargetPoint(world.provider.dimensionId, x, y, z, 50));
			return true;
		}

		return false;
	}

}