package chanceCubes.renderer;

import java.awt.Color;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.model.obj.OBJModel.OBJBakedModel;

import org.lwjgl.opengl.GL11;

import chanceCubes.tileentities.TileChanceD20;
import chanceCubes.util.RenderUtil;

public class TileChanceD20Renderer extends TileEntitySpecialRenderer<TileChanceD20>
{

	private static final float BASE_COLOR_SPEED = 75F;
	private static final float HOVER_SPEED = 12F;

	private static final Random random = new Random();

	public TileChanceD20Renderer()
	{

	}

	@Override
	public void renderTileEntityAt(TileChanceD20 d20, double posX, double posY, double posZ, float partialTick, int var9)
	{

		int stage = d20.getStage();

		random.setSeed(RenderUtil.getCoordinateRandom(d20.getPos().getX(), d20.getPos().getY(), d20.getPos().getY()));

		float wave = stage == 0 ? MathHelper.sin((((d20.getWorld().getTotalWorldTime() % (HOVER_SPEED * 1000F) + partialTick) / (HOVER_SPEED * 1000F)) + random.nextFloat()) * 360F) : ((stage + partialTick) / 10f);
		float rot = d20.rotation + (d20.rotationDelta * partialTick);
		float color = (d20.getWorld().getTotalWorldTime() % BASE_COLOR_SPEED + partialTick) / BASE_COLOR_SPEED;

		GL11.glPushMatrix();

		GlStateManager.translate(posX + 0.5F, posY + 0.5F + wave * 0.1F, posZ + 0.5F);
		GlStateManager.rotate(rot, 0F, 1F, 0F);
		Color tmpClr = new Color(Color.HSBtoRGB(color + random.nextFloat(), 1F, 1F));
		GlStateManager.color(tmpClr.getRed() / 255F, tmpClr.getGreen() / 255F, tmpClr.getBlue() / 255F);

		// Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		OBJBakedModel baked = (OBJBakedModel) Minecraft.getMinecraft().getBlockRendererDispatcher().getModelFromBlockState(d20.getBlockType().getDefaultState(), d20.getWorld(), d20.getPos());
		// System.out.println(baked.getModel().getMatLib().getMaterialNames().get(0));
		// int r = tmpClr.getRed() & 0xFF;
		// int g = tmpClr.getGreen() & 0xFF;
		// int b = tmpClr.getBlue() & 0xFF;
		// int a = tmpClr.getAlpha() & 0xFF;

		int rgb = ((tmpClr.getRed() & 0xFF) << 24) + ((tmpClr.getGreen() & 0xFF) << 16) + ((tmpClr.getBlue() & 0xFF) << 8) + (tmpClr.getAlpha() & 0xFF);
		//System.out.println(baked.getModel().getMatLib().getMaterialNames().get(0));
		baked.getModel().getMatLib().changeMaterialColor("CCIcosahedron", rgb);
		baked.scheduleRebake();
		d20.getWorld().markBlockForUpdate(new BlockPos(posX, posY, posZ));

		GL11.glPopMatrix();

		GL11.glPushMatrix();

		GlStateManager.translate(posX + 0.5F, posY + 1.5F + wave * 0.1F, posZ + 2.5F);
		Tessellator tessellator = Tessellator.getInstance();
		RenderHelper.disableStandardItemLighting();
		float f1 = ((float) d20.getWorld().getTotalWorldTime() % 750 + partialTick) / 750.0F;

		random.setSeed(432L);
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
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

		int r = tmpClr.getRed();
		int g = tmpClr.getGreen();
		int b = tmpClr.getBlue();

		int alpha = (int) (255.0F * (1.0F));

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

		GL11.glPopMatrix();
	}
}
