package chanceCubes.client.listeners;

import chanceCubes.util.Location3I;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldEvent;

public class WorldRenderListener
{
	public static Location3I pos1;
	public static Location3I pos2;

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onGuiRender(RenderWorldEvent.Post event)
	{
		if(pos1 != null && pos2 != null)
		{
			AxisAlignedBB box = AxisAlignedBB.getBoundingBox(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ());
			RenderGlobal.drawOutlinedBoundingBox(box, 1);
		}
	}
}
