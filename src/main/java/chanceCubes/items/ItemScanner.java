package chanceCubes.items;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.listeners.RenderEvent;
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

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		player.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
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

					if(world.getBlockState(position).getBlock().equals(CCubesBlocks.CHANCE_CUBE))
					{
						TileChanceCube te = ((TileChanceCube) world.getTileEntity(new BlockPos(i, j, k)));
						te.setScanned(true);
						CCubesPacketHandler.INSTANCE.sendToServer(new PacketCubeScan(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ()));
						flag = true;
						RenderEvent.setLookingAtChance(te.getChance());
					}
					else if(world.getBlockState(position).getBlock().equals(CCubesBlocks.CHANCE_ICOSAHEDRON))
					{
						TileChanceD20 te = ((TileChanceD20) world.getTileEntity(new BlockPos(i, j, k)));
						te.setScanned(true);
						CCubesPacketHandler.INSTANCE.sendToServer(new PacketCubeScan(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ()));
						flag = true;
						RenderEvent.setLookingAtChance(te.getChance());
					}
					else if(world.getBlockState(position).getBlock().equals(CCubesBlocks.GIANT_CUBE))
					{
						flag = false;
						RenderEvent.setLookingAtChance(-201);
						RenderEvent.setLookingAt(true);
						RenderEvent.setChanceIncrease(0);
					}

					if(flag)
					{
						RenderEvent.setLookingAt(true);
						int chanceInc = 0;
						for(ItemStack s : player.inventory.mainInventory)
						{
							if(s != null && s.getItem() instanceof ItemChancePendant)
							{
								chanceInc += ((ItemChancePendant) s.getItem()).getChanceIncrease();
								break;
							}
						}
						RenderEvent.setChanceIncrease(chanceInc);
					}
				}
			}
		}
	}
}
