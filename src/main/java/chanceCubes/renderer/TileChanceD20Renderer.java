package chanceCubes.renderer;

import java.awt.Color;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import chanceCubes.tileentities.TileChanceD20;
import chanceCubes.util.RenderUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel.OBJBakedModel;

public class TileChanceD20Renderer extends TileEntitySpecialRenderer<TileChanceD20>
{

	private static final float BASE_COLOR_SPEED = 75F;

	private static final Random random = new Random();

	private BlockRendererDispatcher blockRenderer;

	public TileChanceD20Renderer()
	{

	}

	@Override
	public void renderTileEntityAt(TileChanceD20 d20, double x, double y, double z, float partialTick, int var9)
	{
		d20.renderUpdate(partialTick, d20.getWorld().getWorldTime(), d20.getPos());
		int stage = d20.getStage();

		random.setSeed(RenderUtil.getCoordinateRandom(d20.getPos().getX(), d20.getPos().getY(), d20.getPos().getY()));

		// float rot = d20.rotation + (d20.rotationDelta * partialTick);
		float color = (d20.getWorld().getTotalWorldTime() % BASE_COLOR_SPEED + partialTick) / BASE_COLOR_SPEED;
		Color tmpClr = new Color(Color.HSBtoRGB(color + random.nextFloat(), 1F, 1F));

		if(blockRenderer == null)
			blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
		BlockPos blockpos = d20.getPos();
		IBlockState iblockstate = d20.getBlockType().getDefaultState();

		if(iblockstate.getMaterial() != Material.air)
		{
			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer vertexbuffer = tessellator.getBuffer();
			this.bindTexture(TextureMap.locationBlocksTexture);
			RenderHelper.disableStandardItemLighting();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.enableBlend();
			GlStateManager.disableCull();

			if(Minecraft.isAmbientOcclusionEnabled())
				GlStateManager.shadeModel(7425);
			else
				GlStateManager.shadeModel(7424);

			vertexbuffer.begin(7, DefaultVertexFormats.BLOCK);
			vertexbuffer.setTranslation((double) ((float) x - (float) blockpos.getX() + 0.5f), (double) ((float) y - (float) blockpos.getY() + d20.wave + 0.5f), (double) ((float) z - (float) blockpos.getZ() + 0.5f));
			World world = this.getWorld();
			
			IBakedModel model = this.blockRenderer.getModelForState(iblockstate);
			
			/*if(model instanceof OBJBakedModel)
			{
				OBJBakedModel baked = (OBJBakedModel) model;
				int rgb = ((tmpClr.getRed() & 0xFF) << 24) + ((tmpClr.getGreen() & 0xFF) << 16) + ((tmpClr.getBlue() & 0xFF) << 8) + (1 & 0xFF);
				baked.getModel().getMatLib().changeMaterialColor("CCIcosahedron", rgb);
			}*/

			this.renderStateModel(blockpos, iblockstate, model, vertexbuffer, world, false);

			vertexbuffer.setTranslation(0.0D, 0.0D, 0.0D);
			tessellator.draw();
			RenderHelper.enableStandardItemLighting();
		}

		GL11.glPushMatrix();

		GlStateManager.translate(x + 0.5F, y + 1.5F + d20.wave, z + 2.5F);
		Tessellator tessellator = Tessellator.getInstance();
		RenderHelper.disableStandardItemLighting();
		float f1 = ((float) d20.getWorld().getTotalWorldTime() % 750 + partialTick) / 750.0F;

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

	private boolean renderStateModel(BlockPos p_188186_1_, IBlockState p_188186_2_, IBakedModel model, VertexBuffer p_188186_3_, World p_188186_4_, boolean p_188186_5_)
	{
		return this.blockRenderer.getBlockModelRenderer().renderModel(p_188186_4_, model, p_188186_2_, p_188186_1_, p_188186_3_, p_188186_5_);
	}
}
