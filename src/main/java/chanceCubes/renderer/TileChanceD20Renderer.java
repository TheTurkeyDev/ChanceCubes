package chanceCubes.renderer;

import java.awt.Color;
import java.util.Random;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import chanceCubes.tileentities.TileChanceD20;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;

@OnlyIn(Dist.CLIENT)
public class TileChanceD20Renderer extends TileEntityRenderer<TileChanceD20>
{
	private static final Random random = new Random();

	private static final float HOVER_SPEED = 6F;

	@Override
	public void render(TileChanceD20 d20, double x, double y, double z, float partialTicks, int destroyStage)
	{
		float wave = d20.getStage() == 0 ? MathHelper.sin((((d20.getWorld().getGameTime() % (HOVER_SPEED * 1000F) + partialTicks) / (HOVER_SPEED * 1000F)) + random.nextFloat()) * 360F) : ((d20.getStage() + partialTicks) / 10f);
		d20.wave = wave;

		GlStateManager.pushMatrix();

		GlStateManager.translated(x + 0.5, y + 1.5 + wave * 0.15, z + 2.5);
		Tessellator tessellator = Tessellator.getInstance();
		float f1 = ((float) d20.getWorld().getGameTime() % 750 + partialTicks) / 750.0F;

		random.setSeed(432L);
		BufferBuilder worldrenderer = tessellator.getBuffer();
		RenderHelper.disableStandardItemLighting();

		GlStateManager.disableTexture();
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GlStateManager.disableAlphaTest();
		GlStateManager.enableCull();
		GlStateManager.depthMask(false);
		GlStateManager.pushMatrix();
		GlStateManager.translatef(0.0F, -1.0F, -2.0F);
		GlStateManager.scaled(0.25, 0.25, 0.25);

		int stage = 0;

		float color = (d20.getWorld().getGameTime() % 75) / 75F;
		Color tmpClr = new Color(Color.HSBtoRGB(color, 1F, 1F));
		int r = tmpClr.getRed();
		int g = tmpClr.getGreen();
		int b = tmpClr.getBlue();

		int alpha = 255;

		for(int i = 0; i < (16 + (stage / 10)); ++i)
		{
			GlStateManager.rotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotatef(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotatef(random.nextFloat() * 360.0F + f1 * 90.0F, 0.0F, 0.0F, 1.0F);

			float f3 = random.nextFloat() * 20.0F;
			float f4 = random.nextFloat() * 2.0F;
			worldrenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
			worldrenderer.pos(0.0D, 0.0D, 0.0D).color(r, g, b, alpha).endVertex();
			worldrenderer.pos(-0.866D * (double) f4, f3, -0.5F * f4).color(r, g, b, 0).endVertex();
			worldrenderer.pos(0.0D, f3, 1.0F * f4).color(r, g, b, 0).endVertex();
			worldrenderer.pos(-0.866D * (double) f4, f3, -0.5F * f4).color(r, g, b, 0).endVertex();
			tessellator.draw();
		}

		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableTexture();
		GlStateManager.enableAlphaTest();
		RenderHelper.enableStandardItemLighting();

		GlStateManager.popMatrix();
	}
}