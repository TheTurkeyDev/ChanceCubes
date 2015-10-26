package chanceCubes.listeners;

import chanceCubes.util.Scheduler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import cpw.mods.fml.relauncher.Side;

public class TickListener
{

	@SubscribeEvent
	public void onTick(ServerTickEvent event)
	{
		if(event.side == Side.SERVER && event.type == Type.SERVER && event.phase == Phase.START)
			Scheduler.tickTasks();
	}
}
