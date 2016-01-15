package chanceCubes.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import chanceCubes.config.CCubesSettings;
import chanceCubes.tileentities.TileChanceD20;

public class BlockChanceD20 extends BaseChanceBlock implements ITileEntityProvider
{

	public BlockChanceD20()
	{
		super("chance_Icosahedron");
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

	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
	{
		return false;
	}

	public boolean isOpaqueCube()
	{
		return false;
	}

	/*
	 * @SideOnly(Side.CLIENT) public void randomDisplayTick(World world, int x, int y, int z, Random rand) { }
	 * 
	 * public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) { this.startd20(world, pos, player); }
	 * 
	 * public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) { return this.startd20(world, pos, player); }
	 * 
	 * public boolean startd20(World world, BlockPos pos, EntityPlayer player) { if(world.isRemote || player == null || player instanceof FakePlayer) return false;
	 * 
	 * TileChanceD20 te = (TileChanceD20) world.getTileEntity(pos); if(player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant)) { ItemStack stack = new ItemStack(Item.getItemFromBlock(CCubesBlocks.chanceIcosahedron), 1); ((ItemChanceCube) stack.getItem()).setChance(stack, te.getChance()); this.dropBlockAsItem(world, pos, stack); world.setBlockToAir(pos); world.removeTileEntity(pos); return true; }
	 * 
	 * if(te != null) { te.startBreaking(player); CCubesPacketHandler.INSTANCE.sendToAllAround(new PacketTriggerD20(x, y, z), new TargetPoint(world.provider.dimensionId, x, y, z, 50)); return true; }
	 * 
	 * return false; }
	 */

}