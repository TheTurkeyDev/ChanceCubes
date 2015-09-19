package chanceCubes.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

import org.apache.logging.log4j.Level;

import chanceCubes.CCubesCore;
import chanceCubes.containers.CreativePendantContainer;
import chanceCubes.items.CCubesItems;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.tileentities.TileChanceD20;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class CCubesPacket  implements IMessage
{
	private NBTTagCompound tags;

	public CCubesPacket(){
	}

	public CCubesPacket(NBTTagCompound tags) 
	{
		this.tags = tags;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.tags = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, this.tags);
	}

	public static class HandlerServer implements IMessageHandler<CCubesPacket,IMessage>
	{
		@Override
		public IMessage onMessage(CCubesPacket packet, MessageContext ctx)
		{
			int id = packet.tags.hasKey("id")? packet.tags.getInteger("id") : -1;

			if(id == 0)
			{
				Container c = MinecraftServer.getServer().getEntityWorld().getPlayerEntityByName(packet.tags.getString("Player")).openContainer;
				
				if(c instanceof CreativePendantContainer)
				{
					CreativePendantContainer container = (CreativePendantContainer) c;
					ItemStack ccubes = container.getChanceCubesInPendant();
					if(ccubes != null)
					{
						if(ccubes.getItem() instanceof ItemChanceCube)
						{
							((ItemChanceCube)ccubes.getItem()).setChance(ccubes, packet.tags.getInteger("Chance"));
						}
					}
				}
			}
			else if(id == 1)
			{
				ItemStack stack =MinecraftServer.getServer().getEntityWorld().getPlayerEntityByName(packet.tags.getString("Player")).inventory.getCurrentItem();
				if(stack != null && stack.getItem().equals(CCubesItems.rewardSelectorPendant))
				{
					NBTTagCompound nbt = stack.getTagCompound();
					if(nbt == null)
						nbt = new NBTTagCompound();
					nbt.setString("Reward", packet.tags.getString("Reward"));
					stack.setTagCompound(nbt);
				}
			}
			else
				CCubesCore.logger.log(Level.ERROR, "Server received invalid packet!");
			return null;
		}
	}

	public static class HandlerClient implements IMessageHandler<CCubesPacket,IMessage>
	{
		@Override
		public IMessage onMessage(CCubesPacket packet, MessageContext ctx)
		{
			int id = packet.tags.hasKey("id")? packet.tags.getInteger("id") : -1;

			if(id == 0)
			{
				int x = packet.tags.getInteger("x");
				int y = packet.tags.getInteger("y");
				int z = packet.tags.getInteger("z");
				TileEntity ico;
				
				if((ico = CCubesCore.proxy.getClientPlayer().worldObj.getTileEntity(x, y, z)) != null)
					if(ico instanceof TileChanceD20)
						((TileChanceD20)ico).startBreaking(CCubesCore.proxy.getClientPlayer());
				
				
			}
			else
				CCubesCore.logger.log(Level.ERROR, "Received invalid packet on clientside!");
			return null;
		}
	}
}