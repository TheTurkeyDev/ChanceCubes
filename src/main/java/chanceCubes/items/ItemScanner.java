package chanceCubes.items;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.listeners.RenderEvent;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.network.PacketCubeScan;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class ItemScanner extends BaseChanceCubesItem
{
	public ItemScanner()
	{
		super((new Item.Properties()).stacksTo(1), "scanner");
	}

//	@Override
//	public UseAction getUseAction(ItemStack stack)
//	{
//		return UseAction.BLOCK;
//	}

	@Override
	public int getUseDuration(ItemStack stack)
	{
		return 72000;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean isSelected)
	{
		if(!level.isClientSide())
			return;

		if(entity instanceof Player player)
		{
			RenderEvent.setLookingAt(false);
			if(isSelected)
			{
				BlockHitResult movingobjectposition = Item.getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);

				if(movingobjectposition.getType() == BlockHitResult.Type.BLOCK)
				{
					double i = movingobjectposition.getBlockPos().getX();
					double j = movingobjectposition.getBlockPos().getY();
					double k = movingobjectposition.getBlockPos().getZ();
					boolean flag = false;

					BlockPos position = new BlockPos(i, j, k);

					if(level.getBlockState(position).getBlock().equals(CCubesBlocks.CHANCE_CUBE))
					{
						TileChanceCube te = ((TileChanceCube) level.getBlockEntity(new BlockPos(i, j, k)));
						if(te != null)
						{
							te.setScanned(true);
							CCubesPacketHandler.CHANNEL.sendToServer(new PacketCubeScan(te.getBlockPos()));
							flag = true;
							RenderEvent.setLookingAtChance(te.getChance());
						}
					}
//					else if(level.getBlockState(position).getBlock().equals(CCubesBlocks.CHANCE_ICOSAHEDRON))
//					{
//						TileChanceD20 te = ((TileChanceD20) level.getBlockEntity(new BlockPos(i, j, k)));
//						if(te != null)
//						{
//							te.setScanned(true);
//							CCubesPacketHandler.CHANNEL.sendToServer(new PacketCubeScan(te.getBlockPos()));
//							flag = true;
//							RenderEvent.setLookingAtChance(te.getChance());
//						}
//					}
					else if(level.getBlockState(position).getBlock().equals(CCubesBlocks.GIANT_CUBE))
					{
						RenderEvent.setLookingAtChance(-201);
						RenderEvent.setLookingAt(true);
						RenderEvent.setChanceIncrease(0);
					}

					if(flag)
					{
						RenderEvent.setLookingAt(true);
						int chanceInc = 0;
						for(ItemStack s : player.getInventory().items)
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
