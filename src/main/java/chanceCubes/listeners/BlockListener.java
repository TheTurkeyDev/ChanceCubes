package chanceCubes.listeners;

import chanceCubes.client.listeners.RenderEvent;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.SchematicUtil;
import chanceCubes.util.Task;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickEmpty;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickEmpty;
import net.minecraftforge.event.level.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockListener
{
	private boolean setDelay = false;

	@SubscribeEvent
	public void onBlockBreak(BreakEvent event)
	{
		if(this.setSchematicPoint(1, event.getPlayer(), event.getPos()))
			event.setCanceled(true);
	}

	@SubscribeEvent
	public void onBlockInteract(RightClickBlock event)
	{
		if(this.setSchematicPoint(2, event.getEntity(), event.getPos()))
			event.setCanceled(true);
	}

	@SubscribeEvent
	public void onAirInteractRight(RightClickEmpty event)
	{
		this.setSchematicPoint(2, event.getEntity(), event.getPos());
	}

	@SubscribeEvent
	public void onAirInteractLeft(LeftClickEmpty event)
	{
		this.setSchematicPoint(1, event.getEntity(), event.getPos());
	}

	public boolean setSchematicPoint(int point, Player player, BlockPos pos)
	{
		if(Minecraft.getInstance().isLocalServer() && RenderEvent.isCreatingSchematic() && !setDelay)
		{
			if(player.isCreative())
			{
				boolean flag = false;
				if(point == 1)
				{
					SchematicUtil.selectionPoints[0] = pos;
					RewardsUtil.sendMessageToPlayer(player, "Point 1 set");
					flag = true;
				}
				else if(point == 2)
				{
					SchematicUtil.selectionPoints[1] = pos;
					RewardsUtil.sendMessageToPlayer(player, "Point 2 set");
					flag = true;
				}

				if(flag)
				{
					setDelay = true;
					Scheduler.scheduleTask(new Task("Schematic_Point_Set_Delay", 10)
					{

						@Override
						public void callback()
						{
							setDelay = false;
						}

					});
					return true;
				}
			}
		}
		return false;
	}
}