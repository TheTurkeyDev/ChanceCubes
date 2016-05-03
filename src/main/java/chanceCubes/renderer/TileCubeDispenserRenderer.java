package chanceCubes.renderer;

import org.lwjgl.opengl.GL11;

import chanceCubes.blocks.BlockCubeDispenser;
import chanceCubes.blocks.BlockCubeDispenser.DispenseType;
import chanceCubes.tileentities.TileCubeDispenser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TileCubeDispenserRenderer extends TileEntitySpecialRenderer<TileCubeDispenser>
{
	private static final float ROTATE_SPEED = 0.3F;
	private static final float WAVE_SPEED = 0.1F;

	private float rot = 0;
	private float wave = 0;

	public TileCubeDispenserRenderer()
	{

	}

	@Override
	public void renderTileEntityAt(TileCubeDispenser te, double x, double y, double z, float partialTicks, int destroyStage)
	{
		World world = te.getWorld();

		DispenseType type = BlockCubeDispenser.getCurrentState(world.getBlockState(new BlockPos(x,y,z)));
		EntityItem entity = te.getRenderEntityItem(type);

		GL11.glPushMatrix();
		wave += WAVE_SPEED;
		wave %= 125;
		float yy = MathHelper.sin((wave) / 10.0F + entity.hoverStart) * 0.1F + 0.1F;
		GL11.glTranslated(x + 0.5f, y + yy + 1.25f, z + 0.5f);
		entity.getEntityItem().stackSize = 1;
		entity.setNoDespawn();
		entity.rotationYaw = 0;
		rot += ROTATE_SPEED;
		rot %= 360;
		GL11.glRotatef(rot, 0.0F, 1.0F, 0.0F);
		entity.setLocationAndAngles(x + 0.5f, y + yy + 1.25f, z + 0.5f, 0.0F, 0.0F);
		Minecraft.getMinecraft().getRenderManager().doRenderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);
		GL11.glPopMatrix();
	}
}