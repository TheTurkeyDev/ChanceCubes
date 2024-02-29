package chanceCubes.network;

import chanceCubes.CCubesCore;
import chanceCubes.containers.CreativePendantContainer;
import chanceCubes.items.CCubesItems;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.Optional;

public class CCubesPacketHandler
{

	public CCubesPacketHandler(Connection connection)
	{
	}

	public void handleCreativePendant(PacketCreativePendant msg, CustomPayloadEvent.Context ctx)
	{
		ctx.enqueueWork(() ->
		{
			Player player = ctx.getSender();
			if(player == null)
				return;

			try
			{
				AbstractContainerMenu c = player.containerMenu;
				if(c instanceof CreativePendantContainer container)
				{
					ItemStack cCubes = container.getChanceCubesInPendant();
					if(!cCubes.isEmpty() && cCubes.getItem() instanceof ItemChanceCube)
						((ItemChanceCube) cCubes.getItem()).setChance(cCubes, msg.getChanceValue());
				}
			} catch(Exception e)
			{
				CCubesCore.logger.error("Chance Cubes has failed to set the chance of a cube due to a packet failure! Please Inform Turkey of this!");
			}
		});
		ctx.setPacketHandled(true);
	}

	public void handleRewardSelector(PacketRewardSelector msg, CustomPayloadEvent.Context ctx)
	{
		ctx.enqueueWork(() ->
		{
			ItemStack stack = ctx.getSender().getInventory().getSelected();
			if(!stack.isEmpty() && (stack.getItem().equals(CCubesItems.REWARD_SELECTOR_PENDANT.get()) || stack.getItem().equals(CCubesItems.SINGLE_USE_REWARD_SELECTOR_PENDANT.get())))
			{
				CompoundTag nbt = stack.getTag();
				if(nbt == null)
					nbt = new CompoundTag();
				nbt.putString("Reward", msg.getReward());
				stack.setTag(nbt);
			}
		});
		ctx.setPacketHandled(true);
	}

	public void handleTriggerD20(PacketTriggerD20 msg, CustomPayloadEvent.Context ctx)
	{
		ctx.enqueueWork(() ->
		{
			Optional<Level> levelOpt = LogicalSidedProvider.CLIENTWORLD.get(ctx.getDirection().getReceptionSide());
			if(levelOpt.isPresent())
			{
				BlockEntity ico;
				Level level = levelOpt.get();
				if((ico = level.getBlockEntity(msg.pos)) != null)
					if(ico instanceof TileChanceD20 && !level.players().isEmpty())
						((TileChanceD20) ico).startBreaking(level.players().get(0));
			}
		});
		ctx.setPacketHandled(true);
	}

	public void handleCubeScan(PacketCubeScan msg, CustomPayloadEvent.Context ctx)
	{
		ctx.enqueueWork(() ->
		{
			BlockEntity te = ctx.getSender().level().getBlockEntity(msg.pos);
			if(te instanceof TileChanceCube)
				((TileChanceCube) te).setScanned(true);
			else if(te instanceof TileChanceD20)
				((TileChanceD20) te).setScanned(true);
		});
		ctx.setPacketHandled(true);
	}
}
