package chanceCubes.listeners;

import chanceCubes.util.Scheduler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class TickListener
{
	@SubscribeEvent
	public void onTick(ServerTickEvent.Pre event)
	{
		Scheduler.tickTasks();
	}
}
