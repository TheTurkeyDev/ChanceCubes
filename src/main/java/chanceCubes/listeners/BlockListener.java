package chanceCubes.listeners;

import chanceCubes.client.listeners.RenderEvent;
import chanceCubes.util.Scheduler;
import chanceCubes.util.SchematicUtil;
import chanceCubes.util.Task;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickEmpty;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickEmpty;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockListener
{
	private boolean setdelay = false;

	@SubscribeEvent
	public void onBlockBreak(BreakEvent event)
	{
		EntityPlayer player = event.getPlayer();
		BlockPos pos = event.getPos();
		if(this.setSchematicPoint(1, player, pos))
			event.setCanceled(true);
	}

	@SubscribeEvent
	public void onBlockInteract(RightClickBlock event)
	{
		if(this.setSchematicPoint(2, event.getEntityPlayer(), event.getPos()))
			event.setCanceled(true);
	}

	@SubscribeEvent
	public void onAirInteractRight(RightClickEmpty event)
	{
		this.setSchematicPoint(2, event.getEntityPlayer(), event.getPos());
	}

	@SubscribeEvent
	public void onAirInteractLeft(LeftClickEmpty event)
	{
		this.setSchematicPoint(1, event.getEntityPlayer(), event.getPos());
	}

	public boolean setSchematicPoint(int point, EntityPlayer player, BlockPos pos)
	{
		if(Minecraft.getInstance().isSingleplayer() && RenderEvent.isCreatingSchematic() && !setdelay)
		{
			if(player.abilities.isCreativeMode)
			{
				boolean flag = false;
				if(point == 1)
				{
					SchematicUtil.selectionPoints[0] = pos;
					player.sendMessage(new TextComponentString("Point 1 set"));
					flag = true;
				}
				else if(point == 2)
				{
					SchematicUtil.selectionPoints[1] = pos;
					player.sendMessage(new TextComponentString("Point 2 set"));
					flag = true;
				}

				if(flag)
				{
					setdelay = true;
					Scheduler.scheduleTask(new Task("Schematic_Point_Set_Delay", 10)
					{

						@Override
						public void callback()
						{
							setdelay = false;
						}

					});
					return true;
				}
			}
		}
		return false;
	}
}