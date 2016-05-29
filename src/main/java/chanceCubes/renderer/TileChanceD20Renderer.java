package chanceCubes.renderer;

import java.awt.Color;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import chanceCubes.tileentities.TileChanceD20;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;

public class TileChanceD20Renderer extends TileEntitySpecialRenderer<TileChanceD20>
{
	private static final Random random = new Random();
	private Color tmpClr;

	private static final float HOVER_SPEED = 6F;

	@Override
	public void renderTileEntityAt(TileChanceD20 d20, double x, double y, double z, float partialTicks, int var9)
	{
		float wave = d20.getStage() == 0 ? MathHelper.sin((((d20.getWorld().getTotalWorldTime() % (HOVER_SPEED * 1000F) + partialTicks) / (HOVER_SPEED * 1000F)) + random.nextFloat()) * 360F) * 0.25f : ((d20.getStage() + partialTicks) / 10f);

		GlStateManager.pushMatrix();

		GlStateManager.translate(x + 0.5F, y + 1.5F + wave, z + 2.5F);
		Tessellator tessellator = Tessellator.getInstance();
		float f1 = ((float) d20.getWorld().getTotalWorldTime() % 750 + partialTicks) / 750.0F;

		random.setSeed(432L);
		VertexBuffer worldrenderer = tessellator.getBuffer();
		RenderHelper.disableStandardItemLighting();

		GlStateManager.disableTexture2D();
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GlStateManager.disableAlpha();
		GlStateManager.enableCull();
		GlStateManager.depthMask(false);
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, -1.0F, -2.0F);
		GlStateManager.scale(0.25, 0.25, 0.25);

		int stage = 0;

		float color = (d20.getWorld().getTotalWorldTime() % 75) / 75F;
		tmpClr = new Color(Color.HSBtoRGB(color, 1F, 1F));
		int r = tmpClr.getRed();
		int g = tmpClr.getGreen();
		int b = tmpClr.getBlue();

		int alpha = 255;

		for(int i = 0; i < (16 + (stage / 10)); ++i)
		{
			GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F + f1 * 90.0F, 0.0F, 0.0F, 1.0F);

			float f3 = random.nextFloat() * 20.0F;
			float f4 = random.nextFloat() * 2.0F;
			worldrenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
			worldrenderer.pos(0.0D, 0.0D, 0.0D).color(r, g, b, alpha).endVertex();
			worldrenderer.pos(-0.866D * (double) f4, (double) f3, (double) (-0.5F * f4)).color(r, g, b, 0).endVertex();
			worldrenderer.pos(0.866D * (double) f4, (double) f3, (double) (-0.5F * f4)).color(r, g, b, 0).endVertex();
			worldrenderer.pos(0.0D, (double) f3, (double) (1.0F * f4)).color(r, g, b, 0).endVertex();
			worldrenderer.pos(-0.866D * (double) f4, (double) f3, (double) (-0.5F * f4)).color(r, g, b, 0).endVertex();
			tessellator.draw();
		}

		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableTexture2D();
		GlStateManager.enableAlpha();
		RenderHelper.enableStandardItemLighting();

		GlStateManager.popMatrix();
	}
}