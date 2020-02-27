package chanceCubes.listeners;

import chanceCubes.util.Scheduler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.TickEvent.Type;
import net.minecraftforge.event.TickEvent.Phase;

public class TickListener
{
	@SubscribeEvent
	public void onTick(ServerTickEvent event)
	{
		if(event.side == LogicalSide.SERVER && event.type == Type.SERVER && event.phase == Phase.START)
			Scheduler.tickTasks();
	}
}
