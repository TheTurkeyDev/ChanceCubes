package chanceCubes.renderer;

import com.mojang.blaze3d.platform.GlStateManager;

import chanceCubes.blocks.BlockCubeDispenser;
import chanceCubes.blocks.BlockCubeDispenser.DispenseType;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.tileentities.TileCubeDispenser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TileCubeDispenserRenderer extends TileEntityRenderer<TileCubeDispenser>
{
	private static final float ROTATE_SPEED = 2F;
	private static final float WAVE_SPEED = 1F;

	public TileCubeDispenserRenderer()
	{

	}

	@Override
	public void render(TileCubeDispenser te, double x, double y, double z, float partialTicks, int destroyStage)
	{
		World world = te.getWorld();

		if(!world.getBlockState(te.getPos()).getBlock().equals(CCubesBlocks.CUBE_DISPENSER))
			return;

		DispenseType type = BlockCubeDispenser.getCurrentState(world.getBlockState(te.getPos()));
		ItemEntity entity = te.getRenderEntityItem(type);

		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translated((float) x + 0.5F, (float) y, (float) z + 0.5F);
		te.wave += WAVE_SPEED * partialTicks;
		te.wave %= 125;
		float yy = MathHelper.sin((te.wave) / 10.0F + entity.hoverStart) * 0.1F + 0.1F;
		GlStateManager.translated(0f, yy + 1f, 0f);
		entity.getItem().setCount(1);
		entity.setNoDespawn();
		te.rot += ROTATE_SPEED;
		te.rot %= 360;
		GlStateManager.rotatef(te.rot, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		Minecraft.getInstance().getRenderManager().renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.popMatrix();
	}
}