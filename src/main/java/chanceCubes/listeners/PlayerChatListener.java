package chanceCubes.listeners;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.rewards.rewardparts.ResponsePart;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerChatListener
{
	private List<ResponsePart> responseListeners = new ArrayList<ResponsePart>();

	public void registerListener(ResponsePart part)
	{
		responseListeners.add(part);
	}

	@SubscribeEvent
	public void onMessage(ServerChatEvent event)
	{
		for(int i = responseListeners.size() - 1; i >= 0; i--)
		{
			ResponsePart part = responseListeners.get(i);
			if(part.onResponse(event.getPlayer(), event.getMessage()))
			{
				responseListeners.remove(part);
			}
		}
	}
}
