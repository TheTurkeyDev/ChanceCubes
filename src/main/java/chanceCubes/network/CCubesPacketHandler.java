package chanceCubes.network;

import chanceCubes.CCubesCore;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class CCubesPacketHandler
{
	private static int id = 0;
	public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
	            .named(new ResourceLocation(CCubesCore.MODID, "packets"))
	            .clientAcceptedVersions(a -> true)
	            .serverAcceptedVersions(a -> true)
	            .networkProtocolVersion(() -> NetworkHooks.NETVERSION)
	            .simpleChannel();

	public static void init()
	{
		INSTANCE.registerMessage(id++, PacketCreativePendant.class, PacketCreativePendant::encode, PacketCreativePendant::decode, PacketCreativePendant::handle);
		
		INSTANCE.registerMessage(PacketRewardSelector.Handler.class, PacketRewardSelector.class, id++, Side.SERVER);
		INSTANCE.registerMessage(PacketTriggerD20.Handler.class, PacketTriggerD20.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(PacketParticle.Handler.class, PacketParticle.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(PacketCubeScan.Handler.class, PacketCubeScan.class, id++, Side.SERVER);
		
		//Reference
		//INSTANCE.registerMessage(id++, FMLPlayMessages.OpenContainer.class, FMLPlayMessages.OpenContainer::encode, FMLPlayMessages.OpenContainer::decode, FMLPlayMessages.OpenContainer::handle);
	}
}
