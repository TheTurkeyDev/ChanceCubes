package chanceCubes.renderer;

import chanceCubes.blocks.BlockCubeDispenser;
import chanceCubes.blocks.BlockCubeDispenser.DispenseType;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.tileentities.TileCubeDispenser;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.Level;

public class TileCubeDispenserRenderer implements BlockEntityRenderer<TileCubeDispenser>
{
	private static final float ROTATE_SPEED = 0.5F;
	private static final float WAVE_SPEED = 0.3F;

	public TileCubeDispenserRenderer()
	{

	}

	@Override
	public void render(TileCubeDispenser te, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn)
	{
		Level level = te.getLevel();

		if(level == null || !level.getBlockState(te.getBlockPos()).getBlock().equals(CCubesBlocks.CUBE_DISPENSER))
			return;

		DispenseType type = BlockCubeDispenser.getCurrentState(level.getBlockState(te.getBlockPos()));

		poseStack.pushPose();
		te.wave += WAVE_SPEED * partialTicks;
		te.wave %= 125;
		float yy = (float) (Math.sin((te.wave) / 10.0F) * 0.1F + 0.1F);
		poseStack.translate(0.5F, yy + 1.25f, 0.5F);
		te.rot += ROTATE_SPEED;
		te.rot %= 360;
		poseStack.mulPose(Vector3f.YP.rotationDegrees(te.rot));
		poseStack.scale(0.35f, 0.35f, 0.35f);

		Minecraft mc = Minecraft.getInstance();
		ItemRenderer renderer = mc.getItemRenderer();
		BakedModel model = renderer.getModel(te.getCurrentStack(type), te.getLevel(), null, 0);
		renderer.render(te.getCurrentStack(type), ItemTransforms.TransformType.NONE, true, poseStack, bufferIn, level.getLightEmission(te.getBlockPos().offset(0, 1, 0)), OverlayTexture.NO_OVERLAY, model);
		poseStack.popPose();
	}
}