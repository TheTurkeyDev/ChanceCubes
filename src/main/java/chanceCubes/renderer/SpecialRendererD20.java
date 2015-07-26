package chanceCubes.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import chanceCubes.blocks.BlockChanceD20;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class SpecialRendererD20 implements ISimpleBlockRenderingHandler
{
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
	{
		if(block != CCubesBlocks.chanceIcosahedron)
		{
			return;
		}
		
		TileEntity tile = ((BlockChanceD20)block).createNewTileEntity(Minecraft.getMinecraft().theWorld, metadata);
		tile.setWorldObj(Minecraft.getMinecraft().theWorld);
		tile.blockType = block;
		tile.blockMetadata = metadata;
		
		GL11.glPushMatrix();
		
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		TileEntityRendererDispatcher.instance.renderTileEntityAt(tile, 0.0D, 0.0D, 0.0D, 0.0F);
        
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
