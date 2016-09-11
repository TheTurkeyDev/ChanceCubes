package chanceCubes.client.listeners;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldRenderListener
{
	public static BlockPos pos1;
	public static BlockPos pos2;

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onGuiRender(RenderWorldLastEvent event)
	{
		if(pos1 != null && pos2 != null)
		{
			AxisAlignedBB box = new AxisAlignedBB(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ());
			RenderGlobal.func_189697_a(box, 0.3f, 0.3f, 0.9f, 1.0f);
		}
	}
}