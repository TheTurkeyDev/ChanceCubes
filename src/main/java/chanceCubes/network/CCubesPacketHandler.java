package chanceCubes.network;

import chanceCubes.CCubesCore;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class CCubesPacketHandler
{
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(CCubesCore.MODID);
	private static int id = 0;
	
	public static void init()
	{
		INSTANCE.registerMessage(PacketCreativePendant.Handler.class, PacketCreativePendant.class, id++, Side.SERVER);
		INSTANCE.registerMessage(PacketRewardSelector.Handler.class, PacketRewardSelector.class, id++, Side.SERVER);
		INSTANCE.registerMessage(PacketTriggerD20.Handler.class, PacketTriggerD20.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(PacketParticle.Handler.class, PacketParticle.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(PacketCubeScan.Handler.class, PacketCubeScan.class, id++, Side.SERVER);
	}
}
