package chanceCubes.renderer;

import chanceCubes.tileentities.TileChanceD20;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;

import java.awt.*;
import java.util.Random;

public class TileChanceD20Renderer implements BlockEntityRenderer<TileChanceD20>
{
	private static final Random random = new Random();

	private static final float HOVER_SPEED = 6000F;
	private static final float ROTATION_SPEED = 100;
	private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0D) / 2.0D);

	public TileChanceD20Renderer()
	{

	}

	@Override
	public void render(TileChanceD20 d20, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn)
	{
		random.setSeed(432L);
		VertexConsumer vertexconsumer2 = bufferIn.getBuffer(RenderType.lightning());
		poseStack.pushPose();

		float wave;
		if(d20.getStage() == 0)
			wave = (float) (Math.sin((((d20.getLevel().getGameTime() % HOVER_SPEED + partialTicks) / HOVER_SPEED) + random.nextFloat()) * 360F) * 0.3f);
		else
			wave = ((d20.getStage() + partialTicks) / 70f);

		d20.wave = wave;
		float rotation = ((float) d20.getLevel().getGameTime() % ROTATION_SPEED + partialTicks) / ROTATION_SPEED;
		int f7 = 0;

		float color = (d20.getLevel().getGameTime() % 75) / 75F;
		Color tmpClr = new Color(Color.HSBtoRGB(color, 1F, 1F));
		int r = tmpClr.getRed();
		int g = tmpClr.getGreen();
		int b = tmpClr.getBlue();

		poseStack.translate(0.5D, 0.5 + wave, 0.5D);

		for(int i = 0; (float) i < 5; ++i)
		{
			//TODO: They start and end in the same states
			poseStack.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
			poseStack.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
			poseStack.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F));
			poseStack.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
			poseStack.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
			poseStack.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F + rotation * 90.0F));
			float f3 = random.nextFloat() * 20.0F + 5.0F + f7 * 10.0F;
			float f4 = random.nextFloat() * 2.0F + 1.0F + f7 * 2.0F;
			Matrix4f matrix4f = poseStack.last().pose();
			int alpha = (int) (255.0F * (1.0F - f7));
			vertex01(vertexconsumer2, matrix4f, r, g, b, alpha);
			vertex2(vertexconsumer2, matrix4f, f3, f4, r, g, b, alpha);
			vertex3(vertexconsumer2, matrix4f, f3, f4, r, g, b, alpha);
			vertex01(vertexconsumer2, matrix4f, r, g, b, alpha);
			vertex3(vertexconsumer2, matrix4f, f3, f4, r, g, b, alpha);
			vertex4(vertexconsumer2, matrix4f, f3, f4, r, g, b, alpha);
			vertex01(vertexconsumer2, matrix4f, r, g, b, alpha);
			vertex4(vertexconsumer2, matrix4f, f3, f4, r, g, b, alpha);
			vertex2(vertexconsumer2, matrix4f, f3, f4, r, g, b, alpha);
		}

		poseStack.popPose();
	}

	private static void vertex01(VertexConsumer p_114220_, Matrix4f p_114221_, int r, int g, int b, int alpha)
	{
		p_114220_.vertex(p_114221_, 0.0F, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
	}

	private static void vertex2(VertexConsumer p_114215_, Matrix4f p_114216_, float p_114217_, float p_114218_, int r, int g, int b, int alpha)
	{
		p_114215_.vertex(p_114216_, -HALF_SQRT_3 * p_114218_, p_114217_, -0.5F * p_114218_).color(r, g, b, alpha).endVertex();
	}

	private static void vertex3(VertexConsumer p_114224_, Matrix4f p_114225_, float p_114226_, float p_114227_, int r, int g, int b, int alpha)
	{
		p_114224_.vertex(p_114225_, HALF_SQRT_3 * p_114227_, p_114226_, -0.5F * p_114227_).color(r, g, b, alpha).endVertex();
	}

	private static void vertex4(VertexConsumer p_114229_, Matrix4f p_114230_, float p_114231_, float p_114232_, int r, int g, int b, int alpha)
	{
		p_114229_.vertex(p_114230_, 0.0F, p_114231_, p_114232_).color(r, g, b, alpha).endVertex();
	}

}