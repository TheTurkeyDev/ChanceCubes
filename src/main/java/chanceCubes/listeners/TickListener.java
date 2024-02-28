package chanceCubes.listeners;

import chanceCubes.util.Scheduler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.event.TickEvent.ServerTickEvent;
import net.neoforged.neoforge.event.TickEvent.Type;
import net.neoforged.neoforge.event.TickEvent.Phase;

public class TickListener
{
	@SubscribeEvent
	public void onTick(ServerTickEvent event)
	{
		if(event.side == LogicalSide.SERVER && event.type == Type.SERVER && event.phase == Phase.START)
			Scheduler.tickTasks();
	}
}
