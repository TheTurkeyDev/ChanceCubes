package chanceCubes.client.listeners;

import chanceCubes.util.SchematicUtil;
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
	public static final float NO_Z_FIGHTING_PLOX = 0.01f;
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onGuiRender(RenderWorldLastEvent event)
	{
		if(SchematicUtil.selectionPoints[0] != null && SchematicUtil.selectionPoints[1] != null)
		{
			GlStateManager.pushMatrix();

			Entity entity = Minecraft.getInstance().player;
			double interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks();
			double interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() + 1.625;
			double interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks();

			GlStateManager.translated(-interpPosX, -interpPosY, -interpPosZ);
			GlStateManager.disableTexture();
			GlStateManager.enableBlend();
			GlStateManager.enableAlphaTest();
			GlStateManager.disableLighting();
			GlStateManager.lineWidth(2f);
			GlStateManager.begin(GL11.GL_LINES);

			BlockPos pos1 = SchematicUtil.selectionPoints[0];
			BlockPos pos2 = SchematicUtil.selectionPoints[1];
			float lowX = Math.min(pos1.getX(), pos2.getX()) - NO_Z_FIGHTING_PLOX;
			float highX = Math.max(pos1.getX(), pos2.getX()) + NO_Z_FIGHTING_PLOX;
			float lowY = Math.min(pos1.getY(), pos2.getY()) - NO_Z_FIGHTING_PLOX;
			float highY = Math.max(pos1.getY(), pos2.getY()) + NO_Z_FIGHTING_PLOX;
			float lowZ = Math.min(pos1.getZ(), pos2.getZ()) - NO_Z_FIGHTING_PLOX;
			float highZ = Math.max(pos1.getZ(), pos2.getZ()) + NO_Z_FIGHTING_PLOX;

			GlStateManager.color4f(0.9f, 0.0f, 0.5f, 1f);


			GL11.glVertex3f(lowX, lowY, lowZ);
			GL11.glVertex3f(highX, lowY, lowZ);
			GL11.glVertex3f(lowX, lowY, highZ);
			GL11.glVertex3f(highX, lowY, highZ);
			GL11.glVertex3f(lowX, highY, lowZ);
			GL11.glVertex3f(highX, highY, lowZ);
			GL11.glVertex3f(lowX, highY, highZ);
			GL11.glVertex3f(highX, highY, highZ);

			GL11.glVertex3f(lowX, lowY, lowZ);
			GL11.glVertex3f(lowX, lowY, highZ);
			GL11.glVertex3f(highX, lowY, lowZ);
			GL11.glVertex3f(highX, lowY, highZ);
			GL11.glVertex3f(lowX, highY, lowZ);
			GL11.glVertex3f(lowX, highY, highZ);
			GL11.glVertex3f(highX, highY, lowZ);
			GL11.glVertex3f(highX, highY, highZ);

			GL11.glVertex3f(lowX, lowY, lowZ);
			GL11.glVertex3f(lowX, highY, lowZ);
			GL11.glVertex3f(highX, lowY, lowZ);
			GL11.glVertex3f(highX, highY, lowZ);
			GL11.glVertex3f(lowX, lowY, highZ);
			GL11.glVertex3f(lowX, highY, highZ);
			GL11.glVertex3f(highX, lowY, highZ);
			GL11.glVertex3f(highX, highY, highZ);

			GlStateManager.end();
			GlStateManager.enableLighting();
			GlStateManager.enableTexture();
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
	}
}