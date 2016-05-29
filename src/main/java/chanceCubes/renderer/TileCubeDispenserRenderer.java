package chanceCubes.renderer;

import chanceCubes.blocks.BlockCubeDispenser;
import chanceCubes.blocks.BlockCubeDispenser.DispenseType;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.tileentities.TileCubeDispenser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TileCubeDispenserRenderer extends TileEntitySpecialRenderer<TileCubeDispenser>
{
	private static final float ROTATE_SPEED = 0.5F;
	private static final float WAVE_SPEED = 0.3F;

	public TileCubeDispenserRenderer()
	{

	}

	@Override
	public void renderTileEntityAt(TileCubeDispenser te, double x, double y, double z, float partialTicks, int destroyStage)
	{
		World world = te.getWorld();

		if(!world.getBlockState(te.getPos()).getBlock().equals(CCubesBlocks.CUBE_DISPENSER))
			return;

		DispenseType type = BlockCubeDispenser.getCurrentState(world.getBlockState(te.getPos()));
		EntityItem entity = te.getRenderEntityItem(type);

		GlStateManager.pushMatrix();
		te.wave += WAVE_SPEED;
		te.wave %= 125;
		float yy = MathHelper.sin((te.wave) / 10.0F + entity.hoverStart) * 0.1F + 0.1F;
		GlStateManager.translate(x + 0.5f, y + yy + 1f, z + 0.5f);
		entity.getEntityItem().stackSize = 1;
		entity.setNoDespawn();
		entity.rotationYaw = 0;
		te.rot += ROTATE_SPEED;
		te.rot %= 360;
		GlStateManager.rotate(te.rot, 0.0F, 1.0F, 0.0F);
		entity.setLocationAndAngles(x + 0.5f, y + yy + 1f, z + 0.5f, 0.0F, 0.0F);
		RenderHelper.disableStandardItemLighting();
		Minecraft.getMinecraft().getRenderManager().doRenderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}
}