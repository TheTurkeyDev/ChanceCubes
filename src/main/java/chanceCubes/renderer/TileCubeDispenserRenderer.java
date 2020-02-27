package chanceCubes.renderer;

import chanceCubes.blocks.BlockCubeDispenser;
import chanceCubes.blocks.BlockCubeDispenser.DispenseType;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.tileentities.TileCubeDispenser;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TileCubeDispenserRenderer extends TileEntityRenderer<TileCubeDispenser>
{
	private static final float ROTATE_SPEED = 0.5F;
	private static final float WAVE_SPEED = 0.3F;

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

		GlStateManager.pushMatrix();
		GlStateManager.translatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
		te.wave += WAVE_SPEED * partialTicks;
		te.wave %= 125;
		float yy = MathHelper.sin((te.wave) / 10.0F + entity.hoverStart) * 0.1F + 0.1F;
		GlStateManager.translatef(0f, yy + 1f, 0f);
		entity.getItem().setCount(1);
		entity.setNoDespawn();
		entity.rotationYaw = 0;
		te.rot += ROTATE_SPEED;
		te.rot %= 360;
		GlStateManager.rotatef(te.rot, 0.0F, 1.0F, 0.0F);
		entity.setLocationAndAngles(x + 0.5f, y + yy + 1f, z + 0.5f, 0.0F, 0.0F);
		RenderHelper.disableStandardItemLighting();
		Minecraft.getInstance().getRenderManager().renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}
}