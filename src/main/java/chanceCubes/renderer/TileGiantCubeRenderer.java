package chanceCubes.renderer;

import chanceCubes.CCubesCore;
import chanceCubes.tileentities.TileGiantCube;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TileGiantCubeRenderer implements BlockEntityRenderer<TileGiantCube>
{
	private static final Map<Direction, ResourceLocation> GCC_TEXTURES = new HashMap<>();

	static
	{
		GCC_TEXTURES.put(Direction.UP, new ResourceLocation(CCubesCore.MODID, "blocks/chance_cube_face_1"));
		GCC_TEXTURES.put(Direction.NORTH, new ResourceLocation(CCubesCore.MODID, "blocks/chance_cube_face_2"));
		GCC_TEXTURES.put(Direction.EAST, new ResourceLocation(CCubesCore.MODID, "blocks/chance_cube_face_3"));
		GCC_TEXTURES.put(Direction.WEST, new ResourceLocation(CCubesCore.MODID, "blocks/chance_cube_face_4"));
		GCC_TEXTURES.put(Direction.SOUTH, new ResourceLocation(CCubesCore.MODID, "blocks/chance_cube_face_5"));
		GCC_TEXTURES.put(Direction.DOWN, new ResourceLocation(CCubesCore.MODID, "blocks/chance_cube_face_6"));
	}

	/**
	 * Make a new instance.
	 */
	public TileGiantCubeRenderer()
	{

	}

	@SubscribeEvent
	public static void onTextureStitch(TextureStitchEvent.Pre event)
	{
		if(event.getAtlas().location().equals(Sheets.CHEST_SHEET))
			for(ResourceLocation res : GCC_TEXTURES.values())
				event.addSprite(res);
	}

	@Override
	public void render(TileGiantCube tile, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int light, int overlayLight)
	{
		if(!tile.isMaster())
			return;

		poseStack.pushPose();
		//TODO: Breaks with block on top
		int lightToUse = light;
		if(tile.getLevel() != null)
			lightToUse = tile.getLevel().getLightEmission(tile.getBlockPos().offset(0, 2, 0)) * 100;


		for(Direction side : Direction.values())
		{
			Material materialInterface = new Material(Sheets.CHEST_SHEET, GCC_TEXTURES.get(side));
			VertexConsumer buffer = materialInterface.buffer(bufferIn, RenderType::text);

			try(TextureAtlasSprite sprite = materialInterface.sprite())
			{
				poseStack.pushPose();
				poseStack.translate(-1, -1, -1);
				poseStack.scale(0.1875F, -0.1875F, 0.1875F); // (1 / 16) * 3

				float translateX = side.getStepX();
				float translateY = side.getStepY() - 1;
				float translateZ = side.getStepZ();
				short rotationY = 0;
				short rotationX = 0;
				switch(side)
				{
					case NORTH ->
					{
						translateZ += 1F;
						translateX += 1F;
						rotationY = 180;
					}
					case EAST ->
					{
						translateZ += 1F;
						rotationY = 90;
					}
					case WEST ->
					{
						translateX += 1F;
						rotationY = -90;
					}
					case UP ->
					{
						translateZ += 1F;
						rotationX = -90;
					}
					case SOUTH ->
					{
					}
					case DOWN ->
					{
						rotationX = 90;
						translateY += 1F;
					}
				}

				poseStack.translate(translateX * 16, translateY * 16, translateZ * 16);
				poseStack.mulPose(Vector3f.YP.rotationDegrees(rotationY));
				poseStack.mulPose(Vector3f.XP.rotationDegrees(rotationX));

				int r = 255;
				int g = 255;
				int b = 255;
				int a = 255;

				Matrix4f matrix = poseStack.last().pose();
				buffer.vertex(matrix, 16F, 16F, 0).color(r, g, b, a).uv(sprite.getU0(), sprite.getV1()).uv2(lightToUse).endVertex();
				buffer.vertex(matrix, 16F, 0F, 0).color(r, g, b, a).uv(sprite.getU0(), sprite.getV0()).uv2(lightToUse).endVertex();
				buffer.vertex(matrix, 0F, 0F, 0).color(r, g, b, a).uv(sprite.getU1(), sprite.getV0()).uv2(lightToUse).endVertex();
				buffer.vertex(matrix, 0F, 16F, 0).color(r, g, b, a).uv(sprite.getU1(), sprite.getV1()).uv2(lightToUse).endVertex();
				poseStack.popPose();
			} catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		poseStack.popPose();

//		if(destroyStage >= 0)
//		{
//			RenderSystem.bindTexture(DESTROY_STAGES[destroyStage]);
//			GlStateManager.matrixMode(5890);
//			GlStateManager.pushMatrix();
//			GlStateManager.scalef(4.0F, 4.0F, 1.0F);
//			GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
//			GlStateManager.matrixMode(5888);
//		}

//		if(destroyStage >= 0)
//		{
//			GlStateManager.matrixMode(5890);
//			GlStateManager.popMatrix();
//			GlStateManager.matrixMode(5888);
//		}
	}

	//TODO: Make more efficient so not EVERY block is rendered?
	@Override
	public boolean shouldRenderOffScreen(TileGiantCube tile)
	{
		return true;
	}
}