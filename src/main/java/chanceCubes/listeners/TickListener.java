package chanceCubes.listeners;

import chanceCubes.util.Scheduler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

public class TickListener
{
	@SubscribeEvent
	public void onTick(ServerTickEvent event)
	{
		if(event.side == LogicalSide.SERVER && event.type == Type.SERVER && event.phase == Phase.START)
			Scheduler.tickTasks();
	}
}
