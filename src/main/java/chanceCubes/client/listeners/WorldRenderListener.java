package chanceCubes.client.listeners;

import org.lwjgl.opengl.GL11;

import chanceCubes.CCubesCore;
import chanceCubes.util.SchematicUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldRenderListener
{
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onGuiRender(RenderWorldLastEvent event)
	{
		if(SchematicUtil.selectionPoints[0] != null && SchematicUtil.selectionPoints[1] != null)
		{
			GlStateManager.pushMatrix();
			
			Entity entity = CCubesCore.proxy.getClientPlayer();
			double interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks();
	        double interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks();
	        double interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks();

	        GlStateManager.translate(-interpPosX, -interpPosY, -interpPosZ);
	        GlStateManager.disableTexture2D();
	        GlStateManager.enableBlend();
	        GlStateManager.enableAlpha();
	        GlStateManager.disableLighting();
	        GlStateManager.glLineWidth(2f);
	        GlStateManager.glBegin(GL11.GL_LINES);

			BlockPos pos1 = SchematicUtil.selectionPoints[0];
			BlockPos pos2 = SchematicUtil.selectionPoints[1];
			int lowX = pos1.getX() < pos2.getX() ? pos1.getX() : pos2.getX();
			int highX = pos1.getX() > pos2.getX() ? pos1.getX() : pos2.getX();
			int lowY = pos1.getY() < pos2.getY() ? pos1.getY() : pos2.getY();
			int highY = pos1.getY() > pos2.getY() ? pos1.getY() : pos2.getY();
			int lowZ = pos1.getZ() < pos2.getZ() ? pos1.getZ() : pos2.getZ();
			int highZ = pos1.getZ() > pos2.getZ() ? pos1.getZ() : pos2.getZ();

			GlStateManager.color(0.9f, 0.0f, 0.5f, 1f);

			
			GL11.glVertex3d(lowX, lowY, lowZ);
			GL11.glVertex3d(highX, lowY, lowZ);
			GL11.glVertex3d(lowX, lowY, highZ);
			GL11.glVertex3d(highX, lowY, highZ);
			GL11.glVertex3d(lowX, highY, lowZ);
			GL11.glVertex3d(highX, highY, lowZ);
			GL11.glVertex3d(lowX, highY, highZ);
			GL11.glVertex3d(highX, highY, highZ);

			GL11.glVertex3d(lowX, lowY, lowZ);
			GL11.glVertex3d(lowX, lowY, highZ);
			GL11.glVertex3d(highX, lowY, lowZ);
			GL11.glVertex3d(highX, lowY, highZ);
			GL11.glVertex3d(lowX, highY, lowZ);
			GL11.glVertex3d(lowX, highY, highZ);
			GL11.glVertex3d(highX, highY, lowZ);
			GL11.glVertex3d(highX, highY, highZ);

			GL11.glVertex3d(lowX, lowY, lowZ);
			GL11.glVertex3d(lowX, highY, lowZ);
			GL11.glVertex3d(highX, lowY, lowZ);
			GL11.glVertex3d(highX, highY, lowZ);
			GL11.glVertex3d(lowX, lowY, highZ);
			GL11.glVertex3d(lowX, highY, highZ);
			GL11.glVertex3d(highX, lowY, highZ);
			GL11.glVertex3d(highX, highY, highZ);

			GlStateManager.glEnd();
			GlStateManager.enableLighting();
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
	}
}