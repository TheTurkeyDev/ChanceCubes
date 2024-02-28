package chanceCubes.client.listeners;

import chanceCubes.client.rendertype.LineRenderType;
import chanceCubes.util.SchematicUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class WorldRenderListener
{
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onGuiRender(RenderLevelStageEvent event)
	{
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS)
		{
			final Minecraft minecraft = Minecraft.getInstance();
			Camera camera = minecraft.gameRenderer.getMainCamera();
			RenderBuffers renderBuffers = minecraft.renderBuffers();
			MultiBufferSource.BufferSource bufferSource = renderBuffers.bufferSource();
			PoseStack poseStack = event.getPoseStack();

			if(SchematicUtil.selectionPoints[0] != null && SchematicUtil.selectionPoints[1] != null)
			{
				Vec3 pos = camera.getPosition();
				BlockPos pos1 = SchematicUtil.selectionPoints[0];
				BlockPos pos2 = SchematicUtil.selectionPoints[1];
				int lowX = Math.min(pos1.getX(), pos2.getX());
				int highX = Math.max(pos1.getX(), pos2.getX());
				int lowY = Math.min(pos1.getY(), pos2.getY());
				int highY = Math.max(pos1.getY(), pos2.getY());
				int lowZ = Math.min(pos1.getZ(), pos2.getZ());
				int highZ = Math.max(pos1.getZ(), pos2.getZ());

				AABB box = new AABB(lowX, lowY, lowZ, highX + 1, highY + 1, highZ + 1);

				poseStack.pushPose();
				poseStack.translate(-pos.x, -pos.y, -pos.z);

				final RenderType renderType = LineRenderType.lineRenderType();
				VertexConsumer builder = bufferSource.getBuffer(renderType);
				LevelRenderer.renderLineBox(poseStack, builder, box, 0.9f, 0.0f, 0.5f, 1f);
				bufferSource.endBatch(renderType);

				poseStack.popPose();
			}
		}
	}
}