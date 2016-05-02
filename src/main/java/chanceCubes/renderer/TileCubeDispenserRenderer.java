package chanceCubes.renderer;

import org.lwjgl.opengl.GL11;

import chanceCubes.tileentities.TileCubeDispenser;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TileCubeDispenserRenderer extends TileEntitySpecialRenderer
{
	private static final float ROTATE_SPEED = 0.3F;
	private static final float WAVE_SPEED = 0.1F;

	private float rot = 0;
	private float wave = 0;
	
	private RenderBlocks renderer;

	public TileCubeDispenserRenderer()
	{

	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float partialTick)
	{
		World world = tileEntity.getWorldObj();
		
		if(this.renderer == null)
			this.renderer = new RenderBlocks(world);
		
		int x = tileEntity.xCoord;
		int y = tileEntity.yCoord;
		int z = tileEntity.zCoord;

		int meta = world.getBlockMetadata(x, y, z);
		EntityItem entity = ((TileCubeDispenser) tileEntity).getRenderEntityItem(meta);

		GL11.glPushMatrix();
		wave += WAVE_SPEED;
		wave %= 125;
		float yy = MathHelper.sin((wave) / 10.0F + entity.hoverStart) * 0.1F + 0.1F;
		GL11.glTranslated(posX + 0.5f, posY + yy + 1.25f, posZ + 0.5f);
		entity.getEntityItem().stackSize = 1;
		entity.age = 0;
		entity.rotationYaw = 0;
		rot += ROTATE_SPEED;
		rot %= 360;
		GL11.glRotatef(rot, 0.0F, 1.0F, 0.0F);
		entity.setLocationAndAngles(posX + 0.5f, posY + yy + 1.25f, posZ + 0.5f, 0.0F, 0.0F);
		RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
		GL11.glPopMatrix();
	}
	
    public void func_147496_a(World world)
    {
        
    }
}
