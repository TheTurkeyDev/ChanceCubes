package chanceCubes.network;

import chanceCubes.CCubesCore;
import chanceCubes.tileentities.TileChanceD20;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTriggerD20 implements IMessage
{

	public int x;
	public int y;
	public int z;

	public PacketTriggerD20()
	{
	}

	public PacketTriggerD20(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);

	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
	}

	public static final class Handler implements IMessageHandler<PacketTriggerD20, IMessage>
	{
		@Override
		public IMessage onMessage(PacketTriggerD20 message, MessageContext ctx)
		{
			TileEntity ico;
			
			if((ico = CCubesCore.proxy.getClientPlayer().worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z))) != null)
				if(ico instanceof TileChanceD20)
					((TileChanceD20) ico).startBreaking(CCubesCore.proxy.getClientPlayer());

			return null;

		}
	}

}
