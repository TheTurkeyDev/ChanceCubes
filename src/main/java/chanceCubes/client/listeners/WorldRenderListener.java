package chanceCubes.client.listeners;

import org.lwjgl.opengl.GL11;

import chanceCubes.CCubesCore;
import chanceCubes.util.Location3I;
import chanceCubes.util.SchematicUtil;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class WorldRenderListener
{
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onGuiRender(RenderWorldLastEvent event)
	{
		if(SchematicUtil.selectionPoints[0] != null && SchematicUtil.selectionPoints[1] != null)
		{
			GL11.glPushMatrix();
			
			Entity entity = CCubesCore.proxy.getClientPlayer();
			double interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.partialTicks;
	        double interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.partialTicks;
	        double interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.partialTicks;

	        GL11.glTranslated(-interpPosX, -interpPosY, -interpPosZ);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glLineWidth(1.5F);
			GL11.glBegin(GL11.GL_LINES);

			Location3I pos1 = SchematicUtil.selectionPoints[0];
			Location3I pos2 = SchematicUtil.selectionPoints[1];
			int lowX = pos1.getX() < pos2.getX() ? pos1.getX() : pos2.getX();
			int highX = pos1.getX() > pos2.getX() ? pos1.getX() : pos2.getX();
			int lowY = pos1.getY() < pos2.getY() ? pos1.getY() : pos2.getY();
			int highY = pos1.getY() > pos2.getY() ? pos1.getY() : pos2.getY();
			int lowZ = pos1.getZ() < pos2.getZ() ? pos1.getZ() : pos2.getZ();
			int highZ = pos1.getZ() > pos2.getZ() ? pos1.getZ() : pos2.getZ();

			GL11.glColor4d(0.9, 0.0, 0.5, 1);

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

			GL11.glEnd();
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
		}
	}
}
