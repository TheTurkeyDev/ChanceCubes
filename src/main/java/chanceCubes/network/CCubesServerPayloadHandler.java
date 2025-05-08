package chanceCubes.network;

import chanceCubes.CCubesCore;
import chanceCubes.components.CCubesDataComponents;
import chanceCubes.containers.CreativePendantContainer;
import chanceCubes.items.CCubesItems;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class CCubesServerPayloadHandler
{
	private static final CCubesServerPayloadHandler INSTANCE = new CCubesServerPayloadHandler();

	public static CCubesServerPayloadHandler getInstance()
	{
		return INSTANCE;
	}

	public void handleCreativePendant(final PacketCreativePendant msg, final IPayloadContext context)
	{
		context.enqueueWork(() -> {
					Player player = context.player();
					if(player == null)
						return;

					try
					{
						AbstractContainerMenu c = player.containerMenu;
						if(c instanceof CreativePendantContainer container)
						{
							ItemStack cCubes = container.getChanceCubesInPendant();
							if(!cCubes.isEmpty() && cCubes.getItem() instanceof ItemChanceCube)
								((ItemChanceCube) cCubes.getItem()).setChance(cCubes, msg.chanceValue());
						}
					} catch(Exception e)
					{
						CCubesCore.logger.error("Chance Cubes has failed to set the chance of a cube due to a packet failure! Please Inform Turkey of this!");
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.disconnect(Component.translatable("chancecubes.networking.creative_pendant.failed", e.getMessage()));
					return null;
				});
	}

	public void handleRewardSelector(final PacketRewardSelector msg, final IPayloadContext context)
	{
		context.enqueueWork(() -> {
					Player player = context.player();
					if(player == null)
						return;

					ItemStack stack = player.getInventory().getSelected();
					if(!stack.isEmpty() && (stack.getItem().equals(CCubesItems.REWARD_SELECTOR_PENDANT.get()) || stack.getItem().equals(CCubesItems.SINGLE_USE_REWARD_SELECTOR_PENDANT.get())))
					{
						stack.set(CCubesDataComponents.REWARD, msg.reward());
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.disconnect(Component.translatable("chancecubes.networking.reward_selector.failed", e.getMessage()));
					return null;
				});
	}

	public void handleCubeScan(final PacketCubeScan msg, final IPayloadContext context)
	{
		context.enqueueWork(() -> {
					Player player = context.player();
					if(player == null)
						return;
					Level level = player.level();

					BlockEntity te = level.getBlockEntity(msg.pos());
					if(te instanceof TileChanceCube)
						((TileChanceCube) te).setScanned(true);
					else if(te instanceof TileChanceD20)
						((TileChanceD20) te).setScanned(true);
				})
				.exceptionally(e -> {
					// Handle exception
					context.disconnect(Component.translatable("chancecubes.networking.cube_scan.failed", e.getMessage()));
					return null;
				});
	}
}
