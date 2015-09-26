package chanceCubes.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import chanceCubes.items.CCubesItems;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketRewardSelector implements IMessage
{

	private String playerName;
	private String reward;

	public PacketRewardSelector()
	{
	}

	public PacketRewardSelector(String player, String reward)
	{
		this.playerName = player;
		this.reward = reward;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, playerName);
		ByteBufUtils.writeUTF8String(buf, reward);

	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.playerName = ByteBufUtils.readUTF8String(buf);
		this.reward = ByteBufUtils.readUTF8String(buf);

	}

	public static final class Handler implements IMessageHandler<PacketRewardSelector, IMessage>
	{
		@Override
		public IMessage onMessage(PacketRewardSelector message, MessageContext ctx)
		{
			ItemStack stack = MinecraftServer.getServer().getEntityWorld().getPlayerEntityByName(message.playerName).inventory.getCurrentItem();
			if(stack != null && stack.getItem().equals(CCubesItems.rewardSelectorPendant))
			{
				NBTTagCompound nbt = stack.getTagCompound();
				if(nbt == null)
					nbt = new NBTTagCompound();
				nbt.setString("Reward", message.reward);
				stack.setTagCompound(nbt);
			}
			return null;
		}
	}

}
