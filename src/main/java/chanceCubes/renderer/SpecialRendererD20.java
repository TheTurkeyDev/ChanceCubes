package chanceCubes.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import chanceCubes.tileentities.TileChanceD20;
import chanceCubes.util.RenderUtil;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class SpecialRendererD20 implements ISimpleBlockRenderingHandler
{
    private static final TileChanceD20 DUMMY_TILE = new TileChanceD20();
    
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
	{
		if(block != CCubesBlocks.chanceIcosahedron)
			return;
		
        DUMMY_TILE.setWorldObj(Minecraft.getMinecraft().theWorld);
        DUMMY_TILE.blockType = block;
        DUMMY_TILE.blockMetadata = metadata;
        
		GL11.glPushMatrix();
		
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		TileEntityRendererDispatcher.instance.renderTileEntityAt(DUMMY_TILE, 0.0D, 0.0D, 0.0D, RenderUtil.getTimer().renderPartialTicks);
        
        GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId)
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return CCubesSettings.d20RenderID;
	}
}
