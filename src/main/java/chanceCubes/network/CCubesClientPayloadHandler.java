package chanceCubes.network;

import chanceCubes.tileentities.TileChanceD20;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class CCubesClientPayloadHandler
{
	private static final CCubesClientPayloadHandler INSTANCE = new CCubesClientPayloadHandler();

	public static CCubesClientPayloadHandler getInstance()
	{
		return INSTANCE;
	}

	public void handleTrigger(final PacketTriggerD20 msg, final IPayloadContext context)
	{
		context.enqueueWork(() -> {
					//Open Captcha Screen
					Minecraft mc = Minecraft.getInstance();
					Level level = mc.level;
					if (level != null)
					{
						BlockEntity ico;
						if((ico = level.getBlockEntity(msg.pos())) != null)
							if(ico instanceof TileChanceD20 && !level.players().isEmpty())
								((TileChanceD20) ico).startBreaking(level.players().get(0));
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.disconnect(Component.translatable("chancecubes.networking.trigger_d20.failed", e.getMessage()));
					return null;
				});
	}
}
