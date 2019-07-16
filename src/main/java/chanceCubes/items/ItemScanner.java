package chanceCubes.items;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.listeners.RenderEvent;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.network.PacketCubeScan;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemScanner extends BaseChanceCubesItem
{
	public ItemScanner()
	{
		super((new Item.Properties()).maxStackSize(1), "scanner");
	}

	@Override
	public UseAction getUseAction(ItemStack stack)
	{
		return UseAction.BLOCK;
	}

	@Override
	public int getUseDuration(ItemStack stack)
	{
		return 72000;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		player.setActiveHand(hand);
		return new ActionResult<ItemStack>(ActionResultType.SUCCESS, stack);
	}

	public void inventoryTick(ItemStack stack, World world, Entity entity, int p_77663_4_, boolean isSelected)
	{
		if(!world.isRemote)
			return;
		if(entity instanceof PlayerEntity)
		{
			RenderEvent.setLookingAt(false);
			PlayerEntity player = (PlayerEntity) entity;
			if(isSelected)
			{
				RayTraceResult movingobjectposition = Item.rayTrace(world, player, FluidMode.NONE);

				if(movingobjectposition == null)
					return;
				if(movingobjectposition.getType() == RayTraceResult.Type.BLOCK)
				{
					double i = movingobjectposition.getHitVec().getX();
					double j = movingobjectposition.getHitVec().getY();
					double k = movingobjectposition.getHitVec().getZ();
					boolean flag = false;

					BlockPos position = new BlockPos(i, j, k);

					if(world.getBlockState(position).getBlock().equals(CCubesBlocks.CHANCE_CUBE))
					{
						TileChanceCube te = ((TileChanceCube) world.getTileEntity(new BlockPos(i, j, k)));
						te.setScanned(true);
						CCubesPacketHandler.CHANNEL.sendToServer(new PacketCubeScan(te.getPos()));
						flag = true;
						RenderEvent.setLookingAtChance(te.getChance());
					}
					else if(world.getBlockState(position).getBlock().equals(CCubesBlocks.CHANCE_ICOSAHEDRON))
					{
						TileChanceD20 te = ((TileChanceD20) world.getTileEntity(new BlockPos(i, j, k)));
						te.setScanned(true);
						CCubesPacketHandler.CHANNEL.sendToServer(new PacketCubeScan(te.getPos()));
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
							if(!s.isEmpty() && s.getItem() instanceof ItemChancePendant)
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
