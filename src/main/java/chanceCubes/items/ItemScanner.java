package chanceCubes.items;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.RenderEvent;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.network.PacketCubeScan;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemScanner extends BaseChanceCubesItem
{
	public ItemScanner()
	{
		super("scanner");
		this.setMaxStackSize(1);
	}

	public EnumAction getItemUseAction(ItemStack p_77661_1_)
	{
		return EnumAction.BLOCK;
	}

	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}

	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		playerIn.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}

	public void onUpdate(ItemStack stack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_)
	{
		if(!world.isRemote)
			return;
		RenderEvent.setLookingAt(false);
		if(entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;
			if(player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().equals(stack) && player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().equals(stack))
			{
				RayTraceResult movingobjectposition = this.rayTrace(world, player, true);

				if(movingobjectposition == null)
					return;
				if(movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK)
				{
					int i = movingobjectposition.getBlockPos().getX();
					int j = movingobjectposition.getBlockPos().getY();
					int k = movingobjectposition.getBlockPos().getZ();
					boolean flag = false;

					BlockPos position = new BlockPos(i, j, k);

					if(world.getBlockState(position).getBlock().equals(CCubesBlocks.CHANCECUBE))
					{
						flag = true;
						RenderEvent.setLookingAtChance(((TileChanceCube) world.getTileEntity(position)).getChance());
					}

					else if(world.getBlockState(position).getBlock().equals(CCubesBlocks.CHANCEICOSAHEDRON))
					{
						TileChanceCube te = ((TileChanceCube) world.getTileEntity(new BlockPos(i, j, k)));
						te.setScanned(true);
						CCubesPacketHandler.INSTANCE.sendToServer(new PacketCubeScan(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ()));
						flag = true;
						RenderEvent.setLookingAtChance(te.getChance());
					}
					else if(world.getBlockState(position).getBlock().equals(CCubesBlocks.CHANCEGIANTCUBE))
					{
						TileChanceD20 te = ((TileChanceD20) world.getTileEntity(new BlockPos(i, j, k)));
						te.setScanned(true);
						CCubesPacketHandler.INSTANCE.sendToServer(new PacketCubeScan(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ()));
						flag = true;
						RenderEvent.setLookingAtChance(te.getChance());
					}

					if(flag)
					{
						RenderEvent.setLookingAt(true);
						int chanceInc = 0;
						for(ItemStack s : player.inventory.mainInventory)
							if(s != null && s.getItem() instanceof ItemChancePendant)
								chanceInc += ((ItemChancePendant) s.getItem()).getChanceIncrease();
						RenderEvent.setChanceIncrease(chanceInc);
					}
				}
			}
		}
	}
}
