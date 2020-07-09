package chanceCubes.renderer;

import chanceCubes.tileentities.TileChanceD20;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.Random;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

public class TileChanceD20Renderer extends TileEntityRenderer<TileChanceD20>
{
	private static final Random random = new Random();

	private static final float HOVER_SPEED = 6000F;
	private static final float ROTATION_SPEED = 100;
	private static final float MAGIC_NUM = (float) (Math.sqrt(3.0D) / 2.0D);

	public TileChanceD20Renderer(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TileChanceD20 d20, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
	{
		random.setSeed(432L);
		IVertexBuilder ivertexbuilder2 = bufferIn.getBuffer(RenderType.getLightning());
		matrixStackIn.push();

		float wave;
		if(d20.getStage() == 0)
			wave = MathHelper.sin((((d20.getWorld().getGameTime() % HOVER_SPEED + partialTicks) / HOVER_SPEED) + random.nextFloat()) * 360F) * 0.3f;
		else
			wave = ((d20.getStage() + partialTicks) / 70f);

		d20.wave = wave;
		float rotation = ((float) d20.getWorld().getGameTime() % ROTATION_SPEED + partialTicks) / ROTATION_SPEED;
		int f7 = 0;

		float color = (d20.getWorld().getGameTime() % 75) / 75F;
		Color tmpClr = new Color(Color.HSBtoRGB(color, 1F, 1F));
		int r = tmpClr.getRed();
		int g = tmpClr.getGreen();
		int b = tmpClr.getBlue();

		matrixStackIn.translate(0.5D, 0.5 + wave, 0.5D);

		for(int i = 0; (float) i < 5; ++i)
		{
			//TODO: They start and end in the same states
			matrixStackIn.rotate(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
			matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F));
			matrixStackIn.rotate(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
			matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F + rotation * 90.0F));
			float f3 = random.nextFloat() * 1.0F + 4.0F;
			float f4 = random.nextFloat() * 0.25F + 0.5f;
			Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
			int alpha = (int) (255.0F * (1.0F - f7));
			func_229061_a_(ivertexbuilder2, matrix4f, r, g, b, alpha);
			func_229060_a_(ivertexbuilder2, matrix4f, f3, f4, r, g, b);
			func_229062_b_(ivertexbuilder2, matrix4f, f3, f4, r, g, b);
			func_229061_a_(ivertexbuilder2, matrix4f, r, g, b, alpha);
			func_229062_b_(ivertexbuilder2, matrix4f, f3, f4, r, g, b);
			func_229063_c_(ivertexbuilder2, matrix4f, f3, f4, r, g, b);
			func_229061_a_(ivertexbuilder2, matrix4f, r, g, b, alpha);
			func_229063_c_(ivertexbuilder2, matrix4f, f3, f4, r, g, b);
			func_229060_a_(ivertexbuilder2, matrix4f, f3, f4, r, g, b);
		}

		matrixStackIn.pop();
	}

	private static void func_229061_a_(IVertexBuilder vertexBuilder, Matrix4f matrixis, int r, int g, int b, int alpha)
	{
		vertexBuilder.pos(matrixis, 0.0F, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
		vertexBuilder.pos(matrixis, 0.0F, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
	}

	private static void func_229060_a_(IVertexBuilder vertexBuilder, Matrix4f matrixis, float y, float p_229060_3_, int r, int g, int b)
	{
		vertexBuilder.pos(matrixis, -MAGIC_NUM * p_229060_3_, y, -0.5F * p_229060_3_).color(r, g, b, 0).endVertex();
	}

	private static void func_229062_b_(IVertexBuilder vertexBuilder, Matrix4f matrixis, float y, float p_229062_3_, int r, int g, int b)
	{
		vertexBuilder.pos(matrixis, MAGIC_NUM * p_229062_3_, y, -0.5F * p_229062_3_).color(r, g, b, 0).endVertex();
	}

	private static void func_229063_c_(IVertexBuilder vertexBuilder, Matrix4f matrixis, float y, float p_229063_3_, int r, int g, int b)
	{
		vertexBuilder.pos(matrixis, 0.0F, y, 1.0F * p_229063_3_).color(r, g, b, 0).endVertex();
	}

}