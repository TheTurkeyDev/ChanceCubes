package chanceCubes.network;

import chanceCubes.CCubesCore;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class CCubesPacketHandler
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(CCubesCore.MODID);
	private static int id = 0;
	
	public static void init()
	{
		INSTANCE.registerMessage(PacketCreativePendant.Handler.class, PacketCreativePendant.class, id++, Side.SERVER);
		INSTANCE.registerMessage(PacketRewardSelector.Handler.class, PacketRewardSelector.class, id++, Side.SERVER);
		INSTANCE.registerMessage(PacketTriggerD20.Handler.class, PacketTriggerD20.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(PacketParticle.Handler.class, PacketParticle.class, id++, Side.CLIENT);
	}
}
