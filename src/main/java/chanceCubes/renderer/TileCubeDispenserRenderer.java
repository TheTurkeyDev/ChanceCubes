package chanceCubes.renderer;

import chanceCubes.blocks.BlockCubeDispenser;
import chanceCubes.blocks.BlockCubeDispenser.DispenseType;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.tileentities.TileCubeDispenser;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TileCubeDispenserRenderer extends TileEntityRenderer<TileCubeDispenser>
{
	private static final float ROTATE_SPEED = 0.5F;
	private static final float WAVE_SPEED = 0.3F;

	public TileCubeDispenserRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TileCubeDispenser te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay)
	{
		World world = te.getWorld();

		if(!world.getBlockState(te.getPos()).getBlock().equals(CCubesBlocks.CUBE_DISPENSER))
			return;

		DispenseType type = BlockCubeDispenser.getCurrentState(world.getBlockState(te.getPos()));

		matrixStack.push();
		matrixStack.translate(0.5F, 0, 0.5F);
		te.wave += WAVE_SPEED * partialTicks;
		te.wave %= 125;
		float yy = MathHelper.sin((te.wave) / 10.0F) * 0.1F + 0.1F;
		matrixStack.translate(0f, yy + 1f, 0f);
		te.rot += ROTATE_SPEED;
		te.rot %= 360;
		//matrixStack.rotate(te.rot, 0.0F, 1.0F, 0.0F);

		Minecraft mc = Minecraft.getInstance();
		ItemRenderer renderer = mc.getItemRenderer();
		IBakedModel model = renderer.getItemModelWithOverrides(te.getCurrentStack(type), te.getWorld(), null);
		renderer.renderItem(te.getCurrentStack(type), ItemCameraTransforms.TransformType.NONE, true, matrixStack, buffer, combinedOverlay, combinedLight, model);
		matrixStack.pop();
	}
}