package chanceCubes.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import chanceCubes.CCubesCore;

public class CCubesPacketHandler
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(CCubesCore.MODID);
	private static int id = 0;
	
	public static void init()
	{
		INSTANCE.registerMessage(PacketCreativePendant.Handler.class, PacketCreativePendant.class, id++, Side.SERVER);
		INSTANCE.registerMessage(PacketRewardSelector.Handler.class, PacketRewardSelector.class, id++, Side.SERVER);
		INSTANCE.registerMessage(PacketTriggerD20.Handler.class, PacketTriggerD20.class, id++, Side.CLIENT);
	}
}
