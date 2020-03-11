package chanceCubes.client.listeners;

import chanceCubes.util.SchematicUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class WorldRenderListener
{
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onGuiRender(RenderWorldLastEvent event)
	{
		MatrixStack matrixStack = event.getMatrixStack();
		if(SchematicUtil.selectionPoints[0] != null && SchematicUtil.selectionPoints[1] != null)
		{
			matrixStack.push();

			Entity entity = Minecraft.getInstance().player;
			double interpPosX = entity.lastTickPosX + (entity.getPosX() - entity.lastTickPosX) * event.getPartialTicks();
			double interpPosY = entity.lastTickPosY + (entity.getPosY() - entity.lastTickPosY) * event.getPartialTicks();
			double interpPosZ = entity.lastTickPosZ + (entity.getPosZ() - entity.lastTickPosZ) * event.getPartialTicks();

			matrixStack.translate(-interpPosX, -interpPosY, -interpPosZ);
			GlStateManager.disableTexture();
			GlStateManager.enableBlend();
			GlStateManager.enableAlphaTest();
			GlStateManager.disableLighting();
			GlStateManager.lineWidth(2f);
			//TODO
			//GlStateManager.begin(GL11.GL_LINES);

			BlockPos pos1 = SchematicUtil.selectionPoints[0];
			BlockPos pos2 = SchematicUtil.selectionPoints[1];
			int lowX = Math.min(pos1.getX(), pos2.getX());
			int highX = Math.max(pos1.getX(), pos2.getX());
			int lowY = Math.min(pos1.getY(), pos2.getY());
			int highY = Math.max(pos1.getY(), pos2.getY());
			int lowZ = Math.min(pos1.getZ(), pos2.getZ());
			int highZ = Math.max(pos1.getZ(), pos2.getZ());

			GlStateManager.color4f(0.9f, 0.0f, 0.5f, 1f);


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

			//GlStateManager.end();
			GlStateManager.enableLighting();
			GlStateManager.enableTexture();
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
	}
}